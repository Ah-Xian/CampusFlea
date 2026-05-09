package com.cc.springboot_wxx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 */
@SpringBootApplication
@MapperScan("com.cc.springboot_wxx.mapper")
public class SpringbootWxxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootWxxApplication.class, args);
	}

}