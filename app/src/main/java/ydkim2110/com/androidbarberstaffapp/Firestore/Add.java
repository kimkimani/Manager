package ydkim2110.com.androidbarberstaffapp.Firestore;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ydkim2110.com.androidbarberstaffapp.Common.Common;
import ydkim2110.com.androidbarberstaffapp.Model.HostelAdd;
import ydkim2110.com.androidbarberstaffapp.R;

public class Add extends AppCompatActivity implements View.OnClickListener {

    private EditText tvname;
    private EditText tvrent;
    private EditText tvslot;
    private EditText tvpassword;
    private EditText tvusername,tvcontact;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        db = FirebaseFirestore.getInstance();

        tvname = findViewById(R.id.edittext_name);
        tvslot = findViewById(R.id.slot);
        tvrent = findViewById(R.id.rent);
        tvpassword = findViewById(R.id.password);
        tvusername = findViewById(R.id.username);
        tvcontact = findViewById(R.id.edittext_contact);

        findViewById(R.id.button_save).setOnClickListener(this);
    }

    private boolean validateInputs(String name, String rent, String slot, String password, String username,String contact) {
        if (contact.isEmpty()) {
            tvcontact.setError("Name required");
            tvcontact.requestFocus();
            return true;
        }
        if (name.isEmpty()) {
            tvname.setError("Name required");
            tvname.requestFocus();
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

    @Override
    public void onClick(View v) {
        String nnone = "";
        long rate =0;

        String name = tvname.getText().toString().trim();
        String slot = tvrent.getText().toString().trim();
        String rent = tvslot.getText().toString().trim();
        String contact = tvcontact.getText().toString().trim();
        String password = tvusername.getText().toString().trim();
        String username =tvpassword.getText().toString().trim();
        String avslots =tvslot.getText().toString().trim();


        long rating = rate;
        long ratingTimes =rate;

        if (!validateInputs(name, password, slot, username, rent,contact)) {

            CollectionReference dbProducts = db

//            name password slot username  rent
                    .collection("gender")
                    .document(Common.state_name)
                    .collection("Branch")
                    .document(Common.selected_salon.getSalonId())
                    .collection("Hostel");

            HostelAdd product = new HostelAdd(
                                name,
                                password,
                    Long.parseLong(avslots),
                    username,
                    Long.parseLong(rent),
                    Integer.parseInt(slot),
                    rating,
                    ratingTimes,
                    contact




                    );

            dbProducts.add(product)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(Add.this, "Hostel Added", Toast.LENGTH_LONG).show();
                            Add.this.finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Add.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }

    }
}