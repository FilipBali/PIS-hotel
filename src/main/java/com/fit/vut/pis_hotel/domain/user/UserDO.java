package com.fit.vut.pis_hotel.domain.user;

import com.fit.vut.pis_hotel.domain.role.RoleDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email_unique", columnNames = "email")
        }
)
public class UserDO {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "TEXT"
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
            columnDefinition = "TEXT"
    )
    private String address;

    @Column(
            name = "phone_number",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String phoneNumber;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<RoleDO> roles = new ArrayList<>();

    public UserDO(String firstName, String lastName, LocalDate dateOfBirth, String address, String idNumber, String phoneNumber, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

}
