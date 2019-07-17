/*---------------------------------------------------------------------------------

	Basic template code for starting a DS app

---------------------------------------------------------------------------------*/
#include <nds.h>
#include <stdio.h>
#include <math.h>

#define index(x,y) y*255+x

#include "Validation_inference.h"
#include "Validation_parameters.h"

int DrawPoint(float x,float y,float r,float reso);
int DrawPoint2(float x,float y,float r,float reso);
int DrawCanvas(int *pixel);

//---------------------------------------------------------------------------------
int main(void) {
//---------------------------------------------------------------------------------

	int test[] = {
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,5,4,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,10,7,3,0,0,0,0,1,9,6,7,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,4,3,3,5,8,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,1,0,0,2,4,4,3,10,15,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,3,4,1,0,0,0,0,4,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,9,15,0,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,1,0,0,12,54,110,151,109,20,0,5,0,0,0,0,0,0,0,0,4,0,6,1,0,4,3,0,0,17,0,55,218,250,255,255,205,12,0,8,0,0,0,0,0,0,0,0,0,3,0,0,9,0,0,15,5,8,127,255,240,255,242,191,52,0,2,4,0,0,0,0,0,0,0,0,2,5,0,15,1,0,13,0,24,189,243,255,248,241,228,43,0,0,9,0,0,0,0,0,0,0,0,0,4,0,4,0,3,10,1,133,250,255,255,252,223,38,0,13,11,18,1,0,0,0,0,0,0,0,0,0,0,8,0,10,9,0,101,255,238,255,238,177,27,2,14,0,2,2,0,4,0,0,0,0,0,0,0,0,1,0,0,16,0,78,247,244,255,242,60,0,3,0,3,0,0,0,1,6,0,0,0,0,0,0,0,0,10,0,20,0,76,250,252,255,154,9,20,0,8,0,0,21,1,6,9,0,0,0,0,0,0,0,0,0,0,0,17,152,245,247,253,214,11,7,3,1,0,15,0,0,2,0,0,0,0,0,4,12,0,0,5,7,0,6,79,239,247,247,188,19,0,0,0,0,0,0,0,0,0,0,0,0,2,15,0,0,13,71,0,5,0,87,231,255,255,214,16,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,10,0,253,71,7,165,229,255,255,213,23,3,6,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,19,105,198,229,250,255,254,226,111,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,14,0,4,74,255,242,255,239,226,22,2,0,4,5,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,6,0,20,85,255,255,227,85,7,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,1,0,11,0,10,0,81,9,0,0,18,8,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,1,0,1,0,6,0,16,0,8,0,4,0,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
    };

    int pixel[48705];
    for(int i = 0;i < 48705; i++){
        pixel[i] = 0;
    }
    // index(y*255+x)

    touchPosition touchXY;

    //set the mode for 2 text layers and two extended background layers
	videoSetMode(MODE_5_2D); 

	//set the first two banks as background memory and the third as sub background memory
	//D is not used..if you need a bigger background then you will need to map
	//more vram banks consecutivly (VRAM A-D are all 0x20000 bytes in size)
	vramSetPrimaryBanks(	VRAM_A_MAIN_BG_0x06000000, VRAM_B_MAIN_BG_0x06020000, 
		VRAM_C_SUB_BG , VRAM_D_LCD); 

	//lcdMainOnTop(); //put 3D on top
	consoleDemoInit(); //setup the sub screen for basic printing
	
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

    int bg = bgInit(3, BgType_Bmp16, BgSize_B16_256x256, 0,0);
	u16* backBuffer = (u16*)bgGetGfxPtr(bg) + 256*256;

    for(int iy = 0; iy < 190; iy++)
        for(int ix = 0; ix < 256; ix++) 
            backBuffer[iy * 256 + ix] = (0x00) | BIT(15);

    backBuffer = (u16*)bgGetGfxPtr(bg);
    bgSetMapBase(bg, 8);

	while(1) {

        swiWaitForVBlank();
		scanKeys();
        touchRead(&touchXY);

		int pressed = keysDown();
		if(pressed & KEY_START) break;

        iprintf("\x1b[12;12H(%d,%d)      ",touchXY.px,touchXY.py);
        iprintf("\x1b[13;12H%d      ",((keysCurrent() & KEY_TOUCH) == 4096));

        if(((keysCurrent() & KEY_TOUCH) == 4096)){

            for(int y = 0;y < 192;y++){
                for(int x = 0;x < 256;x++){
                    if((y - touchXY.py)*(y - touchXY.py) + (x - touchXY.px)*(x - touchXY.px) <= 35)
                        backBuffer[(y) * 256 + (x)] = 0xFFFF;
                }
            }

            backBuffer = (u16*)bgGetGfxPtr(bg);
            bgSetMapBase(bg, 8);
        }

	}

}
