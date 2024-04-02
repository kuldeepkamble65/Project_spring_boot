package com.example.CloudManagementSystem.service;

import com.example.CloudManagementSystem.entity.SessionDetails;
import com.example.CloudManagementSystem.repository.SessionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
public class SessionDetailsService {

    @Autowired
    private SessionDetailRepository sessionDetailRepository;

    @Scheduled(cron = "0 */20 * * * *")
    @Transactional
    public void cleanExpiredSessions() {
        LocalTime twentyMinutesAgo = LocalTime.now().minus(20, ChronoUnit.MINUTES);
        sessionDetailRepository.deleteByCreationTimeBefore(twentyMinutesAgo);
    }
    public void saveSessionDetails(SessionDetails sessionDetails) {

        sessionDetailRepository.save(sessionDetails);

    }
    public SessionDetails findBysessionId(String sessionId) {
        System.out.println("1");
        return sessionDetailRepository.findBySessionId(sessionId);
    }
}