package com.example.BoxBox.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.BoxBox.R;

public class NobFragment extends Fragment {

    private NobViewModel nobViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        nobViewModel =
                ViewModelProviders.of(this).get(NobViewModel.class);
        View root = inflater.inflate(R.layout.fragment_nob, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        nobViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}