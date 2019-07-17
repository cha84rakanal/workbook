/*---------------------------------------------------------------------------------

	Basic template code for starting a DS app

---------------------------------------------------------------------------------*/
#include <nds.h>
#include <stdio.h>

#include "Validation_inference.h"
#include "Validation_parameters.h"

int DrawGLScene(float a,float b);

//---------------------------------------------------------------------------------
int main(void) {
//---------------------------------------------------------------------------------

	int test[] = {
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,5,4,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,10,7,3,0,0,0,0,1,9,6,7,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,4,3,3,5,8,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,1,0,0,2,4,4,3,10,15,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,3,4,1,0,0,0,0,4,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,9,15,0,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,1,0,0,12,54,110,151,109,20,0,5,0,0,0,0,0,0,0,0,4,0,6,1,0,4,3,0,0,17,0,55,218,250,255,255,205,12,0,8,0,0,0,0,0,0,0,0,0,3,0,0,9,0,0,15,5,8,127,255,240,255,242,191,52,0,2,4,0,0,0,0,0,0,0,0,2,5,0,15,1,0,13,0,24,189,243,255,248,241,228,43,0,0,9,0,0,0,0,0,0,0,0,0,4,0,4,0,3,10,1,133,250,255,255,252,223,38,0,13,11,18,1,0,0,0,0,0,0,0,0,0,0,8,0,10,9,0,101,255,238,255,238,177,27,2,14,0,2,2,0,4,0,0,0,0,0,0,0,0,1,0,0,16,0,78,247,244,255,242,60,0,3,0,3,0,0,0,1,6,0,0,0,0,0,0,0,0,10,0,20,0,76,250,252,255,154,9,20,0,8,0,0,21,1,6,9,0,0,0,0,0,0,0,0,0,0,0,17,152,245,247,253,214,11,7,3,1,0,15,0,0,2,0,0,0,0,0,4,12,0,0,5,7,0,6,79,239,247,247,188,19,0,0,0,0,0,0,0,0,0,0,0,0,2,15,0,0,13,71,0,5,0,87,231,255,255,214,16,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,10,0,253,71,7,165,229,255,255,213,23,3,6,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,19,105,198,229,250,255,254,226,111,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,14,0,4,74,255,242,255,239,226,22,2,0,4,5,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,6,0,20,85,255,255,227,85,7,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,1,0,11,0,10,0,81,9,0,0,18,8,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,1,0,1,0,6,0,16,0,8,0,4,0,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
    };

    touchPosition touchXY;

    int touch_x = 0;
    int touch_y = 0;

	lcdMainOnTop(); //put 3D on top
	consoleDemoInit(); //setup the sub screen for basic printing

	// Setup the Main screen for 3D 
	videoSetMode(MODE_0_3D);
	glInit(); // initialize gl
	glEnable(GL_ANTIALIAS); // enable antialiasing

    // setup the rear plane
	glClearColor(0,0,0,31); // BG must be opaque for AA to work
	glClearPolyID(63); // BG must have a unique polygon ID for AA to work
	glClearDepth(0x7FFF);
	
	// Set our view port to be the same size as the screen
	glViewport(0,0,255,191);
	
	iprintf("Hello World!\n");

	void *_context = nnablart_validation_allocate_context(Validation_parameters);

	float *nn_input_buffer = nnablart_validation_input_buffer(_context, 0);

    for (int i = 0; i < NNABLART_VALIDATION_INPUT0_SIZE; i++) {
        nn_input_buffer[i] = (float)test[i];
    }
    
    nnablart_validation_inference(_context);

	float *pred = nnablart_validation_output_buffer(_context, 0);
    int top_class = 0;
    float top_probability = 0.0f;
    for (int class = 0; class < NNABLART_VALIDATION_OUTPUT0_SIZE; class++) {
        iprintf("[%d] : %d\n",class,(int)(pred[class]*100000));
        if (top_probability < pred[class]) {
            top_probability = pred[class];
            top_class = class;
        }
    }

    iprintf("\nresults: %d\n",top_class);

    nnablart_validation_free_context(_context);

	while(1) {

		scanKeys();
        touchRead(&touchXY);

        glOrtho(-4,4,-3,3,0.1,10);
        glMatrixMode(GL_MODELVIEW);
        glPolyFmt(POLY_ALPHA(31) | POLY_CULL_NONE );

		int pressed = keysDown();
		if(pressed & KEY_START) break;

		//if(pressed & KEY_TOUCH)  {
		//	touch_x = touchXY.px;
		//	touch_y = touchXY.py;
		//}

        glColor3f(1, 1, 1);

        iprintf("\x1b[12;12H(%d,%d)      ",touchXY.px,touchXY.py);
        //iprintf("\x1b[13;12H(%d,%d)      ",(int)(((touchXY.px/255.0f)*4.f)-2.f)*1000,(int)(((touchXY.py/191.0f)*3.f)-1.5f))*1000;

        glPushMatrix();
        DrawGLScene(
            ((touchXY.px/255.0f)*2.f)-1.f,
            -(((touchXY.py/191.0f)*2.f)-1.f)
        );
        glPopMatrix(1);

        swiWaitForVBlank();
        glFlush(0);

	}

}

int DrawGLScene(float a,float b) {
	
	glLoadIdentity();									// Reset The Current Modelview Matrix
	glTranslatef(a,b,0.0f);
	glBegin(GL_TRIANGLES);								// Drawing Using Triangles
		glVertex3f( 0.0f, 0.25f, 0.0f);					// Top
		glVertex3f(-0.25f,-0.25f, 0.0f);					// Bottom Left
		glVertex3f( 0.25f,-0.25f, 0.0f);					// Bottom Right
	glEnd();											// Finished Drawing The Triangle
    return TRUE;
}
