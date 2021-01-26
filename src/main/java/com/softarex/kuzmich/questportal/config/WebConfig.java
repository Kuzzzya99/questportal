package com.softarex.kuzmich.questportal.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.softarex.kuzmich.questportal")
public class WebConfig implements WebMvcConfigurer {

    private Environment environment;

    @Autowired
    public WebConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
