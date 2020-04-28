import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MandelBrotImage {
    public static void saveImage(int[][] iterations, String outputFile){
        int width = iterations.length;
        int height = iterations[0].length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        File f;

        //calculate pixels
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if (iterations[x][y]==255){
                    img.setRGB(x, y, Integer.MAX_VALUE);
                }else{
                    //set rbg values
                    int a = 200; //alpha
                    int r = 0; //red
                    int g = iterations[x][y]; //green
                    int b = (255-iterations[x][y])/2;; //blue

                    int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel
                    img.setRGB(x, y, p);
                }
            }
        }
        //save image
        try{
            f = new File(outputFile + ".png");
            ImageIO.write(img, "png", f);
        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    }
}
