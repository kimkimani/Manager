package ydkim2110.com.androidbarberstaffapp.Interface;

import java.util.List;

import ydkim2110.com.androidbarberstaffapp.Model.Hostel;

public interface IGetHostelListener {
//    void onGetBarberSuccess(Hostel hostel);
void onGetBarberSuccess(List<Hostel> hostelList);
    void onGetBarberFailed(String message);
}
