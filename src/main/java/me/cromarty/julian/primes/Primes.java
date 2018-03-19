package me.cromarty.julian.primes;

import javax.activation.MimeType;

import org.eclipse.jetty.http.HttpStatus;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import io.javalin.Handler;
import io.javalin.Javalin;
import me.cromarty.julian.primes.algorithms.ErastothenesSieve;

/**
 * @author Julian Cromarty
 */
public class Primes {

  public static void main(final String[] args) {
    final String port = System.getProperty("server.port");
    int serverPort;
    if (port == null) {
      serverPort = Integer.valueOf(8080);
    } else {
      serverPort = Integer.valueOf(port);
    }
    final App primesService = new App(serverPort);
  }

  public static class App {

    private final Javalin app;
    private final PrimesFinder primeFinder;
    private final static Handler INVALID_METHOD_HANDLER = ctx -> {
      /* Indicate to client that we only accept the GET method */
      ctx.status(HttpStatus.METHOD_NOT_ALLOWED_405).header("ALLOW", "GET");
    };
    private final Thread warmupThread;

    private volatile boolean isRunning;

    public App(final int port) {
      primeFinder = new ErastothenesSieve();
      isRunning = true;
      warmupThread = new Thread(() -> {
        /*
         * Thread that constantly calculates primes to fill up the cache
         */
        int warmup = 100;
        while (isRunning) {
          primeFinder.getPrimes(warmup);
          warmup += 100;
          if (warmup < 0) {
            /*
             * overflow! Just ensure we've got all the primes we can, then end the thread.
             * Will probably never happen
             */
            primeFinder.getPrimes(Integer.MAX_VALUE);
            return;
          }
        }
      });
      app = Javalin.start(port)
                   .exception(Exception.class, (e, ctx) -> ctx.status(HttpStatus.BAD_REQUEST_400))
                   .get("/primes/:initial", ctx -> {
                     final String initial = ctx.param("initial");
                     if ((initial == null) || initial.isEmpty()) {
                       ctx.status(HttpStatus.NOT_FOUND_404);
                     } else {
                       final PrimesResponse result = primeFinder.getPrimes(Integer.parseInt(initial));
                       final MimeType mime = new MimeType(ctx.header("Accept"));
                       if (mime.getBaseType().equals("application/xml")) {
                         final XmlMapper mapper = new XmlMapper();
                         ctx.contentType("application/xml").result(mapper.writeValueAsString(result));
                       } else {
                         ctx.json(result);
                       }
                     }
                   })
                   .get("/*", ctx -> ctx.status(HttpStatus.NOT_FOUND_404))
                   .delete("/*", INVALID_METHOD_HANDLER)
                   .options("/*", INVALID_METHOD_HANDLER)
                   .patch("/*", INVALID_METHOD_HANDLER)
                   .post("/*", INVALID_METHOD_HANDLER)
                   .put("/*", INVALID_METHOD_HANDLER);
      /* Give the server some time to start before we go thrashing the CPU */
      try {
        Thread.sleep(5000);
      } catch (final InterruptedException ignored) {
      }
      warmupThread.start();
    }

    public void stop() {
      isRunning = false;
      warmupThread.interrupt();
      try {
        warmupThread.join(10000);
      } catch (final InterruptedException ignored) {
      }
      app.stop();
    }
  }
}
