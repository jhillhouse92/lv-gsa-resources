package com.longview.gsa.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

import com.longview.gsa.service.SimpleSocialUsersDetailService;

/**
 * Created by magnus on 18/08/14.
 */
@Configuration
@ComponentScan("com.longview.gsa")
@EnableWebMvcSecurity
@PropertySource("classpath:application.properties")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().url("jdbc:h2:mem:mydb").build();
    }
	
	@Autowired
	public DataSource dataSource;

    @Bean
    public JdbcTemplate getJdbcTemplate(){
      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
       return jdbcTemplate;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.
            jdbcAuthentication()
            .dataSource(dataSource)
            .withDefaultSchema();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/authenticate")
                .failureUrl("/login?param.error=bad_credentials")
                .permitAll()
        .and()
            .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
        .and()
            .authorizeRequests()
                .antMatchers("/favicon.ico", "/static-resources/**").permitAll()
                .antMatchers("/**").authenticated()
        .and()
            .rememberMe()
        .and()
            .apply(new SpringSocialConfigurer()
                .postLoginUrl("/")
                .alwaysUsePostLoginUrl(true));
    }

    @Bean
    public SocialUserDetailsService socialUsersDetailService() {
        return new SimpleSocialUsersDetailService(userDetailsService());
    }
}