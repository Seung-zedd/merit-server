package com.merit.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public abstract class BaseEntity {

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime modifiedOn;

    @PrePersist
    public void prePersist() {
        log.info("prePersist");
        LocalDateTime now = LocalDateTime.now();
        createdOn = now;
        modifiedOn = now;
    }

    @PreUpdate
    public void preUpdate() {
        log.info("preUpdate");
        modifiedOn = LocalDateTime.now();
    }
}
