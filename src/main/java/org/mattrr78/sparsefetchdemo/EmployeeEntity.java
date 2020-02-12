package org.mattrr78.sparsefetchdemo;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Employee")
public class EmployeeEntity implements Employee {

    @Column
    @Id
    private int id;

    @Column
    private String namePrefix;

    @Column
    private String firstName;

    @Column
    private String middleInitial;

    @Column
    private String lastName;

    @Column
    private String gender;

    @Column
    private String email;

    @Column
    private String fatherName;

    @Column
    private String motherFullName;

    @Column
    private String motherMaidenName;

    @Column
    private LocalDate dateOfBirth;

    @Column
    private LocalTime timeOfBirth;

    @Column
    private int age;

    @Column
    private int weight;

    @Column
    private LocalDate joinDate;

    @Column
    private String joinQuarter;

    @Column
    private String joinHalf;

    @Column
    private int joinYear;

    @Column
    private int joinMonth;

    @Column
    private String joinMonthName;

    @Column
    private String joinMonthNameShort;

    @Column
    private int joinDay;

    @Column
    private String joinWeek;

    @Column
    private String joinWeekShort;

    @Column
    private int companyYears;

    @Column
    private int salary;

    @Column
    private int percentHike;

    @Column
    private String socialSecurityNumber;

    @Column
    private String phone;

    @Column
    private String locationName;

    @Column
    private String locationCounty;

    @Column
    private String locationCity;

    @Column
    private String locationState;

    @Column
    private String locationZip;

    @Column
    private String region;

    @Column
    private String username;

    @Column
    private String password;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @Override
    public String getMotherFullName() {
        return motherFullName;
    }

    public void setMotherFullName(String motherFullName) {
        this.motherFullName = motherFullName;
    }

    @Override
    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    @Override
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public LocalTime getTimeOfBirth() {
        return timeOfBirth;
    }

    public void setTimeOfBirth(LocalTime timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }

    @Override
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public String getJoinQuarter() {
        return joinQuarter;
    }

    public void setJoinQuarter(String joinQuarter) {
        this.joinQuarter = joinQuarter;
    }

    @Override
    public String getJoinHalf() {
        return joinHalf;
    }

    public void setJoinHalf(String joinHalf) {
        this.joinHalf = joinHalf;
    }

    @Override
    public int getJoinYear() {
        return joinYear;
    }

    public void setJoinYear(int joinYear) {
        this.joinYear = joinYear;
    }

    @Override
    public int getJoinMonth() {
        return joinMonth;
    }

    public void setJoinMonth(int joinMonth) {
        this.joinMonth = joinMonth;
    }

    @Override
    public String getJoinMonthName() {
        return joinMonthName;
    }

    public void setJoinMonthName(String joinMonthName) {
        this.joinMonthName = joinMonthName;
    }

    @Override
    public String getJoinMonthNameShort() {
        return joinMonthNameShort;
    }

    public void setJoinMonthNameShort(String joinMonthNameShort) {
        this.joinMonthNameShort = joinMonthNameShort;
    }

    @Override
    public int getJoinDay() {
        return joinDay;
    }

    public void setJoinDay(int joinDay) {
        this.joinDay = joinDay;
    }

    @Override
    public String getJoinWeek() {
        return joinWeek;
    }

    public void setJoinWeek(String joinWeek) {
        this.joinWeek = joinWeek;
    }

    @Override
    public String getJoinWeekShort() {
        return joinWeekShort;
    }

    public void setJoinWeekShort(String joinWeekShort) {
        this.joinWeekShort = joinWeekShort;
    }

    @Override
    public int getCompanyYears() {
        return companyYears;
    }

    public void setCompanyYears(int companyYears) {
        this.companyYears = companyYears;
    }

    @Override
    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public int getPercentHike() {
        return percentHike;
    }

    public void setPercentHike(int percentHike) {
        this.percentHike = percentHike;
    }

    @Override
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String getLocationCounty() {
        return locationCounty;
    }

    public void setLocationCounty(String locationCounty) {
        this.locationCounty = locationCounty;
    }

    @Override
    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    @Override
    public String getLocationState() {
        return locationState;
    }

    public void setLocationState(String locationState) {
        this.locationState = locationState;
    }

    @Override
    public String getLocationZip() {
        return locationZip;
    }

    public void setLocationZip(String locationZip) {
        this.locationZip = locationZip;
    }

    @Override
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}