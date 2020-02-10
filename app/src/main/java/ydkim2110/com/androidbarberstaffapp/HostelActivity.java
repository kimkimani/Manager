package ydkim2110.com.androidbarberstaffapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import ydkim2110.com.androidbarberstaffapp.Adapter.MyHostelAdapter;
import ydkim2110.com.androidbarberstaffapp.Common.Common;
import ydkim2110.com.androidbarberstaffapp.Common.SpacesItemDecoration;
import ydkim2110.com.androidbarberstaffapp.Firestore.Add;
import ydkim2110.com.androidbarberstaffapp.Interface.IGetHostelListener;
import ydkim2110.com.androidbarberstaffapp.Interface.IOnLoadCountSalon;
import ydkim2110.com.androidbarberstaffapp.Model.Hostel;

public class HostelActivity extends AppCompatActivity implements IOnLoadCountSalon,
        IGetHostelListener {

    private static final String TAG = HostelActivity.class.getSimpleName();
//    @BindView(R.id.activity_main)
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;

    @BindView(R.id.txt_salon_count)
    TextView txt_salon_count;
    @BindView(R.id.recycler_salon)
    RecyclerView recycler_salon;
    @BindView(R.id.no_item)
//    Context mContext;

    TextView no_item;

    IOnLoadCountSalon mIOnLoadCountSalon;
    IGetHostelListener mIGetHostelListener;
    String usern = "admin";

    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_list);
        Log.d(TAG, "onCreate: called!!");

        ButterKnife.bind(this);

        initView();

        init();

        loadSalonBaseOnCity(Common.state_name);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.staff_hostels, menu);
        MenuItem menuItem = menu.findItem(R.id.addhostel);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addhostel) {
            startActivity(new Intent(HostelActivity.this, Add.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        Log.d(TAG, "initView: called!!");
        recycler_salon.setHasFixedSize(true);
        recycler_salon.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_salon.addItemDecoration(new SpacesItemDecoration(8));
    }

    private void init() {
        Log.d(TAG, "init: called!!");
        mDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        mIOnLoadCountSalon = this;
        mIGetHostelListener = this;
    }

    private void loadSalonBaseOnCity(String name) {
        Log.d(TAG, "loadSalonBaseOnCity: called!!");

        mDialog.show();

        ///gender/gents/Branch/4jydSfTfDi3o26owKCFp/Hostel
        FirebaseFirestore.getInstance()
                .collection("gender")
                .document(Common.state_name)
                .collection("Branch")
                .document(Common.selected_salon.getSalonId())
                .collection("Hostel")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Hostel> hoatelAreas = new ArrayList<>();
                            mIOnLoadCountSalon.onLoadCountSalonSuccess(task.getResult().size());
                            for (DocumentSnapshot salonSnapshot : task.getResult()) {
                                Hostel hoatelArea = salonSnapshot.toObject(Hostel.class);
                                hoatelArea.setBarberId(salonSnapshot.getId());
                                hoatelAreas.add(hoatelArea);
                            }
                            mIGetHostelListener.onGetBarberSuccess(hoatelAreas);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mIGetHostelListener.onGetBarberFailed(e.getMessage());
                    }
                });
    }

    @Override
    public void onLoadCountSalonSuccess(int count) {
        Log.d(TAG, "onLoadCountSalonSuccess: called!!");
        txt_salon_count.setText(new StringBuilder("All Hostels (").append(count).append(")"));
    }

    @Override
    public void onGetBarberSuccess(List<Hostel> hostelList) {
        Log.d(TAG, "onBranchLoadSuccess: called!!");
        if (hostelList.size() == 0) {
            no_item.setVisibility(View.VISIBLE);
            recycler_salon.setVisibility(View.GONE);
        } else {
            no_item.setVisibility(View.GONE);
            recycler_salon.setVisibility(View.VISIBLE);
            MyHostelAdapter mSalonAdapter = new MyHostelAdapter(this, hostelList,this);
            recycler_salon.setAdapter(mSalonAdapter);
        }

        mDialog.dismiss();
    }

    @Override
    public void onGetBarberFailed (String message) {
        Log.d(TAG, "onBranchLoadFailed: called!!");
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
    }

//    @Override
//    public void IGetHostelListener(Hostel hostel) {
//        Log.d(TAG, "onGetBarberSuccess: called!!");
//        Common.currentHostel = hostel;
//        Paper.book().write(Common.BARBER_KEY, new Gson().toJson(hostel));
//    }

//    @Override
//    public void onUserLoginSuccess(String user) {
//        Log.d(TAG, "onUserLoginSuccess: called!!");
//        // Save User
//        Paper.init(this);
//        Paper.book().write(Common.LOGGED_KEY, usern);
//        Paper.book().write(Common.STATE_KEY, Common.state_name);
//        Paper.book().write(Common.SALON_KEY, new Gson().toJson(Common.selected_hoatelArea));
//    }


}
