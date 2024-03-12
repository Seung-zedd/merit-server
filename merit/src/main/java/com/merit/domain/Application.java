package com.merit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPLICATION_ID")
    private Long id;

    @OneToMany(mappedBy = "application")
    @Builder.Default
    private List<ProjectContractor> projectContractors = new ArrayList<>();

    @OneToOne(mappedBy = "application")
    @Setter(AccessLevel.NONE)
    private Contractor contractor;
}
