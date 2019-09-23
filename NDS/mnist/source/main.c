/*---------------------------------------------------------------------------------

	Basic template code for starting a DS app

---------------------------------------------------------------------------------*/
#include <nds.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
//#include <time.h>

#define index(x,y) y*255+x

#include "Validation_inference.h"
#include "Validation_parameters.h"

//---------------------------------------------------------------------------------
int main(void) {
//---------------------------------------------------------------------------------

	int test[] = {
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,5,4,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,10,7,3,0,0,0,0,1,9,6,7,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,4,3,3,5,8,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,1,0,0,2,4,4,3,10,15,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,3,4,1,0,0,0,0,4,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,9,15,0,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,1,0,0,12,54,110,151,109,20,0,5,0,0,0,0,0,0,0,0,4,0,6,1,0,4,3,0,0,17,0,55,218,250,255,255,205,12,0,8,0,0,0,0,0,0,0,0,0,3,0,0,9,0,0,15,5,8,127,255,240,255,242,191,52,0,2,4,0,0,0,0,0,0,0,0,2,5,0,15,1,0,13,0,24,189,243,255,248,241,228,43,0,0,9,0,0,0,0,0,0,0,0,0,4,0,4,0,3,10,1,133,250,255,255,252,223,38,0,13,11,18,1,0,0,0,0,0,0,0,0,0,0,8,0,10,9,0,101,255,238,255,238,177,27,2,14,0,2,2,0,4,0,0,0,0,0,0,0,0,1,0,0,16,0,78,247,244,255,242,60,0,3,0,3,0,0,0,1,6,0,0,0,0,0,0,0,0,10,0,20,0,76,250,252,255,154,9,20,0,8,0,0,21,1,6,9,0,0,0,0,0,0,0,0,0,0,0,17,152,245,247,253,214,11,7,3,1,0,15,0,0,2,0,0,0,0,0,4,12,0,0,5,7,0,6,79,239,247,247,188,19,0,0,0,0,0,0,0,0,0,0,0,0,2,15,0,0,13,71,0,5,0,87,231,255,255,214,16,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,10,0,253,71,7,165,229,255,255,213,23,3,6,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,19,105,198,229,250,255,254,226,111,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,14,0,4,74,255,242,255,239,226,22,2,0,4,5,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,6,0,20,85,255,255,227,85,7,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,1,0,11,0,10,0,81,9,0,0,18,8,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,1,0,1,0,6,0,16,0,8,0,4,0,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
    };

    //struct timespec startTime, endTime;

    touchPosition touchXY;

    //set the mode for 2 text layers and two extended background layers
	videoSetMode(MODE_5_2D); 

	//set the first two banks as background memory and the third as sub background memory
	//D is not used..if you need a bigger background then you will need to map
	//more vram banks consecutivly (VRAM A-D are all 0x20000 bytes in size)
	vramSetPrimaryBanks(	VRAM_A_MAIN_BG_0x06000000, VRAM_B_MAIN_BG_0x06020000, 
		VRAM_C_SUB_BG , VRAM_D_LCD); 

	consoleDemoInit(); //setup the sub screen for basic printing

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
        if (top_probability < pred[class]) {
            top_probability = pred[class];
            top_class = class;
        }
    }

    iprintf("\nInitialize Done\n");

    int bg = bgInit(3, BgType_Bmp16, BgSize_B16_256x256, 0,0);
	u16* backBuffer = (u16*)bgGetGfxPtr(bg) + 256*256;

    for(int iy = 0; iy < 190; iy++)
        for(int ix = 0; ix < 256; ix++) 
            backBuffer[iy * 256 + ix] = (0x00) | BIT(15);

    //40-151
    //72-183
    for(int iy=39;iy < 152+1;iy++){
        backBuffer[iy * 256 + 71] = 0xFFFF;
        backBuffer[iy * 256 + 70] = 0xFFFF;
        backBuffer[iy * 256 + 184] = 0xFFFF;
        backBuffer[iy * 256 + 185] = 0xFFFF;
    }

    for(int ix=70;ix < 185+1;ix++){
        backBuffer[38 * 256 + ix] = 0xFFFF;
        backBuffer[39 * 256 + ix] = 0xFFFF;
        backBuffer[152 * 256 + ix] = 0xFFFF;
        backBuffer[153 * 256 + ix] = 0xFFFF;
    }

    backBuffer = (u16*)bgGetGfxPtr(bg);
    bgSetMapBase(bg, 8);

	while(1) {

        swiWaitForVBlank();
		scanKeys();
        touchRead(&touchXY);

		int pressed = keysDown();
		if(pressed & KEY_START) break;
        if(pressed & KEY_A){

            iprintf("\x1b[3;12HStart");

            int temp = 0;

            // http://www7a.biglobe.ne.jp/~fairytale/article/program/graphics.html
            for (int y = 0; y < 28; y++) {
                for (int x = 0; x < 28; x++) {
                    temp = 0;
                    nn_input_buffer[y * 256 + x] = 0.0f;

                    //72,40 73,40 74,40 75,40
                    //72,41 73,41 74,41 75,41
                    //72,42 73,42 74,42 75,42
                    //72,43 73,43 74,43 75,43
                    temp += (backBuffer[(40+(y*4)) * 256 + 72+(x*4)] == 0xFFFF)? 1:0;
                    temp += (backBuffer[(40+(y*4)) * 256 + 72+(x*4)+1] == 0xFFFF)? 1:0;
                    temp += (backBuffer[(40+(y*4)) * 256 + 72+(x*4)+2] == 0xFFFF)? 1:0;
                    temp += (backBuffer[(40+(y*4)) * 256 + 72+(x*4)+3] == 0xFFFF)? 1:0;

                    temp += (backBuffer[(40+(y*4)+1) * 256 + 72+(x*4)] == 0xFFFF)? 1:0;
                    temp += (backBuffer[(40+(y*4)+1) * 256 + 72+(x*4)+1] == 0xFFFF)? 1:0;
                    temp += (backBuffer[(40+(y*4)+1) * 256 + 72+(x*4)+2] == 0xFFFF)? 1:0;
                    temp += (backBuffer[(40+(y*4)+1) * 256 + 72+(x*4)+3] == 0xFFFF)? 1:0;

                    temp += (backBuffer[(40+(y*4)+2) * 256 + 72+(x*4)] == 0xFFFF)? 1:0;
                    temp += (backBuffer[(40+(y*4)+2) * 256 + 72+(x*4)+1] == 0xFFFF)? 1:0;
                    temp += (backBuffer[(40+(y*4)+2) * 256 + 72+(x*4)+2] == 0xFFFF)? 1:0;
                    temp += (backBuffer[(40+(y*4)+2) * 256 + 72+(x*4)+3] == 0xFFFF)? 1:0;

                    temp += (backBuffer[(40+(y*4)+3) * 256 + 72+(x*4)] == 0xFFFF)? 1:0;
                    temp += (backBuffer[(40+(y*4)+3) * 256 + 72+(x*4)+1] == 0xFFFF)? 1:0;
                    temp += (backBuffer[(40+(y*4)+3) * 256 + 72+(x*4)+2] == 0xFFFF)? 1:0;
                    temp += (backBuffer[(40+(y*4)+3) * 256 + 72+(x*4)+3] == 0xFFFF)? 1:0;

                    if(temp != 0)nn_input_buffer[y * 28 + x] = 255.0f;

                }  
            }

            //clock_gettime(CLOCK_REALTIME, &startTime);

            nnablart_validation_inference(_context);

            //clock_gettime(CLOCK_REALTIME, &endTime);

            float *pred = nnablart_validation_output_buffer(_context, 0);
            top_class = 0;
            top_probability = 0.0f;
            
            for (int class = 0; class < NNABLART_VALIDATION_OUTPUT0_SIZE; class++) {
                if (top_probability < pred[class]) {
                    top_probability = pred[class];
                    top_class = class;
                }
            }

            iprintf("\x1b[5;0H[%d] : %3d.%03d%%",0,(int)(pred[0]*10000)/100,(int)(pred[0]*10000)%100);
            iprintf("\x1b[6;0H[%d] : %3d.%03d%%",1,(int)(pred[1]*10000)/100,(int)(pred[1]*10000)%100);
            iprintf("\x1b[7;0H[%d] : %3d.%03d%%",2,(int)(pred[2]*10000)/100,(int)(pred[2]*10000)%100);
            iprintf("\x1b[8;0H[%d] : %3d.%03d%%",3,(int)(pred[3]*10000)/100,(int)(pred[3]*10000)%100);
            iprintf("\x1b[9;0H[%d] : %3d.%03d%%",4,(int)(pred[4]*10000)/100,(int)(pred[4]*10000)%100);
            iprintf("\x1b[10;0H[%d] : %3d.%03d%%",5,(int)(pred[5]*10000)/100,(int)(pred[5]*10000)%100);
            iprintf("\x1b[11;0H[%d] : %3d.%03d%%",6,(int)(pred[6]*10000)/100,(int)(pred[6]*10000)%100);
            iprintf("\x1b[12;0H[%d] : %3d.%03d%%",7,(int)(pred[7]*10000)/100,(int)(pred[7]*10000)%100);
            iprintf("\x1b[13;0H[%d] : %3d.%03d%%",8,(int)(pred[8]*10000)/100,(int)(pred[8]*10000)%100);
            iprintf("\x1b[14;0H[%d] : %3d.%03d%%",9,(int)(pred[9]*10000)/100,(int)(pred[9]*10000)%100);

            iprintf("\x1b[16;0Hresults: %d",top_class);

            //double time = static_cast<double>(chrono::duration_cast<chrono::microseconds>(end - start).count() / 1000.0);
            /*
            if (endTime.tv_nsec < startTime.tv_nsec) {
                printf("\x1b[17;0H%10lld.%09ld[s]", endTime.tv_sec - startTime.tv_sec - 1 ,endTime.tv_nsec + 1000000000 - startTime.tv_nsec);
            } else {
                printf("\x1b[17;0H%10lld.%09ld[s]", endTime.tv_sec - startTime.tv_sec ,endTime.tv_nsec - startTime.tv_nsec);
            }
            */

            //printf("\x1b[17;0HProcessing time : %lf[ms]", time);

            iprintf("\x1b[3;12HReady");
        }
        if(pressed & KEY_B){
            for(int y = 0;y < 192;y++){
                for(int x = 0;x < 256;x++){
                    backBuffer[(y) * 256 + (x)] = 0x0000;
                }
            }
            for(int iy=39;iy < 152+1;iy++){
                backBuffer[iy * 256 + 71] = 0xFFFF;
                backBuffer[iy * 256 + 70] = 0xFFFF;
                backBuffer[iy * 256 + 184] = 0xFFFF;
                backBuffer[iy * 256 + 185] = 0xFFFF;
            }

            for(int ix=70;ix < 185+1;ix++){
                backBuffer[38 * 256 + ix] = 0xFFFF;
                backBuffer[39 * 256 + ix] = 0xFFFF;
                backBuffer[152 * 256 + ix] = 0xFFFF;
                backBuffer[153 * 256 + ix] = 0xFFFF;
            }
        }

        if(((keysCurrent() & KEY_TOUCH) == 4096)){

            for(int y = 0;y < 192;y++){
                for(int x = 0;x < 256;x++){
                    if((y - touchXY.py)*(y - touchXY.py) + (x - touchXY.px)*(x - touchXY.px) <= 35){
                        if(40 <= y && y < 152 && 72 <= x && x < 184){
                            backBuffer[(y) * 256 + (x)] = 0xFFFF;
                        }
                    }
                }
            }

            backBuffer = (u16*)bgGetGfxPtr(bg);
            bgSetMapBase(bg, 8);
        }

        iprintf("\x1b[19;0H Button A: Start       ");
        iprintf("\x1b[20;0H Button B: Clear       ");

	}

    nnablart_validation_free_context(_context);

}
