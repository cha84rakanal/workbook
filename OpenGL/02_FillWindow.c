#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

void display(void){
    glClear(GL_COLOR_BUFFER_BIT);
    glFlush();
}

void init(void){
    glClearColor(0.0, 0.0, 1.0, 1.0);
}

int main(int argc, char *argv[]){
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_RGBA);
    glutCreateWindow(argv[0]);
    glutDisplayFunc(display); //ウィンドウが開かれたり、他のウィンドウによって隠されたウィンドウが再び現れたりして、ウィンドウを再描画する必要があるときに実行する
    init();
    glutMainLoop();
    return 0;
}