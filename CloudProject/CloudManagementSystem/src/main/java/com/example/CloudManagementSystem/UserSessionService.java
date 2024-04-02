package com.example.CloudManagementSystem;

import com.example.CloudManagementSystem.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserSessionService {

    private final Map<String, User> userSessions = new HashMap<>();

    public void storeSession(String sessionId, User user) {
        userSessions.put(sessionId, user);
    }

    public User getUserBySessionId(String sessionId) {
        return userSessions.get(sessionId);
    }
}