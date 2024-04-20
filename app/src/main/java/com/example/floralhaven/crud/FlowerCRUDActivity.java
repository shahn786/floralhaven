package com.example.floralhaven.crud;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.floralhaven.FlowersActivity;
import com.example.floralhaven.R;
import com.example.floralhaven.entities.CartItem;
import com.example.floralhaven.utilities.CommonMethods;

public class FlowerCRUDActivity extends AppCompatActivity {

    EditText editTextID, editTextName, editTextDescription, editTextPrice, editTextImageURL, editTextCategory, editTextInStock, editTextOrderQuantity;
    Button buttonAdd;
    String id, name, description, price, image_url, category, in_stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_crud_activity);

        editTextID = findViewById(R.id.editTextID);
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextImageURL = findViewById(R.id.editTextImageURL);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextInStock = findViewById(R.id.editTextInStock);
        editTextOrderQuantity = findViewById(R.id.editTextOrderQuantity);
        buttonAdd = findViewById(R.id.buttonAdd);

        getAndSetIntentData();

        buttonAdd.setOnClickListener(view -> addNewFlower());
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("description") && getIntent().hasExtra("price") && getIntent().hasExtra("image_url") && getIntent().hasExtra("category") && getIntent().hasExtra("in_stock")){
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            description = getIntent().getStringExtra("description");
            price = getIntent().getStringExtra("price");
            image_url = getIntent().getStringExtra("image_url");
            category = getIntent().getStringExtra("category");
            in_stock = getIntent().getStringExtra("in_stock");

            editTextID.setText(id);
            editTextName.setText(name);
            editTextDescription.setText(description);
            editTextPrice.setText(price);
            editTextImageURL.setText(image_url);
            editTextCategory.setText(category);
            editTextInStock.setText(in_stock);
            editTextOrderQuantity.setText("1");
        }
        else {
            Toast.makeText(this, "Select flower first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(FlowerCRUDActivity.this, FlowersActivity.class));
            finish();
        }
    }

    private void addNewFlower() {
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

        boolean itemExists = false;
        for(int z = 0; z < CommonMethods.cartItemsList.size(); z++) {
            if(CommonMethods.cartItemsList.get(z).getProductId() == Integer.parseInt(newID)) {
                CommonMethods.cartItemsList.get(z).setQuantity(CommonMethods.cartItemsList.get(z).getQuantity() + Integer.parseInt(newOrderQuantity));
                itemExists = true;
                break;
            }
        }
        if(!itemExists) { CommonMethods.cartItemsList.add(new CartItem(0, Integer.parseInt(newID), Integer.parseInt(newOrderQuantity))); }

        Toast.makeText(this, "New flower added", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(FlowerCRUDActivity.this, FlowersActivity.class));
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, FlowersActivity.class));
        finish();
    }
}