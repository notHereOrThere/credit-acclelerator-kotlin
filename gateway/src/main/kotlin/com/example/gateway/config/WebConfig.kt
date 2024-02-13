package com.example.gateway.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig (private val jwtCheckerInterceptor: JwtCheckerInterceptor): WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(jwtCheckerInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/auth/**")
    }
}