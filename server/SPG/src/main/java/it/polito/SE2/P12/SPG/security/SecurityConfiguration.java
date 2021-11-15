package it.polito.SE2.P12.SPG.security;

import it.polito.SE2.P12.SPG.auth.UserDetailsServiceImpl;
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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public SecurityConfiguration(PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    //whitelist approach
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                //.antMatchers("/api/product/all").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()//.accessDeniedHandler(restAccessDeniedHandler())
                .authenticationEntryPoint(restAuthEntryPoint())
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("http://localhost:3000/ShopEmployee")
                .and()
                .logout()
                .logoutSuccessUrl("http://localhost:3000/")
                .deleteCookies("JSESSIONID").invalidateHttpSession(true);

    }


    @Bean
    public RestAccessDeniedHandler restAccessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl);
    }

    @Bean
    public RestAuthEntryPoint restAuthEntryPoint() {
        return new RestAuthEntryPoint();
    }



    /* HARDCODED USER */
    /*
    @Override
    @Bean
    protected UserDetailsService userDetailsService(){
        //ADMIN USER: ADMIN
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities()) //Here I get the authorities
                //based on the match between role and permissions
                //.roles(ApplicationUserRole.ADMIN.name()) //Internally is ROLE_ADMIN
                .build();
        //SIMPLE USER: USER
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .authorities(ApplicationUserRole.USER.getGrantedAuthorities()) //similar to above
                //.roles("USER") //Internally is ROLE_USER
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

     */


}
