package com.gunuedu.room_escape;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EndingScreen extends AppCompatActivity {

    Button exitexitexit;
    AlphaAnimation anim;

    MediaPlayer rundoor, finalrun;

    long delay = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endingscreen);

        exitexitexit = (Button) findViewById(R.id.exitexitexit);

        anim = new AlphaAnimation(0.0f,1.0f);
        anim.setDuration(100);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        exitexitexit.startAnimation(anim);

        rundoor = MediaPlayer.create(this, R.raw.closedoor);
        finalrun = MediaPlayer.create(this, R.raw.finalrun);

        finalrun.start();
        finalrun.setLooping(true);

        exitexitexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (System.currentTimeMillis() > delay) {
                    delay = System.currentTimeMillis() + 200;
                    return;
                }

                // 두 번 클릭 인식
                if (System.currentTimeMillis() <= delay) {
                    finalrun.stop();
                    rundoor.start();
                    Intent mainscreen = new Intent(getApplicationContext(), MainScreen.class);
                    startActivity(mainscreen);
                }


            }
        });
    }


    @Override
    public void onBackPressed() {

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("게임을 종료하시겠습니까?");

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
//                int pid = android.os.Process.myPid();
//                android.os.Process.killProcess(pid);
//                ActivityCompat.finishAffinity(this);
                finishAffinity();
                System.runFinalization();
                System.exit(0);
//                finish();
            }
        });
    }



}
