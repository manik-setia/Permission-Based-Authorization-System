package com.test.jwt;

import com.test.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
//OncePerRequestFilter -> to ensure the code is executed once per request.
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String header=request.getHeader("Authorization");
//        System.out.println("Authorization header: "+header);

        if(!hasAuthorizationHeader(request)){
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken=getAccessToken(request);

        if(!jwtTokenUtil.validateAccessToken(accessToken)){
            filterChain.doFilter(request, response);
            return;
        }

        //Now, access token is verified.
        setAuthenticationContext(accessToken, request);

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(String accessToken, HttpServletRequest request){
        UserDetails userDetails=getUserDetails(accessToken);

        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails, null, null);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    //constructs user object based on json web token
    private UserDetails getUserDetails(String accessToken){
        User userDetails=new User();
        String[] jwtSubject=jwtTokenUtil.getSubject(accessToken).split(",");

        userDetails.setId(Integer.parseInt(jwtSubject[0].trim()));
        userDetails.setEmail(jwtSubject[1]);

        return userDetails;
    }

    private String getAccessToken(HttpServletRequest request){
        String header=request.getHeader("Authorization");
        String token=header.split(" ")[1].trim();
//        System.out.println("Access token: "+token);
        return token;
    }

    private boolean hasAuthorizationHeader(HttpServletRequest request){
        String header=request.getHeader("Authorization");

        if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")){
            return false;
        }

        return true;
    }
}
