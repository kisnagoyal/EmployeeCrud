package com.example.employeeCrud.controllersTest;

import com.example.employeeCrud.controllers.DepartmentResolver;
import com.example.employeeCrud.model.Department;
import com.example.employeeCrud.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentResolverTest {

    @InjectMocks
    private DepartmentResolver departmentResolver;

    @Mock
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDepartments() {
        Department dept1 = new Department(1L, "HR", "Noida");
        Department dept2 = new Department(2L, "Engineering", "Bangalore");

        when(departmentService.getAllDepartments()).thenReturn(List.of(dept1, dept2));

        List<Department> result = departmentResolver.departments();

        assertEquals(2, result.size());
        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    void testGetDepartmentById() {
        Department dept = new Department(1L, "HR", "Noida");

        when(departmentService.getDepartment(1L)).thenReturn(dept);

        Department result = departmentResolver.department(1L);

        assertEquals("HR", result.getName());
        assertEquals("Noida", result.getLocation());
        verify(departmentService).getDepartment(1L);
    }

    @Test
    void testCreateDepartment() {
        Department dept = new Department(3L, "Marketing", "Pune");

        when(departmentService.createDepartment("Marketing", "Pune")).thenReturn(dept);

        Department result = departmentResolver.createDepartment("Marketing", "Pune");

        assertEquals("Marketing", result.getName());
        assertEquals("Pune", result.getLocation());
        verify(departmentService).createDepartment("Marketing", "Pune");
    }

    @Test
    void testUpdateDepartment() {
        Department updated = new Department(2L, "Engineering", "Chennai");

        when(departmentService.updateDepartment(2L, "Engineering", "Chennai")).thenReturn(updated);

        Department result = departmentResolver.updateDepartment(2L, "Engineering", "Chennai");

        assertEquals("Chennai", result.getLocation());
        verify(departmentService).updateDepartment(2L, "Engineering", "Chennai");
    }

    @Test
    void testDeleteDepartment() {
        when(departmentService.deleteDepartment(1L)).thenReturn(true);

        boolean deleted = departmentResolver.deleteDepartment(1L);

        assertTrue(deleted);
        verify(departmentService).deleteDepartment(1L);
    }
}
