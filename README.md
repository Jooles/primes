# primes

[![Build Status](https://travis-ci.org/Jooles/primes.svg?branch=master)](https://travis-ci.org/Jooles/primes)

A RESTful web service which returns the prime numbers up to and including the number in the request

## Usage

Send a `GET` request to `http(s)://host.name/primes/n`, where n is a positive integer greater than 1 indicating the largest number to check for primality.

The response will usually be a JSON object with the form

```javascript
{
    initial: 10,
    primes: [2, 3, 5, 7]
}
```

If the request contains an `Accept` header of `application/xml` then the response will be sent as XML instead, as in this example:
```xml
<PrimesResponse>
  <initial>10</initial>
  <primes>2</primes>
  <primes>3</primes>
  <primes>5</primes>
  <primes>7</primes>
</PrimesResponse>
```

## Setup

To build and run, just use the following commands:
```bash
$ mvn clean package
$ java -jar /path/to/repository/primes-x.y.z.jar
```

The server listens on port 8080 by default but this can be changed by setting the `server.port` system variable to the desired port
