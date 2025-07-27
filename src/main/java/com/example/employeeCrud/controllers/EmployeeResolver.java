package com.example.employeeCrud.controllers;

import com.example.employeeCrud.model.Employee;
import com.example.employeeCrud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;

@Component
@Controller
public class EmployeeResolver {

    @Autowired
    private EmployeeService employeeService;

    @QueryMapping
    public List<Employee> employees() {
        return employeeService.getAllEmployees();
    }

    @QueryMapping
    public Employee employee(@Argument Long id) {
        return employeeService.getEmployee(id);
    }

    @QueryMapping
    public List<Employee> employeesByDepartment(@Argument String name, @Argument String location) {
        return employeeService.getEmployeesByDepartment(name, location);

    }

    @MutationMapping
    public Employee createEmployee(
            @Argument String name,
            @Argument String email,
            @Argument int yearsOfExperience,
            @Argument Long departmentId
    ) {
        return employeeService.createEmployee(name, email, yearsOfExperience, departmentId);
    }

    // similarly for update/delete
    @MutationMapping
    public boolean deleteEmployee(@Argument Long id) {
        return employeeService.deleteEmployee(id);
    }


    @MutationMapping
    public Employee updateEmployee(@Argument Long id,
            @Argument String name,
            @Argument String email,
            @Argument Integer yearsOfExperience,
            @Argument Long departmentId) {
        try {
            return employeeService.updateEmployee(id, name, email, yearsOfExperience, departmentId);
        } catch (Exception e) {
            // Optionally log or return null gracefully
            return null;
        }
    }


}

