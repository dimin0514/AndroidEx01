package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        final Context _this = MemberUpdate.this;
        final String[] arr = this.getIntent().getStringExtra("spec").split(",");
        final TextView nameTV = findViewById(R.id.name);
        final TextView emailTV = findViewById(R.id.changeEmail);
        final TextView phoneTV = findViewById(R.id.changePhone);
        final TextView addrTV = findViewById(R.id.changeAddress);
        final ImageView profileTV = findViewById(R.id.profile);
        profileTV.setImageDrawable(
                getResources()
                        .getDrawable(
                                getResources()
                                        .getIdentifier(
                                                _this.getPackageName()+":drawable/"+arr[5],
                                                null,
                                                null
                                        )
                        )
        );
        nameTV.setText(arr[1]);
        emailTV.setText(arr[2]);
        phoneTV.setText(arr[3]);
        addrTV.setText(arr[4]);
        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemUpdate query = new ItemUpdate(_this);
                query.name = nameTV.getText().toString();
                query.email = emailTV.getText().toString();
                query.phone = phoneTV.getText().toString();
                query.addr = addrTV.getText().toString();
                query.seq = arr[0];
                query.update();
                Intent intent = new Intent(_this, MemberDetail.class);
                intent.putExtra("seq",arr[0]);
                startActivity(intent);
            }
        });
    }
    private class MemberUpdateQuery extends Main.QueryFactory{
        Main.SQLiteHelper helper;
        public MemberUpdateQuery(Context _this) {
            super(_this);
            helper = new Main.SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemUpdate extends MemberUpdateQuery{
        String name, email, phone, addr, seq;
        public ItemUpdate(Context _this) {
            super(_this);
        }
        public void update(){
            String sql = String.format(
                    " UPDATE %s\n" +
                            "     SET %s = '%s',\n" +
                            "         %s = '%s',\n" +
                            "         %s = '%s',\n" +
                            "         %s = '%s'\n" +
                            "     WHERE %s LIKE '%s'",
                    Main.MEMBERS, Main.NAME, name,
                    Main.EMAIL, email,
                    Main.PHONE, phone,
                    Main.ADDR, addr, Main.SEQ, seq);
            getDatabase().execSQL(sql);
            /*String sql = String.format("UPDATE %s SET %s ='%s' %s='%s' %s='%s' %s='%s' where %s='%s'",
                    Main.MEMBERS, Main.NAME,name,Main.EMAIL,email, Main.PHONE,phone,Main.ADDR,addr, Main.SEQ,seq);
            getDatabase().execSQL(sql);*/
        }

    }

}
