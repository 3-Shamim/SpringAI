## How to set the API key
* Prerequisite
  * We need an API Key for OpenAI.
    * Go to OpenAI console to create it.
    * We need to add a few credits.
  * We need an API Key for Gemini.
    * Go to Gemini console to create it.
    * We need to add a few credits.
* Create a file in resources called
  * `application-dev.properties`
  * Add the below config and update the keys
  * ```yaml
    spring.ai.openai.api-key=API_KEY
    spring.ai.google.genai.api-key=API_KEY
    ```