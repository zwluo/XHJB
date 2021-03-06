package com.maoqi.xhjb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.HibernateTemplate;

@SpringBootApplication
@EnableCaching
public class XhjbApplication {

    public static void main(String[] args) {
        SpringApplication.run(XhjbApplication.class, args);
    }

}
