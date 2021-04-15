package pl.travel.travelapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

 //       http.cors().and().authorizeRequests().antMatchers("/**").permitAll();

        http.cors().and().authorizeRequests()
                .antMatchers("/user/picture").authenticated()
                .and()
                .addFilterBefore(new JwtFilter(userService), UsernamePasswordAuthenticationFilter.class);


        http.csrf().disable();

    }

}
