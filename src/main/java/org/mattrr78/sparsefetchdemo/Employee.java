package org.mattrr78.sparsefetchdemo;

import java.time.LocalDate;

public interface Employee extends Person {

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

    String getLocationName();

    String getLocationCounty();

    String getLocationCity();

    String getLocationState();

    String getLocationZip();

    String getRegion();

    String getUsername();

    String getPassword();

}
