package ydkim2110.com.androidbarberstaffapp.Firestore;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import io.paperdb.Paper;
import ydkim2110.com.androidbarberstaffapp.Common.Common;
import ydkim2110.com.androidbarberstaffapp.Model.ShoppingItem;
import ydkim2110.com.androidbarberstaffapp.R;

public class ItemUpdate extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextPrice;


    private FirebaseFirestore db;

    private ShoppingItem product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_update);
        product = (ShoppingItem) getIntent().getSerializableExtra("product");
        db = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.name);
        editTextPrice = findViewById(R.id.price);


        editTextName.setText(product.getName());
        editTextPrice.setText(String.valueOf(product.getPrice()));

        findViewById(R.id.button_update).setOnClickListener(this);
    }

    private boolean hasValidationErrors(String name, String price) {
        if (name.isEmpty()) {
            editTextName.setError("Name required");
            editTextName.requestFocus();
            return true;
        }


        if (price.isEmpty()) {
            editTextPrice.setError("Price required");
            editTextPrice.requestFocus();
            return true;
        }

        return false;
    }


    private void updateProduct() {
        String name = editTextName.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();



        if (!hasValidationErrors(name, price) ){

            ShoppingItem p = new ShoppingItem(
                    name,
                    Long.parseLong(price)

            );

            Paper.init(ItemUpdate.this);
            db
                    .collection("Shooping")
                    .document(Paper.book().read(Common.CURRENT_ITEM_KEY))
                    .collection("Items")
                    .document(product.getId())
                    .update(
                            "name",p.getName(),
                            "price",p.getPrice(),
                            "quantity",p.getQuantity()

                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ItemUpdate.this, "Product Updated", Toast.LENGTH_LONG).show();
                            ItemUpdate.this.finish();
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_update:
                updateProduct();
                break;
        }
    }


}