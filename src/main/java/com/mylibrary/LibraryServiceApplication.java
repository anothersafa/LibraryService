package com.mylibrary;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = "com.mylibrary.*")
public class LibraryServiceApplication extends SpringBootServletInitializer {

	public static final List<String> privateEndpoints = new ArrayList<>();

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	protected static class ListEndpoints {
		private List<String> include;
		private List<String> exclude;
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryServiceApplication.class, args);
	}

}
