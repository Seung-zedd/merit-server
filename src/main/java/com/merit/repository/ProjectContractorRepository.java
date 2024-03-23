package com.merit.repository;

import com.merit.domain.bridge.ProjectContractor;
import com.merit.domain.enums.ProjectContractorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectContractorRepository extends JpaRepository<ProjectContractor, Long> {

    @Query("SELECT pc FROM ProjectContractor pc WHERE pc.status = :status")
    List<ProjectContractor> findAllByStatus(ProjectContractorStatus status);
}