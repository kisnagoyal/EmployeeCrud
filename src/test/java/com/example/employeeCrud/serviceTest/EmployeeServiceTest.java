package com.example.employeeCrud.serviceTest;

import com.example.employeeCrud.model.Department;
import com.example.employeeCrud.model.Employee;
import com.example.employeeCrud.repository.DepartmentRepository;
import com.example.employeeCrud.repository.EmployeeRepository;
import com.example.employeeCrud.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private DepartmentRepository departmentRepo;

    @InjectMocks
    private EmployeeService employeeService;

    private Department engineeringDept;
    private Employee employee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        engineeringDept = Department.builder()
                .id(1L)
                .name("engineering")
                .location("bangalore")
                .build();

        employee = Employee.builder()
                .id(1L)
                .name("Kisna")
                .email("kisna@example.com")
                .yearsOfExperience(2)
                .department(engineeringDept)
                .salary(73500)
                .build();
    }

    @Test
    public void testGetAllEmployees() {
        when(employeeRepo.findAll()).thenReturn(List.of(employee));
        List<Employee> result = employeeService.getAllEmployees();
        assertEquals(1, result.size());
        verify(employeeRepo, times(1)).findAll();
    }

    @Test
    public void testGetEmployee() {
        when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));
        Employee result = employeeService.getEmployee(1L);
        assertNotNull(result);
        assertEquals("Kisna", result.getName());
    }

    @Test
    public void testCreateEmployeeValid() {
        when(departmentRepo.findById(1L)).thenReturn(Optional.of(engineeringDept));
        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.createEmployee("Kisna", "kisna@example.com", 2, 1L);
        assertNotNull(result);
        assertEquals("Kisna", result.getName());
    }

    @Test
    public void testCreateEmployeeInvalidExperience() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                employeeService.createEmployee("Kisna", "kisna@example.com", -1, 1L));

        assertEquals("Years of experience must be greater than or equal to 0", exception.getMessage());
    }

    @Test
    public void testDeleteEmployeeExists() {
        when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));
        boolean result = employeeService.deleteEmployee(1L);
        assertTrue(result);
        verify(employeeRepo).deleteById(1L);
    }

    @Test
    public void testDeleteEmployeeNotExists() {
        when(employeeRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> employeeService.deleteEmployee(2L));
    }

    @Test
    public void testUpdateEmployeeExperienceAndDept() {
        Department hr = Department.builder().id(2L).name("hr").location("noida").build();

        when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));
        when(departmentRepo.findById(2L)).thenReturn(Optional.of(hr));
        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);

        Employee updated = employeeService.updateEmployee(1L, null, null, 4, 2L);
        assertNotNull(updated);
        assertEquals(4, updated.getYearsOfExperience());
        assertEquals("hr", updated.getDepartment().getName());
    }

    @Test
    public void testGetEmployeesByDepartment() {
        when(departmentRepo.findByNameAndLocation("engineering", "bangalore"))
                .thenReturn(Optional.of(engineeringDept));
        when(employeeRepo.findByDepartment(engineeringDept)).thenReturn(List.of(employee));

        List<Employee> result = employeeService.getEmployeesByDepartment("engineering", "bangalore");
        assertEquals(1, result.size());
        assertEquals("Kisna", result.get(0).getName());
    }

    @Test
    public void testGetEmployeesByDepartmentNotFound() {
        when(departmentRepo.findByNameAndLocation("nonexistent", "nowhere"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                employeeService.getEmployeesByDepartment("nonexistent", "nowhere"));
    }
}
