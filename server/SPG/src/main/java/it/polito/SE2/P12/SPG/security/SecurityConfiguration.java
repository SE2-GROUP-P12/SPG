package it.polito.SE2.P12.SPG.security;

import it.polito.SE2.P12.SPG.auth.UserDetailsServiceImpl;
import it.polito.SE2.P12.SPG.filter.CustomAuthenticationFilter;
import it.polito.SE2.P12.SPG.filter.CustomAuthorizationFilter;
import it.polito.SE2.P12.SPG.service.JWTUserHandlerService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private final JWTUserHandlerService jwtUserHandlerService;
    @Autowired
    private final SpgUserService spgUserService;

    @Autowired
    public SecurityConfiguration(PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsServiceImpl, JWTUserHandlerService jwtUserHandlerService, SpgUserService spgUserService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtUserHandlerService = jwtUserHandlerService;
        this.spgUserService = spgUserService;
    }

    //whitelist approach
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Go inside UsernamePasswordAuthenticationFilter class to set up login api path and method
        //In that case I override that variable
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(jwtUserHandlerService, spgUserService, authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(jwtUserHandlerService, spgUserService), UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/api/feedback").permitAll() //TODO: implement security config for that pai
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/token/refresh").permitAll()
                .antMatchers("/api" + API.EXIST_CUSTOMER_BY_EMAIL).permitAll()
                .antMatchers("/api" + API.EXIST_CUSTOMER).permitAll()
                .antMatchers("/api" + API.CREATE_CUSTOMER).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthEntryPoint())
                .and()
                .logout()
                .logoutSuccessUrl("http://localhost:3000/")
                .deleteCookies("JSESSIONID", "Authorization").invalidateHttpSession(true);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
    }

    @Bean
    public RestAuthEntryPoint restAuthEntryPoint() {
        return new RestAuthEntryPoint();
    }

}
