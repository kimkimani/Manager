package ydkim2110.com.androidbarberstaffapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import ydkim2110.com.androidbarberstaffapp.Common.Common;
import ydkim2110.com.androidbarberstaffapp.Firestore.HostelUpdate;
import ydkim2110.com.androidbarberstaffapp.HostelActivity;
import ydkim2110.com.androidbarberstaffapp.Interface.IRecyclerItemSelectedListener;
import ydkim2110.com.androidbarberstaffapp.Model.Hostel;
import ydkim2110.com.androidbarberstaffapp.R;
import ydkim2110.com.androidbarberstaffapp.StaffHomeActivity;

public class MyHostelAdapter extends RecyclerView.Adapter<MyHostelAdapter.MyViewHolder> {

    private static final String TAG = MyHostelAdapter.class.getSimpleName();

    private Context mContext;
    private List<Hostel> mHostelList;
    private List<CardView> mCardViewList;
    //private LocalBroadcastManager mLocalBroadcastManager;

    public MyHostelAdapter(Context context, List<Hostel> hostelList, HostelActivity hostelActivity) {
        mContext = context;
        mHostelList = hostelList;
        mCardViewList = new ArrayList<>();
        //mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_hostel, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_barber_name.setText(mHostelList.get(position).getName());
        holder.rent.setText(String.valueOf(mHostelList.get(position).getRent()));
        Paper.init(mContext);
//        Paper.book().write(String.valueOf(Common.RENT_KEY),mHostelList.get(position).getRent());
//        Toasty.success(mContext,"rent pricing saved",Toasty.LENGTH_SHORT).show();

        if (mHostelList.get(position).getRatingTimes() != null) {
            holder.ratingBar.setRating(mHostelList.get(position).getRating().floatValue() /
                    mHostelList.get(position).getRatingTimes());
        }
        else {
            holder.ratingBar.setRating(0);
        }

        if (!mCardViewList.contains(holder.card_barber)) {
            mCardViewList.add(holder.card_barber);
        }

        holder.setIRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position) {
                // Set Background for all item not choice
                for (CardView cardView : mCardViewList) {
                    cardView.setCardBackgroundColor(mContext.getResources()
                        .getColor(android.R.color.white));
                }

                // Set Background for choice
                holder.card_barber.setCardBackgroundColor(mContext.getResources()
                        .getColor(android.R.color.holo_orange_dark));

                // Send local broadcast to enable button next
//                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
//                intent.putExtra(Common.KEY_BARBER_SELECTED, mHostelList.get(position));
//                intent.putExtra(Common.KEY_STEP, 2);
//                mLocalBroadcastManager.sendBroadcast(intent);
                // Create Hostel
                Common.currentHostel = mHostelList.get(position);
//                showLoginDialog();


                // EventBus
                // We will navigate Staff Home and clear all previous activity
                Intent staffHome = new Intent(mContext, StaffHomeActivity.class);
                staffHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                staffHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(staffHome);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mHostelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener

    {

        private TextView txt_barber_name,rent;
        private RatingBar ratingBar;
        private CardView card_barber;

        IRecyclerItemSelectedListener mIRecyclerItemSelectedListener;

        public void setIRecyclerItemSelectedListener(IRecyclerItemSelectedListener IRecyclerItemSelectedListener) {
            mIRecyclerItemSelectedListener = IRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_barber = itemView.findViewById(R.id.card_barber);
            txt_barber_name = itemView.findViewById(R.id.hostel_name);
            ratingBar = itemView.findViewById(R.id.rtb_hostel);
            rent = itemView.findViewById(R.id.hostel_rent);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mIRecyclerItemSelectedListener.onItemSelected(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            Hostel hostel = mHostelList.get(getAdapterPosition());
            Intent intent = new Intent(mContext, HostelUpdate.class);
            intent.putExtra("product", hostel);
            mContext.startActivity(intent);
            return true;
        }
    }
}
