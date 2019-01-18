package com.rakuten.fullstackrecruitmenttest.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("select e.name from Employee e where e.name = ?1")
    String findByName(String name);
    @Query("select e from Employee e where e.valid = false")
    List<Employee> findAllWrongRecords();
    @Modifying
    @Transactional
    @Query("update Employee e set e.department = ?1 where e.name = ?2")
    int updateDepartment(String department, String name);
    @Modifying
    @Transactional
    @Query("update Employee e set e.designation = ?1 where e.name = ?2")
    int updateDesignation(String designation, String name);
    @Modifying
    @Transactional
    @Query("update Employee e set e.salary = ?1 where e.name = ?2")
    int updateSalary(String salary, String name);
    @Modifying
    @Transactional
    @Query("update Employee e set e.joinDate = ?1 where e.name = ?2")
    int updateJoinDate(String joinDate, String name);
}