#pragma once

#include "ofMain.h"
#include "ofxDatGui.h"

class ofApp : public ofBaseApp{

	public:
		void setup();
		void update();
		void draw();

		void keyPressed(int key);
		void keyReleased(int key);
		void mouseMoved(int x, int y );
		void mouseDragged(int x, int y, int button);
		void mousePressed(int x, int y, int button);
		void mouseReleased(int x, int y, int button);
		void mouseEntered(int x, int y);
		void mouseExited(int x, int y);
		void windowResized(int w, int h);
		void dragEvent(ofDragInfo dragInfo);
		void gotMessage(ofMessage msg);

		void onSliderEvent(ofxDatGuiSliderEvent e);

		void ccd(ofVec2f target);
		void fk_update();

		ofVec2f o;
		ofVec2f e1;
		ofVec2f e2;
		ofVec2f e3;

		ofVec2f mouse_pos;

		float theta1; //deg
		float theta2;
		float theta3;
		
		float l1;
		float l2;
		float l3;
		
		int max_iter; //最大試行回数
		double epsilon; //許容誤差

		ofxDatGui* gui;

};
