package com.ds.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = 500)
    private String name;


    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "password")
    @Size(min = 8, max = 500)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Email> emails;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Phone> phones;

    @OneToOne(mappedBy = "user")
    private Account account;
}
