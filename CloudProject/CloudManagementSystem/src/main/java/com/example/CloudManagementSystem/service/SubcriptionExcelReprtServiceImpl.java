package com.example.CloudManagementSystem.service;

import com.example.CloudManagementSystem.entity.Subscription;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class SubcriptionExcelReprtServiceImpl implements SubcriptionExcelReprtService{
    @Override
    public String generateSubscriptionReport(List<Subscription> subscriptions, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Subscription Report");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Subscription ID");
        headerRow.createCell(1).setCellValue("Start Date");
        headerRow.createCell(2).setCellValue("End Date");
        headerRow.createCell(3).setCellValue("Payment Status");

        int rowNum = 1;
        for (Subscription subscription : subscriptions) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(subscription.getSubcriptionId());
            row.createCell(1).setCellValue(subscription.getStartDate().toString());
            row.createCell(2).setCellValue(subscription.getEndDate().toString());
            row.createCell(3).setCellValue(subscription.isPaymentStatus() ? "Paid" : "Unpaid");
        }
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            return "Excel report generated successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to generate Excel report ";
        }
    }
}
