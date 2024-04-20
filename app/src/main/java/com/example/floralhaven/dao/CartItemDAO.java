package com.example.floralhaven.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.floralhaven.entities.CartItem;

import java.util.List;

@Dao
public interface CartItemDAO {
    @Insert
    void insert(CartItem cart_item);

    @Query("SELECT * FROM cart_item WHERE cart_id = :cart_id ORDER BY cartItem_id")
    List<CartItem> getCartItemsByCartId(int cart_id);

    @Query("DELETE FROM cart_item WHERE cart_id = :cart_id")
    void deleteCartItemsByCartId(int cart_id);
}
