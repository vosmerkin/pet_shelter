package ua.tc.marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender javaMailSender() {

//        Auto-Configuration Mechanism
//        When you include spring-boot-starter-mail in your dependencies, Spring Boot automatically:
//        Detects the presence of JavaMail on the classpath
//        Looks for mail-related properties in your application.properties or application.yml
//        Creates and configures a JavaMailSender bean for you

//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        // Configure mail server properties from application.properties
//        mailSender.setHost("smtp.yourmailserver.com");
//        mailSender.setPort(587);
//        mailSender.setUsername("your-email@example.com");
//        mailSender.setPassword("your-email-password");
//        // Additional properties
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true"); // Enable debug mode for troubleshooting
//        return mailSender;

//        # SMTP server connection
//        spring.mail.host=smtp.yourmailserver.com  # SMTP server hostname
//        spring.mail.port=587                      # SMTP server port (587 for TLS, 465 for SSL)
//        spring.mail.username=your-email@example.com  # Login username
//        spring.mail.password=your-email-password     # Login password
//
//        # Additional JavaMail properties
//        spring.mail.properties.mail.smtp.auth=true          # Enable authentication
//        spring.mail.properties.mail.smtp.starttls.enable=true  # Enable STARTTLS
//        spring.mail.properties.mail.debug=true             # Enable debug output



        return new JavaMailSenderImpl();
    }
}