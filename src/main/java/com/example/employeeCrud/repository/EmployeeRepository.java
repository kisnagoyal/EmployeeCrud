package com.example.employeeCrud.repository;

import com.example.employeeCrud.model.Employee;
import com.example.employeeCrud.model.Department;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // This interface will automatically inherit methods for CRUD operations
    // from JpaRepository, such as save(), findById(), findAll(), deleteById(), etc.
    List<Employee> findByDepartment(Department department);
}
