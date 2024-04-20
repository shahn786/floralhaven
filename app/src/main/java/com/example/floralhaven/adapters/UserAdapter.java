package com.example.floralhaven.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.floralhaven.R;
import com.example.floralhaven.dao.UsersDAO;
import com.example.floralhaven.database.AppDatabase;
import com.example.floralhaven.entities.Users;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private Context context;
    private Activity activity;
    private List<Users> usersList;
    private UsersDAO usersDAO;
    private List<Integer> selectedUsers = new ArrayList<>();

    public UserAdapter(Activity activity, Context context, List<Users> usersList){
        this.activity = activity;
        this.context = context;
        this.usersList = usersList;

        AppDatabase appDatabase = AppDatabase.getDBInstance(context);
        usersDAO = appDatabase.usersDAO();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() { return usersList.size(); }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Users selectedUser = usersList.get(position);
        holder.txtID.setText(String.valueOf(selectedUser.getUserId()));
        holder.txtUsername.setText(selectedUser.getUsername());
        holder.txtEmail.setText(selectedUser.getEmail());
        holder.txtTotalPurchases.setText("Total Purchases: " + usersDAO.getUsersTotalPurchases(selectedUser.getUserId()));
        if (selectedUsers.contains(selectedUser.getUserId())) { holder.cardView.setCardBackgroundColor(R.color.light_orange); }
        else { holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF")); }

        holder.userRowLayout.setOnClickListener(view -> {
            holder.userRowLayout.setSelected(!holder.userRowLayout.isSelected());
            if(holder.userRowLayout.isSelected()) {
                holder.cardView.setCardBackgroundColor(R.color.light_orange);
                if(!selectedUsers.contains(selectedUser.getUserId())) { selectedUsers.add(selectedUser.getUserId()); }
            }
            else {
                holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                if(selectedUsers.contains(selectedUser.getUserId())) { selectedUsers.remove(selectedUsers.indexOf(selectedUser.getUserId())); }
            }
        });
    }

    public List<Integer> getSelectedUsers() { return selectedUsers; }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtID, txtUsername, txtEmail, txtTotalPurchases;
        LinearLayout userRowLayout;
        CardView cardView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            txtID = itemView.findViewById(R.id.txtID);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtTotalPurchases = itemView.findViewById(R.id.txtTotalPurchases);
            userRowLayout = itemView.findViewById(R.id.userRowLayout);
            userRowLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.translate_anim));
        }
    }
}
