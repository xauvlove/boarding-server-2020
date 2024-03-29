package com.xauv.config;

import com.xauv.utils.ConditionCheckUtil;
import com.xauv.utils.TransferTToKUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@MapperScan("com.xauv.mapper")
public class AppDevConfig {

    @Bean
    public TransferTToKUtil getTransferTToKUtil() {
        return new TransferTToKUtil();
    }

    @Bean
    public ConditionCheckUtil getConditionCheckUtil() {
        return new ConditionCheckUtil();
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setQueueCapacity(100);
        executor.setMaxPoolSize(5);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
