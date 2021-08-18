package com.example.project_318;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadCustomer {

  private static final Logger log = LoggerFactory.getLogger(LoadCustomer.class);

  @Bean
  CommandLineRunner initCustLoadCustomer(CustomerRepository repository) {

    return args -> {
      log.info("Preloading " + repository.save(new Customer("Tyson Ranch", "California", "USA")));
      log.info("Preloading " + repository.save(new Customer("Tesla", "Palo Alto","USA")));
    };
  }
}