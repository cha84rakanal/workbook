#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup(){
    ofBackground(0,0,0);

    ofTrueTypeFont::setGlobalDpi(108);

    max_iter = 100;
    epsilon = 0.000001;

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

    gui = new ofxDatGui(ofxDatGuiAnchor::TOP_LEFT);

    gui->addFRM();
    gui->addBreak()->setHeight(10.0f);
    gui->addLabel("LINK LENGTH");
    ofxDatGuiSlider* ll1 = gui->addSlider("LINK LENGTH 1", 5, 200);
    ofxDatGuiSlider* ll2 = gui->addSlider("LINK LENGTH 2", 5, 200);
    ofxDatGuiSlider* ll3 = gui->addSlider("LINK LENGTH 3", 5, 200);

    ll1->bind(l1);
    ll2->bind(l2);
    ll3->bind(l3);

    gui->addBreak()->setHeight(10.0f);
    gui->addLabel("JOIN ANGLE");
    ofxDatGuiSlider* ee1 = gui->addSlider("JOIN ANGLE 1", -180, 180);
    ofxDatGuiSlider* ee2 = gui->addSlider("JOIN ANGLE 2", -180, 180);
    ofxDatGuiSlider* ee3 = gui->addSlider("JOIN ANGLE 3", -180, 180);

    ee1->bind(theta1);
    ee2->bind(theta2);
    ee3->bind(theta3);

    gui->addBreak()->setHeight(10.0f);
    gui->addLabel("MAX ITERATION");
    ofxDatGuiSlider* m_iters = gui->addSlider("MAX ITERATION", 1, 200);

    m_iters->bind(max_iter);

    gui->onSliderEvent(this, &ofApp::onSliderEvent);

}

void ofApp::onSliderEvent(ofxDatGuiSliderEvent e){
    fk_update();
}

void ofApp::fk_update(){
    e1 = ofVec2f(l1 * cos(ofDegToRad(theta1)),l1 * sin(ofDegToRad(theta1)));
    e2 = ofVec2f(e1.x + l2 * cos(ofDegToRad(theta1 + theta2)),e1.y + l2 * sin(ofDegToRad(theta1 + theta2)));
    e3 = ofVec2f(e2.x + l3 * cos(ofDegToRad(theta1 + theta2 + theta3)),e2.y + l3 * sin(ofDegToRad(theta1 + theta2 + theta3)));
}

//http://jsdo.it/edo_m18/c5vn
//https://qiita.com/keitanxkeitan/items/d16c95635c1087a91036
//https://mukai-lab.org/content/CcdParticleInverseKinematics.pdf
//http://www.thothchildren.com/chapter/5b4e209a103f2f316871121a
void ofApp::ccd(ofVec2f target){

//step1
    theta3 += (ofRadToDeg(atan2((target-e2).y,(target-e2).x)) - ofRadToDeg(atan2((e3-e2).y,(e3-e2).x)));

    if(theta3 < -180)
        theta3 += 360;
    if(theta3 > 180)
        theta3 -= 360;

    fk_update();

//step2
    theta2 += (ofRadToDeg(atan2((target-e1).y,(target-e1).x)) - ofRadToDeg(atan2((e3-e1).y,(e3-e1).x)));
    if(theta2 < -180)
        theta2 += 360;
    if(theta2 > 180)
        theta2 -= 360;
    
    fk_update();

//step3
    theta1 += (ofRadToDeg(atan2((target-o).y,(target-o).x)) - ofRadToDeg(atan2((e3-o).y,(e3-o).x)));
    if(theta1 < -180)
        theta1 += 360;
    if(theta1 > 180)
        theta1 -= 360;
    
    fk_update();

    //e2+e3 -> e2 + target theta3
    //e1+e3 -> e1 + target theta2
    //o +e3 -> o  + target theta1
}

//--------------------------------------------------------------
void ofApp::update(){

}

//--------------------------------------------------------------
void ofApp::draw(){
    ofBackground(0,0,0);

    ofPushMatrix();
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

        ofSetColor(ofColor(255,0,0));
        ofDrawCircle(mouse_pos.x,mouse_pos.y,5);
    ofPopMatrix();
    
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

    mouse_pos.x = x - ofGetWidth()/2;
    mouse_pos.y = ofGetHeight()/2 - y;

    if(!(x < gui->getWidth() && y < gui->getHeight()))
        for(int i = 0; i < max_iter; i++){
            ccd(mouse_pos);

            if((e3 - mouse_pos).length() < epsilon){
                break;
            }
        }
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
