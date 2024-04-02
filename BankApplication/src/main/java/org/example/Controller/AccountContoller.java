package org.example.Controller;


import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.example.Dao.AccountDaoImpl;
import org.example.Exception.AccountNotFoundException;
import org.example.Exception.DataNotFoundException;
import org.example.Exception.InsufficientBalanceException;
import org.example.Handler.AccountHandler;
import org.example.Model.Transaction;
import org.example.descriptionDto.DescriptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
public class AccountContoller {

    private static final Logger LOGGER = LogManager.getLogger(AccountContoller.class);

    @Autowired
    private AccountHandler accountHandler;

    @Autowired
    private AccountDaoImpl accountDao;

    @RequestMapping("/")
    public String indexPage() {
        return "index";
    }

    /**
     * Transfers the specified amount from the source account to the destination account.
     *
     * @param sourceAccountNumber      Account number of the source account
     * @param destinationAccountNumber Account number of the destination account
     * @param description              Description for the transaction
     * @param amount                   Amount to be transferred
     * @return The transaction details if successful
     */
    @PostMapping("/transfer")
    public ResponseEntity<String> transferAmount(@RequestHeader("sourceAccountNumber") Long sourceAccountNumber,
                                                 @RequestHeader("destinationAccountNumber") Long destinationAccountNumber,
                                                 @RequestHeader("description") String description,
                                                 @RequestHeader("amount") double amount) {
        try {
            accountHandler.transferAmount(sourceAccountNumber, destinationAccountNumber, description, amount);
            LOGGER.info("Transaction successful");
            return ResponseEntity.ok("Transaction successful");
        } catch (InsufficientBalanceException e) {
            LOGGER.error("Insufficient balance for the transfer: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AccountNotFoundException e) {
            LOGGER.error("Account not found: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Internal Server Error: {}", e.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }


    /**
     * Exports transactions to an Excel file for a specific account within a given date range.
     *
     * @param accountNo The account number for which transactions need to be exported.
     * @param fromDate  The start date of the date range.
     * @param toDate    The end date of the date range.
     * @return ResponseEntity with a success message upon successful export.
     */
    @GetMapping("/export-transactions")
    public ResponseEntity<String> exportTransactionsToExcel(
            @RequestHeader("accountNo") Long accountNo,
            @RequestHeader("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestHeader("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<Transaction> transactions = accountDao.getTransactionsByAccountNo(accountNo, fromDate, toDate);
        String filePath = "/home/perennial/ExcelFile/transactions.xlsx";
        accountHandler.exportTransactionsToExcel(transactions, filePath);
        return ResponseEntity.ok("Transactions exported to Excel");
    }


    /**
     * Exports transaction and amount details to an Excel file for a specific account within a given date range.
     *
     * @param accountNo The account number for which details need to be exported.
     * @param fromDate  The start date of the date range.
     * @param toDate    The end date of the date range.
     * @return ResponseEntity with a success message upon successful export or appropriate error response.
     */
    @GetMapping("/amount-details")
    public ResponseEntity<String> exportAmountDetailsToExcel(
            @RequestHeader("accountNo") Long accountNo,
            @RequestHeader("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
             @RequestHeader("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        try {
            List<Transaction> transactions = accountDao.getTransactionsByAccountNo(accountNo, fromDate, toDate);
            List<Double> amountDetails = accountHandler.getAmountDetails(accountNo);
            String filePath = "/home/perennial/ExcelFile/transactions.xlsx";
            accountHandler.exportAmountDetailsToExcel(transactions, amountDetails, filePath);
            return ResponseEntity.ok("Amount details exported to Excel");
        } catch (AccountNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    /**
     * Exports transactions for a specific year to an Excel file.
     *
     * @param accountNo The account number for which transactions need to be exported.
     * @param year      The year for which transactions need to be retrieved.
     * @return ResponseEntity indicating the success or failure of the export operation.
     *         If successful, returns OK with a message. If not found, returns NOT_FOUND with an error message.
     *         If an internal server error occurs, returns INTERNAL_SERVER_ERROR with an error message.
     */
    @GetMapping("/transactionsByYear")
    public ResponseEntity<String> exportTransactionsByYearToExcel(
            @RequestHeader("accountNo") Long accountNo,
            @RequestHeader("year") int year) {
        try {
            List<Transaction> transactions = accountHandler.getTransactionsByYear(accountNo, year);
            String filePath = "/home/perennial/ExcelFile/transactions_by_year.xlsx";
            accountHandler.exportTransactionsByYearToExcel(transactions, filePath);
            return ResponseEntity.ok("Transactions exported to Excel for the year " + year);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
    /**
     * Retrieves total amounts grouped by description for a specified account and exports the data to an Excel file.
     *
     * @param accountNo The account number for which total amounts are to be fetched by description.
     * @return ResponseEntity indicating the success or failure of the operation.
     *         If successful, returns OK with a message. If an internal server error occurs, returns INTERNAL_SERVER_ERROR with an error message.
     */
    @GetMapping("/totalAmountByDescription")
    public ResponseEntity<String> getTotalAmountByDescription(@RequestHeader("accountNo") Long accountNo) {
        try {
            List<DescriptionDTO> result = accountHandler.getTotalAmountByDescription(accountNo);
            String filePath = "/home/perennial/ExcelFile/transactions.xlsx";
            accountHandler.exportTotalAmountByDescriptionToExcel(result, filePath);
            return ResponseEntity.ok("Excel file generated for description.... ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/export-transactionsfordiffernetsheet")
    public ResponseEntity<String> exportTransactionsToExcel(
            @RequestHeader("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestHeader("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        List<Long> allAccountNumbers = accountDao.getAllAccountNumbers();

        if (allAccountNumbers.isEmpty()) {
            return ResponseEntity.ok("No accounts found to export");
        }

        String filePath = "/home/perennial/ExcelFile/multiple_accounts_transactions.xlsx";
        accountHandler.exportTransactionsToExcelInDifferentSheet(allAccountNumbers, fromDate, toDate, filePath);

        return ResponseEntity.ok("Transactions exported to Excel");
    }

}


