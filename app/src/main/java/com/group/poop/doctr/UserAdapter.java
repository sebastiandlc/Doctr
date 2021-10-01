package com.group.poop.doctr;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    List<User> users;

    UserAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card, parent, false);
        UserViewHolder uvh = new UserViewHolder(view);
        return uvh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        String userName = users.get(position).getFirstName() + " " + users.get(position).getLastName();
        holder.name.setText(userName);
        holder.gender.setText(users.get(position).getGender());
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String date = format.format(users.get(position).getBirthday().getTime());
        holder.dob.setText(date);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        TextView gender;
        TextView dob;

        UserViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.patient_card_view);
            name = itemView.findViewById(R.id.patient_card_name);
            gender = itemView.findViewById(R.id.patient_card_gender);
            dob = itemView.findViewById(R.id.patient_card_dob);
        }

    }


}
