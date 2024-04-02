package org.example.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;
    private String accountName;

    private long accountNo;

    private double balance;

    private String email;

    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL)
    private List<Transaction> sourceTransactions;

    @OneToMany(mappedBy = "destinationAccount", cascade = CascadeType.ALL)
    private List<Transaction> destinationTransactions;
}
