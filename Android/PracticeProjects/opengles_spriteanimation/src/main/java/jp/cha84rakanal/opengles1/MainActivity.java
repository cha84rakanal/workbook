package jp.cha84rakanal.opengles1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
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

        //MyTriangle triangle;
        //MyTexture myTexture;
        MySpriteAnimation mySpriteAnimation;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //triangle = new MyTriangle();
            //myTexture = new MyTexture();
            mySpriteAnimation = new MySpriteAnimation(R.drawable.animsprite);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.v("MyGLRenderer","onSurfaceChanged:" + width +  "," + height);
            //myTexture.setSize(width,height);
            mySpriteAnimation.setDevicePixel(width,height);
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
            mySpriteAnimation.draw();
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
        public  final String fragmentShaderCode =
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
                1.0f,    0.0f,    0.0f,    0.0f,
                0.0f,    1.0f,    0.0f,    0.0f,
                0.0f,    0.0f,    1.0f,    0.0f,
                0.0f,    0.0f,    0.0f,    1.0f
        };

        private float vertices[] = {
                0.5f, 0.5f,0.0f, // 右上
                0.5f, -0.5f,0.0f, // 右下
                -0.5f, 0.5f,0.0f, // 左上
                -0.5f, -0.5f,0.0f // 左下
        };

        public float uvs[] = new float[] {
                1.0f, 0.0f, // 右上に割り当てられる部分
                1.0f, 1.0f,
                0.0f, 0.0f,
                0.0f, 1.0f // 左下に割り当てられる部分
        };

        private FloatBuffer vertexBuffer;
        private FloatBuffer uvBuffer;
        private ByteBuffer bb1;
        private ByteBuffer bb2;

        private int mWidth = 2;
        private int mHeight = 2;

        public void setDevicePixel(int mWidth,int mHeight){
            this.mWidth = mWidth;
            this.mHeight = mHeight;
        }

        private int mSpriteXNum = 1;
        private int mSpriteYNum = 1;

        private int mImageWidth;
        private int mImageHeight;

        private int[] texture;
        private void setupImage(int id){

            texture = new int[1];
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),id);

            mImageWidth = bitmap.getWidth();
            mImageHeight = bitmap.getHeight();

            // Bind texture to texturename
            GLES20.glGenTextures(1,texture,0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);

            // 拡大縮小の時のフィルターの設定
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

            // ちょっとわからないけど何かの設定
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);

            // 画像をテクスチャに登録
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();

        }

        private int loadShader(int type, String shaderCode){
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            return shader;
        }

        private  int shaderProgram;
        private  int resource_id;

        public MySpriteAnimation(int resource_id){

            this.resource_id = resource_id;

            setupImage(this.resource_id);

            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
            int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
            shaderProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(shaderProgram, vertexShader);
            GLES20.glAttachShader(shaderProgram, fragmentShader);
            GLES20.glLinkProgram(shaderProgram);

            //頂点座標をバッファーに変換
            if(bb1 == null) {
                bb1 = ByteBuffer.allocateDirect(vertices.length * 4);
                bb1.order(ByteOrder.nativeOrder());
                vertexBuffer = bb1.asFloatBuffer();
            }
            //画像側の頂点座標をバッファーに変換
            if(bb2 == null) {
                bb2 = ByteBuffer.allocateDirect(uvs.length * 4);
                bb2.order(ByteOrder.nativeOrder());
                uvBuffer = bb2.asFloatBuffer();
            }
        }

        // デバイス標準形で描画
        public void draw(){
            this.draw(1.f,1.f);
        }

        // 正方形で描画
        public void draw_square(){
            this.draw_with_size(1,1);
        }

        // 横を基準のデバイス標準形で描画
        public void draw_with_size(float size_x,float size_y){
            this.draw(size_x,size_y*mWidth/(float)mHeight);
        }

        // 横幅をpixelで指定して描画
        public void draw_with_pixel_size(int size_x,int size_y){
            this.draw(size_x * (2.0f/this.mWidth),size_y*(2.0f/this.mHeight));
        }

        public void draw(float size_x,float size_y){

            float x_pos = size_x/2.0f;
            float y_pos = size_y/2.0f;

            vertices = new float[]{
                    x_pos,  y_pos,  0.0f, // 右上
                    x_pos, -y_pos,  0.0f, // 右下
                    -x_pos,  y_pos,  0.0f, // 左上
                    -x_pos, -y_pos,  0.0f // 左下
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

    }

    public class MyTexture {

        // http://tkengo.github.io/blog/2015/01/27/opengl-es-2-2d-knowledge-5/
        // https://qiita.com/shunjiro/items/a68f5633532e6591a96f
        // https://bluefish.orz.hm/sdoc/opengles.html
        // http://andante.in/i/opengl-es2/opengl-es2%E3%82%92%E5%8B%89%E5%BC%B7%E3%81%99%E3%82%8B%E3%80%82%E3%81%9D%E3%81%AE3%E3%80%80%E7%94%BB%E5%83%8F%E3%82%92%E8%A4%87%E6%95%B0%E6%8F%8F%E3%81%8F/

        public final String vertexShaderCode =
                "attribute vec4 vPosition;" +
                        "uniform mat4 uMVPMatrix;" +
                        "attribute vec2 a_texCoord;" +
                        "varying vec2 v_texCoord;" +
                        "void main() {" +
                        "  gl_Position = uMVPMatrix*vPosition;" +
                        "  v_texCoord = a_texCoord;" +
                        "}";
        public  final String fragmentShaderCode =
                "precision mediump float;" +
                "varying vec2 v_texCoord;" +
                "uniform sampler2D s_texture;" +
                "uniform float Opacity;" +
                "uniform float Brightness;" +
                "void main() {" +
                "lowp vec4 textureColor = texture2D( s_texture, v_texCoord );" +
                "gl_FragColor = vec4((textureColor.rgb + vec3(Brightness)), textureColor.w*Opacity);" +
                "}";

        //画面の方の座標　zも含む
        private float vertices[] = {
                0.5f, 0.5f,0.0f, // 右上
                0.5f, -0.5f,0.0f, // 右下
                -0.5f, 0.5f,0.0f, // 左上
                -0.5f, -0.5f,0.0f // 左下
        };

        //画像の方の座標 verticesに対応する様に

        /*
        public float uvs[] = new float[] {
                1.0f, 0.0f, // 右上に割り当てられる部分
                1.0f, 1.0f,
                0.0f, 0.0f,
                0.0f, 1.0f // 左下に割り当てられる部分
        };
        */

        int i = 6;
        int j = 5;

        public float uvs[] = new float[] {
                (i + 1)*100.0f/1000.0f, (j)*100.0f/600.0f, // 右上に割り当てられる部分
                (i + 1)*100.0f/1000.0f, (j + 1)*100.0f/600.0f,
                (i)*100.0f/1000.0f, (j)*100.0f/600.0f,
                (i)*100.0f/1000.0f, (j + 1)*100.0f/600.0f // 左下に割り当てられる部分
        };

        /*
        (0.0 , 0.0)O----------- x (1.0 , 0.0)
                   |
                   |
                   |
                   |
                   y (0.0 , 1.0)
         */

        private FloatBuffer vertexBuffer;
        private FloatBuffer uvBuffer;
        private ByteBuffer bb1;
        private ByteBuffer bb2;

        private int mWidth = 2;
        private int mHeight = 2;

        public void setSize(int mWidth,int mHeight){
            this.mWidth = mWidth;
            this.mHeight = mHeight;
        }

        private int mImageWidth;
        private int mImageHeight;

        private int[] texture;
        private void setupImage(int id){

            texture = new int[1];
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),id);

            mImageWidth = bitmap.getWidth();
            mImageHeight = bitmap.getHeight();

            // Bind texture to texturename
            GLES20.glGenTextures(1,texture,0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);

            // 拡大縮小の時のフィルターの設定
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

            // ちょっとわからないけど何かの設定
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);

            // 画像をテクスチャに登録
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();

        }

        private int loadShader(int type, String shaderCode){
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            return shader;
        }

        private  int shaderProgram;

        public MyTexture(){
            setupImage(R.drawable.animsprite);
            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
            int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
            shaderProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(shaderProgram, vertexShader);
            GLES20.glAttachShader(shaderProgram, fragmentShader);
            GLES20.glLinkProgram(shaderProgram);

            //頂点座標をバッファーに変換
            if(bb1 == null) {
                bb1 = ByteBuffer.allocateDirect(vertices.length * 4);
                bb1.order(ByteOrder.nativeOrder());
                vertexBuffer = bb1.asFloatBuffer();
            }
            //画像側の頂点座標をバッファーに変換
            if(bb2 == null) {
                bb2 = ByteBuffer.allocateDirect(uvs.length * 4);
                bb2.order(ByteOrder.nativeOrder());
                uvBuffer = bb2.asFloatBuffer();
            }

            // TODO: 正方形の描画をするには？
            //
        }

        private float[] mMVPMatrix = new float[]{
            1.0f,    0.0f,    0.0f,    0.0f,
            0.0f,    1.0f,    0.0f,    0.0f,
            0.0f,    0.0f,    1.0f,    0.0f,
            0.0f,    0.0f,    0.0f,    1.0f
        };

        // デバイス標準形で描画
        public void draw(){
            this.draw(1.f,1.f);
        }

        // 正方形で描画
        public void draw_square(){
            this.draw_with_size(1,1);
        }

        // 横を基準のデバイス標準形で描画
        public void draw_with_size(float size_x,float size_y){
            this.draw(size_x,size_y*mWidth/(float)mHeight);
        }

        // 横幅を指定して描画
        public void draw_with_pixel_size(int size_x,int size_y){
            this.draw(size_x * (2.0f/this.mWidth),size_y*(2.0f/this.mHeight));
        }

        public void draw(float size_x,float size_y){

            float x_pos = size_x/2.0f;
            float y_pos = size_y/2.0f;

            vertices = new float[]{
                     x_pos,  y_pos,  0.0f, // 右上
                     x_pos, -y_pos,  0.0f, // 右下
                    -x_pos,  y_pos,  0.0f, // 左上
                    -x_pos, -y_pos,  0.0f // 左下
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
    }

}
