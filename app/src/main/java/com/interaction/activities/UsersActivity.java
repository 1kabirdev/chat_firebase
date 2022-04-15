package com.interaction.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.interaction.adapters.UsersAdapter;
import com.interaction.databinding.ActivityUsersBinding;
import com.interaction.listeners.UserListener;
import com.interaction.models.User;
import com.interaction.utilities.Constants;
import com.interaction.utilities.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends BaseActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferencesManager preferencesManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferencesManager = new PreferencesManager(getApplicationContext());
        setListeners();
        getUsers();
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferencesManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            if (currentUserId.equals(snapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.name = snapshot.getString(Constants.KEY_NAME);
                            user.email = snapshot.getString(Constants.KEY_EMAIL);
                            user.image = snapshot.getString(Constants.KEY_IMAGE);
                            user.token = snapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = snapshot.getId();
                            users.add(user);
                        }
                        if (users.size() > 0) {
                            UsersAdapter adapter = new UsersAdapter(users, this);
                            binding.usersRecyclerView.setAdapter(adapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }
                });
    }

    private void showErrorMessage() {
        Toast.makeText(this, "Нет доступных пользователей", Toast.LENGTH_LONG).show();
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBarUsers.setVisibility(View.VISIBLE);
        } else {
            binding.progressBarUsers.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}