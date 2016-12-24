/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inout;


import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author reena
 */
public class RGBInfo {
 int Red = 0;
    int Blue = 0;
    int Green = 0;
    int white=0;
    int black=0;
    int brg=0;
    int grey=0;
    int con=0;
    public static void main(String arg[])
    {
        RGBInfo r =new RGBInfo();
        try
        {
         r.handlepixels();
        }
        catch(Exception e)
        {
            
        }
    }
    public void handlepixels()//Image img, final int x, final int y, final int w, final int h) 
    {
        try {
            Image img = null;
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int tmp = jfc.showOpenDialog(null);
            //System.out.println("HIII::"+str);
            if (tmp == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                
                ImageIcon ii = new ImageIcon(file + "");
                img = ii.getImage();
            }

            final int w = img.getWidth(null);
            final int h = img.getHeight(null);
            int[] pixels = new int[img.getWidth(null) * img.getHeight(null)];
            PixelGrabber pg = new PixelGrabber(img, 0, 0, img.getWidth(null), img.getHeight(null), pixels, 0, img.getWidth(null));
            try {
                pg.grabPixels();
            } catch (InterruptedException e) {
                System.err.println("interrupted waiting for pixels!");
                return;
            }
            if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
                System.err.println("image fetch aborted or errored");
                return;
            }

            final int[] tmp1 = pixels;

            Thread t = new Thread(new Runnable() {

                public void run() {
                    for (int j = 0; j < h; j++) {
                        for (int i = 0; i < w; i++) {
                            final int ii = i;
                            final int jj = j;
                            handlesinglepixel(ii, jj, tmp1[jj * w + ii]);
                        }
                    }
                }
            });
            t.start();
            t.join();
            System.out.println("Red: " + Red + " Blue: " + Blue + " Green: " + Green + " white: " + white + " Black::" + black + " Brightness::" + brg + " CC::" + con);
               JOptionPane.showMessageDialog(null,"Red: " + Red + " Blue: "+ Blue +" Green: "+Green);
        } catch (InterruptedException ex) {
            Logger.getLogger(RGBInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
     //   JOptionPane.showMessageDialog(null,"Red: " + Red + " Blue: "+ Blue +" Green: "+Green);
        
    }
    
    public void handlesinglepixel(int x, int y, int pixel) 
	{
            int alpha = (pixel >> 24) & 0xff;
            int red   = (pixel >> 16) & 0xff;
            int green = (pixel  >>  8) & 0xff;
            int blue  = (pixel      ) & 0xff;   
            int wh=(pixel >> 255)& 0xff;
              
            if((red != 0) && (red != 255)) Red++;
           if((green != 0) && (green != 255)) Green++;
            if((blue != 0) && (blue != 255)) Blue++;
            if((red==0)&&(green==0)&&(blue==0))black++;   
            if((red==255)&&(green==255)&&(blue==255))white++;
            brg=(int) Math.sqrt(red *red * .241 + green * green * .691 + blue * blue * .068);
       
            con=(int)(((red * 299) + (green * 587) + (blue * 114)) / 1000);


            
 	}
   
}
