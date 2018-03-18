# primes

[![Build Status](https://travis-ci.org/Jooles/primes.svg?branch=master)](https://travis-ci.org/Jooles/primes)

A RESTful web service which returns the prime numbers up to and including the number in the request

## Usage

Send a `GET` request to `http(s)://host.name/primes/n`, where n is a positive integer greater than 1 indicating the largest number to check for primality.

The response will be a JSON object with the form

```javascript
{
    initial: 10,
    primes: [2, 3, 5, 7]
}
```
