package com.example.floralhaven.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.floralhaven.entities.Products;

import java.util.List;

@Dao
public interface ProductsDAO {
    @Insert
    void insert(Products products);

    @Query("SELECT * FROM products ORDER BY name")
    List<Products> getProducts();

    @Query("DELETE FROM products")
    void deleteProducts();

    @Query("SELECT COUNT(*) FROM products")
    int getProductCount();

    @Query("DELETE FROM products WHERE product_id = :product_id")
    void deleteProductByID(int product_id);

    @Update
    void update(Products product);

    @Query("SELECT * FROM products WHERE product_id = :product_id")
    Products getProductByID(int product_id);

    @Query("UPDATE products SET in_stock = in_stock - :quantity WHERE product_id = :product_id")
    void updateProductInStockByID(int product_id, int quantity);
}
