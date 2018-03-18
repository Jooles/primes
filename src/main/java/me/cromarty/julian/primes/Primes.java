package me.cromarty.julian.primes;

import org.eclipse.jetty.http.HttpStatus;

import io.javalin.Handler;
import io.javalin.Javalin;
import me.cromarty.julian.primes.algorithms.ErastothenesSieve;

/**
 * @author Julian Cromarty
 */
public class Primes {

  public static void main(final String[] args) {
    final App primesService = new App(17050);
  }

  public static class App {

    private final Javalin app;
    private final PrimesFinder primeFinder;
    private final static Handler INVALID_METHOD_HANDLER = ctx -> {
      /* Indicate to client that we only accept the GET method */
      ctx.status(HttpStatus.METHOD_NOT_ALLOWED_405).header("ALLOW", "GET");
    };

    public App(final int port) {
      primeFinder = new ErastothenesSieve();
      app = Javalin.start(17050)
                   .exception(Exception.class, (e, ctx) -> ctx.status(HttpStatus.BAD_REQUEST_400))
                   .get("/primes/:initial", ctx -> {
                     final String initial = ctx.param("initial");
                     if ((initial == null) || initial.isEmpty()) {
                       ctx.status(HttpStatus.NOT_FOUND_404);
                     } else {
                       ctx.json(primeFinder.getPrimes(Integer.parseInt(initial)));
                     }
                   })
                   .get("/*", ctx -> ctx.status(HttpStatus.NOT_FOUND_404))
                   .delete("/*", INVALID_METHOD_HANDLER)
                   .options("/*", INVALID_METHOD_HANDLER)
                   .patch("/*", INVALID_METHOD_HANDLER)
                   .post("/*", INVALID_METHOD_HANDLER)
                   .put("/*", INVALID_METHOD_HANDLER);
    }

    public void stop() {
      app.stop();
    }
  }
}
