package com.example.CloudManagementSystem.repository;

import com.example.CloudManagementSystem.entity.SessionDetails;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface SessionDetailRepository extends JpaRepository<SessionDetails, Integer> {

   SessionDetails findBySessionId (String sessionId);

   void deleteByCreationTimeBefore(LocalTime creationTime);
}
