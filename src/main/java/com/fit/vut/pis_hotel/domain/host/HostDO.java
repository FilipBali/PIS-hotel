package com.fit.vut.pis_hotel.domain.host;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "host_email_unique", columnNames = "email")
        }
)
public class HostDO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String lastName;


    @Column(
            name = "date_of_birth",
            nullable = false
    )
    private LocalDate dateOfBirth;

    @Column(
            name = "address",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String address;

    @Column(
            name = "id_number",
            nullable = false,
            columnDefinition = "VARCHAR(10)"
    )
    private String idNumber;

    @Column(
            name = "phone_number",
            nullable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String phoneNumber;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String email;

    public HostDO(String firstName, String lastName, LocalDate dateOfBirth, String address, String idNumber, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

}
