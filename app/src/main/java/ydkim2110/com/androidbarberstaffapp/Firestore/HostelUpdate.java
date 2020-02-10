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
import ydkim2110.com.androidbarberstaffapp.Model.Hostel;
import ydkim2110.com.androidbarberstaffapp.Model.HostelAdd;
import ydkim2110.com.androidbarberstaffapp.Model.HostelAdd1;
import ydkim2110.com.androidbarberstaffapp.Model.ShoppingItem;
import ydkim2110.com.androidbarberstaffapp.R;

public class HostelUpdate extends AppCompatActivity implements  View.OnClickListener {

    private EditText tvname;
    private EditText tvrent;
    private EditText tvslot;
    private EditText tvpassword;
    private EditText tvusername,tvcontact;


    private FirebaseFirestore db;

    private HostelAdd1 product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostelupdate);
        product = (HostelAdd1) getIntent().getSerializableExtra("product");
        db = FirebaseFirestore.getInstance();


        db = FirebaseFirestore.getInstance();

        tvname = findViewById(R.id.edittext_name);
        tvslot = findViewById(R.id.slot);
        tvrent = findViewById(R.id.rent);
        tvpassword = findViewById(R.id.password);
        tvusername = findViewById(R.id.username);
        tvcontact = findViewById(R.id.edittext_contact);




        tvname.setText(product.getName());
        tvslot.setText(String.valueOf(product.getSlot()));
        tvrent.setText(String.valueOf(product.getRent()));
        tvpassword.setText(String.valueOf(product.getPassword()));
        tvusername.setText(product.getUsername());
        tvcontact.setText(product.getContact());


        findViewById(R.id.button_save).setOnClickListener(this);
    }

 private boolean hasValidationErrors(String name, String rent, String slot, String password, String username,String contact) {

            if (name.isEmpty()) {
            tvname.setError("Name required");
            tvname.requestFocus();
            return true;
        }
     if (contact.isEmpty()) {
         tvcontact.setError("Name required");
         tvcontact.requestFocus();
         return true;
     }
        if (rent.isEmpty()) {
            tvrent.setError("price required");
            tvrent.requestFocus();
            return true;
        }

        if (slot.isEmpty()) {
            tvslot.setError("slot required");
            tvslot.requestFocus();
            return true;
        }

        if (password.isEmpty()) {
            tvpassword.setError("password required");
            tvpassword.requestFocus();
            return true;
        }

        if (username.isEmpty()) {
            tvusername.setError("username required");
            tvusername.requestFocus();
            return true;
        }
        return false;
    }

    private void updateProduct() {
        String name = tvname.getText().toString().trim();
        String slot = tvslot.getText().toString().trim();
        String rent = tvrent.getText().toString().trim();
        String password = tvpassword.getText().toString().trim();
        String username = tvusername.getText().toString().trim();
        String contact = tvcontact.getText().toString().trim();

        if (!hasValidationErrors(name, password, slot, username, rent ,contact)){

            HostelAdd1 p = new HostelAdd1(
                    name,
                    password,
                    username,
                    Long.parseLong(rent),
                    Integer.parseInt(slot),
                    contact


            );

            Paper.init(HostelUpdate.this);
            db.collection("gender")
                    .document(Common.state_name)
                    .collection("Branch")
                    .document(Common.selected_salon.getSalonId())
                    .collection("Hostel")
                    .document(Common.currentHostel.getBarberId())
                    .update(
                            "name",p.getName(),
                            "rent",p.getRent(),
                            "slot",p.getRent(),
                            "password",p.getPassword(),
                            "password",p.getContact(),

                            "contact",p.getUsername()
                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(HostelUpdate.this, "Product Updated", Toast.LENGTH_LONG).show();
                            HostelUpdate.this.finish();
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
