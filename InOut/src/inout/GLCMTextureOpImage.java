
package inout;

/**
 *
 * @author reena
 */

import com.sun.media.jai.widget.DisplayJAI;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.*;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.*;
import javax.media.jai.ImageLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;


public class GLCMTextureOpImage extends UntiledOpImage
{
        private int xoffset = 1;
        private int yoffset = 1;
        File ff=null;
        String fn=null;
        private double energy = 0, entropy = 0, mtCorrelation = 0, haralickCorrelation1 = 0, haralickCorrelation2 = 0, inverseDifferenceMoment = 0, inertia = 0, mtShade = 0, mtProminence = 0;

        public GLCMTextureOpImage(RenderedImage source, ImageLayout layout, Integer xoffset, Integer yoffset)
        {
                super(source, null, layout);
                this.xoffset = xoffset.intValue();
                this.yoffset = yoffset.intValue();
                
        }

          protected void computeImage(Raster[] srcarr, WritableRaster dst, Rectangle destRect)
        {
        try {
            Raster src = srcarr[0];
            Raster2 source = new Raster2(src);
            WritableRaster2 dest = new WritableRaster2(dst);
            int width = source.getWidth();
            int height = source.getHeight();
            double[][] glcm = new double[256][256];

            /* Implement op here */
            //tally up the pixel spatial relations into a GLCM
            for (int u = 0; u < width; u++) {
                for (int v = 0; v < height; v++) {
                    int u2 = u + xoffset;
                    int v2 = v + yoffset;
                    if (u2 >= 0 && u2 < width && v2 >= 0 && v2 < height) {
                        glcm[source.grey(u, v)][source.grey(u2, v2)]++;
                    }
                }
            }
            for (int u = 0; u < 256; u++) {
                for (int v = 0; v < 256; v++) {
                    glcm[u][v] /= (double) (width * height);
                }
            }

            //compute useful features
            double mtRowMean = 0.;
            double mtColumnMean = 0.;
            double mtRowSigmaSquared = 0.;
            double mtColumnSigmaSquared = 0.;
            double tempSum = 0.;
            double[] rowSum = new double[256];
            double[] columnSum = new double[256];
            double rowMean = 0.;
            double columnMean = 0.;
            double rowColumnMean = 0.;
            double tempProduct = 0.;
            double rowSigmaSquared = 0.;
            double columnSigmaSquared = 0.;

            energy = 0;
            entropy = 0;
            mtCorrelation = 0;
            haralickCorrelation1 = 0;
            haralickCorrelation2 = 0;
            inverseDifferenceMoment = 0;
            inertia = 0;
            mtShade = 0;
            mtProminence = 0;
            int i;
            int j;
            for (i = 0; i < 256; i++) {
                for (j = 0; j < 256; j++) {
                    
                    // the value is small, but if there is repetition, the GLCM entries will be large,
                    // and the squares of them will be even larger.
                    energy += glcm[i][j] * glcm[i][j];

                    // Entropy
                    if (glcm[i][j] > 0) {
                        entropy -= (double) glcm[i][j] * Math.log((double) glcm[i][j]);
                    }
                    mtRowMean += (double) glcm[i][j] * (double) i;
                    mtColumnMean += (double) glcm[i][j] * (double) j;
                    rowSum[i] += (double) glcm[i][j];
                    columnSum[j] += (double) glcm[i][j];

                    // Inverse Difference Moment: measure of local homogeneity
                    inverseDifferenceMoment += 1.0 / (1.0 + (i - j) * (i - j)) * (double) glcm[i][j];

                    // Inertia
                    inertia += (double) (i - j) * (i - j) * (double) glcm[i][j];
                }
            }

            for (i = 0; i < 256; i++) {
                rowMean += rowSum[i];
                columnMean += columnSum[i];
                mtRowSigmaSquared += ((double) i - mtRowMean) * ((double) i - mtRowMean) * rowSum[i];
                mtColumnSigmaSquared += ((double) i - mtColumnMean) * ((double) i - mtColumnMean) * columnSum[i];
            }
            rowMean /= 256.;
            columnMean /= 256.;
            rowColumnMean = rowMean * columnMean;

            for (i = 0; i < 256; i++) {
                rowSigmaSquared += (rowSum[i] - rowMean) * (rowSum[i] - rowMean);
                columnSigmaSquared += (columnSum[i] - columnMean) * (columnSum[i] - columnMean);
            }
            rowSigmaSquared /= (256. - 1.);
            columnSigmaSquared /= (256. - 1.);
         //    System.out.println("ROW::"+rowSigmaSquared);
              System.out.println("ROW::"+rowMean);
              System.out.println("Column::"+columnSigmaSquared);
          //    System.out.println("Column:"+columnMean);
                     ;
            for (i = 0; i < 256; i++) {
                for (j = 0; j < 256; j++) {
                    mtCorrelation += ((double) i - mtRowMean) * ((double) j - mtColumnMean) * (double) glcm[i][j];
                    tempSum = (double) (i + j) - mtRowMean - mtColumnMean;
                    tempProduct = tempSum * tempSum * tempSum * (double) glcm[i][j];
                    mtShade += tempProduct;
                    mtProminence += tempProduct * tempSum;

                    /* Correlation a la Haralick: interpretation 1 */
                    haralickCorrelation1 += (double) (i * j) * (double) glcm[i][j] - rowColumnMean;

                    /* Correlation a la Haralick: interpretation 2 */
                    haralickCorrelation2 += (double) (i * j) * (double) glcm[i][j];
                }
            }
            mtCorrelation /= Math.sqrt(mtRowSigmaSquared * mtColumnSigmaSquared);
            haralickCorrelation1 /= Math.sqrt(rowSigmaSquared * columnSigmaSquared);
            haralickCorrelation2 -= rowColumnMean;
            haralickCorrelation2 /= Math.sqrt(rowSigmaSquared * columnSigmaSquared);

            System.out.println("Energy: " + energy);
            System.out.println("Entropy: " + entropy);
             //  System.out.println("MTCorrelation: "+ mtColumnMean);
           //   System.out.println("MTCorrelation: "+ mtCorrelation);
          //  System.out.println("Haralick Correlation 1: " + haralickCorrelation1);
          //  System.out.println("Haralick Correlation 2: " + haralickCorrelation2);
            System.out.println("Local Homogeneity: " + inverseDifferenceMoment);
            
             //  System.out.println("Cluster Shade: "+ mtShade);
             // System.out.println("Cluster Promience: "+ mtProminence);
            //   gc.computeImage(rt, wr,r);
            //gc.computeImage(null, null,null);
            //file1 = "file1.png";
            //file2 = "file2.png";
          //  String st1=
            RBGInfo rr = new RBGInfo();
            //RenderedImage image1=JAI.create("fileload",st1);
             rr.handlepixels(file1);
             int b1=rr.brg;
            
            int c1=rr.con;
            rr.handlepixels(file2);
            int b2=rr.brg;
            
            int c2=rr.con;
            
            float diff=(float)(inverseDifferenceMoment-energy);
            float en=(float) (entropy - diff);
          // File f1=ff;
           //System.out.println("FILE:::"+f1.toString());
            
           File ffname=Features.ff();
           System.out.println("file by features in GLC======="+ffname.toString());
            //Checkfeature chf=new Checkfeature(b1,b2,c1,c2);
            KNN kn=new KNN(b1, b2, c1, c2);
             System.out.println("Function Before");           
          
               
        } catch (InterruptedException ex) {
            Logger.getLogger(GLCMTextureOpImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        public void  symmetry(PlanarImage image)
        {
        try {
            //PlanarImage img=JAI.create("fileload",str);
           
            float[] kernelMatrix = {-1, -2, -1, 0, 0, 0, 1, 2, 1};

            float scale = 2.0f;
            ParameterBlock pblk = new ParameterBlock();
            pblk.addSource(image);
            pblk.add(scale);
            pblk.add(scale);
            pblk.add(0.1f); //(target.getWidth()-1)*(scale));
            pblk.add(0.1f); //(target.getHeight()-1)*(scale));
            pblk.add(new InterpolationNearest());
            PlanarImage input = JAI.create("scale", pblk);
             //  detector.setObservation(dd); 
            //detector.setObservation(obs.createGraphics());
            //run the detector - symmetries are available from the returned object
            //SymmetryDetector.Symmetry symmetry = detector.detectSymmetry(image1);
            
            
             
            KernelJAI kernel = new KernelJAI(3, 3, kernelMatrix);
            kernel = KernelJAI.GRADIENT_MASK_SOBEL_HORIZONTAL;
            // Run the convolve operator, creating the processed image.
            PlanarImage output = JAI.create("convolve", input, kernel);
            boolean t = kernel.isHorizontallySymmetric();
            boolean t1 = kernel.isVerticallySymmetric();
            float[] ff = kernel.getHorizontalKernelData();
            int km=kernel.getBottomPadding();
            int x=kernel.getXOrigin();
            int y=kernel.getYOrigin();
            float m=kernel.getHeight();
           
            //System.out.println("\nX::"+x+" Y::"+m);
           // System.out.println("KK::"+km);
          // for (int i = 0; i < ff.length; i++) {
              //  System.out.println("ff::" + ff[i] + "fff:");
           // }
            JFrame frame = new JFrame();
            frame.setTitle("Symmetry");
            Container contentPane = frame.getContentPane();
            contentPane.setLayout(new BorderLayout());
            // Create an instance of DisplayJAI.
            DisplayJAI dj = new DisplayJAI(output);
            contentPane.add(new JScrollPane(dj), BorderLayout.CENTER);
                
            //frame.getContentPane().add(new DisplayTwoSynchronizedImages(input,output));
            frame.getContentPane().add(dj);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack(); // adjust the frame size using preferred dimensions.
            frame.setLocation(200, 100);
            frame.setSize(400, 400);
            frame.setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(GLCMTextureOpImage.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        }
      /*  public static void main(String args[])
        {
             Image img1 = null;
         JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int tmp = jfc.showOpenDialog(null);
        if(tmp == JFileChooser.APPROVE_OPTION)
        {
            try {
                File file = jfc.getSelectedFile();
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

                // RenderedOp p2=JAI.create("format", p);
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
                ImageIO.write(p, "png", new File("file1.png"));
                DisplayJAI dj1 = new DisplayJAI(p1);
                ImageIO.write(p1, "png", new File("file2.png"));
                // Add to the JFrame's ContentPane an instance of JScrollPane containing the
                // DisplayJAI instance.
                contentPane.add(new JScrollPane(dj), BorderLayout.WEST);
                contentPane.add(new JScrollPane(dj1), BorderLayout.EAST);
                // Add a text label with the image information.
                //contentPane.add(new JLabel,BorderLayout.SOUTH);
                // Set the closing operation so the application is finished.
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 400); // adjust the frame size.
                frame.setVisible(true);


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

                gc.computeImage(rt, wr, r);
                //gc.computeImage(null, null,null);
                /*   String st1="c:\\file1.png";
                String st2="c:\\file2.png";
                RBGInfo rr=new RBGInfo();
                //RenderedImage image1=JAI.create("fileload",st1);
                rr.handlepixels(st1);
                rr.handlepixels(st2);
                gc.symmetry(img);
            } catch (IOException ex) {
                Logger.getLogger(GLCMTextureOpImage.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        
          

        }    
            
            
            
            
        }*/
        String file1=null;
        String file2=null;
}
