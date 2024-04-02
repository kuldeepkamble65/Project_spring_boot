package com.example.CloudManagementSystem.service;

import com.example.CloudManagementSystem.entity.Subscription;
import com.example.CloudManagementSystem.repository.SubcriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

@Service
public class SubcriptionServiceImpl implements SubcriptionService{
    @Autowired
    private SubcriptionRepository subcriptionRepository;
    @Override
    public List<Subscription> getReportOfPaidSub(LocalDate date) {
        LocalDate startDate;
        LocalDate endDate;

        if (date == null) {
            startDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);
            endDate = YearMonth.from(startDate).atEndOfMonth();
        }else {
            startDate = date.withDayOfMonth(1);
            endDate = date.withDayOfMonth(date.lengthOfMonth());
        }
        return subcriptionRepository.findPaidSubscriptionsExpiringNextMonth(startDate, endDate);
    }
}
