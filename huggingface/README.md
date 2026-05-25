## Hugging face ways of integrations
* Need to create an API key from hugging face console.
  * Read API key is enough
  * If you want you can explore with others
* Shared API
  * MODEL=any model from hugging face 
  * https://router.huggingface.co/hf-inference/models/$MODEL
  * You will find an example request in `huggingface-shared-api.sh`
  * We can config this using SpringAI OpenAI client
  * ```yaml
    spring:
      ai:
        openai:
          api-key: ${HUGGINGFACE_API_KEY}
          base-url: https://router.huggingface.co
          chat:
            options:
              model: Qwen/Qwen2.5-Coder-7B-Instruct:nscale
    ```
  * `nscale` is a provider which is required for Shared API
    * You will find all the providers in hugging face docs
* Dedicated API
  * You have to create endpoints in the hugging face console
  * Ensure your Inference Engine
    * It will define whether it will use OpenAI API or Standalone API
    * If it uses OpenAI API then we have use SpringAI OpenAI client, Hugging Face client will not work
    * If it uses Standalone API then SpringAI Hugging Face client will work
  * For dedicated API we don't have to set the provider
  * ```yaml
    spring:
      application:
        name: huggingface
      ai:
        openai:
          api-key: ${HUGGINGFACE_API_KEY}
          base-url: dedicated_url
          chat:
            options:
              model: Qwen/Qwen2.5-Coder-7B-Instruct
        huggingface:
          chat:
            api-key: ${HUGGINGFACE_API_KEY}
            url: dedicated_url
            options:
              model: Qwen/Qwen2.5-Coder-7B-Instruct
              max-tokens: 1024
              temperature: 0.7
    ```