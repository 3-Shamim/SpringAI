## Need to set up Vertex AI
* Go to https://console.cloud.google.com/
* Login and Search for Agent Platform or Vertex AI
* Activate the AI Agent Platform and Vertex AI
* Go to Service Account or Search for it.
* Create a Service Account with role `Agent Platform User`
  * Might need some organization level permissions and organization policy update
* Download the JSON service account details
* Credential setup
  * Add environment variable `GOOGLE_APPLICATION_CREDENTIALS='path of the JSON service account details'`
    * OS level environment variable will not work, you need to add it to the IntelliJ configurations
  * We can also do it using `gcloud cli`
  * Ref: https://docs.cloud.google.com/docs/authentication/set-up-adc-local-dev-environment#google-account
* 