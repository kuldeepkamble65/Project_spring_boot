package com.example.CloudManagementSystem.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class SubscriptionDataDto {

    private String plan;

    private String price;

    private Integer gstinId;

}