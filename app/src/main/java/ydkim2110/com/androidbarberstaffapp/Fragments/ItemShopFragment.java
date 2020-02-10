package ydkim2110.com.androidbarberstaffapp.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import ydkim2110.com.androidbarberstaffapp.Adapter.ItemShopAdapter;
import ydkim2110.com.androidbarberstaffapp.Common.Common;
import ydkim2110.com.androidbarberstaffapp.Common.SpacesItemDecoration;
import ydkim2110.com.androidbarberstaffapp.Firestore.ItemAdd;
import ydkim2110.com.androidbarberstaffapp.HostelActivity;
import ydkim2110.com.androidbarberstaffapp.Interface.IShoppingDataLoadListener;
import ydkim2110.com.androidbarberstaffapp.Model.ShoppingItem;
import ydkim2110.com.androidbarberstaffapp.NotificationActivity;
import ydkim2110.com.androidbarberstaffapp.R;
import ydkim2110.com.androidbarberstaffapp.StaffHomeActivity;

public class ItemShopFragment extends Fragment implements IShoppingDataLoadListener {

    private static final String TAG = "ShoppingFragment";

    private Unbinder mUnbinder;
    private CollectionReference shoppingItemRef;

    private IShoppingDataLoadListener mIShoppingDataLoadListener;
 //   ActionBarDrawerToggle mActionBarDrawerToggle;

    private AlertDialog mDialog;

    @BindView(R.id.chip_group)
    ChipGroup chipGroup;
    @BindView(R.id.chip_wax)
    Chip chip_wax;
    @OnClick(R.id.chip_wax)
    void waxChipClick() {
        setSelectedChip(chip_wax);
        loadShoppingItem("Beddings");
    }
    @BindView(R.id.chip_spray)
    Chip chip_spray;
    @OnClick(R.id.chip_spray)
    void sprayChipClick() {
        setSelectedChip(chip_spray);
        loadShoppingItem("Kitchen");
    }
    @BindView(R.id.chip_hair_care)
    Chip chip_hair_care;
    @OnClick(R.id.chip_hair_care)
    void haireCareChipClick() {
        setSelectedChip(chip_hair_care);
        loadShoppingItem("curtain ");
    }
    @BindView(R.id.chip_body_care)
    Chip chip_body_care;
    @OnClick(R.id.chip_body_care)
    void bodyCareChipClick() {
        setSelectedChip(chip_body_care);
        loadShoppingItem("personal");
    }

    @BindView(R.id.recycler_items)
    RecyclerView recycler_item;

    private void loadShoppingItem(String itemMenu) {
        Log.d(TAG, "loadShoppingItem: called!!");

        mDialog.show();

        shoppingItemRef = FirebaseFirestore.getInstance()
                .collection("Shooping")
                .document(itemMenu)
                .collection("Items");

        // Get data
        shoppingItemRef.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mDialog.dismiss();
                        mIShoppingDataLoadListener.onShoppingDataLoadFailed(e.getMessage());
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ShoppingItem> shoppingItems = new ArrayList<>();
                            for (DocumentSnapshot itemSnapshot : task.getResult()) {
                                ShoppingItem shoppingItem = itemSnapshot.toObject(ShoppingItem.class);
                                // Remember add it if you don't want to get null!!
                                shoppingItem.setId(itemSnapshot.getId());
                                shoppingItems.add(shoppingItem);
                            }
                            mIShoppingDataLoadListener.onShoppingDataLoadSuccess(shoppingItems);
                            mDialog.dismiss();
                            Paper.book().write(Common.CURRENT_ITEM_KEY,itemMenu );
                        }
                    }
                });
    }

    private void setSelectedChip(Chip chip) {
        Log.d(TAG, "setSelectedChip: called!!");

        // Set color
        for (int i=0; i<chipGroup.getChildCount(); i++) {
            Chip chipItem = (Chip) chipGroup.getChildAt(i);
            Log.d(TAG, "setSelectedChip: chip.getId(): "+chip.getId());
            Log.d(TAG, "setSelectedChip: chipItem.getId(): "+chipItem.getId());
            // If not selected
            if (chipItem.getId() != chip.getId()) {
                chipItem.setChipBackgroundColorResource(android.R.color.darker_gray);
                chipItem.setTextColor(getResources().getColor(android.R.color.white));
            }
            // If selected
            else {
                chipItem.setChipBackgroundColorResource(android.R.color.holo_orange_dark);
                chipItem.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    public ItemShopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_shopping, container, false);

        mUnbinder = ButterKnife.bind(this, view);
        mIShoppingDataLoadListener = this;


        // Default load
        loadShoppingItem("Beddings");

        initView();

        return view;

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.additem, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.additems:
                // Do onlick on menu action here
                Intent intent = new Intent(getActivity(), ItemAdd.class);
                startActivity(intent);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void initView() {
        Log.d(TAG, "initView: called!!");
        recycler_item.setHasFixedSize(true);
        recycler_item.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycler_item.addItemDecoration(new SpacesItemDecoration(8));
    }

    @Override
    public void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList) {
        Log.d(TAG, "onShoppingDataLoadSuccess: called!!");
        ItemShopAdapter adapter = new ItemShopAdapter(getContext(), shoppingItemList);
        recycler_item.setAdapter(adapter);
    }

    @Override
    public void onShoppingDataLoadFailed(String message) {
        Log.d(TAG, "onShoppingDataLoadFailed: called!!");
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
