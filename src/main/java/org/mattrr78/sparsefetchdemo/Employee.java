package org.mattrr78.sparsefetchdemo;

import java.time.LocalDate;
import java.time.LocalTime;

public interface Employee {

    int getId();

    String getNamePrefix();

    String getFirstName();

    String getMiddleInitial();

    String getLastName();

    String getGender();

    String getEmail();

    String getFatherName();

    String getMotherFullName();

    String getMotherMaidenName();

    LocalDate getDateOfBirth();

    LocalTime getTimeOfBirth();

    int getAge();

    int getWeight();

    LocalDate getJoinDate();

    String getJoinQuarter();

    String getJoinHalf();

    int getJoinYear();

    int getJoinMonth();

    String getJoinMonthName();

    String getJoinMonthNameShort();

    int getJoinDay();

    String getJoinWeek();

    String getJoinWeekShort();

    int getCompanyYears();

    int getSalary();

    int getPercentHike();

    String getSocialSecurityNumber();

    String getPhone();

    String getLocationName();

    String getLocationCounty();

    String getLocationCity();

    String getLocationState();

    String getLocationZip();

    String getRegion();

    String getUsername();

    String getPassword();

}
