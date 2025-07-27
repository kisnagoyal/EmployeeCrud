package com.example.employeeCrud.model;

import com.example.employeeCrud.validations.ValidEmail;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data               // generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor  // generates default constructor
@AllArgsConstructor // generates all-args constructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ValidEmail
    @Column(unique = true)
    private String email;

    @Column(name = "years_of_experience")
    private int yearsOfExperience;

    @Column

    private float salary;

    @ManyToOne
    private Department department;
}
