package jp.cha84rakanal.opengles1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends Activity {

    GLSurfaceView glSurfaceView;
    GLRenderer mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this) {
            @Override
            public boolean onTouchEvent(MotionEvent e) {

                float x = e.getX();
                float y = e.getY();

                mRenderer.addSpeite((int)x,(int)y);

                return true;
            }

        };
        /*
        glSurfaceView.setOnTouchListener((v,event)->{
            //mRenderer.addSpeite((int)event.getX(),(int)event.getY());
            return false;
        });
        */
        glSurfaceView.setEGLContextClientVersion(2);
        mRenderer = new GLRenderer();
        glSurfaceView.setRenderer(mRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(glSurfaceView);
    }

    public class GLRenderer implements GLSurfaceView.Renderer {

        public GLRenderer() {

        }

        //MyTriangle triangle;
        //MyTexture myTexture;
        MySpriteAnimation mySpriteAnimation;

        int c_frame = 0;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //triangle = new MyTriangle();
            //myTexture = new MyTexture();
            mySpriteAnimation = new MySpriteAnimation(R.drawable.animsprite);
            mySpriteAnimation.setSpriteNum(10, 6);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.v("MyGLRenderer", "onSurfaceChanged:" + width + "," + height);
            //myTexture.setSize(width,height);
            mySpriteAnimation.setDevicePixel(width, height);
        }

        @Override
        public void onDrawFrame(GL10 unused) {

            //Log.v("GLRenderer","draw");

            //背景色(R,G,B,ALPHA)
            GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            //myTexture.draw();
            //myTexture.draw_square();
            //myTexture.draw_with_pixel_size(500,500);
            mySpriteAnimation.setPosition_with_pixel_size(0, 0);
            mySpriteAnimation.draw_with_pixel_size(200, 200, c_frame);
            c_frame++;
            if (c_frame >= 60) {
                c_frame = 0;
            }
            mySpriteAnimation.draw_sprite_list();

            //mySpriteAnimation.draw_with_pixel_size(1000,1000);
        }

        public void addSpeite(int x, int y) {
            mySpriteAnimation.addSprite(x, y);
        }
    }

    public class AnimationStruct {

        int x;
        int y;
        int frame;

        public AnimationStruct(int x, int y) {
            this.x = x;
            this.y = y;
            frame = 0;
        }

        public void increment_frame() {
            frame++;
            if (frame >= 60) {
                frame = 0;
            }
        }
    }

    public class MySpriteAnimation {

        public final String vertexShaderCode =
                "attribute vec4 vPosition;" +
                        "uniform mat4 uMVPMatrix;" +
                        "attribute vec2 a_texCoord;" +
                        "varying vec2 v_texCoord;" +
                        "void main() {" +
                        "  gl_Position = uMVPMatrix*vPosition;" +
                        "  v_texCoord = a_texCoord;" +
                        "}";
        public final String fragmentShaderCode =
                "precision mediump float;" +
                        "varying vec2 v_texCoord;" +
                        "uniform sampler2D s_texture;" +
                        "uniform float Opacity;" +
                        "uniform float Brightness;" +
                        "void main() {" +
                        "lowp vec4 textureColor = texture2D( s_texture, v_texCoord );" +
                        "gl_FragColor = vec4((textureColor.rgb + vec3(Brightness)), textureColor.w*Opacity);" +
                        "}";

        private float[] mMVPMatrix = new float[]{
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        };

        private float vertices[] = {
                0.5f, 0.5f, 0.0f, // 右上
                0.5f, -0.5f, 0.0f, // 右下
                -0.5f, 0.5f, 0.0f, // 左上
                -0.5f, -0.5f, 0.0f // 左下
        };

        public float uvs[] = new float[]{
                1.0f, 0.0f, // 右上に割り当てられる部分
                1.0f, 1.0f,
                0.0f, 0.0f,
                0.0f, 1.0f // 左下に割り当てられる部分
        };

        private volatile ArrayList<AnimationStruct> sprite_list;

        public synchronized void addSprite(int x, int y) {
            sprite_list.add(new AnimationStruct(x - (int) (mWidth / 2.0f), y - (int) (mHeight / 2.0f)));
        }

        private FloatBuffer vertexBuffer;
        private FloatBuffer uvBuffer;
        private ByteBuffer bb1;
        private ByteBuffer bb2;

        private int mWidth = 2;
        private int mHeight = 2;

        public void setDevicePixel(int mWidth, int mHeight) {
            this.mWidth = mWidth;
            this.mHeight = mHeight;
        }

        private int mSpriteXNum = 1;
        private int mSpriteYNum = 1;

        public void setSpriteNum(int x, int y) {
            this.mSpriteXNum = x;
            this.mSpriteYNum = y;
        }

        private int mImageWidth;
        private int mImageHeight;

        private int[] texture;

        private void setupImage(int id) {

            texture = new int[1];
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);

            mImageWidth = bitmap.getWidth();
            mImageHeight = bitmap.getHeight();

            // Bind texture to texturename
            GLES20.glGenTextures(1, texture, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);

            // 拡大縮小の時のフィルターの設定
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

            // ちょっとわからないけど何かの設定
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            // 画像をテクスチャに登録
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();

        }

        private int loadShader(int type, String shaderCode) {
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            return shader;
        }

        private int shaderProgram;
        private int resource_id;

        public MySpriteAnimation(int resource_id) {

            sprite_list = new ArrayList<>();

            this.resource_id = resource_id;

            setupImage(this.resource_id);

            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
            int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
            shaderProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(shaderProgram, vertexShader);
            GLES20.glAttachShader(shaderProgram, fragmentShader);
            GLES20.glLinkProgram(shaderProgram);

            //頂点座標をバッファーに変換
            if (bb1 == null) {
                bb1 = ByteBuffer.allocateDirect(vertices.length * 4);
                bb1.order(ByteOrder.nativeOrder());
                vertexBuffer = bb1.asFloatBuffer();
            }
            //画像側の頂点座標をバッファーに変換
            if (bb2 == null) {
                bb2 = ByteBuffer.allocateDirect(uvs.length * 4);
                bb2.order(ByteOrder.nativeOrder());
                uvBuffer = bb2.asFloatBuffer();
            }
        }

        // デバイス標準形で描画
        public void draw(int frame) {
            this.draw_frame(1.f, 1.f, frame);
        }

        // 正方形で描画
        public void draw_square(int frame) {
            this.draw_with_size(1, 1, frame);
        }

        // 横を基準のデバイス標準形で描画
        public void draw_with_size(float size_x, float size_y, int frame) {
            this.draw_frame(size_x, size_y * mWidth / (float) mHeight, frame);
        }

        // pixelで指定して描画
        public void draw_with_pixel_size(int size_x, int size_y, int frame) {
            this.draw_frame(size_x * (2.0f / this.mWidth), size_y * (2.0f / this.mHeight), frame);
        }

        public void draw(float size_x, float size_y) {

            float x_pos = size_x / 2.0f;
            float y_pos = size_y / 2.0f;

            vertices = new float[]{
                    x_pos, y_pos, 0.0f, // 右上
                    x_pos, -y_pos, 0.0f, // 右下
                    -x_pos, y_pos, 0.0f, // 左上
                    -x_pos, -y_pos, 0.0f // 左下
            };

            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

            GLES20.glUseProgram(shaderProgram);

            int mPositionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            int mTexCoordLoc = GLES20.glGetAttribLocation(shaderProgram, "a_texCoord");
            GLES20.glEnableVertexAttribArray(mTexCoordLoc);
            vertexBuffer.put(vertices);
            vertexBuffer.position(0);
            uvBuffer.put(uvs);
            uvBuffer.position(0);

            GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
            GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);

            GLES20.glUniform1i(GLES20.glGetUniformLocation(shaderProgram, "s_texture"), 0);
            GLES20.glUniform1f(GLES20.glGetUniformLocation(shaderProgram, "Opacity"), 1.0f);
            GLES20.glUniform1f(GLES20.glGetUniformLocation(shaderProgram, "Brightness"), 0.0f);
            GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(shaderProgram, "uMVPMatrix"), 1, false, mMVPMatrix, 0);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

            GLES20.glDisableVertexAttribArray(mPositionHandle);
            GLES20.glDisableVertexAttribArray(mTexCoordLoc);
            GLES20.glDisable(GLES20.GL_BLEND);

        }

        public void setPosition(float pos_x, float pos_y) {
            mMVPMatrix[12] = pos_x;
            mMVPMatrix[13] = pos_y;
        }

        public void setPosition_with_pixel_size(int pos_x, int pos_y) {
            mMVPMatrix[12] = (float) pos_x * (2.0f / this.mWidth);
            mMVPMatrix[13] = (float) pos_y * (2.0f / this.mHeight);
        }

        public void draw_frame(float size_x, float size_y, int frame) {

            float x_pos = size_x / 2.0f;
            float y_pos = size_y / 2.0f;

            vertices = new float[]{
                    x_pos, y_pos, 0.0f, // 右上
                    x_pos, -y_pos, 0.0f, // 右下
                    -x_pos, y_pos, 0.0f, // 左上
                    -x_pos, -y_pos, 0.0f // 左下
            };


            int pos_x = 0;
            int pos_y = 0;
            if (!(frame <= 0 || mSpriteXNum * mSpriteYNum <= frame)) {
                pos_x = frame % mSpriteXNum;
                pos_y = frame / mSpriteXNum;
            }

            uvs = new float[]{
                    (pos_x + 1) * 1.0f / mSpriteXNum, (pos_y) * 1.0f / mSpriteYNum, // 右上に割り当てられる部分
                    (pos_x + 1) * 1.0f / mSpriteXNum, (pos_y + 1) * 1.0f / mSpriteYNum,
                    (pos_x) * 1.0f / mSpriteXNum, (pos_y) * 1.0f / mSpriteYNum,
                    (pos_x) * 1.0f / mSpriteXNum, (pos_y + 1) * 1.0f / mSpriteYNum // 左下に割り当てられる部分
            };

            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

            GLES20.glUseProgram(shaderProgram);

            int mPositionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            int mTexCoordLoc = GLES20.glGetAttribLocation(shaderProgram, "a_texCoord");
            GLES20.glEnableVertexAttribArray(mTexCoordLoc);
            vertexBuffer.put(vertices);
            vertexBuffer.position(0);
            uvBuffer.put(uvs);
            uvBuffer.position(0);

            GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
            GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);

            GLES20.glUniform1i(GLES20.glGetUniformLocation(shaderProgram, "s_texture"), 0);
            GLES20.glUniform1f(GLES20.glGetUniformLocation(shaderProgram, "Opacity"), 1.0f);
            GLES20.glUniform1f(GLES20.glGetUniformLocation(shaderProgram, "Brightness"), 0.0f);
            GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(shaderProgram, "uMVPMatrix"), 1, false, mMVPMatrix, 0);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

            GLES20.glDisableVertexAttribArray(mPositionHandle);
            GLES20.glDisableVertexAttribArray(mTexCoordLoc);
            GLES20.glDisable(GLES20.GL_BLEND);

        }

        public synchronized void draw_sprite_list(){

            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

            GLES20.glUseProgram(shaderProgram);

            for(AnimationStruct a : sprite_list) {

                float x_pos = (200 * (2.0f / this.mWidth)) / 2.0f;
                float y_pos = (200 * (2.0f / this.mHeight)) / 2.0f;

                vertices = new float[]{
                        x_pos, y_pos, 0.0f, // 右上
                        x_pos, -y_pos, 0.0f, // 右下
                        -x_pos, y_pos, 0.0f, // 左上
                        -x_pos, -y_pos, 0.0f // 左下
                };

                int pos_x = 0;
                int pos_y = 0;
                if (!(a.frame <= 0 || mSpriteXNum * mSpriteYNum <= a.frame)) {
                    pos_x = a.frame % mSpriteXNum;
                    pos_y = a.frame / mSpriteXNum;
                }

                uvs = new float[]{
                        (pos_x + 1) * 1.0f / mSpriteXNum, (pos_y) * 1.0f / mSpriteYNum, // 右上に割り当てられる部分
                        (pos_x + 1) * 1.0f / mSpriteXNum, (pos_y + 1) * 1.0f / mSpriteYNum,
                        (pos_x) * 1.0f / mSpriteXNum, (pos_y) * 1.0f / mSpriteYNum,
                        (pos_x) * 1.0f / mSpriteXNum, (pos_y + 1) * 1.0f / mSpriteYNum // 左下に割り当てられる部分
                };

                mMVPMatrix = new float[]{
                        1.0f, 0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f, 0.0f,
                        0.0f, 0.0f, 1.0f, 0.0f,
                        (float) a.x * (2.0f / this.mWidth),(float) -a.y * (2.0f / this.mHeight), 0.0f, 1.0f
                };

                int mPositionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
                GLES20.glEnableVertexAttribArray(mPositionHandle);
                int mTexCoordLoc = GLES20.glGetAttribLocation(shaderProgram, "a_texCoord");
                GLES20.glEnableVertexAttribArray(mTexCoordLoc);
                vertexBuffer.put(vertices);
                vertexBuffer.position(0);
                uvBuffer.put(uvs);
                uvBuffer.position(0);

                GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
                GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);

                GLES20.glUniform1i(GLES20.glGetUniformLocation(shaderProgram, "s_texture"), 0);
                GLES20.glUniform1f(GLES20.glGetUniformLocation(shaderProgram, "Opacity"), 1.0f);
                GLES20.glUniform1f(GLES20.glGetUniformLocation(shaderProgram, "Brightness"), 0.0f);
                GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(shaderProgram, "uMVPMatrix"), 1, false, mMVPMatrix, 0);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

                GLES20.glDisableVertexAttribArray(mPositionHandle);
                GLES20.glDisableVertexAttribArray(mTexCoordLoc);

                a.increment_frame();

            }

            GLES20.glDisable(GLES20.GL_BLEND);

        }

    }

}