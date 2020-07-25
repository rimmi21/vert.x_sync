import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.Strand;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.sync.Sync;
import io.vertx.ext.sync.SyncVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;

import java.math.BigInteger;
import java.nio.Buffer;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static co.paralleluniverse.strands.Strand.park;
import static io.vertx.ext.sync.Sync.awaitEvent;

public class MainVerticle extends SyncVerticle {

    private static final String COLLECTION_NAME = "Entities";
    private WebClient webClient;

    @Override
    @Suspendable
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        // enable BodyHandler globally for easiness of body accessing
        router.route().handler(BodyHandler.create()).failureHandler(ErrorHandler.create());
        router.route(HttpMethod.POST,
                "/demo").handler(Sync.fiberHandler(this::getWebContent));
        // HttpServer will be automatically shared if port matches
        server.requestHandler(router::accept).listen(8087,result->{
            if(result.succeeded()){
                System.out.println("success");
            }
            else
            {
                System.out.println("Failed");
            }
        });
        webClient = WebClient.create(vertx, new WebClientOptions().setSsl(false));
    }


    @Suspendable
    private void getWebContent(RoutingContext routingContext) {
        System.out.println("Initial Time " + System.currentTimeMillis() + "Thread : " + Thread.currentThread().getName());
        sleep();
        System.out.println("Final Time " + System.currentTimeMillis() + "Thread : " + Thread.currentThread().getName());

        routingContext.response()
                .end("hello world");
    }
    /*@Suspendable
    public BigInteger bigInt(){
        BigInteger veryBig = new BigInteger(2500,new Random());
        veryBig.nextProbablePrime();
        return veryBig;
    }*/
    @Suspendable
    public void sleep()  {

        try {
            Strand.sleep(10, TimeUnit.SECONDS);
        /*try {
            Fibre.park(bigInt());
        } catch (SuspendExecution suspendExecution) {
            suspendExecution.printStackTrace();
        }*/
        } catch (SuspendExecution | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread Time1 " + System.currentTimeMillis() + Strand.currentStrand().getName() + "is strand name");

    }
}
