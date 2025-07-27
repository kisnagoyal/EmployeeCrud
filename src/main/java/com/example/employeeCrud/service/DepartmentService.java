package com.example.employeeCrud.service;

import com.example.employeeCrud.model.Department;
import com.example.employeeCrud.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepo;

    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    public Department getDepartment(Long id) {
        return departmentRepo.findById(id).orElse(null);
    }

    public Department createDepartment(String name, String location) {
        Optional<Department> existing = departmentRepo.findByNameAndLocation(name, location);
        if (existing.isPresent()) {
            throw new RuntimeException("Department with this name and location already exists");
        }

        Department dept = Department.builder()
                .name(name)
                .location(location)
                .build();
        return departmentRepo.save(dept);
    }

    public Department updateDepartment(Long id,String name, String location) {
        Department dept = departmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

       try{
           if (location != null && !location.isBlank()) {
               dept.setLocation(location);
           }
           if (name != null && !name.isBlank()) {
               dept.setName(name);
           }
       } catch (Exception e) {
           throw new RuntimeException(e.getMessage() + " - Error updating department");
       }


        return departmentRepo.save(dept);
    }

    public boolean deleteDepartment(Long id) {
        if (departmentRepo.existsById(id)) {
            departmentRepo.deleteById(id);
            return true;
        }
        return false;
    }
}

