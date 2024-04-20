package com.example.floralhaven.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart", foreignKeys = {@ForeignKey(entity = Users.class, parentColumns = {"user_id"}, childColumns = {"user_id"}, onDelete = ForeignKey.CASCADE)})
public class Cart {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cart_id")
    private int cartId;

    @ColumnInfo(name = "user_id", index = true)
    private int userId;

    @ColumnInfo(name = "total_products")
    private int totalProducts;

    @ColumnInfo(name = "total_quantity")
    private int totalQuantity;

    @ColumnInfo(name = "total_amount")
    private double totalAmount;

    public Cart(int userId, int totalProducts, int totalQuantity, double totalAmount) {
        this.userId = userId;
        this.totalProducts = totalProducts;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getTotalProducts() { return totalProducts; }
    public void setTotalProducts(int totalProducts) { this.totalProducts = totalProducts; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}
