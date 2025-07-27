package com.example.employeeCrud.repository;

import com.example.employeeCrud.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByNameAndLocation(String name, String location);

}
