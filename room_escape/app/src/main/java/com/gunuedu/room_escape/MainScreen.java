package com.gunuedu.room_escape;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainScreen extends AppCompatActivity {


    TextView introText;
    Button startBtn;

    ConstraintLayout mainLay, welcomLay;
    MediaPlayer mediaPlayer, enterBtn;

    Activity activity;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("게임을 종료하시겠습니까?");
        builder.setPositiveButton("예" , new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
//                int pid = android.os.Process.myPid();
//                android.os.Process.killProcess(pid);
//                System.exit(0);
//                finish();
                finishAffinity();
                System.runFinalization();
                System.exit(0);
                mediaPlayer.stop();
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
        setContentView(R.layout.mainscreen);

        Intent welcomeMsg = new Intent(getApplicationContext(), WelcomeMsg.class);

        introText = (TextView)findViewById(R.id.introText);


        startBtn = (Button)findViewById(R.id.startBtn);

        mainLay = (ConstraintLayout)findViewById(R.id.mainLay);
        welcomLay = (ConstraintLayout)findViewById(R.id.countLay);

        // 음악 재생
        mediaPlayer = MediaPlayer.create(this, R.raw.why);

        // 입장하기 효과음
        enterBtn = MediaPlayer.create(this, R.raw.open_door);

        mediaPlayer.start();    // 이게 이제 음악 재생 시작 스탑 걸어줘야.
        mediaPlayer.setLooping(true);   // 반복


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainLay.setVisibility(view.INVISIBLE);
                startBtn.setVisibility(view.INVISIBLE);
                introText.setVisibility(view.INVISIBLE);

                // 입장 효과음
                enterBtn.start();

                // 배경음
                mediaPlayer.stop();

                // 화면 전환 위해
                startActivity(welcomeMsg);

                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }

        });

    }


}
