## Integrate OpenWebUI with Ollama and Docker Model Runner
* OpenWebAI - https://docs.openwebui.com/
* Docker command to run OpenWebUI
  * ```
    docker run -d -p 3000:8080 \
    -e OLLAMA_BASE_URLS="http://host.docker.internal:11434;http://host.docker.internal:12434" \
    -v open-webui:/app/backend/data \
    --add-host=host.docker.internal:host-gateway \
    --name open-webui \
    --restart always \
    ghcr.io/open-webui/open-webui:main
    ```
* Docker compose to run OpenWebUI
  * ```yaml
    version: '3.8'
    
    services:
      open-webui:
        image: ghcr.io/open-webui/open-webui:main
        container_name: open-webui
        ports:
          - "3000:8080"
        environment:
          - OLLAMA_BASE_URLS=http://host.docker.internal:11434;http://host.docker.internal:12434
        volumes:
          - open-webui-data:/app/backend/data
        extra_hosts:
          - "host.docker.internal:host-gateway"
        restart: always
    
    volumes:
      open-webui-data:
    ```
* You can access it with `http://localhost:3000`

## Important notes:

* Docker Model Runner defaults to listening on 0.0.0.0, meaning it accepts connections from anywhere (including other Docker containers like Open WebUI). 
* Native Ollama defaults to strictly listening on 127.0.0.1 (localhost), which explicitly blocks any connections originating from outside your immediate host OS layer—including the isolated Docker bridge network where Open WebUI lives.
  * Let's allow the Native Ollama to accept any request
  * Run following commands in the terminal
  * ```
    sudo mkdir -p /etc/systemd/system/ollama.service.d
    echo -e "[Service]\nEnvironment=\"OLLAMA_HOST=0.0.0.0\"" | sudo tee /etc/systemd/system/ollama.service.d/override.conf
    ```
  * ```
    sudo systemctl daemon-reload
    sudo systemctl restart ollama
    ```
  * Check the listening on by `ss -tlnp | grep 11434`