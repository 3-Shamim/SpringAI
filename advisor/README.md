### SpringAI Advisor examples

* For `SimpleLoggerAdvisor`
  * ```yaml
      logging:
          level:
              root: info
              org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor: debug
    ```
  * This config is required because, it's log on debug level