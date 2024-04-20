package com.example.floralhaven.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.floralhaven.entities.Cart;

import java.util.List;

@Dao
public interface CartDAO {
    @Insert
    void insert(Cart cart);

    @Query("SELECT * FROM cart WHERE user_id = :user_id ORDER BY cart_id DESC")
    List<Cart> getCartsByUserId(int user_id);

    @Query("UPDATE cart SET total_products = :total_products, total_quantity = :total_quantity WHERE cart_id = :cart_id")
    void updateCart(int cart_id, int total_products, int total_quantity);

    @Query("SELECT cart_id FROM cart ORDER BY cart_id DESC LIMIT 1")
    int getLastCartID();
}
