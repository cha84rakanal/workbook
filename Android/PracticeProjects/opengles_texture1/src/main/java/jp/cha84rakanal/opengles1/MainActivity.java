package jp.cha84rakanal.opengles1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;

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
        MyTexture myTexture;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //triangle = new MyTriangle();
            myTexture = new MyTexture();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 unused) {
            //背景色(R,G,B,ALPHA)
            GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            //triangle.draw();
            myTexture.draw();
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

        public float uvs[] = new float[] {
                1.0f, 0.0f, // 右上に割り当てられる部分
                1.0f, 1.0f,
                0.0f, 0.0f,
                0.0f, 1.0f // 左下に割り当てられる部分
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

        private int[] texture;
        private void setupImage(){

            texture = new int[1];
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lena_color);

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
            setupImage();
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

        private float[] mMVPMatrix = new float[]{
            1.0f,    0.0f,    0.0f,    0.0f,
            0.0f,    1.0f,    0.0f,    0.0f,
            0.0f,    0.0f,    1.0f,    0.0f,
            0.0f,    0.0f,    0.0f,    1.0f
        };;

        public void draw(){

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

            //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, uvs.length/2);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

            GLES20.glDisableVertexAttribArray(mPositionHandle);
            GLES20.glDisableVertexAttribArray(mTexCoordLoc);
            GLES20.glDisable(GLES20.GL_BLEND);

        }
    }

    public class MyTriangle {
        //シンプルなシェーダー
        public final String vertexShaderCode =
                "attribute  vec4 vPosition;" +
                        "void main() {" +
                        "  gl_Position = vPosition;" +
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

        private  int shaderProgram;

        public MyTriangle(){
            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
            int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
            shaderProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(shaderProgram, vertexShader);
            GLES20.glAttachShader(shaderProgram, fragmentShader);
            GLES20.glLinkProgram(shaderProgram);
        }

        public void draw(){
            GLES20.glUseProgram(shaderProgram);
            int positionAttrib = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
            GLES20.glEnableVertexAttribArray(positionAttrib);

            float vertices[] = {
                    0.0f, 0.5f, 0.0f,//三角形の点A(x,y,z)
                    -0.5f, -0.5f, 0.0f,//三角形の点B(x,y,z)
                    0.5f, -0.5f, 0.0f//三角形の点C(x,y,z)
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
