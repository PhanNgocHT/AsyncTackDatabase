package com.example.hp.directorymanagement.management.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.hp.directorymanagement.R;
import com.example.hp.directorymanagement.management.custom.PersonAdapter;
import com.example.hp.directorymanagement.management.database.DatabaseImpl;
import com.example.hp.directorymanagement.management.model.Person;
import com.example.hp.directorymanagement.management.utils.AsyncTaskDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PersonAdapter.OnPersonClickListener, PersonAdapter.OnLongPersonClickListener {
    private Toolbar toolbar;
    private RecyclerView rvPerson;
    private List<Person> persons = new ArrayList<>();
    private Dialog dialog;
    private FloatingActionButton actionButton;
    private LinearLayout llActionContact;
    private LinearLayout llListPerson;
    private EditText etName;
    private EditText etNumber;
    private Button btnDelete;
    private Button btnDeleteContact;
    private Button btnCancel;
    private Button btnSave;
    private TextView tvSwitch;
    private ImageButton ibSwitch;
    private PersonAdapter adapter;
    private DatabaseImpl database;
    private Boolean check = false;
    private Boolean checkState = true;
    private int checkboxState=-1;
    private int position;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDialog();
        initView();
        initData();
    }

    private void initDialog() {

        dialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_add, null);
        etName = view.findViewById(R.id.et_name);
        etNumber = view.findViewById(R.id.et_number);
        btnDelete = view.findViewById(R.id.btn_delete);
        btnSave = view.findViewById(R.id.btn_save);
        dialog.setCancelable(true);
        dialog.setContentView(view);
        btnDelete.setOnClickListener(this);
        btnSave.setOnClickListener(this);

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        rvPerson = (RecyclerView) findViewById(R.id.rv_person);
        actionButton = (FloatingActionButton) findViewById(R.id.fac_add);
        llActionContact= (LinearLayout) findViewById(R.id.ll_action_contact);
        llListPerson= (LinearLayout) findViewById(R.id.ll_list_person);

        btnDeleteContact= (Button) findViewById(R.id.btn_delete_contact);
        btnCancel= (Button) findViewById(R.id.btn_cancel);
        tvSwitch = (TextView) findViewById(R.id.tv_switch);
        ibSwitch = (ImageButton) findViewById(R.id.ib_switch);
        database=new DatabaseImpl(this);

    }

    private void initData() {
        ibSwitch.setImageResource(R.drawable.ic_listview);
        tvSwitch.setText(R.string.list_view);

        actionButton.setOnClickListener(this);
        ibSwitch.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDeleteContact.setOnClickListener(this);

        persons.addAll(database.getAllData());
        adapter = new PersonAdapter(persons);
        rvPerson.setAdapter(adapter);
        adapter.setOnPersonClickListener(this);
        adapter.setOnLongPersonClickListener(this);

        rvPerson.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fac_add:
                etName.setText("");
                etNumber.setText("");
                check = false;
                dialog.show();
                break;
            case R.id.btn_delete:
                state=-1;
                if (check) {
                    AsyncTaskDatabase data=new AsyncTaskDatabase(state,persons, adapter,rvPerson, this);
                    data.execute(persons.get(position));
                }
                dialog.dismiss();
                break;

            case R.id.btn_save:
                String id= UUID.randomUUID().toString();
                String name = etName.getText().toString();
                String number = etNumber.getText().toString();
                Person person=new Person(id, name, number);
                if (!check) {
                    state=0;
                    AsyncTaskDatabase data=new AsyncTaskDatabase(state,persons, adapter,rvPerson, this);
                    data.execute(person);

                } else {
                    state=1;
                    AsyncTaskDatabase data=new AsyncTaskDatabase(state,persons, adapter,rvPerson, this);
                    Person person1=new Person(persons.get(position).getmId(), name, number);
                    data.execute(person1);
                }
                setCheckState();
                dialog.dismiss();
                break;
            case R.id.ib_switch:
                checkState=!checkState;
                setCheckState();
                break;
            case R.id.btn_delete_contact:
                state=-1;
                checkboxState=-1;
                setSizeScreen();
                llActionContact.setVisibility(View.INVISIBLE);
                for (Person ps:persons) {
                    if (ps.ismIsChecked()) {
                        AsyncTaskDatabase data=new AsyncTaskDatabase(state,persons, adapter,rvPerson, this);
                        data.execute(ps);
                    }
                }
                break;
            case R.id.btn_cancel:
                checkboxState=-1;
                setSizeScreen();
                llActionContact.setVisibility(View.INVISIBLE);
                for (Person ps:persons) {
                    ps.setmIsLongClick(false);
                    ps.setmIsChecked(false);
                }
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void clicked(int position) {
        switch (checkboxState) {
            case -1:
                etName.setText(persons.get(position).getmName());
                etNumber.setText(persons.get(position).getmNumber());
                dialog.show();
                check = true;
                break;
            case 1:
                if (!persons.get(position).ismIsChecked()) {
                    persons.get(position).setmIsChecked(true);
                    adapter.notifyDataSetChanged();
                }else {
                    persons.get(position).setmIsChecked(false);
                    adapter.notifyDataSetChanged();
                }
                break;
        }

        this.position = position;
    }

    @Override
    public void longClicked(int position) {
        checkboxState=1;
        setSizeScreen();
        for (Person person:persons) {
            person.setmIsLongClick(true);
        }
        llActionContact.setVisibility(View.VISIBLE);
        persons.get(position).setmIsChecked(true);
        adapter.notifyDataSetChanged();
    }

    private void setCheckState() {
        if (checkState) {
            ibSwitch.setImageResource(R.drawable.ic_listview);
            tvSwitch.setText(R.string.list_view);
            rvPerson.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }else{
            ibSwitch.setImageResource(R.drawable.ic_girdview);
            tvSwitch.setText(R.string.gird_view);
            rvPerson.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        }
    }

    private void setSizeScreen() {
        if (checkboxState==1) {
            int height=llListPerson.getHeight()-llActionContact.getHeight();
            llListPerson.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        }else {
            llListPerson.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
        }
    }
}
