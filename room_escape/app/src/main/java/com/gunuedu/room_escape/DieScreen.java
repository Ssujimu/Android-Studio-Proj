package com.gunuedu.room_escape;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DieScreen extends AppCompatActivity {

    Button retryBtn;

    MediaPlayer dieme, dieending, retryBtnm;


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
        setContentView(R.layout.diescreen);

        retryBtn = (Button) findViewById(R.id.retryBtn);

        Intent mainscreen = new Intent(getApplicationContext(), MainScreen.class);

        dieme = MediaPlayer.create(this, R.raw.silent_hill);
        dieme.start();
        dieme.setLooping(true);

        dieending = MediaPlayer.create(this, R.raw.dieending);
        dieending.start();

        retryBtnm = MediaPlayer.create(this, R.raw.retrygame);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mainscreen);

                retryBtnm.start();

                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                dieme.stop();
            }
        });


    }
}
