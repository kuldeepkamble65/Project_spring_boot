package org.example.Dao;

import org.example.Model.Account;
import org.example.Model.Transaction;
import org.example.descriptionDto.DescriptionDTO;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public interface AccountDao {
    Account FindAccountNumber(Long accountNumber);

    void updateBalance(Account account);

    void saveTransaction(Transaction transaction);

    List<Transaction> getTransactionsByAccountNo(Long accountNo, LocalDate fromDate, LocalDate toDate);

    Double getCreditedAmount(Long accountNo);

    Double getDebitedAmount(Long accountNo);

    Double getTotalAmount(Long accountNo);

    List<Transaction> getTransactionsByYear(Long accountNo, Year year);

    List<DescriptionDTO> getTotalAmountByDescription(Long accountNo);

    List<Long> getAllAccountNumbers();
}
