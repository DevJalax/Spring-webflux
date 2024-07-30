# Spring-webflux
Differences between Spring MVC [traditional] vs Spring Webflux

1. Programming Model
Spring MVC: Synchronous, blocking model based on the traditional Model-View-Controller (MVC) pattern.
Spring WebFlux: Asynchronous, non-blocking model based on reactive programming principles.

2. Thread Management
Spring MVC: Each request is handled by a dedicated thread from a thread pool, leading to potential thread blocking during I/O operations.
Spring WebFlux: Uses a small number of threads to handle many concurrent requests, avoiding blocking by using reactive streams.

3. Transaction Management
Spring MVC: Uses @Transactional for synchronous transaction management with traditional transaction managers (JDBC, JPA, Hibernate).
Spring WebFlux: Provides reactive transaction management through ReactiveTransactionManager and supports non-blocking transactions.

4. Data Access
Spring MVC: Typically uses synchronous data access technologies (JDBC, JPA, Hibernate) with blocking operations.
Spring WebFlux: Supports reactive data access technologies (R2DBC, reactive MongoDB) with non-blocking operations returning Mono or Flux.

5. Server Environment
Spring MVC: Built on the Servlet API and runs on traditional servlet containers (Tomcat, Jetty, etc.).
Spring WebFlux: Can run on traditional servlet containers but is optimized for reactive servers like Netty.

6. Routing and Handlers
Spring MVC: Uses @Controller and @RequestMapping annotations to define request handling methods.
Spring WebFlux: Supports both annotated and functional routing (using RouterFunction and HandlerFunction).

7. Response Types
Spring MVC: Returns standard Java objects or ResponseEntity for synchronous responses.
Spring WebFlux: Returns reactive types (Mono for 0 or 1 element, Flux for 0 to N elements) for asynchronous responses.

8. Error Handling
Spring MVC: Uses traditional exception handling mechanisms (e.g., @ExceptionHandler, @ControllerAdvice).
Spring WebFlux: Provides reactive error handling using onErrorResume, onErrorReturn, and similar operators in the reactive stream.

9. Testing
Spring MVC: Uses MockMvc for testing controllers in isolation with synchronous requests.
Spring WebFlux: Uses WebTestClient for testing controllers with asynchronous requests and reactive types.

10. Performance and Scalability
Spring MVC: Generally performs well under moderate load but can struggle with high concurrency due to blocking I/O.
Spring WebFlux: Designed for high concurrency and low latency, efficiently handling many simultaneous connections.

11. Use Cases
Spring MVC: Best suited for traditional web applications and RESTful services where synchronous processing is acceptable.
Spring WebFlux: Ideal for applications requiring high concurrency, real-time processing, or streaming data.

12. Learning Curve
Spring MVC: Easier for developers familiar with traditional synchronous programming models.
Spring WebFlux: Requires a shift in mindset towards reactive programming, which may introduce additional complexity.

13. Integration with Reactive Streams
Spring MVC: Does not natively support reactive streams; typically relies on blocking I/O.
Spring WebFlux: Fully integrates with Project Reactor, allowing for the use of reactive streams throughout the application.

14. Support for WebSockets
Spring MVC: Limited support for WebSockets, primarily through traditional servlet-based mechanisms.
Spring WebFlux: Provides first-class support for WebSockets, allowing for reactive handling of WebSocket connections.

15. Configuration
Spring MVC: Configuration is typically done via XML or Java-based configuration classes.
Spring WebFlux: Configuration can be more programmatic, especially when using functional routing.

# Note

1) Flexibility: By changing the spring.r2dbc.url, username, and password properties, you can connect to various databases without changing your application code.

2) Driver Dependency: Make sure to include the appropriate R2DBC driver dependency in your pom.xml or build.gradle file for the database you are using.

3) Database-Specific Configuration: Some databases may require additional configuration properties, so be sure to consult the specific documentation for the R2DBC driver you are using.


# Producer - Consumer model of Spring Webflux

Spring webflux components :

RxJava , Ratpack , project reactor , akka streams , Vert.x

publisher(mono/flux) - subscribe model[only after subscribe() method is called resultant stream is added to arraylist] 

We don’t need to call subscribe methods in the Controller as the internal classes of Spring would call it for us at the right time.

Publisher (source of data) : 

It is responsible for preparing and transferring data to subscribers as individual messages. A Publisher can serve multiple subscribers but it has only one method, subscribe().

public interface Publisher<T> {
    public void subscribe(Subscriber<? super T> s);
}

Subscriber :

A Subscriber is responsible for receiving messages from a Publisher and processing those messages. It acts as a terminal operation in the Streams API. It has four methods to deal with the events received:

1) onSubscribe(Subscription s): Gets called automatically when a publisher registers itself and allows the subscription to request data.

2) onNext(T t): Gets called on the subscriber every time it is ready to receive a new message of generic type T.

3) onError(Throwable t): Is used to handle the next steps whenever an error is monitored.

4) onComplete(): Allows to perform operations in case of successful subscription of data.


Subscription: It represents a relationship between the subscriber and publisher. It can be used only once by a single Subscriber. It has methods that allow requesting for data and to cancel the demand.

public interface Subscription {
    public void request(long n);
    public void cancel();
}

Processor: It represents a processing stage that consists of both Publisher and Subscriber.

public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {
}


How Data is Published ?

1) Starting the Connection: The subscriber (the one who wants to receive data) calls the subscribe() method on the publisher (the one who has the data). This creates a connection between them.

2) Getting Ready to Receive Data: After the connection is made, the publisher tells the subscriber that they are now connected by calling the onSubscribe() method. This gives the subscriber a subscription object that allows it to request data.

3) Requesting Data: The subscriber uses the subscription to ask for a specific number of data items by calling the request(long n) method. This tells the publisher how much data the subscriber can handle at once.

4) Sending Data: Once the publisher gets the request, it starts sending the requested data items to the subscriber using the onNext() method. This happens for each item sent.

5)Finishing Up: After all the requested data has been sent, the publisher calls the onComplete() method to let the subscriber know that there are no more items to send. If something goes wrong while sending data, the publisher calls onError() to inform the subscriber about the issue.



# Difference between JDBC and R2DBC

| Feature                     | JDBC                                   | R2DBC                                   |
|-----------------------------|----------------------------------------|-----------------------------------------|
| **I/O Model**               | Blocking, synchronous                  | Non-blocking, asynchronous              |
| **Connection Management**    | Uses connection pooling libraries like HikariCP | Supports reactive connection pooling with `r2dbc-pool` |
| **Thread Management**        | Dedicated threads for each request, can lead to thread exhaustion | Uses fewer threads, handles requests reactively |
| **Repository Support**       | Requires manual implementation of repository interfaces | Spring Data R2DBC provides reactive repository support out-of-the-box |
| **Query Definition**         | Uses `@Query` annotation for custom queries | Supports `@Query` annotated methods for custom queries |
| **Reactive Types**           | Returns standard Java types            | Returns reactive types like `Mono` and `Flux` |
| **Configuration**            | Requires explicit configuration for connection pooling | Easier configuration with reactive support |
| **Performance**              | Can lead to bottlenecks under high load | Better scalability and resource utilization |
