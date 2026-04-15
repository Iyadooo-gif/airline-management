package com.airline.model;

import java.time.LocalDate;

public abstract class Employee extends Person {

    private String empNumber;
    private LocalDate hiringDate;

    public Employee(String id, String name, String address, String contact,
                    String empNumber, LocalDate hiringDate) {
        super(id, name, address, contact);
        this.empNumber = empNumber;
        this.hiringDate = hiringDate;
    }

    public String getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }

    public LocalDate getHiringDate() {
        return hiringDate;
    }

    public void setHiringDate(LocalDate hiringDate) {
        this.hiringDate = hiringDate;
    }

    public abstract String getRole();

    @Override
    public String getInfos() {
        return "ID: " + getId()
                + " | Name: " + getName()
                + " | Address: " + getAddress()
                + " | Contact: " + getContact()
                + " | Employee Number: " + empNumber
                + " | Hiring Date: " + hiringDate
                + " | Role: " + getRole();
    }
}
