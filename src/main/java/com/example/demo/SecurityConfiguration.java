package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private SSUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception
    {
        return new SSUserDetailsService(userRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .authorizeRequests()
                .antMatchers("/", "/h2-console/**", "/register").permitAll()//could put these in array (not arraylist)
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()//must be on its own line
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll()//after logout the user is taken to /login
                .and()
                .httpBasic();
        http
                .csrf().disable(); //only for H2 console, NOT IN PRODUCTION
        http
                .headers().frameOptions().disable(); //only for H2 console, NOT IN PRODUCTION
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
       // auth.inMemoryAuthentication().withUser("dave").password(encoder().encode("password")).authorities("ADMIN");
        auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(encoder());
    }
}



