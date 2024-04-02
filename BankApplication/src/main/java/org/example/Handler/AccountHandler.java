package org.example.Handler;



import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.Controller.AccountContoller;
import org.example.Dao.AccountDao;
import org.example.Enum.TransactionStatus;
import org.example.Exception.AccountNotFoundException;
import org.example.Exception.DataNotFoundException;
import org.example.Exception.InsufficientBalanceException;
import org.example.Model.Account;
import org.example.Model.Transaction;
import org.example.descriptionDto.DescriptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AccountHandler {
   private static final Logger logger = LoggerFactory.getLogger(AccountHandler.class);

    @Autowired
    private AccountDao accountDao;

    public Transaction transferAmount(Long sourceAccountNumber, Long destinationAccountNumber, String description, double amount) {
        logger.info("START :: CLASS :: AccountHandler :: METHOD :: transferAmount");
        Account sourceAccount = accountDao.FindAccountNumber(sourceAccountNumber);
        Account destinationAccount = accountDao.FindAccountNumber(destinationAccountNumber);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setDate(LocalDate.now());
        transaction.setSourceAccountNo(sourceAccountNumber);
        transaction.setDestinationAccountNo(destinationAccountNumber);
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);

        try {
            if (sourceAccount.getBalance() >= amount) {
                sourceAccount.setBalance(sourceAccount.getBalance() - amount);
                destinationAccount.setBalance(destinationAccount.getBalance() + amount);
                transaction.setStatus(TransactionStatus.SUCCESSFUL);
                logger.info("Successfully transferred {} from account {} to account {}");
            } else {

                throw new InsufficientBalanceException("Insufficient balance in source account");
            }

            accountDao.saveTransaction(transaction);
            accountDao.updateBalance(sourceAccount);
            accountDao.updateBalance(destinationAccount);

            return transaction;

        } catch (AccountNotFoundException e) {
            transaction.setStatus(TransactionStatus.UNSUCCESSFUL);
            logger.error("Account not found", e);
            throw e;
        } catch (InsufficientBalanceException e) {
            transaction.setStatus(TransactionStatus.UNSUCCESSFUL);
            accountDao.saveTransaction(transaction);
            logger.error("Insufficient balance for the transfer", e);
            throw e;
        }
    }

    /**
     * Exports a list of transactions to an Excel file.
     *
     * @param transactions The list of transactions to be exported.
     * @param filePath     The file path where the Excel file will be saved.
     */
    public static void exportTransactionsToExcel(List<Transaction> transactions, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transaction1");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Transaction ID", "Amount", "Description", "Status", "Date", "Source Account", "Destination Account"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (int i = 0; i < transactions.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Transaction transaction = transactions.get(i);

            row.createCell(0).setCellValue(transaction.getTransactionId());
            row.createCell(1).setCellValue(transaction.getAmount());
            row.createCell(2).setCellValue(transaction.getDescription());
            row.createCell(3).setCellValue(transaction.getStatus().toString());
            row.createCell(4).setCellValue(transaction.getDate().toString());
            row.createCell(5).setCellValue(transaction.getSourceAccount().getAccountNo());
            row.createCell(6).setCellValue(transaction.getDestinationAccount().getAccountNo());
        }


        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Retrieves the credited amount, debited amount, and total amount for a specific account.
     *
     * @param accountNo The account number for which amount details need to be fetched.
     * @return A list containing credited amount, debited amount, and total amount in order.
     * @throws AccountNotFoundException If the specified account is not found.
     * @throws RuntimeException        If an error occurs while fetching amount details.
     */
    public List<Double> getAmountDetails(Long accountNo) {
        try {
            Double creditedAmount = accountDao.getCreditedAmount(accountNo);
            Double debitedAmount = accountDao.getDebitedAmount(accountNo);
            Double totalAmount = accountDao.getTotalAmount(accountNo);

            return Arrays.asList(creditedAmount, debitedAmount, totalAmount);
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching amount details for account " + accountNo, e);
        }
    }
    /**
     * Exports transaction details along with credited, debited, and total amounts to an Excel file.
     *
     * @param transactions  List of transactions to be exported.
     * @param amountDetails List containing credited amount, debited amount, and total amount in order.
     * @param filePath      The file path for the Excel file to be generated.
     * @throws AccountNotFoundException If the specified account is not found during export.
     * @throws RuntimeException        If an error occurs during the Excel file creation or writing process.
     */
    public void exportAmountDetailsToExcel(List<Transaction> transactions, List<Double> amountDetails, String filePath) {
        try {

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Details");

            Row transactionHeaderRow = sheet.createRow(0);
            String[] transactionHeaders = {"Transaction ID", "Amount", "Description", "Status", "Date", "Source Account", "Destination Account"};
            for (int i = 0; i < transactionHeaders.length; i++) {
                Cell cell = transactionHeaderRow.createCell(i);
                cell.setCellValue(transactionHeaders[i]);
            }


            for (int i = 0; i < transactions.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Transaction transaction = transactions.get(i);

                row.createCell(0).setCellValue(transaction.getTransactionId());
                row.createCell(1).setCellValue(transaction.getAmount());
                row.createCell(2).setCellValue(transaction.getDescription());
                row.createCell(3).setCellValue(transaction.getStatus().toString());
                row.createCell(4).setCellValue(transaction.getDate().toString());
                row.createCell(5).setCellValue(transaction.getSourceAccount().getAccountNo());
                row.createCell(6).setCellValue(transaction.getDestinationAccount().getAccountNo());
            }


            Row amountHeaderRow = sheet.createRow(transactions.size() + 2);
            String[] amountHeaders = {"Credited Amount", "Debited Amount", "Total Amount"};
            for (int i = 0; i < amountHeaders.length; i++) {
                Cell cell = amountHeaderRow.createCell(i);
                cell.setCellValue(amountHeaders[i]);
            }


            Row amountDataRow = sheet.createRow(transactions.size() + 3);
            for (int i = 0; i < amountDetails.size(); i++) {
                amountDataRow.createCell(i).setCellValue(amountDetails.get(i));
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                throw new RuntimeException("Error while writing Excel file", e);
            }

            try {
                workbook.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing workbook", e);
            }
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error while exporting details to Excel", e);
        }
    }

    /**
     * Retrieves a list of transactions for a specific account and year.
     *
     * @param accountNo The account number for which transactions need to be fetched.
     * @param year      The year for which transactions need to be retrieved.
     * @return A list of transactions for the specified account and year.
     * @throws DataNotFoundException If no transactions are found for the specified year.
     * @throws RuntimeException      If an error occurs while fetching transactions by year.
     */
    public List<Transaction> getTransactionsByYear(Long accountNo, int year) {
        try {
            List<Transaction> transactions = accountDao.getTransactionsByYear(accountNo, Year.of(year));
            if (transactions.isEmpty()) {
                throw new DataNotFoundException("No transactions found for the year: " + year);
            }

            return transactions;
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching transactions by year", e);
        }
    }
    /**
     * Exports transactions for a specific year to an Excel file.
     *
     * @param transactions List of transactions to be exported.
     * @param filePath     The file path for the Excel file to be generated.
     * @throws RuntimeException If an error occurs during the Excel file creation or writing process.
     */
    public void exportTransactionsByYearToExcel(List<Transaction> transactions, String filePath) {
        try {

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Transactions");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Transaction ID", "Amount", "Description", "Status", "Date", "Source Account", "Destination Account"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            for (int i = 0; i < transactions.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Transaction transaction = transactions.get(i);

                row.createCell(0).setCellValue(transaction.getTransactionId());
                row.createCell(1).setCellValue(transaction.getAmount());
                row.createCell(2).setCellValue(transaction.getDescription());
                row.createCell(3).setCellValue(transaction.getStatus().toString());
                row.createCell(4).setCellValue(transaction.getDate().toString());
                row.createCell(5).setCellValue(transaction.getSourceAccount().getAccountNo());
                row.createCell(6).setCellValue(transaction.getDestinationAccount().getAccountNo());
            }


            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                throw new RuntimeException("Error while writing Excel file", e);
            }

            try {
                workbook.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing workbook", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while exporting transactions by year to Excel", e);
        }
    }
    /**
     * Retrieves total amounts grouped by description for a specified account from the database.
     *
     * @param accountNo The account number for which total amounts are to be fetched by description.
     * @return List of DescriptionDTO containing description and total amount for the specified account.
     * @throws RuntimeException if an error occurs while fetching the data from the database.
     */
    public List<DescriptionDTO> getTotalAmountByDescription(Long accountNo) {
        try {
            return accountDao.getTotalAmountByDescription(accountNo);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching total amount by description", e);
        }
    }
    /**
     * Exports a list of DescriptionDTO to an Excel file.
     *
     * @param result   List of DescriptionDTO containing description and total amount.
     * @param filePath The file path where the Excel file will be generated.
     * @throws IOException if an error occurs while writing to the Excel file.
     */
    public void exportTotalAmountByDescriptionToExcel(List<DescriptionDTO> result, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Description");


            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Description");
            headerRow.createCell(1).setCellValue("Total Amount");


            int rowNum = 1;
            for (DescriptionDTO dto : result) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dto.getDescription());
                row.createCell(1).setCellValue(dto.getTotalAmount());
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportTransactionsToExcelInDifferentSheet(List<Long> accountNumbers, LocalDate fromDate, LocalDate toDate, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {

            for (int i = 0; i < accountNumbers.size(); i++) {
                Long accountNumber = accountNumbers.get(i);
                List<Transaction> transactions = accountDao.getTransactionsByAccountNo(accountNumber, fromDate, toDate);

                if (i < 2) {
                    // Create two sheets in the same workbook for the first two accounts
                    createSheetInWorkbook(workbook, "Account_" + accountNumber, transactions);
                } else {
                    // Create a new workbook for the next two accounts
                    try (Workbook separateWorkbook = new XSSFWorkbook()) {
                        createSheetInWorkbook(separateWorkbook, "Account_" + accountNumber, transactions);

                        try (FileOutputStream separateFileOut = new FileOutputStream(filePath.replace(".xlsx", "_separate.xlsx"))) {
                            separateWorkbook.write(separateFileOut);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSheetInWorkbook(Workbook workbook, String sheetName, List<Transaction> transactions) {
        Sheet sheet = workbook.createSheet(sheetName);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Transaction ID", "Amount", "Description", "Status", "Date", "Source Account", "Destination Account"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (int i = 0; i < transactions.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Transaction transaction = transactions.get(i);

            row.createCell(0).setCellValue(transaction.getTransactionId());
            row.createCell(1).setCellValue(transaction.getAmount());
            row.createCell(2).setCellValue(transaction.getDescription());
            row.createCell(3).setCellValue(transaction.getStatus().toString());
            row.createCell(4).setCellValue(transaction.getDate().toString());
            row.createCell(5).setCellValue(transaction.getSourceAccount().getAccountNo());
            row.createCell(6).setCellValue(transaction.getDestinationAccount().getAccountNo());
        }
    }
}

