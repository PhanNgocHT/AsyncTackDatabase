package com.example.hp.directorymanagement.management.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.hp.directorymanagement.R;
import com.example.hp.directorymanagement.management.model.Person;

import java.util.List;

/**
 * Created by hp on 10/18/2017.
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    private List<Person> persons;
    private OnPersonClickListener onPersonClickListener;
    private OnLongPersonClickListener onLongPersonClickListener;
    public PersonAdapter(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(viewType, parent, false);
        return new PersonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, int position) {
        final Person person=persons.get(position);
        holder.tvName.setText(person.getmName());
        holder.tvNumber.setText(person.getmNumber());
        holder.cbDelete.setVisibility(person.ismIsLongClick()?View.VISIBLE:View.INVISIBLE);
        holder.cbDelete.setChecked(person.ismIsChecked());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPersonClickListener.clicked(holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onLongPersonClickListener.longClicked(holder.getAdapterPosition());
                return true;
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.item_person;
    }

    @Override
    public int getItemCount() {
        return null!=persons ? persons.size() : 0;
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvNumber;
        CheckBox cbDelete;

        public PersonViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            cbDelete=itemView.findViewById(R.id.cb_delete);
        }
    }

    public void setOnPersonClickListener(OnPersonClickListener listener) {
        onPersonClickListener = listener;
    }

    public void setOnLongPersonClickListener(OnLongPersonClickListener listener) {
        onLongPersonClickListener=listener;
    }

    public interface OnPersonClickListener {
        void clicked(int position);
    }

    public interface OnLongPersonClickListener{
        void longClicked(int position);
    }

}
