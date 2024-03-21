package com.merit.repository;

import com.merit.domain.Contractor;
import com.merit.domain.ContractorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContractorRepository extends JpaRepository<Contractor, Long> {

    @Query("SELECT c FROM Contractor c WHERE c.status = :status")
    List<Contractor> findAllByStatus(ContractorStatus status);

    Contractor findByContractorEmailIgnoreCase(String emailId);

    Boolean existsByContractorEmail(String email);

}
