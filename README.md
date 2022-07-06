### Java example with Spring for distributed tracing

This project demonstrates how to implement distributed tracing in java spring boot application with the help for below microservices

- [order-service](order-service)
- [payment-service](payment-service)
- [user-service](user-service)

### TODO
* Fix flaky tests
* 


### Running the code

This application require Eureka service registry

```
cd discovery-server
mvn clean install -Dmaven.test.skip
./scripts/start_sandbox.sh
cd -
```

If everything goes well you can now open `http://localhost:8761`

Start individual microservice using below commands

1. User Service

```
cd user-service
mvn clean install -Dmaven.test.skip
./scripts/start_sandbox.sh
cd -
```

2. Order Service

```
cd order-service
mvn clean install -Dmaven.test.skip
./scripts/start_sandbox.sh
cd -

```

3. Payment Service

```
cd payment-service
mvn clean install -Dmaven.test.skip
./scripts/start_sandbox.sh
cd -

```

5. Check if all the services show up on the Eureka dashboard (`http://localhost:8761`)

5. Jaeger

TODO-> Make sure jaeger is available at `localhost:14250`.

6. Call the order command which will call both the `user-service` and `payment-service`:
```shell
curl --location --request POST 'localhost:8083/orders/create/id/1' \
--header 'X-API-Key: AQEmhmfuXNWTK0Qc+iSEl3cshOuWWIpDFNWZ7BldpzFgqjaFCkrORCwQwV1bDb7kfNy1WIxIIkxgBw==-AOBzxgPlXqtBf91jbKBJPbbLuET0Ou3aYjGklucQmGY=-Px5Jgb5:B6;[^D' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productName": "MacBook Pro",
    "price": "1100"
}'
```

---
## Virtual machine with vagrant and Parallels
One can also use a VM (like I do). There's a `Vagrantfile` in the root directory, so simply run `vagrant up --provision` in this directroy.
The above mentioned start commands are also in `./scripts/rebuild.sh` deploying all 4 apps right away. Note that this vagrant box is specifically for aarch64 (Macbook M1).
