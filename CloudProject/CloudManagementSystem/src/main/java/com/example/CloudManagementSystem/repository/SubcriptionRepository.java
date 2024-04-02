package com.example.CloudManagementSystem.repository;

import com.example.CloudManagementSystem.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubcriptionRepository extends JpaRepository<Subscription, Integer> {
    Subscription findById(int subcriptionId);

    @Query("SELECT s FROM Subscription s WHERE s.paymentStatus = true AND s.endDate BETWEEN :startDate AND :endDate")
    List<Subscription> findPaidSubscriptionsExpiringNextMonth(LocalDate startDate, LocalDate endDate);
}
