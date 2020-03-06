package com.stutern.githublagos.ui_main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.stutern.githublagos.IMainActivity;
import com.stutern.githublagos.R;
import com.stutern.githublagos.ui_details.DetailsFragment;

public class MainActivity extends AppCompatActivity implements IMainActivity {
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        MainFragment mainFragment = new MainFragment();
        container = findViewById(R.id.container);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, mainFragment, "Main Fragment");
        transaction.commit();
    }

    @Override
    public void onItemClick(DetailsFragment detailsFragment) {
        detailsFragment.show(getSupportFragmentManager(), "DetailsFragment");
    }
}
