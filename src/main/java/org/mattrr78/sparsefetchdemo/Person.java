package org.mattrr78.sparsefetchdemo;

import java.time.LocalDate;
import java.time.LocalTime;

public interface Person {

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

    String getSocialSecurityNumber();

    String getPhone();

}
