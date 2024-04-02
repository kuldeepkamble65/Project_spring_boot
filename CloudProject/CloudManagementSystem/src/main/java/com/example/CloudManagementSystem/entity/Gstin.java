package com.example.CloudManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Gstin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gstinId;

    @Column(unique = true)
    private String gstinNo;

    @OneToMany(mappedBy = "gstin", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(mappedBy = "gstin", cascade = CascadeType.ALL)
    private List<UserProductGstinMapping> userProductGstinMappings = new ArrayList<>();

//    @OneToOne(mappedBy = "gstin")
//    private User owner;
}
