package org.mattrr78.sparsefetchdemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    @Query("SELECT emp.id AS id, " +
            "emp.firstName AS firstName, " +
            "emp.lastName AS lastName, " +
            "emp.joinDate AS joinDate, " +
            "emp.salary AS salary " +
            "FROM EmployeeEntity emp")
    List<Map<String, Object>> findSparseJpa();

    @Query("SELECT emp.id, emp.firstName, emp.lastName, emp.joinDate, emp.salary FROM EmployeeEntity emp")
    Object[][] findJpaMultiArray();

    @Query(value = "SELECT id, firstName, lastName, joinDate, salary FROM Employee", nativeQuery = true)
    Object[][] findNativeMultiArray();

    @Query(value = "SELECT id, firstName, lastName, joinDate, salary FROM Employee", nativeQuery = true)
    List<Map<String, Object>> findSparseNative();

}