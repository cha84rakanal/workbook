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
            System.err.println("The file " + filename + " contains bad image data, or may not be an image.");
          }
          return image;
        }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
}

boolean DEBUG_FLAG = false;
boolean MIN_FLAG = true;

//https://qiita.com/clomie/items/d73f6c351f8fe56bba4f
//https://www.setsuki.com/hsp/ext/jpg.htm
PImage byteImage(byte[] bitstream){
  
  ArrayList<Integer> start = new ArrayList<Integer>();
  ArrayList<Integer> end   = new ArrayList<Integer>();
  
  boolean image_start = false;
  
  for(int i = 0;i < bitstream.length;i++){
    if(bitstream[i] == (byte)0xFF){
      if(image_start == false && bitstream[i + 1] == (byte)0xDB){
        if(DEBUG_FLAG){
          print("DQT maker ");
          println(Integer.valueOf(String.format("%02x%02x",bitstream[i + 2],bitstream[i + 3]),16) + " bytes");
        }
        if(MIN_FLAG && Integer.valueOf(String.format("%02x%02x",bitstream[i + 2],bitstream[i + 3]),16) <= 33){
          continue;
        }
        start.add(i + 5);
        end.add(i + 2 + Integer.valueOf(String.format("%02x%02x",bitstream[i + 2],bitstream[i + 3]),16) - 1);
      }
      if(image_start == false && bitstream[i + 1] == (byte)0xC4){
        if(DEBUG_FLAG){
          print("DHT maker ");
          println(Integer.valueOf(String.format("%02x%02x",bitstream[i + 2],bitstream[i + 3]),16) + " bytes");
        }
        if(MIN_FLAG && Integer.valueOf(String.format("%02x%02x",bitstream[i + 2],bitstream[i + 3]),16) <= 33){
          continue;
        }
        start.add(i + 5);
        end.add(i + 2 + Integer.valueOf(String.format("%02x%02x",bitstream[i + 2],bitstream[i + 3]),16) - 1);
      }
      if(image_start == false && bitstream[i + 1] == (byte)0xDA){
        image_start = true;
        if(DEBUG_FLAG){
          println("SOS maker");
        //println(Integer.valueOf(String.format("%02x%02x",bitstream[i + 2],bitstream[i + 3]),16) + " bytes");
        }
      }
      if(bitstream[i + 1] == (byte)0xD9){
        if(DEBUG_FLAG){
          println("EOI maker");
        }
      }
      //println("maker");
    }
  }
  
  for(int i = 0; i < start.size() ;i ++){
  //  println(start.get(i) + "," +  end.get(i));
    bitstream[(int)random(start.get(i),end.get(i))] = (byte)random(0,255);
  }
  
  //31byteしかないところはsun.awt.image.ImageFormatException: Bogus Huffman table definitionになる
  //bitstream[(int)random(start.get(1),end.get(1))] = (byte)random(0,255);
  //bitstream[(int)random(start.get(3),end.get(3))] = (byte)random(0,255);
  //bitstream[(int)random(start.get(4),end.get(4))] = (byte)random(0,255);
  //bitstream[(int)random(start.get(5),end.get(5))] = (byte)random(0,255);
  
  //DQT FF,DB,size,size,Class識別子,data-
  //サイズはffdbを抜いた長さ
  
  //DHT FF,C4,size,size,Class識別子,data-
  //サイズはffc4を抜いた長さ
  
  //FFDA以降はデータなので自由,FF含めた先頭5バイトはだめ
  //FFD9で終了
  
  PImage image;
  Image awtImage = new ImageIcon(bitstream).getImage();
  image = new PImage(awtImage);
    
  if(image.width == -1){ //<>//
    return null;
  }
  
  return image;
}

PImage test;
PImage origin;

byte jpg_bytes[];

String filename = "test.jpg";
    
void setup(){
   size(600,600);
   frameRate(30);
   //test = loadPict("test.jpg");
   jpg_bytes = loadBytes(filename);
   //println(jpg_bytes);
   origin = loadPict(filename);
   test = byteImage(jpg_bytes.clone());
}

void draw(){
  background(0);
  PImage temp = byteImage(jpg_bytes.clone());
  if(temp == null){
    temp = origin;
  }
  image(temp,0,0,600,600);
}