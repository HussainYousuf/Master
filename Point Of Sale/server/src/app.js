/* eslint-disable prettier/prettier */

const db = require("./dbc");
const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");
let models = require("./models")
const app = express();


app.use(cors());
app.use(bodyParser.json());

let Item = models.Item;
let Supplier = models.Supplier;
let Order = models.Order;
let Purchase = models.Purchase;


app.get("/category", (req, res) => {
    Item.find({isCategory: true},{_id: 0},{
        sort:{
            name: 1
        }
    }).lean().exec((err,obj)=>{
        res.send(obj);
    });
});

app.get("/item/:name", (req, res) => {
    let name = req.params.name;
    Item.find({parentName: name},{_id: 0},{
        sort:{
            name: 1
        }
    }).lean().exec((err,obj)=>{
        res.send(obj);
    });
});

app.post("/item", (req, res) => {
    let body = req.body;
    let item = new Item(body)
    item.id = item._id;
    item.save();
    if(item.parentName == "None") res.send(item);
    else res.send("ok");
});

app.post("/item/delete", (req, res) => {
    let selected = req.body.selected
    for (let index = 0; index < selected.length; index++) {
        Item.deleteMany({id: selected[index]}).exec();        
    }
    res.send("ok");
});

app.put("/item", (req, res) => {
    let info = req.body;
    Item.findByIdAndUpdate(info.id,info,{new:true}, function (err, doc){
        res.send("ok");
    });

});

app.put("/item/quantity", (req, res) => {
    let info = req.body;
    Item.findOne({id: info.id}).exec((err,obj)=>{
        obj.qty -= info.qty;
        console.log(obj);
        Item.findByIdAndUpdate(info.id,obj,{new:true}, function (err, doc){
            res.send("ok");
        });
    });
    

});


//////////////////////////////////////////////
app.put("/supplier", (req, res) => {
    let info = req.body;
    Supplier.findByIdAndUpdate(info.id,info,{new:true}, function (err, doc){
        res.send("ok");
    });

});


app.post("/supplier/delete", (req, res) => {
    let id = req.body.id;
    Supplier.deleteMany({id: id}).exec();   
    res.send("ok");
});

app.get("/supplier", (req, res) => {
    Supplier.find({},{_id: 0},{
        sort:{
            name: 1
        }
    }).lean().exec((err,obj)=>{
        res.send(obj);
    });
});

app.post("/supplier", (req, res) => {
    let body = req.body;
    let supplier = new Supplier(body);
    supplier.id = supplier._id;
    supplier.save();
    res.send("ok");
});

/////////////////////
app.post("/order", (req, res) => {
    let body = req.body;
    let order = new Order(body);
    order.status = false;
    order.id = order._id;
    order.save();
    res.send("ok");
});

app.post("/order/delete", (req, res) => {
    let id = req.body.id;
    Order.deleteMany({id: id}).exec();   
    res.send("ok");
    
});

app.get("/order", (req, res) => {
    Order.find({},{_id: 0},{
        sort:{
            item: 1
        }
    }).lean().exec((err,obj)=>{
        res.send(obj);
    });
});

app.put("/order", (req, res) => {
    let info = req.body;
    Order.findByIdAndUpdate(info.id,info,{new:true}, function (err, doc){});
    Item.findOneAndUpdate({name: info.item},{$inc: {qty: info.qty}},{new:true}, function (err, doc){});
    res.send("ok");
});

app.post("/purchase", (req, res) => {
    let info = req.body;
    let items = info.items;
    let purchase = null;
    let uid = (+new Date).toString(36);
    for (let index = 0; index < items.length; index++) {
        purchase = new Purchase();
        purchase.id = uid;
        purchase.cust_name = info.cust_name;
        purchase.cust_email = info.cust_email;
        purchase.payment_type = info.payment_type;
        purchase.item = items[index].name;
        purchase.price = items[index].price;
        purchase.qty = items[index].qty;
        purchase.save();
    }
    res.send(uid);
});

app.get("/bill/:id", (req, res) => {
    let id = req.params.id;
    Purchase.find({id:id},{_id: 0},{
        sort:{
            name: 1
        }
    }).lean().exec((err,obj)=>{
        res.send(obj);
    });
});

app.get("/purchase", (req, res) => {
    Purchase.find({},{_id: 0,id: 0},{
        sort:{
            date_entered: -1
        }
    }).lean().exec((err,obj)=>{
        res.send(obj);
    });
});

app.listen(40005);

