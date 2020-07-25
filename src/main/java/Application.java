import io.vertx.core.Vertx;

public class Application {

    public static void main(String [] args){
        Vertx.vertx().deployVerticle(MainVerticle.class.getName(), h -> {
            if (h.succeeded()) {
                System.out.println("Success: " + h.result());
            } else {
                System.out.println("Something went wrong: " + h.cause());
            }
        });
    }
}

