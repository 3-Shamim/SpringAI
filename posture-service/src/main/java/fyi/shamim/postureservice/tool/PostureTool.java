package fyi.shamim.postureservice.tool;

import fyi.shamim.postureservice.service.SecurityPostureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 7/9/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class PostureTool {

    private final SecurityPostureService postureService;

    @Tool(name = "security_posture", description = "Returns the posture of a service by service id and environment.")
    public Map<String, Object> getPostureByServiceIdAndEnv(String serviceId, String env) {
        log.info("Getting posture for serviceId: {} in env: {}", serviceId, env);
        return postureService.getPosture(serviceId, env);
    }

}
