package com.gunuedu.room_escape;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;

import com.google.ar.core.Frame;

import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;

import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Session mSession;

    // Config imgConfig; // ??????

    GLSurfaceView mySurfaceView;

    MainRenderer mRenderer;

    //?????? ??????-> ??? ????????? ?????????
    GestureDetector mGestureDetector;

    //???????????? -> ??? ????????? ????????????
//    ScaleGestureDetector mScaleGestureDetector;

    // ????????????
    TextView countDown;

    // ????????? ?????? ??????
    String timer = "002000";

    // ???????????????
    ConstraintLayout passInviLay, itemlistLay, AllItemLay, girlLay,
            useWeaponLay, turnLay, turnLay2, turn_eleven_lay, aproLay, gifLay;


    TextView skull_text;

    // password, ???????????? ????????? ?????????
    TextView passTxt, leftTxt, upTxt, downTxt, rightTxt;

    // ????????? ?????????
    ImageView bantoImg, bantoFlashImg;


    // ????????? ??????
    CountDownTimer countdownTimer;

    // ?????? ??????
    Button cancelbtn, turn1, turn2;

    // item Image ??????
    ImageView flash_img_btn, cross_img_btn, knife_img_btn, heart_img_btn, bandage_img_btn, item_img_btn;

    // ?????? ?????? ??????
    WindowManager.LayoutParams wl;

    AlphaAnimation animation;

    // ?????????
    boolean flashlight, cross, show = false, showChk = false, modelInit = false, moving = false;

    boolean brush_moving = false, brush_modelInit = false ;

    // ?????????, ??????, ???
    boolean try_knife = false, try_bandage = false, try_heart =false;

    // zombie ??????
    boolean girl_chk = false, girlshow = false, girlstop = false, girlback = false;

    Display display;

    float[] showObjMatrix = new float[16];

    // ??????????????? ????????? ??????
    float dx, dy;

    // ????????? ?????? ????????????
    float [] knifeMatrix = new float[16];
    float [] monsterMatrix = new float[16];
    float [] bandageMatrix = new float[16];
    float [] heartMatrix = new float[16];

    // ?????? ??????
    // 5 matrix
    float [] keyMatrix5 = new float[16];

    // 9 matrix
    float [] keyMatrix9 = new float[16];

    // 1 matrix
    float [] keyMatrix1 = new float[16];

    // 2 matrix
    float [] keyMatrix2 = new float[16];

    // ?????? ?????? ???


    // girl ??????
    float [] girlZombieObjMatrix = new float[16];

    /// ??? ????????? ?????? ?????? ??????
    float [] brushMatrix = new float[16];
    float [] aphro1Matrix = new float[16];


    // ?????????
    float [] colorRed = {1f,0f,0f,0.6f};
    float [] colorYellow = {1f,1f,0f,0.6f};
    float [] colorGreen = {0f,1f,0f,0.6f};
    float [] colorBlue = {0f,0f,1f,0.6f};
    float [] colorBlack = {0f,0f,0f,0.6f};

    // ????????? ????????? ????????? ?????? ???
    int cnt = 1;
    /// ????????????

    // ????????? ???
    // ?????? ????????? ??? true??? ??????
    boolean eachOther = false, eleven = false;
    int dd = 0, ddd=0, dd2 = 180;
    float [] skullMatrix1 = new float[16];
    float [] skullMatrix2 = new float[16];
    float [] nineMatrix = new float[16];


    float displayX, displayY, mRotate = 0f, brush_mRotate = 0f, mScale = 1f, brush_mScale = 0.0003f;

    float [] viewMatrix = new float[16];
    float [] projMatrix = new float[16];

    // ?????????, ??????
    MediaPlayer rainBgm, crazy_laugh_short, doong_MP3, du, re, mi ,pa ,stone;

    ImageView  gif_1;



    // Oncreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hidStatusBarTitleBar();
        setContentView(R.layout.activity_main);

        //??????????????? gif
        gifLay = (ConstraintLayout) findViewById(R.id.gifLay);
        gifLay.setVisibility(View.INVISIBLE);
        gif_1 = (ImageView)findViewById(R.id.gif_1);
        Glide.with(this).load(R.raw.gho).into(gif_1);

        // ????????? ?????? ??????
        du = MediaPlayer.create(this, R.raw.du);// ????????? ??????
        re = MediaPlayer.create(this, R.raw.re);
        mi = MediaPlayer.create(this, R.raw.mi);
        pa = MediaPlayer.create(this, R.raw.pa);
        //????????????
        stone = MediaPlayer.create(this, R.raw.open_rock_door);

        // ?????????, ??????
        rainBgm = MediaPlayer.create(this, R.raw.rain);
        rainBgm.start();
        rainBgm.setLooping(true);

        crazy_laugh_short = MediaPlayer.create(this, R.raw.crazy_laugh_short);

        doong_MP3 = MediaPlayer.create(this, R.raw.dieending);


        // ????????? ?????????
        bantoImg = (ImageView) findViewById(R.id.bantoImg);
        bantoFlashImg = (ImageView) findViewById(R.id.bantoFlashImg);
        bantoFlashImg.setVisibility(View.INVISIBLE);

        // ?????? ??????
        turn1 = (Button)findViewById(R.id.turn1);
        turn2 = (Button)findViewById(R.id.turn2);

        skull_text = (TextView)findViewById(R.id.skull_text);

        skull_text.setVisibility(View.INVISIBLE);

        turnLay = (ConstraintLayout) findViewById(R.id.turnLay);
        turnLay2 = (ConstraintLayout) findViewById(R.id.turnLay2);

        turnLay.setVisibility(View.INVISIBLE);
        turnLay2.setVisibility(View.INVISIBLE);

        // 11??? ????????? ?????? text
        turn_eleven_lay = (ConstraintLayout) findViewById(R.id.turn_eleven_lay);
        turn_eleven_lay.setVisibility(View.INVISIBLE);
        // ?????? ???



        display = getWindowManager().getDefaultDisplay();
        // ??????
        wl = getWindow().getAttributes(); //????????????
        wl.screenBrightness = 0.5f;
        getWindow().setAttributes(wl);

        // ?????? ??????
        Point point = new Point();
        display.getRealSize(point);
        dx = point.x / 2;
        dy = point.y / 2;

        // id ?????????
        mySurfaceView = (GLSurfaceView)findViewById(R.id.glSsurfaceview);

        countDown = (TextView) findViewById(R.id.countDown);

        // ???????????? ????????? ??????
        passInviLay = (ConstraintLayout) findViewById(R.id.passInviLay);
        passInviLay.setVisibility(View.INVISIBLE);
        AllItemLay = (ConstraintLayout) findViewById(R.id.AllItemLay);
        passTxt = (TextView) findViewById(R.id.passTxt);

        cancelbtn = (Button) findViewById(R.id.cancelbtn);

        itemlistLay = (ConstraintLayout)findViewById(R.id.itemLay);
        itemlistLay.setVisibility(View.INVISIBLE);

        item_img_btn = (ImageView) findViewById(R.id.item_img_btn);

        // girl
        leftTxt = (TextView) findViewById(R.id.leftTxt);
        upTxt = (TextView) findViewById(R.id.upTxt);
        downTxt = (TextView) findViewById(R.id.downTxt);
        rightTxt = (TextView) findViewById(R.id.rightTxt);

        girlLay = (ConstraintLayout) findViewById(R.id.girlLay);

        girlLay.setVisibility(View.INVISIBLE);

        // ????????? image
        flash_img_btn = (ImageView) findViewById(R.id.flash_img_btn);
        cross_img_btn = (ImageView) findViewById(R.id.cross_img_btn);
        knife_img_btn = (ImageView) findViewById(R.id.knife_img_btn);
        bandage_img_btn = (ImageView) findViewById(R.id.bandage_img_btn);
        heart_img_btn = (ImageView) findViewById(R.id.heart_img_btn);


        itemlistLay.setVisibility(View.INVISIBLE);

        // ?????? ????????? ????????????
        useWeaponLay = (ConstraintLayout) findViewById(R.id.useWeaponLay);
        useWeaponLay.setVisibility(View.INVISIBLE);

        //??????????????? ?????? ????????????
        aproLay = (ConstraintLayout) findViewById(R.id.aproLay);
        aproLay.setVisibility(View.INVISIBLE);

        // ?????? Thread ??????
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

            //            //??????
//            @Override
            public boolean onDoubleTap(MotionEvent event) {
                // draw ??????
//                mTouched = true;
//                modelInit = false;
                displayX= event.getX();
                displayY = event.getY();
                Log.d("onDoubleTap ???",displayX+","+displayY);
                return true;
            }

            // rabbit??? ????????? ?????????
            class MyGo extends Thread{

                int code;

//                MyGo(float distance){
//                    code = distance < 0 ? 1 : -1;
//                }

                @Override
                public void run() {

                    if(!moving) {
                        moving = true;
                        float[] bufMatirx = knifeMatrix.clone();


                        for (int i = 0; i < 400; i++) {
                            Matrix.translateM(knifeMatrix, 0, 0, 0, 0.005f);
                            try {
                                sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        knifeMatrix = bufMatirx;
                        moving = false;

                    }
                }
            }

            // ??? ?????????
            class brushGo extends Thread{
                @Override
                public void run() {

                    if(!brush_moving) {

                        // ???????????? 1~5(??????~??????)?????? ?????? ?????? ????????? 6??? ?????? 1(??????)??? ?????????
                        if (cnt>4) {
                            cnt = 1;
                        }

                        brush_moving = true;
                        float[] bufMatirx = brushMatrix.clone();

                        for (int i = 0; i < 150; i++) {
                            Log.d("Thorw ???", "?????????!!!!!");

                            Matrix.translateM(brushMatrix, 0, 0, 300, 0);
                            try {
                                sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        brushMatrix = bufMatirx;
                        brush_moving = false;

                        // cnt = 1 : ?????? , cnt = 2 : ?????? , cnt = 3 : ?????? , cnt = 4 : ?????? , cnt = 5 : ??????
                        cnt ++;
                        Log.d("cnt????????? ???" , cnt+"");

                    }
                }
            }

            //??????
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d("onScroll ???", "Scroll??? ???????????????.");
                if(modelInit && !moving) {
                    if(distanceY >-50 || distanceY <50) {
                        Log.d("onScroll ???", "????????? ?????? ?????????????????????");
                        mRotate += 000.1;
//                        mRotate += -distanceX / 5;
                        Matrix.rotateM(knifeMatrix, 0, -distanceX / 20, 0f, 100f, 0f);
                    } else {
                        new MyGo().start();
                    }

                }

                // ??? ?????????
                if(brush_modelInit && !brush_moving) {

                    if(distanceY >-50 && distanceY <50) {
                        brush_mRotate += -distanceX / 5;
                        Matrix.rotateM(brushMatrix, 0, -distanceX / 5, 0f, 100f, 0f);
                    }else{
                        new brushGo().start();
                    }
                }
//                 Log.d("onScroll ???", distanceX + "," + distanceY);
                return true;
            }
        });



        // ?????????
        item_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemlistLay.requestLayout();

                if(itemlistLay.getVisibility()==View.INVISIBLE ){
                    itemlistLay.setVisibility(View.VISIBLE);
                }else{
                    itemlistLay.setVisibility(View.INVISIBLE);

                }
            }
        });

        // ?????????
        flash_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashlight = !flashlight;
                mRenderer.makeFlash(flashlight);

                // ????????? ????????? ?????? ??????????????? ??????~
                if (bantoFlashImg.getVisibility() == View.INVISIBLE && flashlight == true) {


                    runOnUiThread(() -> {
                        bantoFlashImg.setVisibility(View.VISIBLE);
                        bantoFlashImg.requestLayout();
                        bantoImg.setVisibility(View.INVISIBLE);
                        bantoImg.requestLayout();
                    });

                    mRenderer.makeCross(false);

                } else {
                    bantoFlashImg.setVisibility(View.INVISIBLE);
                    bantoImg.setVisibility(View.VISIBLE);
                }



            }
        });

        // ?????????
        cross_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cross = !cross;

                mRenderer.makeCross(cross);
                girlstop = true;
                new girlBye().start();


                if (bantoImg.getVisibility() == View.VISIBLE && cross == true) {


                    runOnUiThread(() -> {
                        bantoFlashImg.setVisibility(View.INVISIBLE);
                        bantoFlashImg.requestLayout();
                        bantoImg.setVisibility(View.VISIBLE);
                        bantoImg.requestLayout();
                    });

                    mRenderer.makeFlash(false);

                } else {
                    bantoImg.setVisibility(View.VISIBLE);
                    bantoFlashImg.setVisibility(View.INVISIBLE);
                }

//                Handler mSe = new Handler();

//                if(cross){
//                    wl.screenBrightness = 1.0f;
//                    getWindow().setAttributes(wl);
//
//                    mRenderer.makeFlash(false);
//
//
//                    brightseek.setVisibility(View.INVISIBLE);
//
//                    mSe.postDelayed(new Runnable()  {
//                        public void run() {   // ?????? ?????? ??? ????????? ??????
//                            wl.screenBrightness = 0.3f;
//                            getWindow().setAttributes(wl);
//                            mRenderer.makeCross(false);
//                        }
//                    }, 3000); //3?????? ????????????
//
//                }

            }
        });

        // ?????????
        knife_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("????????? ???: ", try_knife+"");
                try_knife = true;
                Log.d("????????? ???: ", try_knife+"");
                Log.d("????????? ???: ", "??????????????????");

                try_bandage = false;
                try_heart = false;
            }
        });

        // ??????
        bandage_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("???????????? ???: ", try_bandage+"");
                try_bandage = true;
                Log.d("???????????? ???: ", try_bandage+"");
                Log.d("???????????? ???: ", "?????????????????????");

                try_knife = false;
                try_heart = false;

            }
        });

        // ??????
        heart_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("????????? ???: ", try_heart+"");
                try_heart = true;
                Log.d("????????? ???: ", try_heart+"");
                Log.d("????????? ???: ", "??????????????????");

                try_knife = false;
                try_bandage = false;

            }
        });

        // ????????? ????????? ????????? ????????? ??????. ????????? ??????
        countDown(timer);

        // ?????? ???????????? ??????????????? ??????????????????
        class runTest extends Thread {
            @Override
            public void run() {
                if (!show) {
                    show = true;
                    float[] bb = showObjMatrix.clone();

                    for (int i = 0; i < 200; i++) {
                        Matrix.translateM(showObjMatrix, 0, 0, 0, -0.2f);
                        mRenderer.showObj.setModelMatrix(showObjMatrix);
                        try {
                            sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mRenderer.showObj = null;
                    showObjMatrix = bb;
                    // rrr = false;
                }
            }
        }

        //?????? ?????? ?????? --> ?????? ??????
        DisplayManager displayManager = (DisplayManager)getSystemService(DISPLAY_SERVICE);

        if(displayManager != null){

            //?????? ????????? ??????
            displayManager.registerDisplayListener(
                    new DisplayManager.DisplayListener() {
                        @Override
                        public void onDisplayAdded(int displayId) {

                        }

                        @Override
                        public void onDisplayRemoved(int displayId) {

                        }

                        //????????? ??????????????????
                        @Override
                        public void onDisplayChanged(int displayId) {
                            //????????? --> ????????? ???????????? ????????? ??????
                            synchronized (this){
                                //?????? ????????? ????????????
                                mRenderer.onDisplayChanged();
                            }

                        }
                    }, null);
        }



        MainRenderer.RenderCallBack mr = new MainRenderer.RenderCallBack() {

            //MainRenderer??? onDrawFrame() -- ????????? ?????? ?????? ??????
            //MainActivity?????? ????????? ?????? ????????? ?????? ????????? ??????.
            @Override
            public void preRender() {

                if(mRenderer.viewportChange){
                    display = getWindowManager().getDefaultDisplay();

                    mRenderer.updateSession(mSession, display.getRotation());
                }


                //session??? ????????? ????????? ?????????  mainRenderer??? ???????????? ????????? ????????? ??????
                // session ????????? : ????????????  --> mainRenderer??? ????????? :  ????????? ????????? ????????????
                mSession.setCameraTextureName(mRenderer.getTextureID());


                Frame frame = null;

                try {
                    frame = mSession.update(); //???????????? ????????? ??????????????????.
                } catch (CameraNotAvailableException e) {
                    e.printStackTrace();
                }
//                imgConfig = new Config(mSession);
//                imgConfig.setFocusMode(Config.FocusMode.AUTO); // ??????
//                mSession.configure(imgConfig);
                mRenderer.mCamera.transformDisplayGeometry(frame);

                // ?????????????????? obj ????????? ??????????????? ??????
                // if(mRenderer.mObj_1 != null && clear2 && !clear3){
                if (!showChk) {
//                    Log.d("preRender ???", "???????");
                    List<HitResult> results = frame.hitTest(dx, dy);
                    for (HitResult hr : results) {
                        Pose pose = hr.getHitPose();
                        Trackable trackable = hr.getTrackable();

                        // ?????? ?????? ????????? Plane ?????? Plane ??? ???????????? ?????????
                        if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(pose)) {
                            pose.toMatrix(showObjMatrix, 0);
                            // Matrix.translateM(modelMatrix, 0, 0f, 0f, 0f);
                            // Matrix.rotateM(modelMatrix, 0, 60f, 0f, 100f, 0f);
                            Matrix.scaleM(showObjMatrix, 0, 0.6f, 0.6f, 0.6f);
                            mRenderer.showObj.setModelMatrix(showObjMatrix);
                            crazy_laugh_short.start();
                            doong_MP3.start();
                            showChk = true;
                            new Thread(() -> {
                                try {
                                    sleep(5000);
                                    new runTest().start();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                    }
                    // clear3 = true;
                }
                // }

                Collection<Plane> planes = mSession.getAllTrackables(Plane.class);
                for (Plane plane : planes) {
                    if (plane.getTrackingState() == TrackingState.TRACKING && plane.getSubsumedBy() == null) {
                        mRenderer.mPlane.update(plane);
                    } else {

                    }
                }

                Collection<Plane> planes2 = mSession.getAllTrackables(Plane.class);

                for (Plane plane: planes) {
//                    if(plane.getTrackingState() == TrackingState.TRACKING && plane.getSubsumedBy() == null) {
//                        mRenderer.mPlane.update(plane);
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                myTextView.setText("?????? ?????????");
//                            }
//                        });
//
//                    }else{
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                myTextView.setText("????????????");
//                            }
//                        });
//                    }

                    if(brush_moving){
                        Log.d("????????? ???", "???????????????!!!!");
                        brush_catchCheck();

                    }
                }

                Camera camera = frame.getCamera();

                drawImages(frame);

                float [] viewMatrix = new float[16];
                float [] projMatrix = new float[16];

                camera.getProjectionMatrix(projMatrix, 0, 0.1f, 100f);
                camera.getViewMatrix(viewMatrix, 0);


                mRenderer.updateProjMatrix(projMatrix);
                mRenderer.updateViewMatrix(viewMatrix);
                //Log.d("MainActivity ???","preRender() ???");


            }
        };

        mRenderer = new MainRenderer(mr, this);

        //pause ??? ?????? ????????? ???????????? ?????? ??????.
        mySurfaceView.setPreserveEGLContextOnPause(true);
        mySurfaceView.setEGLContextClientVersion(3); //?????? 3.0 ??????

        //????????? ??????
        mySurfaceView.setRenderer(mRenderer);
        //????????? ?????? ??????
        mySurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //????????? ??????
        requestPermission();

        //ARCore session ?????? --> ????????? ??????
        if(mSession == null){

            try {

                Log.d("?????? ???????",
                        ArCoreApk.getInstance().requestInstall(this, true)+"");

                switch (ArCoreApk.getInstance().requestInstall(this, true)){
                    case INSTALLED:
                        //ARCore  ?????? ????????? ?????? ??????
                        mSession = new Session(this);

                        //ARCore ??????????????? Config
                        Config config = new Config(mSession);
                        // imgConfig = new Config(mSession); // ??????
                        //?????? ?????? ??????
                        config.setInstantPlacementMode(Config.InstantPlacementMode.LOCAL_Y_UP);

                        config.setFocusMode(Config.FocusMode.AUTO);

                        //???????????? ????????? ?????? ??????????????? ??????
                        // imgConfig.setGeospatialMode(Config.GeospatialMode.ENABLED);
                        mSession.configure(config);

                        Log.d("?????? ???????","???????????????");
                        break;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            Config imgConfig = new Config(mSession);
            setImgDB(imgConfig);
            imgConfig.setFocusMode(Config.FocusMode.AUTO);
            mSession.configure(imgConfig);


        }


        try {
            mSession.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
        mySurfaceView.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mySurfaceView.onPause();
        mSession.pause();
    }

    void requestPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.CAMERA},
                    0
            );
        }
    }

    void hidStatusBarTitleBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }


    @Override
    public void onBackPressed() {

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("????????? ?????????????????????????");

        builder.setPositiveButton("???" ,new DialogInterface.OnClickListener() {
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

        builder.setNegativeButton("?????????" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    // ????????? ?????????
    public void countDown(String time) {
        long timer = 0;

        // 1000 ????????? 1???
        // 60000 ????????? 1???
        // 60000 * 3600 = 1??????

        String getHour = time.substring(0, 2);
        String getMin = time.substring(2, 4);
        String getSecond = time.substring(4, 6);

        // "00"??? ?????????, ????????? ????????? 0 ?????? ??????
        if (getHour.substring(0, 1) == "0") {
            getHour = getHour.substring(1, 2);
        }

        if (getMin.substring(0, 1) == "0") {
            getMin = getMin.substring(1, 2);
        }

        if (getSecond.substring(0, 1) == "0") {
            getSecond = getSecond.substring(1, 2);
        }

        // ????????????
        timer = Long.valueOf(getHour) * 1000 * 3600 + Long.valueOf(getMin) * 60 * 1000 + Long.valueOf(getSecond) * 1000;

        // ????????? ?????? : ????????? ?????? (???????????? 30?????? 30 x 1000(??????))
        // ????????? ?????? : ??????( 1000 = 1???)

        countdownTimer = new CountDownTimer(timer, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {
                // ????????????
                String hour = String.valueOf(millisUntilFinished / (60 * 60 * 1000));

                // ?????????
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000)) ;
                String min = String.valueOf(getMin / (60 * 1000)); // ???

                // ?????????
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // ?????????

                // ??????????????? ??????
                String millis = String.valueOf((getMin % (60 * 1000)) % 1000); // ???

                // ????????? ???????????? 0??? ?????????
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }

                // ?????? ???????????? 0??? ?????????
                if (min.length() == 1) {
                    min = "0" + min;
                }

                // ?????? ???????????? 0??? ?????????
                if (second.length() == 1) {
                    second = "0" + second;
                }

                countDown.setText(min + ":" + second);
            }

            @Override
            public void onFinish() {
                // ????????? ??? . ?????? ??????
                Intent dieScreen = new Intent(getApplicationContext(), DieScreen.class);
                rainBgm.stop(); // ????????? ????????? ??????. ?????? ?????? stop
                startActivity(dieScreen);
//                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }.start();
    }

    // ????????? ?????? ???????????? ?????? ?????? ??????
    String passSave = "";

    // ?????? ???????????? ??????
    public void passbtn(View v) {

        switch (v.getId()){
            case R.id.btn1:

                passSave += "1";
                passTxt.setText(passSave);

                break;
            case R.id.btn2:
                passSave += "2";
                passTxt.setText(passSave);

                break;
            case R.id.btn3:
                passSave += "3";
                passTxt.setText(passSave);


                break;
            case R.id.btn4:
                passSave += "4";
                passTxt.setText(passSave);


                break;
            case R.id.btn5:
                passSave += "5";
                passTxt.setText(passSave);

                break;
            case R.id.btn6:
                passSave += "6";
                passTxt.setText(passSave);

                break;
            case R.id.btn7:
                passSave += "7";
                passTxt.setText(passSave);

                break;
            case R.id.btn8:
                passSave += "8";
                passTxt.setText(passSave);

                break;
            case R.id.btn9:
                passSave += "9";
                passTxt.setText(passSave);

                break;
            case R.id.backbtn:
                if(passSave.length() != 0){
                    passSave = passSave.substring(0, passSave.length()-1);
                    passTxt.setText(passSave);
                }
                break;
            case R.id.okbtn:
                // ????????? ???????????? ?????? == ??????
                passTxt.setText(passSave);
                if (passTxt.getText().toString().equals("5912")) {
                    Intent ending = new Intent(getApplicationContext(), EndingScreen.class);
                    startActivity(ending);
                    rainBgm.stop(); // ending ??? ??? ?????? ?????? stop
                    countdownTimer.cancel();
                } else {
                    YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(findViewById(R.id.passTxt));
                    Toast.makeText(getApplicationContext(), "????????? ???????????????.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancelbtn:

                animation = new AlphaAnimation(0.0f,1.0f);
                animation.setDuration(100);
                animation.setStartOffset(1);
//                animation.setRepeatMode(Animation.REVERSE);
                animation.setRepeatCount(Animation.REVERSE);

                cancelbtn.startAnimation(animation);

//                Toast.makeText(getApplicationContext(), "????????? ???????????? ??????." , Toast.LENGTH_SHORT).show();

//                cancelbtn.setVisibility(View.INVISIBLE);

                runOnUiThread(() -> {
                    passInviLay.setVisibility(View.INVISIBLE);

                    passInviLay.requestLayout();
                });

                break;
        }
        if (passSave.length() >= 4){
            passSave = passSave.substring(0, 4);
            passTxt.setText(passSave);
        }
    }

    //?????????DB ??????
    void setImgDB(Config mConfig){
        //??????????????????????????? ??????
        AugmentedImageDatabase imgDB = new AugmentedImageDatabase(mSession);

        try {
            // ????????? ??????
            InputStream is = getAssets().open("starry_night.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("final_escape", bitmap);
            is.close();

            // ???????????????
            is = getAssets().open("angel.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("angel_jpg", bitmap);
            is.close();

            // ??? ???????????? ???????????? ??????
            is = getAssets().open("shako.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("shako_jpg", bitmap);
            is.close();

            // ????????? ??????
            is = getAssets().open("girl.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("girl_jpg", bitmap);
            is.close();

            // ????????? ??????
            is = getAssets().open("ghost.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("ghost_jpg", bitmap);
            is.close();

            // ?????? 1
            is = getAssets().open("killedhim.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("surprise1", bitmap);
            is.close();

            // ?????? 2
            is = getAssets().open("mirror.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("surprise2", bitmap);
            is.close();

            // ??????_1(???????????? ????????? ???)??? ????????? ?????????
            is = getAssets().open("alone_girl.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("alone_girl_jpg", bitmap);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //????????? ?????????????????? ??????
        mConfig.setAugmentedImageDatabase(imgDB);
    }


    //????????? ???????????? ?????? ?????? ?????????
    void drawImages(Frame frame){

        Collection<AugmentedImage> agImgs = frame.getUpdatedTrackables(AugmentedImage.class);

        for (AugmentedImage img: agImgs){

            if(img.getTrackingState()== TrackingState.TRACKING) {

                Pose pose = img.getCenterPose();
                String imgName = img.getName();
                switch (imgName){

                    // ?????? ????????? ??????
                        // 5 ??? Obj ?????? ???
                    case "angel_jpg":
                        imgName = null;
//                        Log.d("angelImg ???", "?????? ????????? ??????");
                        modelInit = true;

                        runOnUiThread(() -> {
                            useWeaponLay.setVisibility(View.VISIBLE);
                            useWeaponLay.requestLayout();
                        });

//                        modelMatrix = new float[16];
                        List<HitResult> results = frame.hitTest(displayX, displayY);
                        for (HitResult hr : results) {
                            Trackable trackable = hr.getTrackable();

                            float [] monsterMatrix = new float[16];
                            float [] changeMatrix= new float[16];

                            float [] keyMatrix = new float[16];
//ddd
//                            pose.toMatrix(rabbitMatrix,0);

                            pose.toMatrix(keyMatrix,0);
                            // ??????0609 -> ?????? ????????? ????????????
                            pose.toMatrix(monsterMatrix,0);
                            // ?????? 0609 changeMatrix ??????
//                            pose.toMatrix(changeMatrix,0);

                            mRenderer.mObj_monster.setModelMatrix(monsterMatrix);


//                            mRenderer.mObj_ex.setModelMatrix(modelMatrix2);
                            catchCheck();
                            Log.d("catchCheck ???", "???????????? ??????" );

                        }

                        pose.toMatrix(monsterMatrix, 0);
                        Matrix.scaleM(monsterMatrix, 0, 0.002f, 0.002f, 0.002f);
                        Matrix.rotateM(monsterMatrix, 0, 90, 0, 1, 0); //
//                Matrix.translateM(modelMatrix,0,0,0f,0);
                        mRenderer.mObj_monster.setModelMatrix(monsterMatrix);

                        float[] add = calculateInitialBallPoint(mRenderer.width, mRenderer.height, projMatrix, viewMatrix);


                        if (try_knife) {
                            try_bandage = false;
                            try_heart = false;

                            // ????????? scale??? 0?????? ????????????
                            Matrix.scaleM(bandageMatrix, 0, 0f, 0, 0f);
                            mRenderer.mObj_bandage.setModelMatrix(bandageMatrix);

                            Matrix.scaleM(heartMatrix, 0, 0f, 0, 0f);
                            mRenderer.mObj_heart.setModelMatrix(heartMatrix);


                            pose.toMatrix(knifeMatrix,0);

                            Matrix.translateM(knifeMatrix, 0, 0, 0.25f, 0.1f);

                            Matrix.rotateM(knifeMatrix,0,270,1,0,0);
                            Matrix.translateM(knifeMatrix, 0, 0, 0, -mRotate);

                            Matrix.scaleM(knifeMatrix, 0, 0.004f, 0.004f, 0.004f);
                            mRenderer.mObj_knife.setModelMatrix(knifeMatrix);
                        } else if (try_bandage) {
                            try_knife = false;
                            try_heart = false;

                            // ???????????? scale??? 0?????? ????????????
                            Matrix.scaleM(knifeMatrix, 0, 0f, 0, 0f);
                            mRenderer.mObj_knife.setModelMatrix(knifeMatrix);

                            Matrix.scaleM(heartMatrix, 0, 0f, 0, 0f);
                            mRenderer.mObj_heart.setModelMatrix(heartMatrix);

                            pose.toMatrix(bandageMatrix,0);

                            Matrix.translateM(bandageMatrix, 0, 0, 0.25f, 0.1f);

                            Matrix.rotateM(bandageMatrix,0,270,1,0,0);
                            Matrix.translateM(bandageMatrix, 0, 0, 0, -mRotate);
                            Matrix.scaleM(bandageMatrix, 0, .004f, 0.004f, 0.004f);
                            mRenderer.mObj_bandage.setModelMatrix(bandageMatrix);

                        } else if (try_heart) {
                            try_knife = false;
                            try_bandage = false;

                            // ???????????? scale??? 0?????? ????????????
                            Matrix.scaleM(knifeMatrix, 0, 0f, 0, 0f);
                            mRenderer.mObj_knife.setModelMatrix(knifeMatrix);

                            Matrix.scaleM(bandageMatrix, 0, 0f, 0, 0f);
                            mRenderer.mObj_bandage.setModelMatrix(bandageMatrix);

                            pose.toMatrix(heartMatrix,0);

                            Matrix.translateM(heartMatrix, 0, 0, 0.25f, 0.1f);

                            Matrix.rotateM(heartMatrix,0,270,1,0,0);
                            Matrix.translateM(heartMatrix, 0, 0, 0, -mRotate);
                            Matrix.scaleM(heartMatrix, 0, .004f, 0.004f, 0.004f);
                            mRenderer.mObj_heart.setModelMatrix(heartMatrix);

                        }


                        Log.d("Rotate ??? ", mRotate+"");
                        if (mRotate>1.5){
                            Log.d("Rotate ??? ", mRotate+"");
                            Matrix.scaleM(knifeMatrix, 0, 0,0,0);
                            mRenderer.mObj_knife.setModelMatrix(knifeMatrix);

                            Matrix.scaleM(monsterMatrix, 0, 0f, 0f, 0f);
                            mRenderer.mObj_monster.setModelMatrix(monsterMatrix);

                            pose.toMatrix(keyMatrix5, 0);
                            Matrix.translateM(keyMatrix5, 0, 0.01f, 0.01f, 0.01f);
                            Matrix.rotateM(keyMatrix5,0,270,1,0,0);
                            Matrix.scaleM(keyMatrix5, 0, 0.1f, 0.1f, 0.1f);

                            mRenderer.mObj_number5.setModelMatrix(keyMatrix5);

                            runOnUiThread(() -> {
                                useWeaponLay.removeAllViews();
                                useWeaponLay.requestLayout();
                            });
//                            Matrix.translateM(modelMatrix, 0, 0, 0, 0);
//                            mRenderer.mObj.setModelMatrix(modelMatrix);
                        }
                        break;

                        // brush

                    // 2??? ?????? obj ?????????
                    case "shako_jpg":
                        Log.d("image ??????", "?????? ????????? ??????");
                        runOnUiThread(() -> {
                            aproLay.setVisibility(View.VISIBLE);
                            aproLay.requestLayout();
                        });

                        if(!brush_modelInit){
                            brush_modelInit = true;

                            aphro1Matrix = new float[16];
                            pose.toMatrix(brushMatrix,0);
                            pose.toMatrix(aphro1Matrix,0);
                            pose.toMatrix(keyMatrix2,0);

                            // 1 ????????? 0?????? 0 ?????? ?????? 1 ?????? ???????????? -1
                            float zz = ((float)Math.random()*4+1) * ((int)(Math.random()*2) >0 ?1 : -1);
                            float xx = ((float)Math.random()*6-3);

                            // ????????? ??????
                            zz = -4f;
                            xx = 0f;

//                                Matrix.scaleM(modelMatrix, 0, 0.0003f, 0.0003f, 0.0003f);

                            Matrix.translateM(aphro1Matrix, 0, xx, 0, zz);

                            // ??????????????? ?????? ??????
                            Matrix.rotateM(aphro1Matrix, 0, 270, 1, 0, 0);
                            Matrix.translateM(aphro1Matrix,0,0,0,-0.5f);
                            Matrix.scaleM(aphro1Matrix, 0, 0.008f, 0.008f, 0.008f);

                            mRenderer.aphro1.setColorCorrection(colorBlack);
                            mRenderer.aphro1.setModelMatrix(aphro1Matrix);

                            //??? ????????? ??????
                            Matrix.rotateM(brushMatrix, 0, 270, 1f, 0f, 0f);
                            Matrix.translateM(brushMatrix,0,0,0,0.3f);
                            Matrix.scaleM(brushMatrix,0, brush_mScale, brush_mScale, brush_mScale);

                            mRenderer.aphro1.setModelMatrix(aphro1Matrix);

                            Matrix.scaleM(keyMatrix2,0,1f,1f,1f);
                            Matrix.translateM(keyMatrix2,0,0,0,-2f);


                        }


                        // Matrix.translateM(modelMatrix,0,0f,0.0f,0f);
                        // Matrix.rotateM(modelMatrix,0, 45f,0f,100f,0f);
                        // Matrix.scaleM(modelMatrix,0,1f,3f,1f);

//                        mRenderer.mObj.setLightIntensity(lightyIntensity);

                        float [] colorArr = {1f,1f,1f,0.6f};

                        mRenderer.mObj_brush.setColorCorrection(colorArr);
                        mRenderer.mObj_brush.setModelMatrix(brushMatrix);
                        break;

                        // 1??? Obj ?????? ???
                    case "girl_jpg":
                        Log.d("drawImage ???", "girl ?????????");

                        runOnUiThread(() -> {
                            girlLay.setVisibility(View.VISIBLE);
                            girlLay.requestLayout();
                        });
                        pose.toMatrix(keyMatrix1, 0);
//                        mRenderer.mObj_number1.setModelMatrix(keyMatrix1);
                        Matrix.translateM(keyMatrix1, 0, 0.1f, -0.1f, 0.3f);
                        Matrix.rotateM(keyMatrix1,0,180,1,0,0);
                        Matrix.scaleM(keyMatrix1, 0, 0.3f, 0.3f, 0.3f);

                        break;

                        // 9??? Obj ?????? ???
                    case "ghost_jpg" :
                        runOnUiThread(() -> {
                            skull_text.setVisibility(View.VISIBLE);

                            if (eleven==true) {
                                turn_eleven_lay.setVisibility(View.INVISIBLE);
                                turn_eleven_lay.requestLayout();
                            } else{
                                turn_eleven_lay.setVisibility(View.VISIBLE);
                                turn_eleven_lay.requestLayout();
                            }

                            turnLay.setVisibility(View.VISIBLE);
                            turnLay.requestLayout();

                            // ?????? 9 ???????????? obj
                            if (dd==270 && ddd==90) {

                                runOnUiThread(() -> {

                                    eleven = true;

                                    // eachOther??? true ??? ??????.
                                    eachOther = true;

                                    turn_eleven_lay.setVisibility(View.INVISIBLE);
                                    turn_eleven_lay.requestLayout();

                                    turnLay2.setVisibility(View.VISIBLE);
                                    turnLay2.requestLayout();

                                });

                            }

                            if (dd==270 && ddd==270 && eachOther==true) {
                                runOnUiThread(() -> {

                                    turn_eleven_lay.setVisibility(View.INVISIBLE);
                                    turn_eleven_lay.requestLayout();

                                    turnLay.setVisibility(View.INVISIBLE);
                                    turnLay.requestLayout();

                                    turnLay2.setVisibility(View.INVISIBLE);
                                    turnLay2.requestLayout();

                                    skull_text.setVisibility(View.INVISIBLE);

                                    pose.toMatrix(nineMatrix, 0);

                                    // nine??? ????????? ??????????????? ???????????? ?????? ?????????
                                    Matrix.translateM(nineMatrix, 0, 0.5f,0, 0);
                                    Matrix.scaleM(nineMatrix, 0, 0.1f, 0.1f, 0.1f);
                                    Matrix.rotateM(nineMatrix, 0, 270, 0.1f, 0f, 0f);

                                    // nine??? ????????? ??????????????? ???????????? ?????? ?????????
                                    Matrix.rotateM(nineMatrix, 0, 290, 0, 0.1f, 0f);

                                    mRenderer.mObj_number9.setModelMatrix(nineMatrix);

                                });
                            }

//                            else {
//                                turnLay.setVisibility(View.VISIBLE);
//                                turnLay.requestLayout();

//                            }

                        });


//                        andyMatrix = new float[16];
//                        andyMatrix2 = new float[16];
                        pose.toMatrix(skullMatrix1,0);
                        pose.toMatrix(skullMatrix2,0);

                        Matrix.scaleM(skullMatrix1, 0 ,0.0005f, 0.0005f, 0.0005f);
                        Matrix.rotateM(skullMatrix1, 0, 90, 1f, 0f, 0f);


                        Matrix.translateM(skullMatrix1, 0 , -150f, 0f, 0);
                        Matrix.rotateM(skullMatrix1, 0, dd, 0f, 0.1f, 0f);
                        Matrix.rotateM(skullMatrix1, 0, dd2, 0.1f, 0f, 0f);
//                        Matrix.rotateM(andyMatrix, 0, dd2, 0.1f, 0f, 0f);
                        mRenderer.mObj_skull_1.setModelMatrix(skullMatrix1);

                        turn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                stone.start();
                                dd += 90;
                                Log.d("dd??? ?????? ???" , dd+"");
//                                Matrix.rotateM(andyMatrix, 0, dd, 0f, 0.1f, 0f);
                                mRenderer.mObj_skull_1.setModelMatrix(skullMatrix1);
                            }
                        });

                        Matrix.scaleM(skullMatrix2, 0 ,0.0005f, 0.0005f, 0.0005f);
                        Matrix.rotateM(skullMatrix2, 0, 90, 1f, 0f, 0f);

                        Matrix.translateM(skullMatrix2, 0 , 150f, 0f, 0);
                        Matrix.rotateM(skullMatrix2, 0, ddd, 0f, 0.1f, 0f);
                        Matrix.rotateM(skullMatrix2, 0, dd2, 0.1f, 0f, 0f);

                        turn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                stone.start();
                                ddd +=90;
                                Log.d("ddd??? ?????? ???" , ddd+"");
                                mRenderer.mObj_skull_2.setModelMatrix(skullMatrix2);
                            }
                        });

                        mRenderer.mObj_skull_2.setModelMatrix(skullMatrix2);

                        if (dd==360) {
                            dd=0;
                        }

                        if (ddd==360) {
                            ddd=0;
                        }

//                        if (dd==270 && ddd==90) {
//                            runOnUiThread(() -> {
//                                turnLay.setVisibility(View.INVISIBLE);
//                                turnLay.requestLayout();
//                            });
//
//                        }

//                        int result = dd-ddd;
//                        if (dd-ddd)


                        break;

                    // ?????? ?????? ??????
                    case "final_escape":
                        imgName = null;
                        Log.d("drawImages1 ???", "?????? ?????? ????????? ??????");

                        runOnUiThread(() -> {
                            passInviLay.setVisibility(View.VISIBLE);

                            AllItemLay.setVisibility(View.INVISIBLE);
                            AllItemLay.requestLayout();

                            passInviLay.requestLayout();
                        });

                        break;

                    case "surprise1":
                        imgName = null;
                        Log.d("drawImages1 ???" , "?????? ????????? ????????????");

                        runOnUiThread(() -> {

                        });

                        break;

                    case "surprise2":
                        imgName = null;
                        Log.d("drawImages ??? " , "?????? ????????? ??????2???");

                        runOnUiThread(() -> {

                        });
                        break;

                    // ??????_1 ???????????? ????????? ???
                    // ?????? ??? ?????? ?????????. ????????? ????????? ??? ?????? ??????.
                    case "alone_girl_jpg":
                        imgName = null;


                        if(!girl_chk) {
                            Log.d("????????? ?????? ??????", "????????? ????????? ???????????????.");
                            girl_chk = true;

                            // pose (????????? ??????) obj ?????????
                            pose.toMatrix(girlZombieObjMatrix, 0);

                            // zombie setup
                            Matrix.rotateM(girlZombieObjMatrix, 0, 270f, 1f, 0f, 0f);
                            Matrix.scaleM(girlZombieObjMatrix, 0, 0.05f, 0.05f, 0.05f);
                            Matrix.translateM(girlZombieObjMatrix, 0, 0, -0.1f, 0);
                            mRenderer.mObj_girl_zombi.setModelMatrix(girlZombieObjMatrix);
                            new girlRun().start();
                        }
                        break;
                }

            }
        }
    }


    // ???????????? ??????
    class girlBye extends Thread {
        @Override
        public void run() {
            if (!girlback) {
                girlback = true;
                float [] girlM = girlZombieObjMatrix.clone();

                for (int i = 0; i < 100; i++) {
                    Matrix.translateM(girlZombieObjMatrix, 0, 0f, 0f, -0.2f);
                    mRenderer.mObj_girl_zombi.setModelMatrix(girlZombieObjMatrix);

                    try {
                        sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mRenderer.mObj_girl_zombi = null;
                girlZombieObjMatrix = girlM;
            }
        }
    }

    // ???????????? ?????? ???????????????
    // ???????????? ????????? ?????? ?????????
    // ???????????? girlzombie ??????
    class girlRun extends Thread {
        @Override
        public void run() {
            if (!girlshow) {
                girlshow = true;
                float [] girlM = girlZombieObjMatrix.clone();

                for (int i = 0; i < 80; i++) {
                    if(girlstop) {
                        break;
                    }
                    Matrix.translateM(girlZombieObjMatrix, 0, 0f, 0f, 0.1f);
                    mRenderer.mObj_girl_zombi.setModelMatrix(girlZombieObjMatrix);

                    try {
                        sleep(80);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // mRenderer.mObj_girl_zombi = null;
                // girlZombieObjMatrix = girlM;
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mGestureDetector.onTouchEvent(event); //????????? ?????????
//        mScaleGestureDetector.onTouchEvent(event);

        return true;
    }

    void catchCheck(){

        float [][]  cubePP = mRenderer.mObj_monster.getPoint();
        float [][]  objPP = mRenderer.mObj_knife.getPoint();

        Log.d("catchCheck ???", "cubepp : " + cubePP[0][2]);
//        float [][]  objPP2 = mRenderer.mObj_ex.getPoint();


        if(cubePP[0][2]<=objPP[0][2] && cubePP[1][2]>=objPP[1][2] &&
                cubePP[0][0]<=objPP[0][0] && cubePP[1][0]>=objPP[1][0]

        ){

            Log.d("catchCheck ???", "????????????");
        }

    }

    // 0609??????
    void brush_catchCheck(){

        float [][]  aphro1PP = mRenderer.aphro1.getPoint();
        float [][]  objPP = mRenderer.mObj_brush.getPoint();

        Log.d("catchCheck ???", "?????? ?????????????");
        //Log.d("cubePP ???", Arrays.toString(cubePP[0]) + "");
        //Log.d("objPP ???", Arrays.toString(objPP[0]) + "");

        if(aphro1PP[0][2]<=objPP[0][2]
//                && aphro1PP[1][2]>=objPP[1][2]
//                && aphro1PP[0][0]<=objPP[0][0] && aphro1PP[1][0]>=objPP[1][0]
        ){

            Log.d("???????????????? ???", "????????? ?????????");

//            Matrix.scaleM(aphro1Matrix,0, 0, 0, 0);
//            mRenderer.aphro1.setModelMatrix(aphro1Matrix);

//            Matrix.translateM(modelMatrix2, 0, 0f, 0, -3.0f);
//            Matrix.scaleM(modelMatrix2,0, 10f, 10f, 10f);
//            mRenderer.mObj_ex.setModelMatrix(modelMatrix2);

//            mRenderer.aphro1.setColorCorrection(colorBlack);
//            mRenderer.aphro1.setModelMatrix(aphro1Matrix);

            if (cnt==1) {
                mRenderer.aphro1.setColorCorrection(colorBlue);


            }

            if (cnt==2) {
                mRenderer.aphro1.setColorCorrection(colorRed);

            }

            if (cnt==3) {
                mRenderer.aphro1.setColorCorrection(colorRed);
                mRenderer.mObj_number2.setModelMatrix(keyMatrix2);

                runOnUiThread(() -> {
                    aproLay.removeAllViews();
                    aproLay.requestLayout();
                    gifLay.setVisibility(View.VISIBLE);    //?????? gif
                });
                try {
                    sleep(2000);
                    gifLay.setVisibility(View.INVISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();   //?????? gif
                }

            }

            if (cnt==4) {
                mRenderer.aphro1.setColorCorrection(colorBlack);

            }


//            if(change) {
//                mRenderer.mCube_ex.setModelMatrix(cubeMatrix);
//            }
//            // ??????0609 ??????

            Log.d("catchCheck ???", "????????????");
        }


//        if (objPP2[0][2]<=objPP[0][2] && objPP2[1][2]>=objPP[1][2] &&
//                objPP2[0][0]<=objPP[0][0] && objPP2[1][0]>=objPP[1][0]) {
//            float [] colorArr = {1f,0.5f,0.5f,0.6f};
//            mRenderer.mObj_ex.setColorCorrection(colorArr);
//            mRenderer.mObj_ex.setModelMatrix(modelMatrix2);
//            cnt+=1;
//
//            Log.d("?????? ????????? ??????", cnt+"");
////            if (cnt>3) {
////                Matrix.scaleM(modelMatrix2,0, 0, 0, 0);
////                mRenderer.mObj_ex.setModelMatrix(modelMatrix2);
////            }
//
//        }


//        Log.d("?????? ????????? ??????", cnt+"");

    }
    public float[] calculateInitialBallPoint(int width, int height,
                                             float[] projMat, float[] viewMat) {
        return getScreenPoint(width / 2, height , width, height, projMat, viewMat);
    }

    //?????????
    public float[] getScreenPoint(float x, float y, float w, float h,
                                  float[] projMat, float[] viewMat) {
        float[] position = new float[3];
        float[] direction = new float[3];

        x = x * 2 / w - 1.0f;
        y = (h - y) * 2 / h - 1.0f;

        float[] viewProjMat = new float[16];
        Matrix.multiplyMM(viewProjMat, 0, projMat, 0, viewMat, 0);

        float[] invertedMat = new float[16];
        Matrix.setIdentityM(invertedMat, 0);
        Matrix.invertM(invertedMat, 0, viewProjMat, 0);

        float[] farScreenPoint = new float[]{x, y, 1.0F, 1.0F};
        float[] nearScreenPoint = new float[]{x, y, -1.0F, 1.0F};
        float[] nearPlanePoint = new float[4];
        float[] farPlanePoint = new float[4];

        Matrix.multiplyMV(nearPlanePoint, 0, invertedMat, 0, nearScreenPoint, 0);
        Matrix.multiplyMV(farPlanePoint, 0, invertedMat, 0, farScreenPoint, 0);

        position[0] = nearPlanePoint[0] / nearPlanePoint[3];
        position[1] = nearPlanePoint[1] / nearPlanePoint[3];
        position[2] = nearPlanePoint[2] / nearPlanePoint[3];

        direction[0] = farPlanePoint[0] / farPlanePoint[3] - position[0];
        direction[1] = farPlanePoint[1] / farPlanePoint[3] - position[1];
        direction[2] = farPlanePoint[2] / farPlanePoint[3] - position[2];

        normalize(direction);

        position[0] += (direction[0] * 0.1f);
        position[1] += (direction[1] * 0.1f);
        position[2] += (direction[2] * 0.1f);

        return position;
    }
    private void normalize(float[] v) {
        double norm = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        v[0] /= norm;
        v[1] /= norm;
        v[2] /= norm;
    }

    // ???????????? ?????? ?????????
    int leftcnt, upcnt, downcnt, rightcnt = 0;

    public void arrowBtnClk(View view) {

        Log.d("???????????? ?????? ??????", " ????????? ??? ?????????");

        switch (view.getId()){
            case R.id.leftABtn:
                du.start();
                leftcnt += 1;
                if(leftcnt >= 10){
                    leftcnt = 0;
                }
                leftTxt.setText(Integer.toString(leftcnt));

                break;

            case R.id.upABtn:
                re.start();
                upcnt += 1;
                if (upcnt >= 10) {
                    upcnt = 0;
                }
                upTxt.setText(Integer.toString(upcnt));

                break;

            case R.id.downABtn:
                mi.start();
                downcnt += 1;
                if (downcnt >= 10) {
                    downcnt = 0;
                }
                downTxt.setText(Integer.toString(downcnt));

                break;

            case R.id.rightABtn:
                pa.start();
                rightcnt += 1;
                if (rightcnt >= 10) {
                    rightcnt = 0;
                }
                rightTxt.setText(Integer.toString(rightcnt));

                break;

        }

        // 6644 ??????
        if (leftTxt.getText().equals("6") && upTxt.getText().equals("6") && downTxt.getText().equals("4") && rightTxt.getText().equals("4")){

            // obj ??????

//            Matrix.translateM(keyMatrix1, 0, 0.1f, 0.1f, -2f);
//            Matrix.rotateM(keyMatrix1,0,90,1,0,0);
//            Matrix.scaleM(keyMatrix1, 0, 1f, 1f, 1f);
            mRenderer.mObj_number1.setModelMatrix(keyMatrix1);

            Log.d("obj ???????????? ???", "????????? ?????? ???????????????");

            runOnUiThread(() -> {
                girlLay.removeAllViews();
            });

        }
    }

}