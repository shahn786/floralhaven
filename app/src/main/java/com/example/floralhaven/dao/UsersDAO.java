package com.example.floralhaven.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.floralhaven.entities.Users;
import java.util.List;

@Dao
public interface UsersDAO {
    @Insert
    void insert(Users users);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    Users getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    Users getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE user_id = :user_id LIMIT 1")
    Users getUserById(int user_id);

    @Query("SELECT * FROM users WHERE is_admin = 1 ORDER BY username")
    List<Users> getAdmins();

    @Query("DELETE FROM users WHERE is_admin = 0")
    void deleteUsers();

    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();

    @Query("SELECT * FROM users WHERE is_admin = 0 ORDER BY username")
    List<Users> getUsers();

    @Query("DELETE FROM users WHERE user_id = (:user_id)")
    void deleteUserByID(int user_id);

    @Query("SELECT SUM(total_amount) FROM cart WHERE user_id = :user_id")
    double getUsersTotalPurchases(int user_id);
}
