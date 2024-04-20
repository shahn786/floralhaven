package com.example.floralhaven.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_item", foreignKeys = {@ForeignKey(entity = Cart.class, parentColumns = {"cart_id"}, childColumns = {"cart_id"}, onDelete = ForeignKey.CASCADE), @ForeignKey(entity = Products.class, parentColumns = {"product_id"}, childColumns = {"product_id"}, onDelete = ForeignKey.CASCADE)})
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cartItem_id")
    private int cartItemId;

    @ColumnInfo(name = "cart_id", index = true)
    private int cartId;

    @ColumnInfo(name = "product_id", index = true)
    private int productId;

    private int quantity;

    public CartItem(int cartId, int productId, int quantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getCartItemId() { return cartItemId; }
    public void setCartItemId(int cartItemId) { this.cartItemId = cartItemId; }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
