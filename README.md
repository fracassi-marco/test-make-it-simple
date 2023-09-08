# Test: make it simple!

## Initial scenario:
* read persisted products
* retrieve price from external service
* pay with external service and if payment succeeded empty cart
* if payment succeeded save bill results on S3 bucket `bills`
* if cart has more than three items send `big_cart_created` message on SQS `events_queue`

## Requirements
You need:
- Java 1.8 or later
- docker
- docker-compose

## How to run tests
```bash
docker-compose up
./gradlew test
```