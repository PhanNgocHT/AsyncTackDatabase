package com.example.hp.directorymanagement.management.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.example.hp.directorymanagement.management.custom.PersonAdapter;
import com.example.hp.directorymanagement.management.database.DatabaseImpl;
import com.example.hp.directorymanagement.management.model.Person;

import java.util.List;

/**
 * Created by hp on 10/27/2017.
 */

public class AsyncTaskDatabase extends AsyncTask<Person, Void, Person> {
    private RecyclerView rvPerson;
    private DatabaseImpl database;
    private Context context;
    private PersonAdapter adapter;
    private List<Person> persons;
    private int state;

    public AsyncTaskDatabase(int state, List<Person> persons, PersonAdapter adapter, RecyclerView rvPerson, Context context) {
        this.state=state;
        this.persons=persons;
        this.adapter=adapter;
        this.rvPerson = rvPerson;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        database=new DatabaseImpl(context);
        rvPerson.setAdapter(adapter);
        super.onPreExecute();
    }

        @Override
    protected Person doInBackground(Person... persons) {
        Person person=persons[0];
        switch (state) {
            case -1:
                database.delete(person);
                break;
            case 0:
                database.save(person);
                break;
            case 1:
                database.update(person);
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Person person) {
        persons.clear();
        persons.addAll(database.getAllData());
        adapter.notifyDataSetChanged();
        super.onPostExecute(person);
    }
}
