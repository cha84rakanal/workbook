#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

void display(void){
    glClear(GL_COLOR_BUFFER_BIT);
    glBegin(GL_POLYGON);
    glColor3d(1.0, 0.0, 0.0); /* 赤 */
    glVertex2d(-0.9, -0.9);
    glColor3d(0.0, 1.0, 0.0); /* 緑 */
    glVertex2d(0.9, -0.9);
    glColor3d(0.0, 0.0, 1.0); /* 青 */
    glVertex2d(0.9, 0.9);
    glColor3d(1.0, 1.0, 0.0); /* 黄 */
    glVertex2d(-0.9, 0.9);
    glEnd();
    glFlush();
}

void resize(int w, int h){

    //w,hはウィンドウ大きさ

    /* ウィンドウ全体をビューポートにする */
    glViewport(0, 0, w, h);

    /* 変換行列の初期化 */
    glLoadIdentity();

    /* スクリーン上の表示領域をビューポートの大きさに比例させる */
    glOrtho(-w / 200.0, w / 200.0, -h / 200.0, h / 200.0, -1.0, 1.0);

    //left,right,buttom,right,near,far
    //デフォルトは300,300なので、横の表示座標は、-1.5右1.5、上も1.5、-1.5になる
}

void init(void){
    glClearColor(0.0, 0.0, 1.0, 1.0);
}

int main(int argc, char *argv[]){
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_RGBA);
    glutCreateWindow(argv[0]);
    glutDisplayFunc(display); //ウィンドウが開かれたり、他のウィンドウによって隠されたウィンドウが再び現れたりして、ウィンドウを再描画する必要があるときに実行する
    glutReshapeFunc(resize);
    init();
    glutMainLoop();
    return 0;
}