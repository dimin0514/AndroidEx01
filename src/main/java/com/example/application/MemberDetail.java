package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context _this = MemberDetail.this;
        //들어오는 녀석  this로 받음
        Intent intent = this.getIntent();
        String seq = intent.getExtras().getString("seq");
        Toast.makeText(_this,"넘어온 값:"+seq, Toast.LENGTH_LONG).show();
    }
}
