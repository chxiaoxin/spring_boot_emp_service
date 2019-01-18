package com.rakuten.fullstackrecruitmenttest.database;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Employee {

    private @Id
    @GeneratedValue
    Long id;
    private String name;
    private String department;
    private String designation;
    private String salary;
    private String joinDate;
    private boolean valid;

    private Employee() {}

    public Employee(String name, String department, String designation, String salary, String joinDate, boolean valid) {
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.salary = salary;
        this.joinDate = joinDate;
        this.valid = valid;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s", name, department, designation, salary, joinDate);
    }
}