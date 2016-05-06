package com.wavemaker.jasper.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by kishorer on 16/3/16.
 */
@Entity
@Table(name = "EMPLOYEE")
public class Employee  {

    @Id
    @Column(name = "ID")
    private Long employeeId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DEPARTMENT")
    private String department;

    @Column(name = "EMAIL")
    private String emailId;

    @Column(name = "STOCKS")
    private Integer noOfStocks;

    public Employee() {
    }

    public Integer getNoOfStocks() {
        return noOfStocks;
    }

    public void setNoOfStocks(final Integer noOfStocks) {
        this.noOfStocks = noOfStocks;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(final String emailId) {
        this.emailId = emailId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(final String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(final Long employeeId) {
        this.employeeId = employeeId;
    }
}
