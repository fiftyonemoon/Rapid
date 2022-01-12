package com.fom.rapid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fom.rapid.databinding.ActivityRecyclerViewBinding;

public class RecyclerViewActivity extends AppCompatActivity {

    private ActivityRecyclerViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecyclerViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setRecyclerView();
    }

    private void setRecyclerView() {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this);
        binding.rvExample.setAdapter(adapter);
    }
}