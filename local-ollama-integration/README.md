## SpringAI with Ollama
* Prerequisite
  * Install Ollama in your local
  * Run a model in your local with Ollama
  * https://ollama.com/
  * https://docs.ollama.com/
  * Ollam expose endpoints at http://localhost:11434
  * To see all Ollama commands enter `ollama help` in the terminal
* How to run a Model with Ollama
  * `ollama pull qwen3.5`
  * `ollama list`
  * `ollama run qwen3.5`
* Way of integration
  * Using OpenAI client
    * For this client we need to add suffix `/v1` with Ollama URL
    * URL: http://localhost:11434/v1
  * Using Ollama client
    * We don't need any extra suffix
    * URL: http://localhost:11434
  * Ollama expose endpoints for both clients
    * Ollama compatible
    * OpenAI compatible
  * This application itself is a complete example
  * Follow the `application.yaml`
* We can create a custom model using `modelfile`
  * Example: `mistral-email-draft-modelfile`
  * To create custom model: `ollama create mistral-email-draft -f ./mistral-email-draft-modelfile`
    * It will create a custom model based on `mistral-email-draft-modelfile`