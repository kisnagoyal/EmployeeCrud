package com.example.employeeCrud.serviceTest;

import com.example.employeeCrud.model.Department;
import com.example.employeeCrud.repository.DepartmentRepository;
import com.example.employeeCrud.service.DepartmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepo;

    @InjectMocks
    private DepartmentService departmentService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDepartments() {
        List<Department> mockList = List.of(
                new Department(1L, "HR", "Delhi"),
                new Department(2L, "Engineering", "Noida")
        );

        when(departmentRepo.findAll()).thenReturn(mockList);

        List<Department> result = departmentService.getAllDepartments();

        assertEquals(2, result.size());
        verify(departmentRepo, times(1)).findAll();
    }

    @Test
    void testGetDepartmentById() {
        Department dept = new Department(1L, "HR", "Mumbai");
        when(departmentRepo.findById(1L)).thenReturn(Optional.of(dept));

        Department result = departmentService.getDepartment(1L);

        assertNotNull(result);
        assertEquals("HR", result.getName());
        assertEquals("Mumbai", result.getLocation());
    }

    @Test
    void testCreateDepartmentSuccess() {
        Department newDept = new Department(null, "Finance", "Pune");

        when(departmentRepo.findByNameAndLocation("Finance", "Pune"))
                .thenReturn(Optional.empty());
        when(departmentRepo.save(any(Department.class)))
                .thenReturn(new Department(3L, "Finance", "Pune"));

        Department created = departmentService.createDepartment("Finance", "Pune");

        assertNotNull(created);
        assertEquals("Finance", created.getName());
        verify(departmentRepo).save(any(Department.class));
    }

    @Test
    void testCreateDepartmentDuplicateThrowsException() {
        Department existing = new Department(1L, "Finance", "Pune");

        when(departmentRepo.findByNameAndLocation("Finance", "Pune"))
                .thenReturn(Optional.of(existing));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> departmentService.createDepartment("Finance", "Pune"));

        assertEquals("Department with this name and location already exists", ex.getMessage());
    }

    @Test
    void testUpdateDepartment() {
        Department existing = new Department(2L, "IT", "Chennai");

        when(departmentRepo.findById(2L)).thenReturn(Optional.of(existing));
        when(departmentRepo.save(any(Department.class))).thenReturn(existing);

        Department updated = departmentService.updateDepartment(2L, "IT", "Bangalore");

        assertEquals("Bangalore", updated.getLocation());
        assertEquals("IT", updated.getName());
    }

    @Test
    void testUpdateDepartmentNotFoundThrows() {
        when(departmentRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> departmentService.updateDepartment(99L, "Sales", "Delhi"));

        assertEquals("Department not found", ex.getMessage());
    }

    @Test
    void testDeleteDepartmentExists() {
        when(departmentRepo.existsById(5L)).thenReturn(true);

        boolean result = departmentService.deleteDepartment(5L);

        assertTrue(result);
        verify(departmentRepo).deleteById(5L);
    }

    @Test
    void testDeleteDepartmentNotExists() {
        when(departmentRepo.existsById(100L)).thenReturn(false);

        boolean result = departmentService.deleteDepartment(100L);

        assertFalse(result);
        verify(departmentRepo, never()).deleteById(anyLong());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
