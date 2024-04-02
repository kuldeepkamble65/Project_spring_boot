package com.example.CloudManagementSystem.controller;

import com.example.CloudManagementSystem.entity.Subscription;
import com.example.CloudManagementSystem.service.SubcriptionExcelReprtService;
import com.example.CloudManagementSystem.service.SubcriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Api(tags = "Subscription Management")
@RequestMapping("/subscriptions")
@RestController
public class SubcriptionController {

    private static final Logger LOGGER = LogManager.getLogger(SubcriptionController .class);
    @Autowired
    private SubcriptionService subscriptionService;

    @Autowired
    private SubcriptionExcelReprtService subcriptionExcelReprtService;

    @ApiOperation(value = "Get report of paid subscriptions expiring next month")
    @GetMapping("/report")
    public ResponseEntity<String> getReportOfPaidSub(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestHeader(value = "NDate", required = false) LocalDate date) {
        LOGGER.info("Request received to fetch report of paid subscriptions expiring next month");
        List<Subscription> subscriptions = subscriptionService.getReportOfPaidSub(date);

        String filePath = "/home/perennial/ExcelFile/Subcription_Report.xlsx";
        String reportGenerationResult = subcriptionExcelReprtService.generateSubscriptionReport(subscriptions, filePath);

        return new ResponseEntity<>(reportGenerationResult, HttpStatus.OK);
    }
}
