package com.api.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.api.authorization.swagger.SwaggerFilter;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class AuthorizationApplication {
	
	public static void main(String[] args) {
		log.debug("Authorization Application Started!!!");
		SpringApplication.run(AuthorizationApplication.class, args);
		log.debug("Authorization Application Exited!!!");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		log.debug("Entering filterRegisterationBean Method !!!");
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		SwaggerFilter myFilter = new SwaggerFilter();
		filterRegistrationBean.setFilter(myFilter);
		log.debug("Exiting filterRegisterationBean Method !!!");
		return filterRegistrationBean;
	}
	
	@Bean

	public PasswordEncoder encoder() {
	  return new BCryptPasswordEncoder();
	}

}
