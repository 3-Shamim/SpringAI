#!/bin/bash

set -euo pipefail

PROMPT="hello"

JSON_PAYLOAD=$(jq -n --arg inputs "$PROMPT" '{
  inputs: $inputs,
  parameters: { max_new_tokens: 20, temperature: 0.7},
  options: { wait_for_model: true }
}')

echo "Request Payload:"
echo "$JSON_PAYLOAD" | jq .

MODEL="facebook/bart-large-cnn"

curl https://router.huggingface.co/hf-inference/models/$MODEL \
  -H "Authorization: Bearer $HUGGINGFACE_API_KEY" \
  -H "Content-Type: application/json" \
  -d "$JSON_PAYLOAD"
