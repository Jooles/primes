package me.cromarty.julian.primes.it;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.jayway.restassured.RestAssured;

import me.cromarty.julian.primes.Primes.App;

/**
 * @author Julian Cromarty
 */
public abstract class IntegrationTestBase {

  private static App primesService;

  @BeforeClass
  public static void setup() {
    final String port = System.getProperty("server.port");
    if (port == null) {
      RestAssured.port = Integer.valueOf(17050);
    } else {
      RestAssured.port = Integer.valueOf(port);
    }

    RestAssured.basePath = "/";
    RestAssured.baseURI = "http://localhost";
    primesService = new App(RestAssured.port);
  }

  @AfterClass
  public static void stopServer() {
    primesService.stop();
  }
}
