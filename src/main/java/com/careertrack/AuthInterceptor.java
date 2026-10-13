
package com.careertrack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
    	System.out.println("Cookie header: " + request.getHeader("Cookie"));

        HttpSession session = request.getSession(false);
        System.out.println("Session ID: " +
        	    (session == null ? "null" : session.getId()));

        	System.out.println("User ID: " +
        	    (session == null ? "null" : session.getAttribute("userId")));

        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"error\":\"Please log in first\"}"
            );
            return false;
        }

        return true;
    }
}
