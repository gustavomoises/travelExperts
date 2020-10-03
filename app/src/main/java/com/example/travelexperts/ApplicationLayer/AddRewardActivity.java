package com.example.travelexperts.ApplicationLayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelexperts.BusinessLayer.Affiliation;
import com.example.travelexperts.BusinessLayer.Reward;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;

public class AddRewardActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddReward;
    Button btnAddRewardCancel,btnAddRewardSave, btnAddRewardDelete;
    DataSource dataSource;
    String mode;
    Reward reward;
    TextView tvAddRewardId;
    EditText etAddRwdName,etAddRwdDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward);
        btnAddRewardSave=findViewById(R.id.btnAddRewardSave);
        btnAddRewardCancel=findViewById(R.id.btnAddRewardCancel);
        btnAddRewardDelete=findViewById(R.id.btnAddRewardDelete);
        tvAddRewardId=findViewById(R.id.tvAddRewardId);
        etAddRwdDesc=findViewById(R.id.etAddRewardRwdDesc);
        etAddRwdName=findViewById(R.id.etAddRewardRwdName);
        dataSource = new DataSource(this);


        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (mode.equals("update")) {
            btnAddRewardDelete.setEnabled(true);
            reward = (Reward) intent.getSerializableExtra("Reward");
            etAddRwdName.setText(reward.getRwdName()+"");
            tvAddRewardId.setText(reward.getRewardId()+"");
            etAddRwdDesc.setText(reward.getRwdDesc()+"");
        }
        else
        {
            btnAddRewardDelete.setEnabled(false);
            reward=new Reward();
            etAddRwdName.setText("");
            tvAddRewardId.setText("*");
            etAddRwdDesc.setText("");
        }
        btnAddRewardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RewardActivity.class);
                startActivity(intent);

            }
        });

        btnAddRewardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                reward.setRwdName(etAddRwdName.getText() + "");
                reward.setRwdDesc(etAddRwdDesc.getText() + "");
                if (mode.equals("update"))
                {
                    if (dataSource.updateReward(reward)) {
                        Toast.makeText(getApplicationContext(), " Reward Updated!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),RewardActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), " Reward Ypdate Failed!", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    if (dataSource.insertReward(reward)) {
                        Toast.makeText(getApplicationContext(), " Reward Inserted!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),RewardActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), " Reward Insertion Failed!", Toast.LENGTH_LONG).show();
                    }
                }



            }
        });

        btnAddRewardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.deleteReward(reward);
                Toast.makeText(getApplicationContext(), " Reward Deleted!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), RewardActivity.class);
                startActivity(intent);
            }
        });




        //Set background color form Settings
        clAddReward= findViewById(R.id.clAddReward);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");



        switch (basicColor){
            case "White":
                clAddReward.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddReward.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddReward.setBackgroundColor(Color.GREEN);
                break;

        }
    }

    //Load Application Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.travelexpertsmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Go to selected menu item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.miBooking:
                Toast.makeText(this, item.getTitle()+" was clicked", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(this, BookingActivity.class);
                startActivity(intent1);
                break;
            case R.id.miPackage:
                Toast.makeText(this, item.getTitle()+" was clicked", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(this, PackageActivity.class);
                startActivity(intent2);
                break;
            case R.id.miProduct:
                Toast.makeText(this, item.getTitle()+" was clicked", Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent(this, ProductActivity.class);
                startActivity(intent3);
                break;
            case R.id.miMiscelaneous:
                Toast.makeText(this, item.getTitle()+" was clicked", Toast.LENGTH_LONG).show();
                Intent intent4 = new Intent(this, MiscellaneousActivity.class);
                startActivity(intent4);
                break;
            case R.id.miSettings:
                Toast.makeText(this, item.getTitle()+" was clicked", Toast.LENGTH_LONG).show();
                Intent intent5 = new Intent(this, SettingsActivity.class);
                startActivity(intent5);
                break;
            case R.id.miAbout:
                Toast.makeText(this, item.getTitle()+" was clicked", Toast.LENGTH_LONG).show();
                Intent intent6 = new Intent(this, AboutActivity.class);
                startActivity(intent6);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Set background color form Settings
        clAddReward= findViewById(R.id.clAddReward);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddReward.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddReward.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddReward.setBackgroundColor(Color.GREEN);
                break;
        }
    }

}