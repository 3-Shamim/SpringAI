## SpringAI with Docker Model Runner
* Prerequisite
  * Install Docker
    * Docker Model Runner -- Inclusive with Docker
  * To see all Docker commands enter `docker help` in the terminal
  * To see all Docker Model Runner enter `docker model help` in the terminal
* How to run a model with Docker Model Runner
  * `docker model search gemma`
  * `docker model pull ai/gemma4`
  * `docker model list`
  * `docker model run ai/gemma4`
* How to integrate with SpringAI
  * It's expose OpenAI endpoints at http://localhost:12434/engines/llama.cpp
  * So we can use SpringAI OpenAI client to integrate this
    * ```yaml
      spring:
        ai:
          openai:
            api-key: dummy-token
            base-url: http://localhost:12434/engines/llama.cpp/v1
            chat:
              model: ai/gemma4
      ```
    * `/engines/llama.cpp` is mandatory for Docker Model Runner
      * Because it exposes multiple URL
      * This URL points to the Model
    * We have added suffix `/v1` with the Docker Model Runner URL
      * This is required for SpringAI 2.0.0-* OpenAI client