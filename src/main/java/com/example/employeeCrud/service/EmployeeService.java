package com.example.employeeCrud.service;


import com.example.employeeCrud.model.Employee;
import com.example.employeeCrud.model.Department;
import com.example.employeeCrud.repository.EmployeeRepository;
import com.example.employeeCrud.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private DepartmentRepository departmentRepo;

    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    public Employee getEmployee(Long id) {
        return employeeRepo.findById(id).orElse(null);
    }

    public Employee createEmployee(String name, String email, int yearsOfExperience, Long deptId) {
        if (yearsOfExperience < 0) {
            throw new IllegalArgumentException("Years of experience must be greater than or equal to 0");
        }
        Department dept = departmentRepo.findById(deptId).orElseThrow();
        float salary = computeSalary(yearsOfExperience,dept);
        Employee e = Employee.builder()
                .name(name)
                .email(email)
                .yearsOfExperience(yearsOfExperience)
                .department(dept)
                .salary(salary)
                .build();
        return employeeRepo.save(e);
    }

    public float computeSalary(int yearsOfExperience, Department department) {
        float base = 50000;
        float multiplier = 1 + (yearsOfExperience * 0.05f);
        float bonus = switch (department.getName().toLowerCase()) {
            case "engineering" -> 20000;
            case "hr" -> 10000;
            default -> 5000;
        };
        return (base + bonus) * multiplier;
    }

    // other methods...

    public boolean deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepo.findById(id);
        try{
            if (employee.isPresent()) {
                employeeRepo.deleteById(id);
                return true;
            } else {
                throw new RuntimeException("Employee not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting employee: " + e.getMessage());
        }
    }

    public Employee updateEmployee(Long id, String name, String email, Integer yearsOfExperience, Long deptId) {
        Employee e = employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        boolean recalculateSalary = false;

        if (name != null && !name.isBlank()) {
            e.setName(name);
        }

        if (email != null && !email.isBlank()) {
            e.setEmail(email);
        }

        if (yearsOfExperience != null) {
            if (yearsOfExperience < 0) {
                throw new IllegalArgumentException("Years of experience must be greater than or equal to 0");
            }
            e.setYearsOfExperience(yearsOfExperience);
            recalculateSalary = true;
        }

        if (deptId != null) {
            Department newDept = departmentRepo.findById(deptId)
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            e.setDepartment(newDept);
            recalculateSalary = true;
        }

        if (recalculateSalary) {
            float salary = computeSalary(e.getYearsOfExperience(), e.getDepartment());
            e.setSalary(salary);
        }

        return employeeRepo.save(e);
    }

    public List<Employee> getEmployeesByDepartment(String name, String location) {
        Optional<Department> dept = departmentRepo.findByNameAndLocation(name, location);
        if (dept.isPresent()) {
            return employeeRepo.findByDepartment(dept.get());
        } else {
            throw new RuntimeException("Department not found");
        }
    }

}
