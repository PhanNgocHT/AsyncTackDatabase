package com.example.hp.directorymanagement.management.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hp.directorymanagement.management.model.Person;

import java.util.ArrayList;

public class DatabaseImpl implements Database {
    private static final String DATABASE_NAME = "persons.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME_PERSON = "person";
    private static final String COLUMN_PERSON_ID = "id";
    private static final String COLUMN_PERSON_NAME = "name";
    private static final String COLUMN_PERSON_NUMBER = "number";

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseImpl(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private void openDatabase() {
        if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }
    }

    private void closeDatabase() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }

    @Override
    public boolean save(Person person) {
        openDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PERSON_ID, person.getmId());
        values.put(COLUMN_PERSON_NAME, person.getmName());
        values.put(COLUMN_PERSON_NUMBER, person.getmNumber());

        long id = sqLiteDatabase.insert(TABLE_NAME_PERSON, null, values);
        closeDatabase();
        return id != -1;
    }

    @Override
    public boolean update(Person person) {
        openDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PERSON_NAME, person.getmName());
        values.put(COLUMN_PERSON_NUMBER, person.getmNumber());
        long id = sqLiteDatabase.update(
                TABLE_NAME_PERSON,
                values,
                "id=?",
                new String[]{person.getmId()});

        closeDatabase();
        return id != -1;
    }

    @Override
    public boolean delete(Person person) {
        openDatabase();

        long id = sqLiteDatabase.delete(
                TABLE_NAME_PERSON,
                "id=?",
                new String[]{person.getmId()}
        );

        closeDatabase();
        return id != -1;
    }

    @Override
    public ArrayList<Person> getAllData() {
        openDatabase();
        String sqlGetAllData = "SELECT * FROM " + TABLE_NAME_PERSON ;
        Cursor cursor = sqLiteDatabase.rawQuery(sqlGetAllData, null);

        ArrayList<Person> persons = new ArrayList<>();

        if (cursor == null || cursor.getCount() == 0) {
            closeDatabase();
            return persons;
        }

        cursor.moveToLast();
        int indexId = cursor.getColumnIndex(COLUMN_PERSON_ID);
        int indexName = cursor.getColumnIndex(COLUMN_PERSON_NAME);
        int indexNumber = cursor.getColumnIndex(COLUMN_PERSON_NUMBER);
        while (!cursor.isBeforeFirst()) {
            String id = cursor.getString(indexId);
            String name = cursor.getString(indexName);
            String number = cursor.getString(indexNumber);
            persons.add(new Person(id, name, number));
            cursor.moveToPrevious();
        }
        cursor.close();
        closeDatabase();
        return persons;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        private String sqlCreationPersonTable = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME_PERSON + "(" +
                COLUMN_PERSON_ID + " TEXT PRIMARY KEY, " +
                COLUMN_PERSON_NAME + " TEXT NOT NULL, " +
                COLUMN_PERSON_NUMBER + " TEXT NOT NULL" +
                ")";
        private String sqlDropPersonTable = "DROP TABLE IF EXISTS " + TABLE_NAME_PERSON;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(sqlCreationPersonTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(sqlDropPersonTable);
            onCreate(db);
        }
    }

}