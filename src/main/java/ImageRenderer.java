import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageRenderer extends JPanel {

    private Image image;
    private int x;
    private int y;

    public ImageRenderer() {
        setPreferredSize(new Dimension(400, 300));
    }

    public void showImage(String path, int x, int y, double width, double height) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            Image scaledImg = img.getScaledInstance((int) width, (int) height, Image.SCALE_SMOOTH);
            setImage(scaledImg, x, y);
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen '" + path + "': " + e.getMessage());
        }
    }

    public void setImage(Image image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, x, y, this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Image Renderer");
            ImageRenderer renderer = new ImageRenderer();
            frame.add(renderer);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            // Ejemplo de uso
            String imagePath = "sword.png";
            renderer.showImage(imagePath, 50, 50, 300, 200);
        });
    }
}
