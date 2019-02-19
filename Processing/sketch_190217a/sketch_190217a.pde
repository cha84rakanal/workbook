import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
//JPEG RGB Only
PImage loadPict(String filename){
    try {
        byte bytes[] = loadBytes(filename);
        if (bytes == null) {
          return null;
        } else {
          Image awtImage = new ImageIcon(bytes).getImage();
 
          PImage image = new PImage(awtImage);
          if (image.width == -1) {
            System.err.println("The file " + filename +
                               " contains bad image data, or may not be an image.");
          }
          return image;
        }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
}

//https://qiita.com/clomie/items/d73f6c351f8fe56bba4f
PImage byteImage(byte[] bitstream){
  
  //DQT 25-89
  //DQT 94-158
  
  //DHT 231-392
  //DHT 447-608
  
  //for(int i = 0;i < 64;i ++){
  //  bitstream[25+i] = 0;//(byte)random(0,255);
  //}
  
  //for(int i = 0;i < 64;i ++){
  // bitstream[94+i] = 0;//(byte)random(0,255);
  //}
  //bitstream[(int)random(25,88)] = (byte)random(0,128);
  //bitstream[(int)random(25,88)] = (byte)random(0,128);
  //int random(0,100);
  
  bitstream[(int)random(232,390)] = 1;//(byte)random(0,4);
  //bitstream[(int)random(447,608)] = (byte)random(0,4);
  //bitstream[(int)random(25,89)] = (byte)random(0,255);
  try{
  Image awtImage = new ImageIcon(bitstream).getImage();
  PImage image = new PImage(awtImage);
  return image; //<>//
  }catch(Exception e){
      return new PImage();
  }
  }

PImage test;

byte jpg_bytes[];
    
void setup(){
   size(600,600);
   frameRate(30);
   //test = loadPict("test.jpg");
   jpg_bytes = loadBytes("test.jpg");
   //println(jpg_bytes);
   test = byteImage(jpg_bytes);
}

void draw(){
  background(0);
  test = byteImage(jpg_bytes);
  image(test,0,0);
}