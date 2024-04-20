package com.example.floralhaven;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.floralhaven.adapters.ProductAdapter;
import com.example.floralhaven.crud.ProductCRUDActivity;
import com.example.floralhaven.dao.ProductsDAO;
import com.example.floralhaven.database.AppDatabase;
import com.example.floralhaven.entities.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    RecyclerView productsRecyclerView;
    FloatingActionButton buttonAddProduct;
    ImageView emptyImageview;
    TextView noData;
    ProductAdapter productAdapter;
    private ProductsDAO productsDAO;
    List<Products> productsList;

    private EditText editTextSearch;
    private Button buttonSearch, buttonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        AppDatabase appDatabase = AppDatabase.getDBInstance(this);
        productsDAO = appDatabase.productsDAO();

        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        emptyImageview = findViewById(R.id.emptyImageview);
        noData = findViewById(R.id.noData);
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonReset = findViewById(R.id.buttonReset);

        buttonAddProduct.setOnClickListener(view -> {
            startActivity(new Intent(ProductsActivity.this, ProductCRUDActivity.class));
            finish();
        });

        loadProductsList();
        reloadRecyclerView();

        buttonSearch.setOnClickListener(v -> {
            String searchText = editTextSearch.getText().toString().trim().toUpperCase();
            if(searchText.length() < 3) {
                Toast.makeText(this, "Enter atleast 3 characters to search", Toast.LENGTH_SHORT).show();
            }
            else {
                editTextSearch.setText("");
                List<Products> searchProductsList = new ArrayList<>();
                for (Products currentProduct : productsList) {
                    if(currentProduct.getName().contains(searchText)) { searchProductsList.add(currentProduct); }
                }
                productsList = searchProductsList;
                if(productsList.size() == 0){
                    emptyImageview.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.VISIBLE);
                }
                else{
                    emptyImageview.setVisibility(View.GONE);
                    noData.setVisibility(View.GONE);
                }
                reloadRecyclerView();
            }
        });

        buttonReset.setOnClickListener(v -> {
            editTextSearch.setText("");
            loadProductsList();
            reloadRecyclerView();
        });
    }

    void reloadRecyclerView() {
        productAdapter = new ProductAdapter(ProductsActivity.this,this, productsList);
        productsRecyclerView.setAdapter(productAdapter);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(ProductsActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){ recreate(); }
    }

    void loadProductsList(){
        productsList = productsDAO.getProducts();
        if(productsList.size() == 0){
            emptyImageview.setVisibility(View.VISIBLE);
            noData.setVisibility(View.VISIBLE);
        }
        else{
            emptyImageview.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, AdminPortalActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all){ confirmDialog(); }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all products?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            productsDAO.deleteProducts();
            startActivity(new Intent(ProductsActivity.this, ProductsActivity.class));
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.create().show();
    }
}