/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inout;
import com.sun.media.jai.opimage.PhaseCRIF;
import com.sun.media.jai.widget.DisplayJAI;
import java.awt.*;
import java.awt.GridLayout;
import java.awt.GridLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.image.*;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.jai.*;
import javax.media.jai.ImageLayout;
import javax.media.jai.ImageLayout;
import javax.media.jai.widget.ScrollingImagePanel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.jdesktop.application.Application;


/**
 *
 * @author reena
 */
public class Features {
  static File file=null;
  static String fname=null;
  static int i=0;
public Features()
{
         Image img1 = null;
         String filename="";
          try {
         File ff=new File("Images");
         String str1[]=ff.list();
              System.out.println("FILE LIST LENGTH ::"+str1.length);
         for(int k=0;k<str1.length;k++)
         {
             filename="Images//"+str1[k];
             if(filename.equals("Images//Thumbs.db"))
             {
                 continue;
             }
             else
             {
 /*        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int tmp = jfc.showOpenDialog(null);
        if(tmp == JFileChooser.APPROVE_OPTION)
        {*/
           
                
                File ff2=new File(filename);
                 file = ff2;
                 System.out.println("File selected ------------"+file);
                 fname=file.getName();
                  System.out.println("File selected name ------------"+fname);
                System.out.println(file);
                ImageIcon ii = new ImageIcon(file + "");
                img1 = ii.getImage();


                String str = file.toString();


                PlanarImage img = JAI.create("fileload", str);
                //RenderedImage image=JAI.create(str, img,2);
                ParameterBlock pb1 = new ParameterBlock();
                int height;
                int width;
                height = img.getHeight();
                width = img.getWidth();
                float ww = width;
                float hh = height / 2;
                pb1.addSource(img);
                pb1.add(0.0f);
                pb1.add(0.0f);
                pb1.add(ww);
                pb1.add(hh);

                RenderedImage p = JAI.create("crop", pb1, null);

                 RenderedOp p2=JAI.create("format", p);
                // JAI.create("filestore",p2,"c:\\file1.jpg","JPEG",null);
                OperationRegistry or = JAI.getDefaultInstance().getOperationRegistry();
                ParameterBlock pb = new ParameterBlock();
                float h1 = height;
                pb.addSource(img);
                pb.add(0.0f);
                pb.add(hh);
                pb.add(ww);
                pb.add(h1 - hh);

                RenderedImage p1 = JAI.create("crop", pb, null);
                //RenderedOp p3=JAI.create("format", p1);
                //JAI.create("filestore",p3,"c:\\file2.jpg","JPEG",null);
                RenderedOp src = JAI.create("fileload", str);
                pb.addSource(src);
                JFrame j = new JFrame("Image Segmentation");
                JFrame frame = new JFrame();
                frame.setTitle("DisplayJAI: " + str);


                // Get the JFrame's ContentPane.
                Container contentPane = frame.getContentPane();
                contentPane.setLayout(new BorderLayout());
                // Create an instance of DisplayJAI.
                DisplayJAI dj = new DisplayJAI(p);
                ImageIO.write(p, "png", new File("temp/file"+i+".png"));
               
               String file1="temp/file"+i+".png";
                i=i+1;
                DisplayJAI dj1 = new DisplayJAI(p1);
                ImageIO.write(p1, "png", new File("temp/file"+i+".png"));
                String file2="temp/file"+i+".png";
                i=i+1;
                // Add to the JFrame's ContentPane an instance of JScrollPane containing the
                // DisplayJAI instance.
                contentPane.add(new JScrollPane(dj), BorderLayout.PAGE_START);
                contentPane.add(new JScrollPane(dj1), BorderLayout.CENTER);
                // Add a text label with the image information.
                //contentPane.add(new JLabel,BorderLayout.SOUTH);
                // Set the closing operation so the application is finished.
              //  frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
               // frame.setSize(500, 600); // adjust the frame size.
             //   frame.setVisible(true);


                //Raster rs;
                SampleModel sm = img.getSampleModel();
                int bb = sm.getNumBands();
                System.out.println("No of Bands:" + bb);
                System.out.println("Height::" + height);
                System.out.println("Width::" + width);

                DataBuffer db = new DataBufferByte(width);
                ColorModel cm = PlanarImage.createColorModel(sm);
                WritableRaster wr = Raster.createBandedRaster(1, width, height, bb, new Point(0, 0));
                TiledImage ti = new TiledImage(0, 0, height, width, 0, 0, sm, cm);
                ti.setData(wr);

                Raster rrt = Raster.createBandedRaster(0, width, height, bb, new Point(0, 0));
                // Raster rst= Raster.createRaster(sm, db, new Point(100,100));
                Raster[] rt = new Raster[10];
                rt[0] = rrt;
                //System.out.println("Raster"+rt[0].toString());
                ImageLayout iml = new ImageLayout();
                // rt[1]=rrt;
                GLCMTextureOpImage gc = new GLCMTextureOpImage(img, iml, 1, -1);

                Rectangle r = new Rectangle(bb, bb, width, height);
                gc.file1=file1;
                gc.file2=file2;
                gc.computeImage(rt, wr, r);
               
                //gc.computeImage(null, null,null);
                /*   String st1="c:\\file1.png";
                String st2="c:\\file2.png";
                RBGInfo rr=new RBGInfo();
                //RenderedImage image1=JAI.create("fileload",st1);
                rr.handlepixels(st1);
                rr.handlepixels(st2);*/
               // gc.symmetry(img);
             }
              }
                  } catch (Exception ex) {
                Logger.getLogger(GLCMTextureOpImage.class.getName()).log(Level.SEVERE, null, ex);
            }
             
          
       
       JOptionPane.showMessageDialog(null, "Prcess Completed!!!!");   
}
public static File ff()
{
    return file;
}
}
