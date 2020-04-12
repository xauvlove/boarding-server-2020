package com.xauv.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wx.app")
public class AppConfigurationProperties {
    private String appId;
    private String secret;
    private String baseUrl;
}
