package fyi.shamim.aiagents.config;

import io.micrometer.context.ContextSnapshotFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 7/2/26
 * Email: mdshamim723@gmail.com
 */

@Configuration
public class AsyncConfig {

    @Bean
    public TaskDecorator tracingTaskDecorator() {
        return (runnable) -> ContextSnapshotFactory.builder().build().captureAll().wrap(runnable);
    }

    @Bean("traceableAsyncExecutor")
    public Executor traceableAsyncExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        int availableProcessors = Runtime.getRuntime().availableProcessors();

        executor.setCorePoolSize(availableProcessors);
        executor.setMaxPoolSize(availableProcessors);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("traceableThreadPoolExecutor-");
        executor.setTaskDecorator(tracingTaskDecorator());
        executor.initialize();

        return executor;
    }

}
