package com.acme.insurance.infrastructure.repository;

import com.acme.insurance.domain.model.PolicyRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PolicyRequestRepository extends JpaRepository<PolicyRequest, UUID> {

    List<PolicyRequest> findByCustomerId(UUID customerId);
}
