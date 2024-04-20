package com.example.floralhaven;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.floralhaven.adapters.UserAdapter;
import com.example.floralhaven.dao.UsersDAO;
import com.example.floralhaven.database.AppDatabase;
import com.example.floralhaven.entities.Users;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    RecyclerView usersRecyclerView;
    ImageView emptyImageview;
    TextView noData;
    UserAdapter userAdapter;
    private UsersDAO usersDAO;
    List<Users> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        AppDatabase appDatabase = AppDatabase.getDBInstance(this);
        usersDAO = appDatabase.usersDAO();

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        emptyImageview = findViewById(R.id.emptyImageview);
        noData = findViewById(R.id.noData);

        loadUsersList();

        userAdapter = new UserAdapter(UsersActivity.this,this, usersList);
        usersRecyclerView.setAdapter(userAdapter);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(UsersActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){ recreate(); }
    }

    void loadUsersList(){
        usersList = usersDAO.getUsers();
        if(usersList.size() == 0){
            emptyImageview.setVisibility(View.VISIBLE);
            noData.setVisibility(View.VISIBLE);
        }
        else{
            emptyImageview.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.deleteAll){ confirmDeleteAllDialog(); }
        else if(item.getItemId() == R.id.deleteSelected){ confirmDeleteSelectedDialog(); }
        return super.onOptionsItemSelected(item);
    }

    void confirmDeleteSelectedDialog(){
        List<Integer> selectedUsers = userAdapter.getSelectedUsers();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Selected?");
        builder.setMessage("Are you sure you want to delete " + selectedUsers.size() + " selected users?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            selectedUsers.forEach(currentID -> usersDAO.deleteUserByID(currentID));
            startActivity(new Intent(UsersActivity.this, UsersActivity.class));
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.create().show();
    }

    void confirmDeleteAllDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all users?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            usersDAO.deleteUsers();
            startActivity(new Intent(UsersActivity.this, UsersActivity.class));
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.create().show();
    }
}
