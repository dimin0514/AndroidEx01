package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.function.Supplier;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context _this = MemberDetail.this;
        //들어오는 녀석  this로 받음
        Intent intent = this.getIntent();
//        String seq = intent.getExtras().getString("seq");
//        Toast.makeText(_this,"넘어온 값:"+seq, Toast.LENGTH_LONG).show();
        final ItemDetail query = new ItemDetail(_this);
        query.seq = Integer.parseInt(intent.getExtras().getString("seq"))+1;
         final Main.Member member = query.get();
         //지금 이 상황에서는 그냥 리스트 몽땅 가져오는 거라 아래처럼 람다 안해도 된다.
 /*       Main.Member member = new Supplier<Main.Member>() {

            @Override
            public Main.Member get() {
                return query.get();
            }
        }.get();*/
        ImageView profile = findViewById(R.id.profile);
        profile.setImageDrawable(
                getResources()
                        .getDrawable(
                                getResources()
                                        .getIdentifier(
                                                _this.getPackageName()+":drawable/"
                                                        +member.photo, null, null
                                        )
                        )
        );
        TextView name = findViewById(R.id.name);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView addr = findViewById(R.id.addr);
        name.setText(member.name);
        email.setText(member.email);
        phone.setText(member.phone);
        addr.setText(member.addr);
//        Toast.makeText(_this, "회원이름 "+member.name, Toast.LENGTH_LONG).show();
        findViewById(R.id.updateBtn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(_this, MemberUpdate.class);
                        intent.putExtra("spec", String.format("%s,%s,%s,%s,%s,%s",
                                member.seq,member.name,
                                member.email,member.phone,
                                member.addr,member.photo));
                        startActivity(intent);
                    }
                });
        findViewById(R.id.listBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(_this, MemberList.class));
            }
        });

    }
    private class MemberDetailQuery extends Main.QueryFactory{
        // DB를 폰에 가져와서 실행. 헬퍼가 메인에 있는 녀석임.
        SQLiteOpenHelper helper;
        public MemberDetailQuery(Context _this) {
            super(_this);
            helper = new Main.SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemDetail extends MemberDetailQuery{
        int seq;
        public ItemDetail(Context _this) {
            super(_this);
        }
        public Main.Member get(){
            Main.Member member = null;
            //커서가 움직이면서 디비에서 결과값을 가져오는데 결과값이 없는 경우도 있음!
            Cursor cursor = getDatabase()
                    .rawQuery(String.format("SELECT * FROM %s WHERE %s LIKE '%s'",
                            Main.MEMBERS, Main.SEQ, seq),null);
            if(cursor !=null && cursor.moveToNext()){
                //커서야 값 담고, 커서를 다음으로 이동시킬때.. 이동시켜서 값을 담음, 하나만 가져오는 경우는 if를 씀 둘 이상일 경우 while  while(cursor.moveToNext())
                member = new Main.Member();
                member.seq = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Main.SEQ)));
                member.name = cursor.getString(cursor.getColumnIndex(Main.NAME));
                member.passwd = cursor.getString(cursor.getColumnIndex(Main.PASSWD));
                member.email = cursor.getString(cursor.getColumnIndex(Main.EMAIL));
                member.phone = cursor.getString(cursor.getColumnIndex(Main.PHONE));
                member.addr = cursor.getString(cursor.getColumnIndex(Main.ADDR));
                member.photo = cursor.getString(cursor.getColumnIndex(Main.PHOTO));
            }
            return member;
        }
    }
}
