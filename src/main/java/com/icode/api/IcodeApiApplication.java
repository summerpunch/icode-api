package com.icode.api;

import com.icode.api.common.listener.ApplicationInitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class IcodeApiApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(IcodeApiApplication.class);
		springApplication.addListeners(new ApplicationInitListener());
		springApplication.run(args);
	}

}
