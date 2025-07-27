package com.example.employeeCrud.controllers;

import com.example.employeeCrud.model.Department;
import com.example.employeeCrud.model.Employee;
import com.example.employeeCrud.service.DepartmentService;
import com.example.employeeCrud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DepartmentResolver {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    @QueryMapping
    public List<Department> departments() {
        return departmentService.getAllDepartments();
    }

    @QueryMapping
    public Department department(@Argument Long id) {
        return departmentService.getDepartment(id);
    }

    @MutationMapping
    public Department createDepartment(@Argument String name, @Argument String location) {
        return departmentService.createDepartment(name, location);
    }

    @MutationMapping
    public Department updateDepartment(@Argument Long id,@Argument String name, @Argument String location) {
        return departmentService.updateDepartment(id,name, location);
    }



    @MutationMapping
    public boolean deleteDepartment(@Argument Long id) {
        return departmentService.deleteDepartment(id);
    }


}
