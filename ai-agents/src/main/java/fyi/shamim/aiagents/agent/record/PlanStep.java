package fyi.shamim.aiagents.agent.record;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/29/26
 * Email: mdshamim723@gmail.com
 */

public record PlanStep(int step, String goal, String toolHint, List<String> targets) {
}
