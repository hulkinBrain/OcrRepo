import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by soory on 11-Dec-15.
 */


public class testOutStuff {
    File convImg = new File ("E:\\downloads\\court.png");
    //File convImg = new File ("d:/test.png");
    BufferedImage bImg;
    BufferedImage grayImg;
    BufferedImage grayImgCopy;

    File grayFile;

    void convertToGray(){
        try{
            bImg = ImageIO.read(convImg);


            grayImg = new BufferedImage(bImg.getWidth(), bImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            grayImg.createGraphics().drawImage(bImg, 0, 0, Color.WHITE, null);
            grayFile = new File ("D:\\grey.png");
            ImageIO.write(grayImg,"png",grayFile);

        }
        catch(IOException ie){}
        System.out.println("Done!");
    }
    void extract(){
        Color imgC, imgC1, imgC2, imgCheckPixel;
        Color white = new Color(255,255,255);
        Color black = new Color(0,0,0);
        int check = 0;
        for(int x=0; x<grayImg.getWidth(); x++){
            for(int y=0; y<grayImg.getHeight(); y++){
                imgC = new Color(grayImg.getRGB(x,y));
                if( imgC.getRed() > 230){
                    grayImg.setRGB(x, y, white.getRGB());
                }
            }
        }
        /*for(int x=0; x<grayImg.getWidth();x++){
            for(int y=1; y<grayImg.getHeight(); y++){
                imgC = new Color(grayImg.getRGB(x,y));
                imgC1 = new Color(grayImg.getRGB(x, y-1));
                if(imgC.getRed() == 255 && imgC1.getRed() < 230){
                    if(y+4 < grayImg.getHeight())
                        imgCheckPixel = new Color(grayImg.getRGB(x, y+4));
                    else
                        imgCheckPixel = new Color(grayImg.getRGB(x, y));
                    if(imgCheckPixel.getRed() <= 230){
                        grayImg.setRGB(x, y, imgCheckPixel.getRGB());
                    }

                }
            }
        }*/
        /*for(int y=0; y<grayImg.getHeight();y++){
            for(int x=1; x<grayImg.getWidth(); x++){
                imgC = new Color(grayImg.getRGB(x,y));
                imgC1 = new Color(grayImg.getRGB(x-1,y));
                if(imgC.getRed() == 255 && imgC1.getRed() <= 150){
                    if(x+2 < grayImg.getWidth())
                        imgCheckPixel = new Color(grayImg.getRGB(x+2, y));
                    else
                        imgCheckPixel = new Color(grayImg.getRGB(x,y));
                    if(imgCheckPixel.getRed() <= 150){
                        grayImg.setRGB(x,y , imgCheckPixel.getRGB());
                    }

                }
            }
        }*/
        /*for(int x=0; x<grayImg.getWidth(); x++) {
            for (int y = 0; y < grayImg.getHeight(); y++) {
                imgC = new Color(grayImg.getRGB(x, y));
                if ((y - 1) >= 0 && (y + 5) <= grayImg.getHeight()) {
                    imgC1 = new Color(grayImg.getRGB(x, y - 1));
                    if (imgC1.getRed() == 255) {
                        for (int i = y + 1; i < 5; i++) {
                            imgC1 = new Color(grayImg.getRGB(x, i));
                            if (imgC1.getRed() <= 150) {
                                grayImg.setRGB(x, y, imgC.getRGB());
                            }
                        }
                    }
                }
            }
        }*/
        try{
            ImageIO.write(grayImg,"png",grayFile);
        }
        catch(IOException ie){}
    }
    void deNoise(){

    }
    public static void main(String[] args) {
        testOutStuff obj = new testOutStuff();
        obj.convertToGray();
        obj.extract();
    }
}
