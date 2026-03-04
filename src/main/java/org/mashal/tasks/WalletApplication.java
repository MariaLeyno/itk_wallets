package org.mashal.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class WalletApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        System.out.println("Starting WalletApplication...");
        SpringApplication.run(WalletApplication.class);
        System.out.println("WalletApplication works!");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WalletApplication.class);
    }
}
