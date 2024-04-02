package com.example.CloudManagementSystem.service;

import com.example.CloudManagementSystem.entity.Subscription;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface SubcriptionService {

    List<Subscription> getReportOfPaidSub(LocalDate date);
}
