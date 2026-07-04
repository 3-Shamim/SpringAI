package fyi.shamim.postureservice.controller;

import fyi.shamim.postureservice.service.SecurityPostureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/27/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@RestController
@RequestMapping("/api/posture")
@RequiredArgsConstructor
public class SecurityPostureController {

    private final SecurityPostureService securityPostureService;

    @GetMapping("/{serviceId}")
    public Map<String, Object> getPostureByServiceIdAndEnv(@PathVariable String serviceId,
                                                           @RequestParam(defaultValue = "prod") String env) {

        return securityPostureService.getPosture(serviceId, env);
    }

}
