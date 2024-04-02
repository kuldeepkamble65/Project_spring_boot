package com.example.CloudManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int subcriptionId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String plan;

    private String price;

    public Subscription() {
        this.paymentStatus = false;
    }

    private boolean paymentStatus;

    @ManyToOne
    @JoinColumn(name = "gstin_id")
    @JsonIgnore
    private Gstin gstin;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionId=" + subcriptionId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", plan='" + plan + '\'' +
                ", price='" + price + '\'' +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
