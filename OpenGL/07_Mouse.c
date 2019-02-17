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

void mouse(int button, int state, int x, int y){
  switch (button) {
  case GLUT_LEFT_BUTTON:
    printf("left");
    break;
  case GLUT_MIDDLE_BUTTON:
    printf("middle");
    break;
  case GLUT_RIGHT_BUTTON:
    printf("right");
    break;
  default:
    break;
  }

  printf(" button is ");

  switch (state) {
  case GLUT_UP:
    printf("up");
    break;
  case GLUT_DOWN:
    printf("down");
    break;
  default:
    break;
  }

  printf(" at (%d, %d)\n", x, y);
}

void init(void){
    glClearColor(0.0, 0.0, 1.0, 1.0);
}

int main(int argc, char *argv[]){
    glutInitWindowPosition(100, 100); //位置
    glutInitWindowSize(320, 240); //サイズの指定
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_RGBA);
    glutCreateWindow(argv[0]);
    glutDisplayFunc(display); //ウィンドウが開かれたり、他のウィンドウによって隠されたウィンドウが再び現れたりして、ウィンドウを再描画する必要があるときに実行する
    glutReshapeFunc(resize);
    glutMouseFunc(mouse);
    init();
    glutMainLoop();
    return 0;
}