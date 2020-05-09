import io.vertx.core.Vertx;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File messages = new File("messages");
        if(!messages.exists()) messages.mkdir();

        File uploads = new File("uploads");
        if(!uploads.exists()) uploads.mkdir();

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Server());
    }

}
