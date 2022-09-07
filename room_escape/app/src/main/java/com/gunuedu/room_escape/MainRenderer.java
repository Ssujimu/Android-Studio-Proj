
package com.gunuedu.room_escape;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.google.ar.core.Session;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainRenderer implements GLSurfaceView.Renderer {

    float [] mMVPMatrix = new float[16];
    float [] mViewMatrix = new float[16];
    float [] mViewMatrix2 = new float[16];
    float [] mViewMatrix3 = new float[16];
    float [] mProjectionMatrix = new float[16];
    float [] modelMatrix = new float[16];

    interface RenderCallBack{
        void preRender();
    }
    int width, height;
    RenderCallBack myCallBack;
    boolean viewportChange = false;

    CameraPreView mCamera;
    PlaneRenderer mPlane;
    ObjRenderer Obj1, Obj2,  showObj, mObj_monster, mObj_number5, mObj_knife, mObj_brush, aphro1, mObj_number9, mObj_number1, mObj_number2
            , mObj_skull_1, mObj_skull_2, mObj_bandage, mObj_heart, mObj_girl_zombi;


    // 0613 myPlace 그리기
    MainActivity mainActivity;

    MainRenderer(RenderCallBack myCallBack, Context context){
        this.myCallBack = myCallBack;
        mCamera = new CameraPreView();
        mPlane = new PlaneRenderer(000000, 0.1f);
        Obj1 = new ObjRenderer(context, "Flashlight.obj", "FlashlightTexture.png");
        Obj2 = new ObjRenderer(context, "cross.obj", "cross.jpg");
        showObj = new ObjRenderer(context, "ghostt.obj", "ghostt.png");

        // 몬스터에게 칼, 붕대 던지기
        mObj_knife = new ObjRenderer(context, "Knife.obj","Knife_BaseColor.jpg");
        mObj_monster = new ObjRenderer(context, "Corpse.obj", "Corpse_Color.jpg");
        mObj_bandage = new ObjRenderer(context, "bandage.obj", "bandage2.png");
        mObj_heart = new ObjRenderer(context, "heart.obj", "bandage.jpg");

        // 번호
        mObj_number5 = new ObjRenderer(context, "five.obj", "num.png");
        mObj_number9 = new ObjRenderer(context, "nine.obj", "num.png");
        mObj_number1 = new ObjRenderer(context, "one.obj", "num.png");
        mObj_number2 = new ObjRenderer(context, "two.obj", "num.png");

        // 붓 던지기
        mObj_brush = new ObjRenderer(context, "brushh.obj","truebru.jpg");
        aphro1 = new ObjRenderer(context, "aphrodite.obj","aphrodite.jpg");

        // 해골
        mObj_skull_1 = new ObjRenderer(context, "skulll.obj","skulll.jpg");
        mObj_skull_2 = new ObjRenderer(context, "skulll.obj","skulll.jpg");

        // 귀신_1 트릭에 쓸 여자좀비
        mObj_girl_zombi = new ObjRenderer(context, "Zombie.obj", "zombie.png");

        // add 0613
        mainActivity = (MainActivity) context;

        Log.d("MainRenderer 여","생성자여");
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        GLES30.glClearColor(0f,1f,1f,1f);

        mCamera.init();
        Obj1.init();
        Obj2.init();

        // 몬스터에게 칼, 붕대 던지기
        mObj_monster.init();
        mObj_knife.init();
        mObj_bandage.init();
        mObj_heart.init();

        mObj_number5.init();
        mObj_number9.init();
        mObj_number1.init();
        mObj_number2.init();


        // 붓 던지기
        mObj_brush.init();
        aphro1.init();

        // 해골
        mObj_skull_1.init();
        mObj_skull_2.init();

        if(showObj != null) {
            showObj.init();
        }

        if (mObj_girl_zombi != null) {
            // 귀신_1 트릭에 쓸 여자좀비
            mObj_girl_zombi.init();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        GLES30.glViewport(0,0,width, height);
        viewportChange = true;
        this.width = width;
        this.height = height;

        float ratio = (float) width * -5 / height;

        Matrix.frustumM(mProjectionMatrix, 0,
                -ratio, ratio,
                -1f, 10f,
                45, 200);    //위치 화면상에나오는 크기
    }

    // 0613 Myplace에 있는 것을 그려주려면 여기서 그려줘야함
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        //mainActivity 로부터 카메라 화면 정보를 받기 위해 메소드 실행
        myCallBack.preRender();
        GLES30.glDepthMask(false);
        mCamera.draw();
        GLES30.glDepthMask(true);

        // 몬스터에게 칼, 붕대 던지기
        mObj_knife.draw();
        mObj_monster.draw();
        mObj_bandage.draw();
        mObj_heart.draw();


        mObj_number5.draw();
        mObj_number9.draw();
        mObj_number1.draw();
        mObj_number2.draw();


        // 붓 던지기
        mObj_brush.draw();
        aphro1.draw();

        // 해골
        mObj_skull_1.draw();
        mObj_skull_2.draw();

        Matrix.setLookAtM(
                mViewMatrix, 0,
                0, 160, 40,
                0, 8, 3,
                0, -2, 0   //각도 위치 크기

        );
        Matrix.setLookAtM(
                mViewMatrix2, 0,
                0, 160, 70,
                0, 8, 3,
                0, -1, 0   //각도 위치 크기

        );
        Matrix.setLookAtM(
                mViewMatrix3, 0,
                0, 160, 40,
                0, 8, 3,
                0, -2, 0   //각도 위치 크기

        );

        Matrix.rotateM(mViewMatrix,0,60,1,0,0);

        Matrix.scaleM(mViewMatrix2,0,0.1f,0.1f,0.1f);
        Matrix.rotateM(mViewMatrix2,0,180,50,0,0);

        Matrix.scaleM(mViewMatrix3,0,30f,30f,30f);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setIdentityM(modelMatrix, 0);


        // float [] modelMatrix = new float[16];

        Obj1.draw();
        Obj2.draw();

        if(showObj != null) {
            showObj.draw();
        }

        if (mObj_girl_zombi != null) {
            // 귀신_1 트릭에 쓸 여자좀비
            mObj_girl_zombi.draw();
        }
    }

    //화면 변화 감지하면 내가 실행된다.
    void onDisplayChanged(){
        viewportChange = true;

        Log.d("MainRenderer 여","onDisplayChanged 여");
    }


    int getTextureID(){  //카메라의 색칠하기 id를 리턴 한다.
        return mCamera == null ? -1 : mCamera.mTextures[0];
    }

    void updateSession(Session session, int rotation){
        if(viewportChange){
            session.setDisplayGeometry(rotation, width, height);

            viewportChange = false;
        }
    }

    void updateProjMatrix(float [] matrix){
        if(showObj != null){
            showObj.setProjectionMatrix(matrix);
        }

        // 몬스터에게 칼, 붕대 던지기
        mObj_knife.setProjectionMatrix(matrix);
        mObj_monster.setProjectionMatrix(matrix);
        mObj_bandage.setProjectionMatrix(matrix);
        mObj_heart.setProjectionMatrix(matrix);

        mObj_number5.setProjectionMatrix(matrix);
        mObj_number1.setProjectionMatrix(matrix);
        mObj_number2.setProjectionMatrix(matrix);


        // 붓 던지기
        mObj_brush.setProjectionMatrix(matrix);
        aphro1.setProjectionMatrix(matrix);

        // 해골
        mObj_skull_1.setProjectionMatrix(matrix);
        mObj_skull_2.setProjectionMatrix(matrix);
        mObj_number9.setProjectionMatrix(matrix);

        if (mObj_girl_zombi != null) {
            // 귀신_1 트릭에 쓸 여자좀비
            mObj_girl_zombi.setProjectionMatrix(matrix);
        }
    }

    void updateViewMatrix(float [] matrix){
        if(showObj != null){
            showObj.setViewMatrix(matrix);
        }

        // 몬스터에게 칼, 붕대 던지기
        mObj_knife.setViewMatrix(matrix);
        mObj_monster.setViewMatrix(matrix);
        mObj_bandage.setViewMatrix(matrix);
        mObj_heart.setViewMatrix(matrix);

        mObj_number5.setViewMatrix(matrix);
        mObj_number1.setViewMatrix(matrix);
        mObj_number2.setViewMatrix(matrix);


        // 붓 던지기
        mObj_brush.setViewMatrix(matrix);
        aphro1.setViewMatrix(matrix);

        // 해골
        mObj_skull_1.setViewMatrix(matrix);
        mObj_skull_2.setViewMatrix(matrix);
        mObj_number9.setViewMatrix(matrix);

        if (mObj_girl_zombi != null) {
            // 귀신_1 트릭에 쓸 여자좀비
            mObj_girl_zombi.setViewMatrix(matrix);
        }

    }


    //아이템 on,off기능
    void makeFlash(boolean itemOn){ //
        if(itemOn){
            Matrix.setIdentityM(modelMatrix,0);

            Obj1.setModelMatrix(modelMatrix);
            Obj1.setViewMatrix(mViewMatrix);
            Obj1.setProjectionMatrix(mProjectionMatrix);
        }else{
            float[] mm = new float[16];
            Obj1.setModelMatrix(mm);
        }
    }
    void makeCross(boolean itemOn) {
        if (itemOn) {
            Matrix.setIdentityM(modelMatrix, 0);

            Obj2.setModelMatrix(modelMatrix);
            Obj2.setViewMatrix(mViewMatrix2);
            Obj2.setProjectionMatrix(mProjectionMatrix);
        } else {
            float[] mm = new float[16];
            Obj2.setModelMatrix(mm);
        }
    }

}
