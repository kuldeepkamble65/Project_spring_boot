package com.example.CloudManagementSystem.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
public class SessionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private String sessionId;
    private LocalTime creationTime;

    @ManyToOne
    private User user;


}
