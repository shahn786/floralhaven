package com.example.floralhaven.crud;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.floralhaven.CartActivity;
import com.example.floralhaven.R;
import com.example.floralhaven.utilities.CommonMethods;

public class CartCRUDActivity extends AppCompatActivity {

    EditText editTextID, editTextName, editTextDescription, editTextPrice, editTextImageURL, editTextCategory, editTextInStock, editTextOrderQuantity;
    Button buttonUpdate, buttonDelete;
    String id, name, description, price, image_url, category, in_stock, quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_crud_activity);

        editTextID = findViewById(R.id.editTextID);
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextImageURL = findViewById(R.id.editTextImageURL);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextInStock = findViewById(R.id.editTextInStock);
        editTextOrderQuantity = findViewById(R.id.editTextOrderQuantity);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);

        getAndSetIntentData();

        buttonUpdate.setOnClickListener(view -> editCartItem());
        buttonDelete.setOnClickListener(view -> deleteCartItem());
    }

    void getAndSetIntentData() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("description") && getIntent().hasExtra("price") && getIntent().hasExtra("image_url") && getIntent().hasExtra("quantity") && getIntent().hasExtra("category") && getIntent().hasExtra("in_stock")){
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            description = getIntent().getStringExtra("description");
            price = getIntent().getStringExtra("price");
            image_url = getIntent().getStringExtra("image_url");
            category = getIntent().getStringExtra("category");
            quantity = getIntent().getStringExtra("quantity");
            in_stock = getIntent().getStringExtra("in_stock");

            editTextID.setText(id);
            editTextName.setText(name);
            editTextDescription.setText(description);
            editTextPrice.setText(price);
            editTextImageURL.setText(image_url);
            editTextCategory.setText(category);
            editTextInStock.setText(in_stock);
            editTextOrderQuantity.setText(quantity);
        }
        else {
            Toast.makeText(this, "Select cart item first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CartCRUDActivity.this, CartActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, CartActivity.class));
        finish();
    }

    private void editCartItem() {
        String newID = editTextID.getText().toString().trim();
        String newInStock = editTextInStock.getText().toString().trim();
        String newOrderQuantity = editTextOrderQuantity.getText().toString().trim();

        if (newID.isEmpty() || newInStock.isEmpty() || newOrderQuantity.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Double.parseDouble(newOrderQuantity) < 1) {
            Toast.makeText(this, "Select atleast 1 quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Double.parseDouble(newOrderQuantity) > Double.parseDouble(newInStock)) {
            Toast.makeText(this, "Order quantity more than available quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        for(int z = 0; z < CommonMethods.cartItemsList.size(); z++) {
            if(CommonMethods.cartItemsList.get(z).getProductId() == Integer.parseInt(newID)) {
                CommonMethods.cartItemsList.get(z).setQuantity(Integer.parseInt(newOrderQuantity));
                break;
            }
        }

        Toast.makeText(this, "Cart item edited", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CartCRUDActivity.this, CartActivity.class));
        finish();
    }

    private void deleteCartItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete product '" + name + "'?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            for(int z = 0; z < CommonMethods.cartItemsList.size(); z++) {
                if(CommonMethods.cartItemsList.get(z).getProductId() == Integer.parseInt(id)) {
                    CommonMethods.cartItemsList.remove(z);
                    break;
                }
            }
            Toast.makeText(this, "Product removed from cart", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CartCRUDActivity.this, CartActivity.class));
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.create().show();
    }
}