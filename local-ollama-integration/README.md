## SpringAI with Ollama
* Prerequisite
  * Install Ollama in your local
  * Run a model in your local with Ollama
  * https://ollama.com/
  * https://docs.ollama.com/
* Way of integration
  * Using OpenAI client
    * For this client we need to add suffix `/v1` with ollama url
  * Using Ollama client
  * Ollama expose endpoints for both clients
  * This application has complete example
  * Follow the `application.yaml`
* We can create a custom model using `modelfile`
  * Example: `mistral-email-draft-modelfile`
  * To create custom model: `ollama create mistral-email-draft -f ./mistral-email-draft-modelfile`
    * It will create a custom model based on `mistral-email-draft-modelfile`