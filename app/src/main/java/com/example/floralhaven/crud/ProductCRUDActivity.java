package com.example.floralhaven.crud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.floralhaven.ProductsActivity;
import com.example.floralhaven.R;
import com.example.floralhaven.dao.ProductsDAO;
import com.example.floralhaven.database.AppDatabase;
import com.example.floralhaven.entities.Products;

public class ProductCRUDActivity extends AppCompatActivity {

    EditText editTextID, editTextName, editTextDescription, editTextPrice, editTextImageURL, editTextCategory, editTextInStock;
    Button buttonAdd, buttonUpdate, buttonDelete;
    String id, name, description, price, image_url, category, in_stock;

    private ProductsDAO productsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_crud_activity);

        AppDatabase appDatabase = AppDatabase.getDBInstance(this);
        productsDAO = appDatabase.productsDAO();

        editTextID = findViewById(R.id.editTextID);
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextImageURL = findViewById(R.id.editTextImageURL);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextInStock = findViewById(R.id.editTextInStock);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);

        getAndSetIntentData();

        buttonAdd.setOnClickListener(view -> addNewProduct());
        buttonUpdate.setOnClickListener(view -> updateProduct());
        buttonDelete.setOnClickListener(view -> deleteProduct());
    }

    void getAndSetIntentData(){
        boolean isAddMode = true;
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

            isAddMode = false;
        }
        if(isAddMode) {
            buttonAdd.setVisibility(View.VISIBLE);
            buttonUpdate.setVisibility(View.GONE);
            buttonDelete.setVisibility(View.GONE);
        }
        else {
            buttonAdd.setVisibility(View.GONE);
            buttonUpdate.setVisibility(View.VISIBLE);
            buttonDelete.setVisibility(View.VISIBLE);
        }
    }

    private void addNewProduct() {
        String newName = editTextName.getText().toString().trim().toUpperCase();
        String newDescription = editTextDescription.getText().toString().trim().toUpperCase();
        String newPrice = editTextPrice.getText().toString().trim();
        String newImageURL = editTextImageURL.getText().toString().trim();
        String newCategory = editTextCategory.getText().toString().trim().toUpperCase();
        String newInStock = editTextInStock.getText().toString().trim();

        if (newImageURL.isEmpty() || newCategory.isEmpty() || newInStock.isEmpty() || newName.isEmpty() || newDescription.isEmpty() || newPrice.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Double.parseDouble(newPrice) <= 0) {
            Toast.makeText(this, "Invalid product price", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "New product added", Toast.LENGTH_SHORT).show();
        Products newProduct = new Products(newName, newDescription, Double.parseDouble(newPrice), newImageURL, newCategory, Integer.parseInt(newInStock));
        productsDAO.insert(newProduct);

        startActivity(new Intent(ProductCRUDActivity.this, ProductsActivity.class));
        finish();
    }

    private void updateProduct() {
        String newName = editTextName.getText().toString().trim().toUpperCase();
        String newDescription = editTextDescription.getText().toString().trim().toUpperCase();
        String newPrice = editTextPrice.getText().toString().trim();
        String newImageURL = editTextImageURL.getText().toString().trim();
        String newCategory = editTextCategory.getText().toString().trim().toUpperCase();
        String newInStock = editTextInStock.getText().toString().trim();

        if (newImageURL.isEmpty() || newCategory.isEmpty() || newInStock.isEmpty() || newName.isEmpty() || newDescription.isEmpty() || newPrice.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Double.parseDouble(newPrice) <= 0) {
            Toast.makeText(this, "Invalid product price", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
        Products editProduct = new Products(newName, newDescription, Double.parseDouble(newPrice), newImageURL, newCategory, Integer.parseInt(newInStock));
        editProduct.setProductId(Integer.parseInt(id));
        productsDAO.update(editProduct);

        startActivity(new Intent(ProductCRUDActivity.this, ProductsActivity.class));
        finish();
    }

    private void deleteProduct(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete product '" + name + "'?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            productsDAO.deleteProductByID(Integer.parseInt(id));
            Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProductCRUDActivity.this, ProductsActivity.class));
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.create().show();
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ProductsActivity.class));
        finish();
    }
}