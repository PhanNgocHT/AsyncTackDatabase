package com.example.hp.directorymanagement.management.database;

import com.example.hp.directorymanagement.management.model.Person;

import java.util.ArrayList;

/**
 * Created by hp on 10/20/2017.
 */

public interface Database {
    boolean save(Person person);

    boolean update(Person person);

    boolean delete(Person person);

    ArrayList<Person> getAllData();
}
