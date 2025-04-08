package com.order_service.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer{
	
	public void addCorsMapping(CorsRegistry registry) {
		registry.addMapping("*")
		.allowedMethods("*")
		.allowedHeaders("*")
		.allowedOriginPatterns("*")
		.allowCredentials(false);
	}

}
