# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=HuggingFaceApplicationTests

# Raw curl test against the HuggingFace Shared API (requires jq)
HUGGINGFACE_API_KEY=<your-key> bash huggingface-shared-api.sh
```

Swagger UI is available at `http://localhost:8080/swagger-ui.html` after starting the app.

## Environment

The app requires `HUGGINGFACE_API_KEY` to be set (a read-only HuggingFace API key is sufficient).

## Architecture

This is a Spring Boot 3 + Spring AI 1.1.6 app that demonstrates two distinct ways to integrate HuggingFace models.

### Dual-client design

`AiProviderConfig` creates two named `ChatClient` beans from two different underlying Spring AI models:

| Bean | Model class | Backed by |
|---|---|---|
| `openAiChatClient` | `OpenAiChatModel` | HuggingFace **Shared Inference API** via the OpenAI-compatible endpoint at `https://router.huggingface.co` |
| `huggingFaceChatClient` | `HuggingfaceChatModel` | HuggingFace **Dedicated Endpoint** (Standalone API) |

Both beans are injected by name into `HuggingFaceController`, which exposes them at:
- `POST /api/chats/shared-huggingface-using-openai` — uses the OpenAI-compatible client
- `POST /api/chats/huggingface` — uses the native HuggingFace client

### Shared vs Dedicated API

- **Shared API** (`spring.ai.openai.*`): Uses the OpenAI-compatible router at `https://router.huggingface.co`. The model name must include a provider suffix (e.g., `Qwen/Qwen2.5-Coder-7B-Instruct:nscale`). `nscale` is one of the available inference providers.
- **Dedicated API** (`spring.ai.huggingface.*`): Points to a user-provisioned endpoint URL. The model name has no provider suffix. Whether to use the OpenAI client or the HuggingFace client depends on which Inference Engine the endpoint was created with — OpenAI-compatible engines must use `OpenAiChatModel`; Standalone engines use `HuggingfaceChatModel`.
