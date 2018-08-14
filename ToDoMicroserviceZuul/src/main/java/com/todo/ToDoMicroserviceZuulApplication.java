package com.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.todo.filters.PreFilter;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ToDoMicroserviceZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoMicroserviceZuulApplication.class, args);
	}
	
	@Bean
	public PreFilter preFilter()
	{
		return new PreFilter();
	}
	
	
}
