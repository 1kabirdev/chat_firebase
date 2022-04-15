package com.interaction.bottomSheet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.interaction.R;
import com.interaction.activities.MainActivity;

public class InfoProfileFragment extends BottomSheetDialogFragment {

    private Button btnLogoutProfile;

    public static InfoProfileFragment newInstance() {
        return new InfoProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_profile, container, false);
        btnLogoutProfile = view.findViewById(R.id.btnLogoutProfile);
        btnLogoutProfile.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).signOut();
        });
        return view;
    }
}