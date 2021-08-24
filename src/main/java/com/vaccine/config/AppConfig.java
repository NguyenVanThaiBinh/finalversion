package com.vaccine.config;

import com.vaccine.fomatter.AdminDestinationFomatter;
import com.vaccine.service.admindestination.AdminDestinationService;
import com.vaccine.service.admindestination.IAdminDestinationService;
import com.vaccine.service.warehouseVaccine.IWarehouseVaccineService;
import com.vaccine.service.warehouseVaccine.WarehouseVaccineService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableSpringDataWebSupport
@EnableJpaRepositories("com.vaccine.repository")
@ComponentScan("com.vaccine")
public class AppConfig implements WebMvcConfigurer, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    //C?u hình JPA
    @Bean
    @Qualifier(value = "entityManager")
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.vaccine.model");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/vaccine_project?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false");
//        dataSource.setUsername("root");
//        dataSource.setPassword("admin");
//        return dataSource;
//    }

//Online database

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://vkh7buea61avxg07.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/z3rn8ljtlqxim1yb");
        dataSource.setUsername("u550jnhq3l1kt2vz");
        dataSource.setPassword("pxg6fk8bkixh3p9s");
        return dataSource;
    }
//        @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://s465z7sj4pwhp7fn.cbetxkdyhwsb.us-east-1.rds.amazonaws.com/jfzalr15be9nk065");
//        dataSource.setUsername("rlt59ipoclmob2az");
//        dataSource.setPassword("lqkl3bp7ak8ik0hn");
//        return dataSource;
//    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    public Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        return properties;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/template/**")
                .addResourceLocations("/template/")
                .resourceChain(false);

    }
    @Bean
    public JavaMailSender getMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("boyte.vaccine.covid@gmail.com");
        mailSender.setPassword("Binhhu123");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable","true");
        javaMailProperties.put("mail.smtp.auth","true");
        javaMailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
//        javaMailProperties.put("mail.transport.protocol","smtp");
        javaMailProperties.put("mail.debug","true");
        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }


    @Override
    public void addFormatters(FormatterRegistry registry){
        registry.addFormatter(new AdminDestinationFomatter(applicationContext.getBean(AdminDestinationService.class)));
    }

    @Bean
    public IAdminDestinationService adminDestinationService(){
        return new AdminDestinationService();
    }

    @Bean
    public IWarehouseVaccineService warehouseVaccineService(){
        return new WarehouseVaccineService();
    }

}
