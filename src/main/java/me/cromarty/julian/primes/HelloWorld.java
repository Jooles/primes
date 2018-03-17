package me.cromarty.julian.primes;

import io.javalin.Javalin;

/**
 * @author Julian Cromarty
 */
public class HelloWorld {

  public static void main(final String[] args) {
    final Javalin app = Javalin.start(17050);
    app.get("/", ctx -> ctx.result("Hello World"));
  }

}
