package com.h5radar.account.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.h5radar.account.AuthRequestInterceptor;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  @Autowired
  private AuthRequestInterceptor authRequestInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authRequestInterceptor)
        .addPathPatterns("/api/v1/**")
        .excludePathPatterns("/api/v1/account-app/**")
        .excludePathPatterns("/actuator/**")
        .excludePathPatterns("/swagger-ui/**")
        .excludePathPatterns("/v3/api-docs/**");
  }
}
