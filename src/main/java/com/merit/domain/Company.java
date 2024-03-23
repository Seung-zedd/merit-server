package com.merit.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.merit.domain.embedded.Address;
import com.merit.domain.enums.CompanyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Getter
public class Company extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPANY_ID")
    private Long id;

    @Column(name = "COMPANY_NAME")
    private String name;
    @Column(nullable = false)
    private String email;
    private String website;

    private String contactNumber;

    @Embedded
    private Address address;
    private String about;

    @Enumerated(EnumType.STRING)
    @Setter
    private CompanyStatus status;

    @OneToMany(mappedBy = "company")
    @JsonManagedReference
    @Builder.Default
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    @JsonManagedReference
    @Builder.Default
    private List<Contractor> contractors = new ArrayList<>();

    // * INVITATION_SENT: gmail SMTP 서비스를 사용해서 메일을 보낸 다음에 상태를 변경시켜야함
    public void changeStatus(CompanyStatus newStatus) {
        this.setStatus(newStatus);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + getId() + ", " +
                "createdOn = " + getCreatedOn() + ", " +
                "modifiedOn = " + getModifiedOn() + ", " +
                "name = " + getName() + ", " +
                "email = " + getEmail() + ", " +
                "website = " + getWebsite() + ", " +
                "contactNumber = " + getContactNumber() + ", " +
                "address = " + getAddress() + ", " +
                "about = " + getAbout() + ", " +
                "status = " + getStatus() + ")";
    }
}
