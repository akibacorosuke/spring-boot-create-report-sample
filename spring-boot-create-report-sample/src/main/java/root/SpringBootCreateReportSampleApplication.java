package root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import root.service.SpringBootCreateReportSampleService;

@SpringBootApplication
public class SpringBootCreateReportSampleApplication {
    
    @Autowired
    SpringBootCreateReportSampleService springBootSampleFormatService;

    public static void main(final String[] args) {
        SpringApplication.run(SpringBootCreateReportSampleApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> System.exit(springBootSampleFormatService.execute().getValue());
    }
}
