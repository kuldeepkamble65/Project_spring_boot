package org.example.Model;

import lombok.Data;
import org.example.Enum.TransactionStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;
    private double amount;
    private String description;
    private TransactionStatus status ;
    private LocalDate Date;
    private long sourceAccountNo;
    private long destinationAccountNo;

    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;
}
