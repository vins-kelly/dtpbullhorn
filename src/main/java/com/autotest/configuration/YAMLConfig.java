package com.autotest.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("spring.common")
public class YAMLConfig {
	
	private String pat;

	public String getPat() {
		return pat;
	}
	
	public void setPat(String pat) {
		this.pat = pat;
	}

	@Override
	public String toString() {
		return "YAMLConfig [pat=" + pat + "]";
	}
	
}
