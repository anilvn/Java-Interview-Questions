# Spring Boot and Its Key Modules

## Spring Boot Core Concepts

### Spring Boot Basics
1. Convention over configuration framework
2. Standalone applications with embedded servers
3. Starters provide dependency descriptors for common functionality
4. Auto-configuration attempts to configure beans based on classpath
5. `@SpringBootApplication` combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`
6. Property-based configuration with sensible defaults
7. Spring Boot applications can be run as executable jars
8. `SpringApplication` class bootstraps the application
9. Profiles allow for environment-specific configurations
10. Externalized configuration with hierarchical property resolution

### Auto-Configuration
1. Activated by `@EnableAutoConfiguration` or `@SpringBootApplication`
2. Configures beans based on classpath, properties, and existing beans
3. Conditional annotations control when auto-configuration is applied
4. `@ConditionalOnClass`, `@ConditionalOnMissingBean`, `@ConditionalOnProperty` most commonly used
5. Can be disabled for specific classes with `@EnableAutoConfiguration(exclude={Class})`
6. Can be examined with `--debug` flag to see what was auto-configured
7. Custom auto-configuration created with `@Configuration` and registered in `META-INF/spring.factories`
8. Order can be controlled with `@AutoConfigureBefore`, `@AutoConfigureAfter`
9. Provides sensible defaults but allows overriding
10. Auto-configuration classes are loaded from JAR files in `META-INF/spring.factories`

### Spring Boot Configuration
1. `application.properties` or `application.yml` for configuration
2. Environment-specific configs: `application-{profile}.properties`
3. Configuration hierarchy: command line args > OS environment variables > application properties
4. `@ConfigurationProperties` binds external properties to Java beans
5. Type-safe configuration with `@ConfigurationProperties`
6. Relaxed binding supports various naming formats (camelCase, kebab-case, etc.)
7. `@Value` for simple property injection
8. Property validation with `@Validated` and JSR-303 annotations
9. Configuration files can be placed in `/config` subdirectory or current directory
10. YAML support for more structured configuration

### Spring Boot Actuator
1. Production-ready features for monitoring and managing applications
2. Health checks with customizable health indicators
3. Metrics collection with support for various monitoring systems
4. Audit events capturing with AuditEventRepository
5. HTTP endpoints for runtime information (/health, /info, /metrics, etc.)
6. Secured by default, requires explicit exposure
7. Customizable using properties and custom endpoints
8. JMX support for management
9. Remote shell access (removed in later versions)
10. Distributed tracing integration

## Spring Boot Modules

### Spring MVC (Web)
1. Part of `spring-boot-starter-web`
2. Embedded Tomcat, Jetty, or Undertow server
3. Auto-configures DispatcherServlet, error pages, and common web features
4. JSON serialization with Jackson by default
5. Static resources served from `/static`, `/public`, `/resources`, or `/META-INF/resources`
6. Templates with Thymeleaf, FreeMarker, etc.
7. `@RestController` combines `@Controller` and `@ResponseBody`
8. `@RequestMapping` maps HTTP requests to handler methods
9. Content negotiation and message conversion
10. Custom error handling with `@ControllerAdvice` and `@ExceptionHandler`

### Spring WebFlux
1. Reactive alternative to Spring MVC
2. Non-blocking, reactive streams-based
3. Part of `spring-boot-starter-webflux`
4. Uses Netty by default instead of Tomcat
5. Router functions as alternative to annotated controllers
6. `WebClient` as reactive alternative to `RestTemplate`
7. Reactive data repositories supported
8. `Mono<T>` for 0-1 elements, `Flux<T>` for 0-n elements
9. Cannot be used alongside Spring MVC in the same application
10. Best for high concurrency with limited threads

### Spring Data
1. Repository abstraction to reduce boilerplate code
2. Supports various databases (JPA, MongoDB, Redis, etc.)
3. Query methods derived from method names
4. `@Query` for custom queries
5. Pagination and sorting with `Pageable`
6. Auditing with `@CreatedDate`, `@LastModifiedDate`, etc.
7. Custom repository implementations
8. Transaction management
9. Specification API for dynamic queries
10. Multiple persistence modules can coexist

### Spring Security
1. Authentication and authorization framework
2. Auto-configured with sensible defaults
3. Form-based, Basic, OAuth2, and JWT authentication
4. Method-level security with `@PreAuthorize`, `@PostAuthorize`
5. CSRF protection enabled by default
6. Session fixation protection
7. Security headers automatically applied
8. `WebSecurityConfigurerAdapter` for custom configuration (deprecated in later versions)
9. User details service for authentication
10. Role-based and permission-based authorization

### Spring Batch
1. Framework for batch processing
2. Job, Step, ItemReader, ItemProcessor, ItemWriter abstractions
3. Transaction management for steps
4. Restart, skip, and retry capabilities
5. Chunk-based processing for efficiency
6. Parallel processing with partitioning
7. Job repository for metadata storage
8. JobLauncher to run jobs
9. Listeners for various batch events
10. Integration with scheduling frameworks

### Spring Integration
1. Implementation of Enterprise Integration Patterns
2. Message-based integration framework
3. Channel adapters for various protocols
4. Message transformers and routers
5. Message endpoints
6. Gateways to invoke services via messaging
7. Message channels (direct, queue, priority, etc.)
8. Java DSL for fluent configuration
9. XML configuration support
10. Integration with external systems

### Spring Cloud
1. Tools for common distributed system patterns
2. Service discovery with Eureka
3. Client-side load balancing with Ribbon
4. Circuit breaker with Resilience4j or Hystrix
5. Distributed configuration with Config Server
6. API gateway with Spring Cloud Gateway or Zuul
7. Distributed tracing with Sleuth and Zipkin
8. Stream processing with Spring Cloud Stream
9. Contract testing with Spring Cloud Contract
10. OAuth2 integration with Spring Cloud Security

### Spring Testing
1. `@SpringBootTest` for integration testing
2. TestRestTemplate for HTTP client testing
3. MockMvc for server-side testing without HTTP
4. WebTestClient for reactive applications
5. Slice tests (`@WebMvcTest`, `@DataJpaTest`, etc.) for focused testing
6. TestPropertySource for test-specific properties
7. OutputCapture for testing console output
8. MockBean to replace beans with mocks
9. ApplicationContextRunner for testing auto-configuration
10. Random port testing with `@LocalServerPort`

### Spring Boot Devtools
1. Automatic application restart when files change
2. LiveReload server for browser refresh
3. Global settings with devtools properties
4. Remote application debugging
5. Developer tools excluded from production builds
6. Property defaults overrides
7. Automatic H2 console enablement
8. Remote update and restart support
9. File system file watcher
10. Template cache disabled for development

### Spring Boot Actuator
1. Health indicators for application health
2. Info endpoints for application information
3. Metrics with Micrometer
4. Audit events for security auditing
5. HTTP tracing for request/response logging
6. Beans endpoint for application context beans
7. Environment endpoint for configuration properties
8. Loggers endpoint for changing log levels
9. Mappings endpoint for RequestMapping information
10. Customizable via properties and code extensions

### Messaging with Spring Boot
1. JMS support with `spring-boot-starter-activemq` or `spring-boot-starter-artemis`
2. AMQP support with `spring-boot-starter-amqp` (RabbitMQ)
3. Kafka support with `spring-boot-starter-kafka`
4. `@JmsListener` for consuming JMS messages
5. `@RabbitListener` for consuming AMQP messages
6. `@KafkaListener` for consuming Kafka messages
7. Template classes for sending messages (JmsTemplate, RabbitTemplate, KafkaTemplate)
8. Message converters for serialization/deserialization
9. Container factories for listener configuration
10. Error handling with error handlers and retry templates
