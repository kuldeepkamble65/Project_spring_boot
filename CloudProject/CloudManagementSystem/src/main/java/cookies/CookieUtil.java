package cookies;

import com.example.CloudManagementSystem.entity.SessionDetails;
import com.example.CloudManagementSystem.entity.User;
import com.example.CloudManagementSystem.service.SessionDetailsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalTime;
import java.util.UUID;

public class CookieUtil {
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    public static void addCookieForUser(HttpServletResponse response, User user, SessionDetailsService sessionDetailsService) {
        String sessionId = UUID.randomUUID().toString();
        addCookie(response, "sessionId", sessionId, 60 * 20);
        SessionDetails sessionDetails = new SessionDetails();
        sessionDetails.setSessionId(sessionId);
        sessionDetails.setCreationTime(LocalTime.now());
        sessionDetails.setUser(user);
        sessionDetailsService.saveSessionDetails(sessionDetails);

        addCookie(response, "name", user.getName(), 60);
        addCookie(response, "email", user.getEmail(), 60);
    }

    public static void delete(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    public static void clearCookies(HttpServletResponse response) {
        CookieUtil.delete(response, "sessionId");
        CookieUtil.delete(response, "name");
        CookieUtil.delete(response, "email");
    }
}
//    public static Cookie getCookieByName(HttpServletRequest request, String name) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(name)) {
//                    return cookie;
//                }
//            }
//        }
//        return null;
//    }
//
//}