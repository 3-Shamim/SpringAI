### Chat Memory

* In Memory (in git branch: `in-memory`)
    * Comes with `SpringAI` dependency.
    * Use `ChatMemoryRepository`
    * Default Implementation `InMemoryChatMemoryRepository`
* Jdbc (in git branch: `master`)
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