//package com.example.CloudManagementSystem.interceptor;
//
//import com.example.CloudManagementSystem.entity.SessionDetails;
//import com.example.CloudManagementSystem.repository.SessionDetailRepository;
//import com.example.CloudManagementSystem.service.SessionDetailsService;
//import cookies.CookieUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpRequest;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.temporal.ChronoUnit;
//
//@Component
//public class Interceptor implements HandlerInterceptor {
//
//   @Autowired
//   private SessionDetailsService sessionDetailsService;
//
//
////   private Interceptor(SessionDetailsService service){
////       this.sessionDetailsService = service;
////   }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String sessionId = extractSessionId(request);
//
//
//        if (sessionId != null) {
//
//            SessionDetails sessionDetails = sessionDetailsService.findBysessionId(sessionId);
//
//            if (sessionDetails != null && !isExpired(sessionDetails)) {
//
//                return true;
//            } else {
//
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Session has expired");
//                return false;
//            }
//        }
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("SessionId not found");
//        return false;
//    }
//
//    private String extractSessionId(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("sessionId".equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//
//    private boolean isExpired(SessionDetails sessionDetails) {
//        LocalTime expirationTime = sessionDetails.getCreationTime().plus(20, ChronoUnit.MINUTES);
//        return LocalTime.now().isAfter(expirationTime);
//    }
//}
//
