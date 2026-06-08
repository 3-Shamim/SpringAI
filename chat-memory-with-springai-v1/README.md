### Chat Memory

* In Memory
    * Comes with `SpringAI` dependency.
    * Use `ChatMemoryRepository`
    * Default Implementation `InMemoryChatMemoryRepository`
* Jdbc
    * Need to add a new dependency
        * JDBC Repository and DB/PostgreSQL
        * ```xml
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-starter-model-chat-memory-repository-jdbc</artifactId>
            </dependency>
            
            <dependency>
                <groupId>org.postgresql</groupId>
                <artifactId>postgresql</artifactId>
            </dependency>
          ```
    * All config you will find in `application.yaml`
    * Use `ChatMemoryRepository`
    * Default Implementation `JdbcChatMemoryRepository`