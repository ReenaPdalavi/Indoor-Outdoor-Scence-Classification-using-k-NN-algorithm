// Source File Name:   ImageDisplay.java
package inout;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class ImageDisplay extends JComponent
    implements MouseListener, MouseMotionListener
{

    private synchronized void initialize()
    {
        if(source == null)
            return;
        componentWidth = source.getWidth();
        componentHeight = source.getHeight();
        setPreferredSize(new Dimension(componentWidth, componentHeight));
        sampleModel = source.getSampleModel();
        colorModel = source.getColorModel();
        if(colorModel == null)
        {
            colorModel = PlanarImage.createColorModel(sampleModel);
            if(colorModel == null)
                throw new IllegalArgumentException("no color model");
        }
        minTileX = source.getMinTileX();
        maxTileX = (source.getMinTileX() + source.getNumXTiles()) - 1;
        minTileY = source.getMinTileY();
        maxTileY = (source.getMinTileY() + source.getNumYTiles()) - 1;
        tileWidth = source.getTileWidth();
        tileHeight = source.getTileHeight();
        tileGridXOffset = source.getTileGridXOffset();
        tileGridYOffset = source.getTileGridYOffset();
    }

    public ImageDisplay()
    {
        originX = 0;
        originY = 0;
        shift_x = 0;
        shift_y = 0;
        odometer = null;
        biop = null;
        brightnessEnabled = false;
        brightness = 0;
        source = null;
        lutData = new byte[256];
        for(int i = 0; i < 256; i++)
            lutData[i] = (byte)i;

        componentWidth = 64;
        componentHeight = 64;
        setPreferredSize(new Dimension(componentWidth, componentHeight));
        setOrigin(0, 0);
        setBrightnessEnabled(true);
    }

    public ImageDisplay(PlanarImage planarimage)
    {
        originX = 0;
        originY = 0;
        shift_x = 0;
        shift_y = 0;
        odometer = null;
        biop = null;
        brightnessEnabled = false;
        brightness = 0;
        source = planarimage;
        initialize();
        lutData = new byte[256];
        for(int i = 0; i < 256; i++)
            lutData[i] = (byte)i;

        setOrigin(0, 0);
        setBrightnessEnabled(true);
    }

    public ImageDisplay(int i, int j)
    {
        originX = 0;
        originY = 0;
        shift_x = 0;
        shift_y = 0;
        odometer = null;
        biop = null;
        brightnessEnabled = false;
        brightness = 0;
        source = null;
        lutData = new byte[256];
        for(int k = 0; k < 256; k++)
            lutData[k] = (byte)k;

        componentWidth = i;
        componentHeight = j;
        setPreferredSize(new Dimension(componentWidth, componentHeight));
        setOrigin(0, 0);
        setBrightnessEnabled(true);
    }

    public void set(PlanarImage planarimage)
    {
        source = planarimage;
        initialize();
        repaint();
    }

    public void set(PlanarImage planarimage, int i, int j)
    {
        source = planarimage;
        initialize();
        setOrigin(i, j);
    }

    public PlanarImage getImage()
    {
        return source;
    }

    public final JLabel getOdometer()
    {
        if(odometer == null)
        {
            odometer = new JLabel();
            odometer.setVerticalAlignment(0);
            odometer.setHorizontalAlignment(2);
            odometer.setText(" ");
            addMouseListener(this);
            addMouseMotionListener(this);
        }
        return odometer;
    }

    public final void setOrigin(int i, int j)
    {
        originX = -i;
        originY = -j;
        repaint();
    }

    public int getXOrigin()
    {
        return originX;
    }

    public int getYOrigin()
    {
        return originY;
    }

    public void setBounds(int i, int j, int k, int l)
    {
        Insets insets = getInsets();
        int i1;
        int j1;
        if(source == null)
        {
            i1 = k;
            j1 = l;
        } else
        {
            i1 = source.getWidth();
            j1 = source.getHeight();
            if(k < i1)
                i1 = k;
            if(l < j1)
                j1 = l;
        }
        componentWidth = i1 + insets.left + insets.right;
        componentHeight = j1 + insets.top + insets.bottom;
        super.setBounds(i + shift_x, j + shift_y, componentWidth, componentHeight);
    }

    public void setLocation(int i, int j)
    {
        shift_x = i;
        shift_y = j;
        super.setLocation(i, j);
    }

    private final int XtoTileX(int i)
    {
        return (int)Math.floor((double)(i - tileGridXOffset) / (double)tileWidth);
    }

    private final int YtoTileY(int i)
    {
        return (int)Math.floor((double)(i - tileGridYOffset) / (double)tileHeight);
    }

    private final int TileXtoX(int i)
    {
        return i * tileWidth + tileGridXOffset;
    }

    private final int TileYtoY(int i)
    {
        return i * tileHeight + tileGridYOffset;
    }

    private static final void debug(String s)
    {
        System.out.println(s);
    }

    private final byte clampByte(int i)
    {
        if(i > 255)
            return -1;
        if(i < 0)
            return 0;
        else
            return (byte)i;
    }

    private final void setBrightnessEnabled(boolean flag)
    {
        brightnessEnabled = flag;
        if(brightnessEnabled)
            biop = new AffineTransformOp(new AffineTransform(), 1);
        else
            biop = null;
    }

    public final boolean getBrightnessEnabled()
    {
        return brightnessEnabled;
    }

    public final void setBrightness(int i)
    {
        if(i != brightness && brightnessEnabled)
        {
            for(int j = 0; j < 256; j++)
                lutData[j] = clampByte(j + i);

            repaint();
        }
    }

    public synchronized void paintComponent(Graphics g)
    {
        Graphics2D graphics2d = null;
        if(g instanceof Graphics2D)
            graphics2d = (Graphics2D)g;
        else
            return;
        if(source == null)
        {
            graphics2d.setColor(getBackground());
            graphics2d.fillRect(0, 0, componentWidth, componentHeight);
            return;
        }
        int i = -originX;
        int j = -originY;
        Rectangle rectangle = g.getClipBounds();
        if(rectangle == null)
            rectangle = new Rectangle(0, 0, componentWidth, componentHeight);
        if(i > 0 || j > 0 || i < componentWidth - source.getWidth() || j < componentHeight - source.getHeight())
        {
            graphics2d.setColor(getBackground());
            graphics2d.fillRect(0, 0, componentWidth, componentHeight);
        }
        rectangle.translate(-i, -j);
        int k = XtoTileX(rectangle.x);
        k = Math.max(k, minTileX);
        k = Math.min(k, maxTileX);
        int l = XtoTileX((rectangle.x + rectangle.width) - 1);
        l = Math.max(l, minTileX);
        l = Math.min(l, maxTileX);
        int i1 = YtoTileY(rectangle.y);
        i1 = Math.max(i1, minTileY);
        i1 = Math.min(i1, maxTileY);
        int j1 = YtoTileY((rectangle.y + rectangle.height) - 1);
        j1 = Math.max(j1, minTileY);
        j1 = Math.min(j1, maxTileY);
        Insets insets = getInsets();
        for(int l1 = i1; l1 <= j1; l1++)
        {
            for(int k1 = k; k1 <= l; k1++)
            {
                int i2 = TileXtoX(k1);
                int j2 = TileYtoY(l1);
                Raster raster = source.getTile(k1, l1);
                if(raster != null)
                {
                    java.awt.image.DataBuffer databuffer = raster.getDataBuffer();
                    java.awt.image.WritableRaster writableraster = Raster.createWritableRaster(sampleModel, databuffer, null);
                    BufferedImage bufferedimage = new BufferedImage(colorModel, writableraster, colorModel.isAlphaPremultiplied(), null);
                    if(brightnessEnabled)
                    {
                        SampleModel samplemodel = sampleModel.createCompatibleSampleModel(raster.getWidth(), raster.getHeight());
                        java.awt.image.WritableRaster writableraster1 = RasterFactory.createWritableRaster(samplemodel, null);
                        BufferedImage bufferedimage1 = new BufferedImage(colorModel, writableraster1, colorModel.isAlphaPremultiplied(), null);
                        ByteLookupTable bytelookuptable = new ByteLookupTable(0, lutData);
                        LookupOp lookupop = new LookupOp(bytelookuptable, null);
                        lookupop.filter(bufferedimage, bufferedimage1);
                        graphics2d.drawImage(bufferedimage1, biop, i2 + i + insets.left, j2 + j + insets.top);
                    } else
                    {
                        AffineTransform affinetransform = AffineTransform.getTranslateInstance(i2 + i + insets.left, j2 + j + insets.top);
                        graphics2d.drawRenderedImage(bufferedimage, affinetransform);
                    }
                }
            }

        }

    }

    public final void mouseEntered(MouseEvent mouseevent)
    {
    }

    public final void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        Point point = mouseevent.getPoint();
        int i = mouseevent.getModifiers();
        if(odometer != null)
        {
            String s = " (" + point.x + ", " + point.y + ")";
            odometer.setText(s);
        }
    }

    public final void mouseReleased(MouseEvent mouseevent)
    {
        Point point = mouseevent.getPoint();
        if(odometer != null)
        {
            String s = " (" + point.x + ", " + point.y + ")";
            odometer.setText(s);
        }
    }

    public final void mouseClicked(MouseEvent mouseevent)
    {
    }

    public final void mouseMoved(MouseEvent mouseevent)
    {
        Point point = mouseevent.getPoint();
        if(odometer != null)
        {
            String s = " (" + point.x + ", " + point.y + ")";
            odometer.setText(s);
        }
    }

    public final void mouseDragged(MouseEvent mouseevent)
    {
        mousePressed(mouseevent);
    }

    protected PlanarImage source;
    protected SampleModel sampleModel;
    protected ColorModel colorModel;
    protected int minTileX;
    protected int maxTileX;
    protected int minTileY;
    protected int maxTileY;
    protected int tileWidth;
    protected int tileHeight;
    protected int tileGridXOffset;
    protected int tileGridYOffset;
    protected int originX;
    protected int originY;
    protected int shift_x;
    protected int shift_y;
    protected JLabel odometer;
    protected int componentWidth;
    protected int componentHeight;
    protected BufferedImageOp biop;
    protected boolean brightnessEnabled;
    protected int brightness;
    protected byte lutData[];
}