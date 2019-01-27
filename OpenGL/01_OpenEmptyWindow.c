#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

void display(void){
}

int main(int argc, char *argv[]){
    glutInit(&argc, argv);
    glutCreateWindow(argv[0]);
    glutDisplayFunc(display); //ウィンドウが開かれたり、他のウィンドウによって隠されたウィンドウが再び現れたりして、ウィンドウを再描画する必要があるときに実行する
    glutMainLoop();
    return 0;
}

//cc 01_OpenEmptyWindow.c -framework GLUT -framework OpenGL -mmacosx-version-min=10.8