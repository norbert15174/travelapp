package pl.travel.travelapp.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.travel.travelapp.exceptions.UnAuthException;
import pl.travel.travelapp.services.UserService;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


public class JwtFilter extends OncePerRequestFilter {

    @Value("${Algorithm-key}")
    private String key;

    UserService userService;

    public JwtFilter(UserService userAppService) {
        this.userService = userAppService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorization = httpServletRequest.getHeader("Authorization");

        try {
            UsernamePasswordAuthenticationToken authenticationToken = getUsernamePasswordAuthenticationToken(authorization);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch ( UnAuthException e ) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
        }
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String authorization) throws UnAuthException {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512("x!A%D*G-KaPdSgVkYp3s6v8y/B?E(H+MbQeThWmZq4t7w!z$C&F)J@NcRfUjXn2r")).build();
            DecodedJWT verify = jwtVerifier.verify(authorization.substring(7));
            String username = verify.getClaim("username").asString();
            String password = verify.getClaim("password").asString();
            UserDetails userDetails = userService.accountVerify(username, password);

            if (userDetails == null) throw new UnAuthException();

            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } catch (Exception e) {
            throw new UnAuthException();
        }


    }


}