#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup(){
    ofBackground(0,0,0);

    l1 = 150;
    l2 = 150;
    l3 = 150;

    theta1 = 10;
    theta2 = 10;
    theta3 = 10;

    o = ofVec2f(0,0);
    e1 = ofVec2f(l1 * cos(ofDegToRad(theta1)),l1 * sin(ofDegToRad(theta1)));
    e2 = ofVec2f(e1.x + l2 * cos(ofDegToRad(theta1 + theta2)),e1.y + l2 * sin(ofDegToRad(theta1 + theta2)));
    e3 = ofVec2f(e2.x + l3 * cos(ofDegToRad(theta1 + theta2 + theta3)),e2.y + l3 * sin(ofDegToRad(theta1 + theta2 + theta3)));
}

//--------------------------------------------------------------
void ofApp::update(){

}

//--------------------------------------------------------------
void ofApp::draw(){
    ofBackground(0,0,0);

    ofTranslate(ofGetWidth()/2, ofGetHeight()/2);
    ofScale(1,-1);

    ofSetLineWidth(2);
    ofNoFill();
    
    ofSetColor(ofColor(255,255,255));
    
    ofDrawCircle(o.x,o.y,5);
    ofDrawLine(o.x,o.y,e1.x,e1.y);

    ofDrawCircle(e1.x,e1.y,5);
    ofDrawLine(e1.x,e1.y,e2.x,e2.y);

    ofDrawCircle(e2.x,e2.y,5);
    ofDrawLine(e2.x,e2.y,e3.x,e3.y);

    ofDrawCircle(e3.x,e3.y,5);
    
}

//--------------------------------------------------------------
void ofApp::keyPressed(int key){

}

//--------------------------------------------------------------
void ofApp::keyReleased(int key){

}

//--------------------------------------------------------------
void ofApp::mouseMoved(int x, int y ){

}

//--------------------------------------------------------------
void ofApp::mouseDragged(int x, int y, int button){

}

//--------------------------------------------------------------
void ofApp::mousePressed(int x, int y, int button){
    std::cout << x - ofGetWidth()/2 << "," << ofGetHeight()/2 - y << std::endl;
    
}

//--------------------------------------------------------------
void ofApp::mouseReleased(int x, int y, int button){

}

//--------------------------------------------------------------
void ofApp::mouseEntered(int x, int y){

}

//--------------------------------------------------------------
void ofApp::mouseExited(int x, int y){

}

//--------------------------------------------------------------
void ofApp::windowResized(int w, int h){

}

//--------------------------------------------------------------
void ofApp::gotMessage(ofMessage msg){

}

//--------------------------------------------------------------
void ofApp::dragEvent(ofDragInfo dragInfo){ 

}
