package ydkim2110.com.androidbarberstaffapp.Firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import io.paperdb.Paper;
import ydkim2110.com.androidbarberstaffapp.Common.Common;
import ydkim2110.com.androidbarberstaffapp.MainActivity;
import ydkim2110.com.androidbarberstaffapp.Model.HostelAdd;
import ydkim2110.com.androidbarberstaffapp.Model.ShoppingItem;
import ydkim2110.com.androidbarberstaffapp.R;

public class ItemAdd extends AppCompatActivity implements View.OnClickListener {

private EditText tvname;
private EditText tvprice;
private EditText tvquantity;
    private StorageReference mStorageRef;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private FirebaseFirestore   mDatabaseRef;



    private StorageTask mUploadTask;
    private Uri mImageUri;

private FirebaseFirestore db;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        db = FirebaseFirestore.getInstance();

        tvname = findViewById(R.id.edittext_name);
        tvprice = findViewById(R.id.price);
        tvquantity = findViewById(R.id.quantity);

    mButtonChooseImage = findViewById(R.id.button_choose_image);
    mButtonUpload = findViewById(R.id.button_upload);
    mProgressBar = findViewById(R.id.progress_bar);


    mImageView = findViewById(R.id.image_view);

        findViewById(R.id.button_save).setOnClickListener(this);
    mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
//    mDatabaseRef = FirebaseFirestore.getInstance().get("uploads");
    mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openFileChooser();
        }
    });

    mButtonUpload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    });

}

private boolean validateInputs(String name, String price, String quantity ) {
        if (name.isEmpty()) {
        tvname.setError("Name required");
        tvname.requestFocus();
        return true;
        }

        if (price.isEmpty()) {
        tvprice.setError("price required");
            tvprice.requestFocus();
        return true;
        }

        if (quantity.isEmpty()) {
            tvquantity.setError("quantity required");
            tvquantity.requestFocus();
        return true;
        }


        return false;
        }

    @Override
    public void onClick(View v) {
        if (mUploadTask != null && mUploadTask.isInProgress()) {
            Toast.makeText(ItemAdd.this, "Upload in progress", Toast.LENGTH_SHORT).show();
        } else {
            uploadFile();
        }
        String name = tvname.getText().toString().trim();
        String price = tvprice.getText().toString().trim();
        String quantity = tvquantity.getText().toString().trim();

        Paper.init(ItemAdd.this);

        if (!validateInputs(name, price, quantity)) {

            CollectionReference dbProducts = db

//            name password slot username  rent
                    .collection("Shooping")
                    .document(Paper.book().read(Common.CURRENT_ITEM_KEY))
                    .collection("Items");

             ShoppingItem product = new ShoppingItem(
                    name,
                    Long.parseLong(price),
                    Long.parseLong(quantity)


                    );

            dbProducts.add(product)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(ItemAdd.this, "Item  Added", Toast.LENGTH_LONG).show();
                            ItemAdd.this.finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ItemAdd.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }

    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get()
                    .load(mImageUri)
                    .into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(ItemAdd.this, "Upload successful", Toast.LENGTH_LONG).show();

                            tvname.setText((CharSequence) taskSnapshot.getUploadSessionUri());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ItemAdd.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}