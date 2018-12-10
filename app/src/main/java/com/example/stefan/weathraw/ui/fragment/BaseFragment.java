package com.example.stefan.weathraw.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.stefan.weathraw.model.ResponseMessage;
import com.example.stefan.weathraw.viewmodel.BaseViewModel;

public abstract class BaseFragment extends Fragment implements Observer<Throwable> {

    private BaseViewModel baseViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
    }

    @Override
    public void onChanged(@Nullable Throwable error) {
        if (error == null) return;

        if (!hasInternetConnection()) {
            makeToast("No internet connection");
        } else {
            makeToast(error.getMessage());
        }
    }

    public void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
