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

    // Config imgConfig; // 초점

    GLSurfaceView mySurfaceView;

    MainRenderer mRenderer;

    //이동 회전-> 한 손가락 이벤트
    GestureDetector mGestureDetector;

    //크기조절 -> 두 손가락 이벤트트
//    ScaleGestureDetector mScaleGestureDetector;

    // 텍스트뷰
    TextView countDown;

    // 타이머 시간 설정
    String timer = "002000";

    // 레이아웃들
    ConstraintLayout passInviLay, itemlistLay, AllItemLay, girlLay,
            useWeaponLay, turnLay, turnLay2, turn_eleven_lay, aproLay, gifLay;


    TextView skull_text;

    // password, 상하좌우 텍스트 카운트
    TextView passTxt, leftTxt, upTxt, downTxt, rightTxt;

    // 반투명 이미지
    ImageView bantoImg, bantoFlashImg;


    // 카운트 다운
    CountDownTimer countdownTimer;

    // 버튼 선언
    Button cancelbtn, turn1, turn2;

    // item Image 선언
    ImageView flash_img_btn, cross_img_btn, knife_img_btn, heart_img_btn, bandage_img_btn, item_img_btn;

    // 화면 밝기 설정
    WindowManager.LayoutParams wl;

    AlphaAnimation animation;

    // 아이템
    boolean flashlight, cross, show = false, showChk = false, modelInit = false, moving = false;

    boolean brush_moving = false, brush_modelInit = false ;

    // 나이프, 붕대, 공
    boolean try_knife = false, try_bandage = false, try_heart =false;

    // zombie 좀비
    boolean girl_chk = false, girlshow = false, girlstop = false, girlback = false;

    Display display;

    float[] showObjMatrix = new float[16];

    // 디스플레이 가운데 좌표
    float dx, dy;

    // 몬스터 퇴치 매트릭스
    float [] knifeMatrix = new float[16];
    float [] monsterMatrix = new float[16];
    float [] bandageMatrix = new float[16];
    float [] heartMatrix = new float[16];

    // 탈출 번호
    // 5 matrix
    float [] keyMatrix5 = new float[16];

    // 9 matrix
    float [] keyMatrix9 = new float[16];

    // 1 matrix
    float [] keyMatrix1 = new float[16];

    // 2 matrix
    float [] keyMatrix2 = new float[16];

    // 탈출 번호 끝


    // girl 좀비
    float [] girlZombieObjMatrix = new float[16];

    /// 붓 던지기 위해 만든 것들
    float [] brushMatrix = new float[16];
    float [] aphro1Matrix = new float[16];


    // 색상표
    float [] colorRed = {1f,0f,0f,0.6f};
    float [] colorYellow = {1f,1f,0f,0.6f};
    float [] colorGreen = {0f,1f,0f,0.6f};
    float [] colorBlue = {0f,0f,1f,0.6f};
    float [] colorBlack = {0f,0f,0f,0.6f};

    // 색상들 변하게 만들기 위한 것
    int cnt = 1;
    /// 여기까지

    // 해골에 씀
    // 서로 마주볼 때 true로 변경
    boolean eachOther = false, eleven = false;
    int dd = 0, ddd=0, dd2 = 180;
    float [] skullMatrix1 = new float[16];
    float [] skullMatrix2 = new float[16];
    float [] nineMatrix = new float[16];


    float displayX, displayY, mRotate = 0f, brush_mRotate = 0f, mScale = 1f, brush_mScale = 0.0003f;

    float [] viewMatrix = new float[16];
    float [] projMatrix = new float[16];

    // 효과음, 브금
    MediaPlayer rainBgm, crazy_laugh_short, doong_MP3, du, re, mi ,pa ,stone;

    ImageView  gif_1;



    // Oncreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hidStatusBarTitleBar();
        setContentView(R.layout.activity_main);

        //아프로디테 gif
        gifLay = (ConstraintLayout) findViewById(R.id.gifLay);
        gifLay.setVisibility(View.INVISIBLE);
        gif_1 = (ImageView)findViewById(R.id.gif_1);
        Glide.with(this).load(R.raw.gho).into(gif_1);

        // 화살표 버튼 브금
        du = MediaPlayer.create(this, R.raw.du);// 화살표 쿨릭
        re = MediaPlayer.create(this, R.raw.re);
        mi = MediaPlayer.create(this, R.raw.mi);
        pa = MediaPlayer.create(this, R.raw.pa);
        //석상브금
        stone = MediaPlayer.create(this, R.raw.open_rock_door);

        // 효과음, 브금
        rainBgm = MediaPlayer.create(this, R.raw.rain);
        rainBgm.start();
        rainBgm.setLooping(true);

        crazy_laugh_short = MediaPlayer.create(this, R.raw.crazy_laugh_short);

        doong_MP3 = MediaPlayer.create(this, R.raw.dieending);


        // 반투명 이미지
        bantoImg = (ImageView) findViewById(R.id.bantoImg);
        bantoFlashImg = (ImageView) findViewById(R.id.bantoFlashImg);
        bantoFlashImg.setVisibility(View.INVISIBLE);

        // 해골 시작
        turn1 = (Button)findViewById(R.id.turn1);
        turn2 = (Button)findViewById(R.id.turn2);

        skull_text = (TextView)findViewById(R.id.skull_text);

        skull_text.setVisibility(View.INVISIBLE);

        turnLay = (ConstraintLayout) findViewById(R.id.turnLay);
        turnLay2 = (ConstraintLayout) findViewById(R.id.turnLay2);

        turnLay.setVisibility(View.INVISIBLE);
        turnLay2.setVisibility(View.INVISIBLE);

        // 11시 나오게 하는 text
        turn_eleven_lay = (ConstraintLayout) findViewById(R.id.turn_eleven_lay);
        turn_eleven_lay.setVisibility(View.INVISIBLE);
        // 해골 끝



        display = getWindowManager().getDefaultDisplay();
        // 밝기
        wl = getWindow().getAttributes(); //밝기조절
        wl.screenBrightness = 0.5f;
        getWindow().setAttributes(wl);

        // 화면 좌표
        Point point = new Point();
        display.getRealSize(point);
        dx = point.x / 2;
        dy = point.y / 2;

        // id 설정들
        mySurfaceView = (GLSurfaceView)findViewById(R.id.glSsurfaceview);

        countDown = (TextView) findViewById(R.id.countDown);

        // 비밀번호 누르고 탈출
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

        // 아이템 image
        flash_img_btn = (ImageView) findViewById(R.id.flash_img_btn);
        cross_img_btn = (ImageView) findViewById(R.id.cross_img_btn);
        knife_img_btn = (ImageView) findViewById(R.id.knife_img_btn);
        bandage_img_btn = (ImageView) findViewById(R.id.bandage_img_btn);
        heart_img_btn = (ImageView) findViewById(R.id.heart_img_btn);


        itemlistLay.setVisibility(View.INVISIBLE);

        // 악마 죽이기 레이아웃
        useWeaponLay = (ConstraintLayout) findViewById(R.id.useWeaponLay);
        useWeaponLay.setVisibility(View.INVISIBLE);

        //아프로디테 문제 레이아웃
        aproLay = (ConstraintLayout) findViewById(R.id.aproLay);
        aproLay.setVisibility(View.INVISIBLE);

        // 종현 Thread 추가
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

            //            //이동
//            @Override
            public boolean onDoubleTap(MotionEvent event) {
                // draw 되면
//                mTouched = true;
//                modelInit = false;
                displayX= event.getX();
                displayY = event.getY();
                Log.d("onDoubleTap 여",displayX+","+displayY);
                return true;
            }

            // rabbit에 쓰이는 쓰래드
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

            // 붓 던지기
            class brushGo extends Thread{
                @Override
                public void run() {

                    if(!brush_moving) {

                        // 색상표는 1~5(빨강~검정)까지 밖에 없기 때문에 6이 되면 1(빨강)로 바꿔줌
                        if (cnt>4) {
                            cnt = 1;
                        }

                        brush_moving = true;
                        float[] bufMatirx = brushMatrix.clone();

                        for (int i = 0; i < 150; i++) {
                            Log.d("Thorw 여", "됩니다!!!!!");

                            Matrix.translateM(brushMatrix, 0, 0, 300, 0);
                            try {
                                sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        brushMatrix = bufMatirx;
                        brush_moving = false;

                        // cnt = 1 : 빨강 , cnt = 2 : 노랑 , cnt = 3 : 녹색 , cnt = 4 : 파랑 , cnt = 5 : 검정
                        cnt ++;
                        Log.d("cnt입니다 여" , cnt+"");

                    }
                }
            }

            //회전
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d("onScroll 여", "Scroll은 인식합니다.");
                if(modelInit && !moving) {
                    if(distanceY >-50 || distanceY <50) {
                        Log.d("onScroll 여", "여기가 인식 할까요????????????");
                        mRotate += 000.1;
//                        mRotate += -distanceX / 5;
                        Matrix.rotateM(knifeMatrix, 0, -distanceX / 20, 0f, 100f, 0f);
                    } else {
                        new MyGo().start();
                    }

                }

                // 붓 던지기
                if(brush_modelInit && !brush_moving) {

                    if(distanceY >-50 && distanceY <50) {
                        brush_mRotate += -distanceX / 5;
                        Matrix.rotateM(brushMatrix, 0, -distanceX / 5, 0f, 100f, 0f);
                    }else{
                        new brushGo().start();
                    }
                }
//                 Log.d("onScroll 여", distanceX + "," + distanceY);
                return true;
            }
        });



        // 아이템
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

        // 손전등
        flash_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashlight = !flashlight;
                mRenderer.makeFlash(flashlight);

                // 손전등 눌렀을 때랑 안눌렀을때 다시~
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

        // 십자가
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
//                        public void run() {   // 시간 지난 후 실행할 코딩
//                            wl.screenBrightness = 0.3f;
//                            getWindow().setAttributes(wl);
//                            mRenderer.makeCross(false);
//                        }
//                    }, 3000); //3초후 실행된다
//
//                }

            }
        });

        // 나이프
        knife_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("칼칼칼 여: ", try_knife+"");
                try_knife = true;
                Log.d("칼칼칼 여: ", try_knife+"");
                Log.d("칼칼칼 여: ", "칼칼칼입니다");

                try_bandage = false;
                try_heart = false;
            }
        });

        // 붕대
        bandage_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("붕대붕대 여: ", try_bandage+"");
                try_bandage = true;
                Log.d("붕대붕대 여: ", try_bandage+"");
                Log.d("붕대붕대 여: ", "붕대붕대입니다");

                try_knife = false;
                try_heart = false;

            }
        });

        // 심장
        heart_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("공공공 여: ", try_heart+"");
                try_heart = true;
                Log.d("공공공 여: ", try_heart+"");
                Log.d("공공공 여: ", "공공공입니다");

                try_knife = false;
                try_bandage = false;

            }
        });

        // 카메라 켜짐과 동시에 타이머 시작. 메소드 호출
        countDown(timer);

        // 평면 인식하고 이동시키는 스레드클래스
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

        //화면 변화 감지 --> 회전 등등
        DisplayManager displayManager = (DisplayManager)getSystemService(DISPLAY_SERVICE);

        if(displayManager != null){

            //화면 리스너 실행
            displayManager.registerDisplayListener(
                    new DisplayManager.DisplayListener() {
                        @Override
                        public void onDisplayAdded(int displayId) {

                        }

                        @Override
                        public void onDisplayRemoved(int displayId) {

                        }

                        //화면이 변경되었다면
                        @Override
                        public void onDisplayChanged(int displayId) {
                            //동기화 --> 변환시 한번되고 난뒤에 작업
                            synchronized (this){
                                //화면 변화를 알려준다
                                mRenderer.onDisplayChanged();
                            }

                        }
                    }, null);
        }



        MainRenderer.RenderCallBack mr = new MainRenderer.RenderCallBack() {

            //MainRenderer의 onDrawFrame() -- 그리기 할때 마다 호출
            //MainActivity에서 카메라 화면 정보를 얻기 위해서 이다.
            @Override
            public void preRender() {

                if(mRenderer.viewportChange){
                    display = getWindowManager().getDefaultDisplay();

                    mRenderer.updateSession(mSession, display.getRotation());
                }


                //session의 카메라 텍스처 이름을  mainRenderer의 카메라의 텍스처 번호로 지정
                // session 카메라 : 입력정보  --> mainRenderer의 카메라 :  화면에 뿌리는 출력정보
                mSession.setCameraTextureName(mRenderer.getTextureID());


                Frame frame = null;

                try {
                    frame = mSession.update(); //카메라의 화면을 업데이트한다.
                } catch (CameraNotAvailableException e) {
                    e.printStackTrace();
                }
//                imgConfig = new Config(mSession);
//                imgConfig.setFocusMode(Config.FocusMode.AUTO); // 초점
//                mSession.configure(imgConfig);
                mRenderer.mCamera.transformDisplayGeometry(frame);

                // 평면인식하고 obj 한번만 실행시키기 위해
                // if(mRenderer.mObj_1 != null && clear2 && !clear3){
                if (!showChk) {
//                    Log.d("preRender 여", "되냐?");
                    List<HitResult> results = frame.hitTest(dx, dy);
                    for (HitResult hr : results) {
                        Pose pose = hr.getHitPose();
                        Trackable trackable = hr.getTrackable();

                        // 클릭 좌표 추적이 Plane 이고 Plane 의 도형안에 있는가
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
//                                myTextView.setText("평면 찾음요");
//                            }
//                        });
//
//                    }else{
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                myTextView.setText("아직도요");
//                            }
//                        });
//                    }

                    if(brush_moving){
                        Log.d("터치를 여", "인식합니다!!!!");
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
                //Log.d("MainActivity 여","preRender() 여");


            }
        };

        mRenderer = new MainRenderer(mr, this);

        //pause 시 관련 데이터 사라지지 않게 한다.
        mySurfaceView.setPreserveEGLContextOnPause(true);
        mySurfaceView.setEGLContextClientVersion(3); //버전 3.0 사용

        //렌더링 지정
        mySurfaceView.setRenderer(mRenderer);
        //렌더링 계속 호출
        mySurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //퍼미션 요청
        requestPermission();

        //ARCore session 유무 --> 없으면 생성
        if(mSession == null){

            try {

                Log.d("세션 돼냐?",
                        ArCoreApk.getInstance().requestInstall(this, true)+"");

                switch (ArCoreApk.getInstance().requestInstall(this, true)){
                    case INSTALLED:
                        //ARCore  정상 설치후 세션 생성
                        mSession = new Session(this);

                        //ARCore 환경설정용 Config
                        Config config = new Config(mSession);
                        // imgConfig = new Config(mSession); // 초점
                        //평면 배치 인식
                        config.setInstantPlacementMode(Config.InstantPlacementMode.LOCAL_Y_UP);

                        config.setFocusMode(Config.FocusMode.AUTO);

                        //지리정보 데이터 사용 가능상태로 변경
                        // imgConfig.setGeospatialMode(Config.GeospatialMode.ENABLED);
                        mSession.configure(config);

                        Log.d("세션 생성?","생성됐으요");
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

        builder.setMessage("게임을 종료하시겠습니까?");

        builder.setPositiveButton("예" ,new DialogInterface.OnClickListener() {
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

        builder.setNegativeButton("아니오" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    // 타이머 메소드
    public void countDown(String time) {
        long timer = 0;

        // 1000 단위가 1초
        // 60000 단위가 1분
        // 60000 * 3600 = 1시간

        String getHour = time.substring(0, 2);
        String getMin = time.substring(2, 4);
        String getSecond = time.substring(4, 6);

        // "00"이 아니고, 첫번째 자리가 0 이면 제거
        if (getHour.substring(0, 1) == "0") {
            getHour = getHour.substring(1, 2);
        }

        if (getMin.substring(0, 1) == "0") {
            getMin = getMin.substring(1, 2);
        }

        if (getSecond.substring(0, 1) == "0") {
            getSecond = getSecond.substring(1, 2);
        }

        // 변환시간
        timer = Long.valueOf(getHour) * 1000 * 3600 + Long.valueOf(getMin) * 60 * 1000 + Long.valueOf(getSecond) * 1000;

        // 첫번쨰 인자 : 원하는 시간 (예를들어 30초면 30 x 1000(주기))
        // 두번쨰 인자 : 주기( 1000 = 1초)

        countdownTimer = new CountDownTimer(timer, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {
                // 시간단위
                String hour = String.valueOf(millisUntilFinished / (60 * 60 * 1000));

                // 분단위
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000)) ;
                String min = String.valueOf(getMin / (60 * 1000)); // 몫

                // 초단위
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지

                // 밀리세컨드 단위
                String millis = String.valueOf((getMin % (60 * 1000)) % 1000); // 몫

                // 시간이 한자리면 0을 붙인다
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }

                // 분이 한자리면 0을 붙인다
                if (min.length() == 1) {
                    min = "0" + min;
                }

                // 초가 한자리면 0을 붙인다
                if (second.length() == 1) {
                    second = "0" + second;
                }

                countDown.setText(min + ":" + second);
            }

            @Override
            public void onFinish() {
                // 타이머 끝 . 화면 전환
                Intent dieScreen = new Intent(getApplicationContext(), DieScreen.class);
                rainBgm.stop(); // 타이머 끝나면 사망. 메인 브금 stop
                startActivity(dieScreen);
//                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }.start();
    }

    // 마지막 탈출 비밀번호 조합 버튼 설정
    String passSave = "";

    // 탈출 비밀번호 버튼
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
                // 새로운 액티비티 추가 == 엔딩
                passTxt.setText(passSave);
                if (passTxt.getText().toString().equals("5912")) {
                    Intent ending = new Intent(getApplicationContext(), EndingScreen.class);
                    startActivity(ending);
                    rainBgm.stop(); // ending 갈 때 메인 브금 stop
                    countdownTimer.cancel();
                } else {
                    YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(findViewById(R.id.passTxt));
                    Toast.makeText(getApplicationContext(), "번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancelbtn:

                animation = new AlphaAnimation(0.0f,1.0f);
                animation.setDuration(100);
                animation.setStartOffset(1);
//                animation.setRepeatMode(Animation.REVERSE);
                animation.setRepeatCount(Animation.REVERSE);

                cancelbtn.startAnimation(animation);

//                Toast.makeText(getApplicationContext(), "번호를 적어야만 한다." , Toast.LENGTH_SHORT).show();

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

    //이미지DB 설정
    void setImgDB(Config mConfig){
        //이미지데이터베이스 생성
        AugmentedImageDatabase imgDB = new AugmentedImageDatabase(mSession);

        try {
            // 마지막 탈출
            InputStream is = getAssets().open("starry_night.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("final_escape", bitmap);
            is.close();

            // 천사이미지
            is = getAssets().open("angel.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("angel_jpg", bitmap);
            is.close();

            // 붓 던지기에 사용되는 그림
            is = getAssets().open("shako.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("shako_jpg", bitmap);
            is.close();

            // 여자애 인식
            is = getAssets().open("girl.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("girl_jpg", bitmap);
            is.close();

            // 모아이 석상
            is = getAssets().open("ghost.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("ghost_jpg", bitmap);
            is.close();

            // 깜놀 1
            is = getAssets().open("killedhim.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("surprise1", bitmap);
            is.close();

            // 깜놀 2
            is = getAssets().open("mirror.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("surprise2", bitmap);
            is.close();

            // 귀신_1(십자가로 죽이는 거)을 띄우는 이미지
            is = getAssets().open("alone_girl.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("alone_girl_jpg", bitmap);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //이미지 데이터베이스 설정
        mConfig.setAugmentedImageDatabase(imgDB);
    }


    //이미지 추적하여 객체 위치 설정정
    void drawImages(Frame frame){

        Collection<AugmentedImage> agImgs = frame.getUpdatedTrackables(AugmentedImage.class);

        for (AugmentedImage img: agImgs){

            if(img.getTrackingState()== TrackingState.TRACKING) {

                Pose pose = img.getCenterPose();
                String imgName = img.getName();
                switch (imgName){

                    // 무기 던져서 퇴치
                        // 5 번 Obj 띄울 곳
                    case "angel_jpg":
                        imgName = null;
//                        Log.d("angelImg 여", "천사 이미지 인식");
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
                            // 과제0609 -> 이거 안써서 안나왔음
                            pose.toMatrix(monsterMatrix,0);
                            // 과제 0609 changeMatrix 추가
//                            pose.toMatrix(changeMatrix,0);

                            mRenderer.mObj_monster.setModelMatrix(monsterMatrix);


//                            mRenderer.mObj_ex.setModelMatrix(modelMatrix2);
                            catchCheck();
                            Log.d("catchCheck 여", "인식하긴 한다" );

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

                            // 붕대의 scale을 0으로 없애버림
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

                            // 나이프의 scale을 0으로 없애버림
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

                            // 나이프의 scale을 0으로 없애버림
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


                        Log.d("Rotate 나 ", mRotate+"");
                        if (mRotate>1.5){
                            Log.d("Rotate 나 ", mRotate+"");
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

                    // 2번 넘버 obj 띄울곳
                    case "shako_jpg":
                        Log.d("image 인식", "샤코 이미지 인식");
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

                            // 1 아니면 0인데 0 보다 크면 1 그게 아니라면 -1
                            float zz = ((float)Math.random()*4+1) * ((int)(Math.random()*2) >0 ?1 : -1);
                            float xx = ((float)Math.random()*6-3);

                            // 정면에 존재
                            zz = -4f;
                            xx = 0f;

//                                Matrix.scaleM(modelMatrix, 0, 0.0003f, 0.0003f, 0.0003f);

                            Matrix.translateM(aphro1Matrix, 0, xx, 0, zz);

                            // 아프로디테 석상 위치
                            Matrix.rotateM(aphro1Matrix, 0, 270, 1, 0, 0);
                            Matrix.translateM(aphro1Matrix,0,0,0,-0.5f);
                            Matrix.scaleM(aphro1Matrix, 0, 0.008f, 0.008f, 0.008f);

                            mRenderer.aphro1.setColorCorrection(colorBlack);
                            mRenderer.aphro1.setModelMatrix(aphro1Matrix);

                            //붓 부러쉬 위치
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

                        // 1번 Obj 띄울 곳
                    case "girl_jpg":
                        Log.d("drawImage 여", "girl 이미지");

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

                        // 9번 Obj 띄울 곳
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

                            // 숫자 9 띄워주는 obj
                            if (dd==270 && ddd==90) {

                                runOnUiThread(() -> {

                                    eleven = true;

                                    // eachOther가 true 가 된다.
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

                                    // nine의 방향을 오른쪽으로 변경하기 위해 한거임
                                    Matrix.translateM(nineMatrix, 0, 0.5f,0, 0);
                                    Matrix.scaleM(nineMatrix, 0, 0.1f, 0.1f, 0.1f);
                                    Matrix.rotateM(nineMatrix, 0, 270, 0.1f, 0f, 0f);

                                    // nine의 방향을 오른쪽으로 변경하기 위해 한거임
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
                                Log.d("dd의 각도 여" , dd+"");
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
                                Log.d("ddd의 각도 여" , ddd+"");
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

                    // 탈출 번호 조합
                    case "final_escape":
                        imgName = null;
                        Log.d("drawImages1 여", "탈출 비번 이미지 인식");

                        runOnUiThread(() -> {
                            passInviLay.setVisibility(View.VISIBLE);

                            AllItemLay.setVisibility(View.INVISIBLE);
                            AllItemLay.requestLayout();

                            passInviLay.requestLayout();
                        });

                        break;

                    case "surprise1":
                        imgName = null;
                        Log.d("drawImages1 여" , "깜짝 놀라는 사진이야");

                        runOnUiThread(() -> {

                        });

                        break;

                    case "surprise2":
                        imgName = null;
                        Log.d("drawImages 여 " , "깜짝 놀라는 사진2야");

                        runOnUiThread(() -> {

                        });
                        break;

                    // 귀신_1 십자가로 죽이는 거
                    // 인식 후 귀신 다가옴. 십자가 들었을 시 귀신 퇴치.
                    case "alone_girl_jpg":
                        imgName = null;


                        if(!girl_chk) {
                            Log.d("외로운 여자 여여", "외로운 여자를 인식합니다.");
                            girl_chk = true;

                            // pose (이미지 위에) obj 올리기
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


    // 멀어지는 좀비
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

    // 다가오는 좀비 천천히오게
    // 어느정도 앞으로 오면 멈추게
    // 다가오는 girlzombie 좀비
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

        mGestureDetector.onTouchEvent(event); //이벤트 위임임
//        mScaleGestureDetector.onTouchEvent(event);

        return true;
    }

    void catchCheck(){

        float [][]  cubePP = mRenderer.mObj_monster.getPoint();
        float [][]  objPP = mRenderer.mObj_knife.getPoint();

        Log.d("catchCheck 여", "cubepp : " + cubePP[0][2]);
//        float [][]  objPP2 = mRenderer.mObj_ex.getPoint();


        if(cubePP[0][2]<=objPP[0][2] && cubePP[1][2]>=objPP[1][2] &&
                cubePP[0][0]<=objPP[0][0] && cubePP[1][0]>=objPP[1][0]

        ){

            Log.d("catchCheck 여", "부딪쳤다");
        }

    }

    // 0609추가
    void brush_catchCheck(){

        float [][]  aphro1PP = mRenderer.aphro1.getPoint();
        float [][]  objPP = mRenderer.mObj_brush.getPoint();

        Log.d("catchCheck 여", "돌기 돌아???????");
        //Log.d("cubePP 여", Arrays.toString(cubePP[0]) + "");
        //Log.d("objPP 여", Arrays.toString(objPP[0]) + "");

        if(aphro1PP[0][2]<=objPP[0][2]
//                && aphro1PP[1][2]>=objPP[1][2]
//                && aphro1PP[0][0]<=objPP[0][0] && aphro1PP[1][0]>=objPP[1][0]
        ){

            Log.d("인식할수도? 여", "되긴느 합니다");

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
                    gifLay.setVisibility(View.VISIBLE);    //귀신 gif
                });
                try {
                    sleep(2000);
                    gifLay.setVisibility(View.INVISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();   //귀신 gif
                }

            }

            if (cnt==4) {
                mRenderer.aphro1.setColorCorrection(colorBlack);

            }


//            if(change) {
//                mRenderer.mCube_ex.setModelMatrix(cubeMatrix);
//            }
//            // 과제0609 추가

            Log.d("catchCheck 여", "부딪쳤다");
        }


//        if (objPP2[0][2]<=objPP[0][2] && objPP2[1][2]>=objPP[1][2] &&
//                objPP2[0][0]<=objPP[0][0] && objPP2[1][0]>=objPP[1][0]) {
//            float [] colorArr = {1f,0.5f,0.5f,0.6f};
//            mRenderer.mObj_ex.setColorCorrection(colorArr);
//            mRenderer.mObj_ex.setModelMatrix(modelMatrix2);
//            cnt+=1;
//
//            Log.d("여긴 부딪혀 여여", cnt+"");
////            if (cnt>3) {
////                Matrix.scaleM(modelMatrix2,0, 0, 0, 0);
////                mRenderer.mObj_ex.setModelMatrix(modelMatrix2);
////            }
//
//        }


//        Log.d("여긴 부딪혀 여여", cnt+"");

    }
    public float[] calculateInitialBallPoint(int width, int height,
                                             float[] projMat, float[] viewMat) {
        return getScreenPoint(width / 2, height , width, height, projMat, viewMat);
    }

    //평면화
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

    // 상하좌우 버튼 클릭시
    int leftcnt, upcnt, downcnt, rightcnt = 0;

    public void arrowBtnClk(View view) {

        Log.d("상하좌우 버튼 시작", " 위아래 몇 맞추기");

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

        // 6644 정답
        if (leftTxt.getText().equals("6") && upTxt.getText().equals("6") && downTxt.getText().equals("4") && rightTxt.getText().equals("4")){

            // obj 내놔

//            Matrix.translateM(keyMatrix1, 0, 0.1f, 0.1f, -2f);
//            Matrix.rotateM(keyMatrix1,0,90,1,0,0);
//            Matrix.scaleM(keyMatrix1, 0, 1f, 1f, 1f);
            mRenderer.mObj_number1.setModelMatrix(keyMatrix1);

            Log.d("obj 시발새끼 여", "여기는 된다 시발새끼여");

            runOnUiThread(() -> {
                girlLay.removeAllViews();
            });

        }
    }

}