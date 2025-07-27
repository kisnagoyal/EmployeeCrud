package com.example.employeeCrud.controllersTest;

import com.example.employeeCrud.controllers.EmployeeResolver;
import com.example.employeeCrud.model.Department;
import com.example.employeeCrud.model.Employee;
import com.example.employeeCrud.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeResolverTest {

    @InjectMocks
    private EmployeeResolver employeeResolver;

    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        Employee emp1 = new Employee(1L, "John", "john@example.com", 2, 60000f, null);
        Employee emp2 = new Employee(2L, "Jane", "jane@example.com", 3, 70000f, null);

        when(employeeService.getAllEmployees()).thenReturn(List.of(emp1, emp2));

        List<Employee> result = employeeResolver.employees();

        assertEquals(2, result.size());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetEmployeeById() {
        Employee emp = new Employee(1L, "John", "john@example.com", 2, 60000f, null);

        when(employeeService.getEmployee(1L)).thenReturn(emp);

        Employee result = employeeResolver.employee(1L);

        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(employeeService).getEmployee(1L);
    }

    @Test
    void testGetEmployeesByDepartment() {
        Department dept = new Department(1L, "Engineering", "Noida");
        Employee emp = new Employee(1L, "John", "john@example.com", 2, 60000f, dept);

        when(employeeService.getEmployeesByDepartment("Engineering", "Noida")).thenReturn(List.of(emp));

        List<Employee> result = employeeResolver.employeesByDepartment("Engineering", "Noida");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
        verify(employeeService).getEmployeesByDepartment("Engineering", "Noida");
    }

    @Test
    void testCreateEmployee() {
        Department dept = new Department(1L, "HR", "Noida");
        Employee emp = new Employee(1L, "Kisna", "kisna@example.com", 1, 55000f, dept);

        when(employeeService.createEmployee("Kisna", "kisna@example.com", 1, 1L)).thenReturn(emp);

        Employee result = employeeResolver.createEmployee("Kisna", "kisna@example.com", 1, 1L);

        assertEquals("Kisna", result.getName());
        verify(employeeService).createEmployee("Kisna", "kisna@example.com", 1, 1L);
    }

    @Test
    void testDeleteEmployee() {
        when(employeeService.deleteEmployee(1L)).thenReturn(true);

        boolean deleted = employeeResolver.deleteEmployee(1L);

        assertTrue(deleted);
        verify(employeeService).deleteEmployee(1L);
    }

    @Test
    void testUpdateEmployee() {
        Department dept = new Department(1L, "HR", "Noida");
        Employee emp = new Employee(1L, "Kisna", "kisna@example.com", 2, 60000f, dept);

        when(employeeService.updateEmployee(1L, "Kisna", "kisna@example.com", 2, 1L)).thenReturn(emp);

        Employee result = employeeResolver.updateEmployee(1L, "Kisna", "kisna@example.com", 2, 1L);

        assertNotNull(result);
        assertEquals("Kisna", result.getName());
        verify(employeeService).updateEmployee(1L, "Kisna", "kisna@example.com", 2, 1L);
    }

    @Test
    void testUpdateEmployee_Exception() {
        when(employeeService.updateEmployee(any(), any(), any(), any(), any())).thenThrow(new RuntimeException("error"));

        Employee result = employeeResolver.updateEmployee(1L, "Test", "test@test.com", 2, 1L);

        assertNull(result);
    }
}

