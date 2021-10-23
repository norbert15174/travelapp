package pl.travel.travelapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.travel.travelapp.services.UserService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;

    @Autowired
    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.POST , "/auth/**")
                .antMatchers(HttpMethod.GET , "/auth/**")
                .antMatchers(HttpMethod.DELETE , "/auth/**")
                .antMatchers("/public/**")
                .antMatchers("/albums/user/{id}")
                .antMatchers("/albums/name")
                .antMatchers("/data/**")
                .antMatchers("/chat")
                .antMatchers("/chat/**")
                .antMatchers("/topic")
                .antMatchers("/topic/**")
                .antMatchers("/v2/api-docs" ,
                        "/configuration/ui" ,
                        "/swagger-resources/**" ,
                        "/configuration/security" ,
                        "/swagger-ui.html" ,
                        "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.cors().and().authorizeRequests().antMatchers("/**").permitAll();
//.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()).
        http.cors().and().authorizeRequests()
                .antMatchers("/user/picture").hasAnyRole("ADMIN")
                .antMatchers("/user/*").authenticated()
                .antMatchers("/photos/*").authenticated()
                .antMatchers("/friends/*").authenticated()
                .antMatchers("/albums/*").authenticated()
                .antMatchers("/resources").authenticated()
                .antMatchers("/group/*").authenticated()
                .antMatchers(HttpMethod.PUT , "/auth/password").authenticated()
                .antMatchers("/news/*").authenticated()
                .and()
                .addFilterBefore(new JwtFilter(userService) , UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable();

    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("*")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowedOriginPatterns("*")
                        .allowCredentials(true);
            }
        };
    }


}
