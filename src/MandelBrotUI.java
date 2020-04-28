import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MandelBrotUI extends JFrame {
    JFrame parent;
    QuickDrawPanel quickDrawPanel;

    public MandelBrotUI(String filePath) {
        super("File View Test Frame");
        setSize(1920, 1080);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        parent = this;
        quickDrawPanel = new QuickDrawPanel();

        Container c = getContentPane();

        JPanel north = new JPanel();
        north.setBackground(Color.GRAY);
        north.setForeground(Color.BLUE);
        c.add(north, "First");
        c.add(new JScrollPane(quickDrawPanel), "Center");
        loadImage(new File(filePath));
    }

    /**
     * ImageIO will read a bmp image in j2se 1.5+
     */
    private void loadImage(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch(IOException e) {
            System.out.println("read error: " + e.getMessage());
        }
        quickDrawPanel.setImage(image);
    }

}

class QuickDrawPanel extends JPanel {
    BufferedImage image;
    Dimension size = new Dimension();

    public QuickDrawPanel() { }

    public QuickDrawPanel(BufferedImage image) {
        this.image = image;
        setComponentSize();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

    public Dimension getPreferredSize() {
        return size;
    }

    public void setImage(BufferedImage bi) {
        image = bi;
        setComponentSize();
        repaint();
    }

    private void setComponentSize() {
        if(image != null) {
            size.width  = image.getWidth();
            size.height = image.getHeight();
            revalidate();  // signal parent/scrollpane
        }
    }
}
