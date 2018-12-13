package com.example.stefan.weathraw.ui.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.weathraw.databinding.BottomSheetMenuBinding;
import com.example.stefan.weathraw.ui.adapter.BottomMenuAdapter;

public class BottomMenuDialog extends BottomSheetDialogFragment {

    private BottomMenuAdapter.OnMenuItemClickListener listener;

    public static BottomMenuDialog newInstance() {
        return new BottomMenuDialog();
    }

    public void setOnMenuItemClickListener(BottomMenuAdapter.OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomSheetMenuBinding binding = BottomSheetMenuBinding.inflate(inflater, container, false);
        initViews(binding);
        return binding.getRoot();
    }

    private void initViews(BottomSheetMenuBinding binding) {
        binding.recyclerBottomMenu.setHasFixedSize(true);
        binding.recyclerBottomMenu.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerBottomMenu.setAdapter(new BottomMenuAdapter(binding.getRoot().getContext(), listener));
    }
}
