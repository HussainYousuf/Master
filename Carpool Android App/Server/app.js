const express = require('express')
const MongoClient = require('mongodb').MongoClient;
const ObjectID = require('mongodb').ObjectID;
const assert = require('assert');
const geo = require('spherical-geometry-js');
const polyline = require( 'google-polyline' )

const app = express()
const port = 3000
const url = 'mongodb://localhost:27017';
const dbName = "demo"
const client = new MongoClient(url, {useNewUrlParser: true, useUnifiedTopology: true});
const key = ""
const googleMapsClient = require('@google/maps').createClient({
  key: key,
  Promise: Promise
});

let db;

app.use(express.json())

client.connect(function(err) {
  assert.equal(null, err);
  console.log("Connected successfully to server");
  db = client.db(dbName)
});


app.get("/",function(req,response){
console.log("here")
	const fs = require('fs');

// Where fileName is name of the file and response is Node.js Reponse. 

  const fileName =  "carpool.apk" // or any file format

  // Check if file specified by the filePath exists 
  fs.exists(fileName, function(exists){
      if (exists) {     
        // Content-type is very interesting part that guarantee that
        // Web browser will handle response in an appropriate manner.
        response.writeHead(200, {
          "Content-Type": "application/octet-stream",
          "Content-Disposition": "attachment; filename=" + fileName
        });
        fs.createReadStream(fileName).pipe(response);
      } else {
        response.writeHead(400, {"Content-Type": "text/plain"});
        response.end("ERROR File does not exist");
      }
    });
  

})

app.post('/login', function (req, res) {
  (async()=>{
    let r = await db.collection("user").findOne(req.body);
    if(r) res.json({_id: r._id});
    else {
      let r = await db.collection("user").findOne({name:req.body.name});
      if(r) res.json({});
      else{
        let r = await db.collection("user").insertOne(req.body)
        res.json({_id: r.insertedId});
      }
    }
  })();
})

app.post("/addTrip",function(req,response) {
  let obj = req.body
  let _id = obj._id
  delete obj._id
  obj.tripId = ObjectID()

  if(obj.type == "passenger"){
    (async ()=> {
      let docs = await db.collection("user").find({"trips.routes.polyline":{$exists: true}}).toArray();
      for (let doc of docs) {
        let nextDriver = false;
        for(let trip of doc.trips){
          if(!trip.routes) continue;
          for(let route of trip.routes){
            let points = polyline.decode(route.polyline)
            let originInSpan = false;
            let destinationInSpan = false;
            for (let point of points) {
              if(!originInSpan && geo.computeDistanceBetween(new geo.LatLng(obj.originLat,obj.originLng),new geo.LatLng(point[0],point[1])) <= 1000)
                originInSpan = true
              if(originInSpan && geo.computeDistanceBetween(new geo.LatLng(obj.destinationLat,obj.destinationLng),new geo.LatLng(point[0],point[1])) <= 1000)
                destinationInSpan = true
              if(originInSpan && destinationInSpan){
                if("drivers" in obj == false) obj.drivers = []
                obj.drivers.push({tripId: new ObjectID(trip.tripId), routeId: new ObjectID(route.routeId)})
                nextDriver = true;
                break;
              }
            }
            if(nextDriver) break;
          }
          if(nextDriver) break;
        }
      }
      await db.collection("user").findOneAndUpdate({_id:new ObjectID(_id)},{
        $addToSet:{
          trips: obj,
          notifications: "driver available"}
      }),
      response.json({"ok":""})
    })();

  }else{
    (async()=>{
      let res = await googleMapsClient.directions({origin: obj.originLat+","+obj.originLng, destination: obj.destinationLat+","+obj.destinationLng, alternatives: true}).asPromise();
      if(!res.json.status == "OK") return;
      obj.routes = [];
      for (let route of res.json.routes) {
        delete route.bounds
        delete route.copyrights
        delete route.legs
        delete route.warnings
        delete route.waypoint_order
        route.polyline = route.overview_polyline.points
        route.routeId = ObjectID()
        delete route.overview_polyline
        obj.routes.push(route)
      }
      let docs = await db.collection("user").find({'trips.type':"passenger"}).toArray();
      for (let doc of docs) {
        for (let trip of doc.trips) {
          if(trip.routes) continue;
          for (let route of obj.routes){
            let nextTrip = false;
            let points = polyline.decode(route.polyline)
            let originInSpan = false;
            let destinationInSpan = false;
            for (let point of points) {
              if(!originInSpan && geo.computeDistanceBetween(new geo.LatLng(trip.originLat,trip.originLng),new geo.LatLng(point[0],point[1])) <= 1000)
                originInSpan = true
              if(originInSpan && geo.computeDistanceBetween(new geo.LatLng(trip.destinationLat,trip.destinationLng),new geo.LatLng(point[0],point[1])) <= 1000)
                destinationInSpan = true
              if(originInSpan && destinationInSpan){
                await db.collection("user").findOneAndUpdate({"trips.tripId": new ObjectID(trip.tripId)},
                {$addToSet:{
                  "trips.$.drivers":{tripId: obj.tripId, routeId: route.routeId},
                  notifications: "driver available"}});
                nextTrip = true;
                break;
              }
            }
            if(nextTrip) break;
          }
        }
      }     
      await db.collection("user").findOneAndUpdate({_id:new ObjectID(_id)},{$addToSet:{trips:obj}})
      response.json({"ok":""})
    })(); 
  }
});

app.post("/getTrips",function(req,res) {
  (async()=>{
    let doc  = await db.collection("user").findOne({_id: new ObjectID(req.body._id)})
    if(!doc.trips) return;
    let trips = []
    for (let trip of doc.trips) {
      let temp = {}
      temp.tripId = trip.tripId
      temp.type = trip.type
      temp.originName = trip.originName
      temp.destinationName = trip.destinationName
      temp.status = false
      if(trip.drivers && trip.drivers.length > 0) temp.status = true
      if(trip.routes){
        for (let route of trip.routes) {
          if(route.passengers && route.passengers.length > 0){
            temp.status = true;
            break;
          } 
        }
      }
      trips.push(temp)
    }
    res.json({trips:trips});
  })();    
});

app.post("/deleteTrip",function(req,res) {
  (async()=>{
    await db.collection("user").findOneAndUpdate({"trips.tripId": new ObjectID(req.body.tripId)},{$pull: {trips: {tripId: new ObjectID(req.body.tripId)}}})
    if(req.body.type == "passenger"){
      await db.collection("user").updateMany({"trips.routes.passengers.tripId": new ObjectID(req.body.tripId)},{
        $pull: {"trips.$[i].routes.$[j].passengers": {tripId: new ObjectID(req.body.tripId)}}
      },{
        arrayFilters: [{"i.routes":{$exists:true}},{"j.passengers":{$exists:true}}]
      });
    }else {
      await db.collection("user").updateMany({"trips.drivers.tripId": new ObjectID(req.body.tripId)},{
        $pull: {"trips.$[i].drivers": {tripId: new ObjectID(req.body.tripId)}}
      },{
        arrayFilters: [{"i.drivers":{$exists:true}}]
      });
    }
    res.json({"ok":""});
  })();
})

app.post("/getDrivers",function(req,res){
  (async ()=> {
    let doc = await db.collection("user").findOne({"trips.tripId": new ObjectID(req.body.tripId)})
    let trip;
    for (let temp of doc.trips) {
      if(temp.tripId == req.body.tripId){
        trip = temp;
        break;
      } 
    }
    if(!trip.drivers) return;
    let drivers = []
    for (let driver of trip.drivers) {
      let doc = await db.collection("user").findOne({"trips.tripId": new ObjectID(driver.tripId)});
      let trip;
      for (let temp of doc.trips) {
        if(temp.tripId == String(driver.tripId)){
          trip = temp;
          break;
        }
      }
      let obj = {}
      obj.tripId = driver.tripId
      obj.routeId = driver.routeId
      obj.status = driver.status ? driver.status : ""
      obj.name = doc.name
      obj.mobile = doc.mobile
      obj.destinationName = trip.destinationName
      obj.originName = trip.originName
      obj.fare = trip.fare
      obj.timing = trip.timing
      drivers.push(obj)
    }
    res.json({drivers:drivers})
  })();
})

app.post("/getRoutes",function(req,res){
  (async ()=> {
    let doc = await db.collection("user").findOne({"trips.tripId": new ObjectID(req.body.tripId)});
    let trip;
    for(let temp of doc.trips) {
      if(temp.tripId == req.body.tripId){
        trip = temp;
        break;
      }
    }
    let routes = []
    for(let route of trip.routes){
      let obj = {}
      obj.routeId = route.routeId
      obj.summary = route.summary
      obj.polyline = route.polyline
      obj.passengers = []
      routes.push(obj)
      if(!route.passengers) continue;
      for(let passenger of route.passengers){
        let doc = await db.collection("user").findOne({"trips.tripId":new ObjectID(passenger.tripId)})
        let trip;
        for(let temp of doc.trips){
          if(temp.tripId == String(passenger.tripId)){
            trip = temp;
            break;
          }
        }
        temp = {}
        temp.passengerTripId = passenger.tripId
        temp.status = passenger.status ? passenger.status : ""
        temp.name = doc.name
        temp.mobile = doc.mobile
        temp.destinationLat = trip.destinationLat
        temp.destinationLng = trip.destinationLng
        temp.originLat = trip.originLat
        temp.originLng = trip.originLng
        temp.originName = trip.originName
        temp.destinationName = trip.destinationName
        obj.passengers.push(temp)
      }
    }
    res.json({routes:routes})
  })();
})

app.post("/sendRequest",(req,res)=>{
  (async ()=> {
    await db.collection("user").findOneAndUpdate({"trips.tripId": new ObjectID(req.body.tripId)},{
      $set: {"trips.$.drivers.$[i].status": "requested"}
    },
    {
      arrayFilters: [{"i.routeId": new ObjectID(req.body.driverRouteId)}]
    });
    await db.collection("user").findOneAndUpdate({"trips.tripId": new ObjectID(req.body.driverTripId)},{
      $addToSet: {"trips.$.routes.$[i].passengers": {tripId: new ObjectID(req.body.tripId)},notifications: "passenger available"}
    },{
      arrayFilters: [{"i.routeId": new ObjectID(req.body.driverRouteId)}]
    })
    res.json({"ok":""})
  })();
})

app.post("/acceptRequest",(req,res)=>{
  (async()=>{
    await db.collection("user").findOneAndUpdate({"trips.tripId": new ObjectID(req.body.tripId)},
    {
      $set: {"trips.$.routes.$[j].passengers.$[k].status": "accepted"}
    },
    {
      arrayFilters: [{"j.routeId": new ObjectID(req.body.routeId)},{"k.tripId": new ObjectID(req.body.passengerTripId)}]
    })

    await db.collection("user").findOneAndUpdate({"trips.tripId": new ObjectID(req.body.passengerTripId)},
    {
      $set: {"trips.$.drivers.$[j].status": "accepted"},
      $addToSet: {notifications: "request accepted"}
    },
    {
      arrayFilters: [{"j.routeId": new ObjectID(req.body.routeId)}]
    })
    res.json({"ok":""})
  })();
})

app.post("/cancelRequest",(req,res)=>{
  (async()=>{
    await db.collection("user").findOneAndUpdate({"trips.tripId": new ObjectID(req.body.passengerTripId)},
    {
      $set: {"trips.$.drivers.$[j].status": ""}
    },
    {
      arrayFilters: [{"j.routeId": new ObjectID(req.body.driverRouteId)}]
    })
    await db.collection("user").findOneAndUpdate({"trips.tripId": new ObjectID(req.body.driverTripId)},
    {
      $pull: {"trips.$.routes.$[j].passengers": {tripId: ObjectID(req.body.passengerTripId)}}
    },
    {
      arrayFilters: [{"j.routeId": new ObjectID(req.body.driverRouteId)}]
    })
    res.json({"ok":""})
  })();
})

app.get("/notifications",(req,res)=>{

  (async()=>{
    let doc = await db.collection("user").findOneAndUpdate({_id: new ObjectID(req.body._id)},{$set: {notifications: []}});
    if(doc.notifications){
      res.json({notifications:doc.notifications});
    }
  })();
})

app.listen(port, () => console.log(`Example app listening on port ${port}!`))
