package jp.cha84rakanal.opengles1;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends Activity {

    GLSurfaceView glSurfaceView;
    GLRenderer mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        mRenderer = new GLRenderer();
        glSurfaceView.setRenderer(mRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(glSurfaceView);
    }

    public class GLRenderer implements GLSurfaceView.Renderer {

        public GLRenderer(){

        }

        MyTriangle triangle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            triangle = new MyTriangle();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.v("MyGLRenderer","onSurfaceChanged:" + width +  "," + height);
            triangle.setSize(width,height);
        }

        @Override
        public void onDrawFrame(GL10 unused) {
            //背景色(R,G,B,ALPHA)
            GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            triangle.draw();
        }
    }

    public class MyTriangle {
        //シンプルなシェーダー
        public final String vertexShaderCode =
                "uniform mat4 u_MVPMatrix;"+
                "attribute  vec4 vPosition;" +
                        "void main() {" +
                        "  gl_Position = u_MVPMatrix * vPosition;" +
                        "}";
        //シンプル色は自分で指定(R,G,B ALPHA)指定
        public final String fragmentShaderCode =
                "precision mediump float;" +
                        "void main() {" +
                        "  gl_FragColor =vec4(1.0, 0.0, 0.0, 1.0);" +
                        "}";
        private int loadShader(int type, String shaderCode){
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            return shader;
        }

        private int shaderProgram;
        private float[] mMVPMatrix;// = new float[16];

        private int mWidth = 2;
        private int mHeight = 2;

        public void setSize(int mWidth,int mHeight){
            this.mWidth = mWidth;
            this.mHeight = mHeight;
        }

        /*
        int mat[16] = {
            0,  1,  2,  3,
            4,  5,  6,  7,
            8,  9,  10, 11,
            12, 13, 14, 15,
        }
         */

        public MyTriangle(){
            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
            int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
            shaderProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(shaderProgram, vertexShader);
            GLES20.glAttachShader(shaderProgram, fragmentShader);
            GLES20.glLinkProgram(shaderProgram);

            mMVPMatrix = new float[]{
                    1.0f,    0.0f,    0.0f,    0.0f,
                    0.0f,    1.0f,    0.0f,    0.0f,
                    0.0f,    0.0f,    1.0f,    0.0f,
                    0.0f,    0.0f,    0.0f,    1.0f
            };
        }

        public void draw(){


            mMVPMatrix[0] = (float) (2.0/this.mWidth);
            mMVPMatrix[12] = -1.0f;
            mMVPMatrix[5] = (float) (-2.0/this.mHeight);
            mMVPMatrix[13] = 1.0f;

            GLES20.glUseProgram(shaderProgram);
            int positionAttrib = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
            GLES20.glEnableVertexAttribArray(positionAttrib);
            int mMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram, "u_MVPMatrix");
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

            // 左回り
            /*
            float vertices[] = {
                    0.0f, 0.5f, 0.0f,//三角形の点A(x,y,z)
                    -0.5f, -0.5f, 0.0f,//三角形の点B(x,y,z)
                    0.5f, -0.5f, 0.0f//三角形の点C(x,y,z)
            };
            */

            float vertices[] = {
                    0.0f, 0.0f, 0.0f,
                    0.0f, 400f, 0.0f,
                    300f, 150f, 0.0f
            };

            ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(vertices);
            vertexBuffer.position(0);
            //GLES20.glVertexAttribPointer(positionAttrib,vertices.length, GLES20.GL_FLOAT, false, 0, vertexBuffer);
            GLES20.glVertexAttribPointer(positionAttrib, 3, GLES20.GL_FLOAT, false, 3 *4, vertexBuffer);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length/3);

            GLES20.glDisableVertexAttribArray(positionAttrib);
        }
    }

}
