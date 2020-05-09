import io.netty.handler.codec.http.QueryStringDecoder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
//todo make messages file and uploads folder and put messages in its folder
public class Server extends AbstractVerticle {

    public static ConcurrentHashMap<String,ArrayList<String>> websocketIDs = new ConcurrentHashMap<String,ArrayList<String>>();

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            routingContext.response().sendFile("index.html");
        });

        router.route("/bootstrap.min.css").handler(routingContext -> {
            routingContext.response().sendFile("bootstrap.min.css");
        });

        router.route("/font-awesome.min.css").handler(routingContext -> {
            routingContext.response().sendFile("font-awesome.min.css");
        });

        router.route("/chat").handler(routingContext -> {
            routingContext.response().sendFile("chat.html");
        });

        router.route("/chat.js").handler(routingContext -> {
            routingContext.response().sendFile("chat.js");
        });

        router.route("/BlissMedium.ttf").handler(routingContext -> {
            routingContext.response().sendFile("BlissMedium.ttf");
        });

        router.route("/uploads/*").handler(routingContext -> {
            String fileName = routingContext.request().path();
            String filename = fileName.substring(9);
            String filePath = QueryStringDecoder.decodeComponent(fileName.substring(1));
            routingContext.response().setChunked(true);
            routingContext.response().putHeader("Content-disposition","attachment; filename=" + filename);
            routingContext.response().sendFile(filePath);
            routingContext.next();
        });

        router.route("/uploads/*").handler(routingContext -> {
            routingContext.response().putHeader("Content-type","text/html");
            routingContext.response().sendFile("chat.html");
            routingContext.response().end();
        });

        router.post("/form").handler(routingContext -> {

            HttpServerRequest req = routingContext.request();
            req.setExpectMultipart(true);
            req.uploadHandler(upload -> {
                upload.exceptionHandler(cause -> {
                    System.out.println("sorry can't download file");
                });

                upload.endHandler(v -> {
                    req.response().sendFile("chat.html");
                    req.response().end();
                });
                // FIXME - Potential security exploit! In a real system you must check this filename
                // to make sure you're not saving to a place where you don't want!
                // Or better still, just use Vert.x-Web which controls the upload area.
                upload.streamToFileSystem("uploads/"+upload.filename());

            });
        });



        vertx.createHttpServer().requestHandler(router::accept).websocketHandler(ws -> {

            ws.handler(buffer -> {
                String message = buffer.toString();
                if(message.startsWith("@&@ID")){
                    String group = message.substring(5);
                    if(websocketIDs.keySet().contains(group)){
                        websocketIDs.get(group).add(ws.textHandlerID());
                        File file = new File("messages/"+group+".txt");
                        if(file.exists()) {
                            try(BufferedReader reader = new BufferedReader(new FileReader(file))){
                                String line;
                                while((line = reader.readLine()) != null){
                                    vertx.eventBus().send(ws.textHandlerID(),line);
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        ArrayList<String> list = new ArrayList<String>();
                        list.add(ws.textHandlerID());
                        websocketIDs.put(group,list);
                    }
                }else{
                    JsonObject json = new JsonObject(message);
                    String group = json.getValue("id").toString();
                    //saving message to file
                    try(PrintWriter writer = new PrintWriter(new FileWriter("messages/"+group+".txt",true)))
                    {
                        writer.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //broadcasting to specific group
                    ArrayList<String> wsids = websocketIDs.get(group);
                    for(String id : wsids){
                        if(ws.textHandlerID().equals(id)){
                            continue;
                        }else{
                            vertx.eventBus().send(id,message);
                        }
                    }
                }
            });

            ws.closeHandler(e ->{
                Set<String> groups = websocketIDs.keySet();
                for(String group : groups){
                    ArrayList<String> ids = websocketIDs.get(group);
                    for(String id : ids){
                        if(id.equals(ws.textHandlerID())){
                            System.out.println("ArrayList before: "+websocketIDs.get(group).toString());
                            ids.remove(id);
                            System.out.println("ArrayList after: "+websocketIDs.get(group).toString());
                            System.out.println("removed id: "+id);
                        }
                    }
                }
            });

        }).listen(8080);

    }

}
