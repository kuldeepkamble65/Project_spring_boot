package com.example.CloudManagementSystem.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserProductGstinMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapped_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "gstin_id")
    private Gstin gstin;
}
