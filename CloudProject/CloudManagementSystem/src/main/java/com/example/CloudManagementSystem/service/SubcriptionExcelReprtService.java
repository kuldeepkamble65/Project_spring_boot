package com.example.CloudManagementSystem.service;

import com.example.CloudManagementSystem.entity.Subscription;

import java.util.List;

public interface SubcriptionExcelReprtService {
    public String generateSubscriptionReport(List<Subscription> subscriptions, String filePath);
}
