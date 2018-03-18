package me.cromarty.julian.primes.it;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import com.jayway.restassured.RestAssured;

import junitparams.Parameters;
import me.cromarty.julian.primes.Primes.App;

/**
 * @author Julian Cromarty
 */
public class PrimesRestTest extends IntegrationTestBase {

  @Test
  public void testAlternateServerPort() {
    final int port = 39284;
    final App server2 = new App(port);
    final int oldPort = RestAssured.port;
    RestAssured.port = port;
    given().when().get("/primes/2").then().statusCode(HttpStatus.OK_200);
    server2.stop();
    RestAssured.port = oldPort;
  }

  @Test
  public void incorrectEndPoint() {
    given().when().get("/wibble").then().statusCode(HttpStatus.NOT_FOUND_404);
  }

  @Test
  @Parameters(method = "invalidMethodParams")
  public void invalidMethods() {
    final String path = "/primes/5";
    given().when().delete(path).then().statusCode(HttpStatus.METHOD_NOT_ALLOWED_405).and().header("ALLOW", "GET");
    given().when().options(path).then().statusCode(HttpStatus.METHOD_NOT_ALLOWED_405).and().header("ALLOW", "GET");
    given().when().patch(path).then().statusCode(HttpStatus.METHOD_NOT_ALLOWED_405).and().header("ALLOW", "GET");
    given().when().post(path).then().statusCode(HttpStatus.METHOD_NOT_ALLOWED_405).and().header("ALLOW", "GET");
    given().when().put(path).then().statusCode(HttpStatus.METHOD_NOT_ALLOWED_405).and().header("ALLOW", "GET");
  }

  @Test
  public void missingIntialValue() {
    given().when().get("/primes/").then().statusCode(HttpStatus.NOT_FOUND_404);
  }

  @Test
  public void invalidIntialValues() {
    given().when().get("/primes/-5").then().statusCode(HttpStatus.BAD_REQUEST_400);
    given().when().get("/primes/0").then().statusCode(HttpStatus.BAD_REQUEST_400);
    given().when().get("/primes/1").then().statusCode(HttpStatus.BAD_REQUEST_400);
    given().when().get("/primes/wibble").then().statusCode(HttpStatus.BAD_REQUEST_400);
  }

  @Test
  public void testPrimesToTen() {
    given().when()
           .get("/primes/10")
           .then()
           .statusCode(HttpStatus.OK_200)
           .and()
           .body("primes", response -> equalTo(Arrays.asList(2, 3, 5, 7)));
  }
}
