package com.acme.insurance.infrastructure.repository;

import com.acme.insurance.domain.model.PolicyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface PolicyRequestRepository extends JpaRepository<PolicyRequest, UUID> {

    List<PolicyRequest> findByCustomerId(UUID customerId);

    @Query("SELECT pr FROM PolicyRequest pr LEFT JOIN FETCH pr.history WHERE pr.id = :id")
    Optional<PolicyRequest> findByIdWithHistory(@Param("id") UUID id);
}