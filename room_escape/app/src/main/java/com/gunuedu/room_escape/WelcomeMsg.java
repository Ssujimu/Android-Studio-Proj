package com.gunuedu.room_escape;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeMsg extends AppCompatActivity {

    TextView welMsg;

    Button enterBtn;

    MediaPlayer mediaPlayer;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("게임을 종료하시겠습니까?");
        builder.setPositiveButton("예" ,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
//                int pid = android.os.Process.myPid();
//                android.os.Process.killProcess(pid);
//                System.exit(0);
//                finish();
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });

        builder.setNegativeButton("아니오" , new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

//        activity.finish();
//        ActivityCompat.finishAffinity(this);
//        System.exit(0);


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcomemsg);

        welMsg = (TextView) findViewById(R.id.welMsg);

        enterBtn = (Button) findViewById(R.id.enterBtn);

        mediaPlayer = MediaPlayer.create(this, R.raw.paper);

        Intent playgame = new Intent(getApplicationContext(), MainActivity.class);


        // 확인버튼
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();

                startActivity(playgame);

            }
        });

    }
}
