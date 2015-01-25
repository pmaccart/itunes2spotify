package com.itunes2spotify.api.config;

import com.itunes2spotify.api.security.JdbcSecurityContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("marissa").password("wombat").roles("USER").and().withUser("sam")
                .password("kangaroo").roles("USER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf().disable()
//                .securityContext().securityContextRepository(securityContextRepository())
//                .and()
                .authorizeRequests()
                .antMatchers("/api/spotify/**", "/spotify/**", "profile.html").hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login.html")
                .logoutUrl("/logout.do")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login.do")
                .failureUrl("/login.html?authentication_error=true")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .permitAll();

        // @formatter:on
    }

    @Bean
    SecurityContextRepository securityContextRepository() {
        return new JdbcSecurityContextRepository();
    }

}
