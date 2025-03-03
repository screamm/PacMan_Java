import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class GameImages {
    
    // Ökad bildstorlek för bättre visuellt uttryck
    private static final int IMAGE_SIZE = 64; // Dubbelt så stor (från 32 till 64)
    
    // Pacman-bilder i olika riktningar
    public static Image createPacmanRightImage(double mouthAngle) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Aktivera anti-aliasing för jämnare kanter
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Rita Pacman med ljusgul färg
        g2d.setColor(new Color(255, 255, 0)); // Ren gul
        
        // Räkna ut vinklar för öppning (i grader)
        double startAngle = mouthAngle / 2.0;
        double arcAngle = 360.0 - mouthAngle;
        
        // Rita en cirkel med en "bit" borttagen
        g2d.fill(new Arc2D.Double(4.0, 4.0, IMAGE_SIZE-8, IMAGE_SIZE-8, 
                startAngle, arcAngle, Arc2D.PIE));
        
        // Lägg till en subtil gradient för mer visuellt djup
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Double(IMAGE_SIZE/2, IMAGE_SIZE/2),
            IMAGE_SIZE/2,
            new float[] { 0.0f, 0.8f },
            new Color[] { new Color(255, 255, 100), new Color(255, 220, 0) }
        );
        g2d.setPaint(paint);
        g2d.fill(new Arc2D.Double(8.0, 8.0, IMAGE_SIZE-16, IMAGE_SIZE-16, 
                startAngle, arcAngle, Arc2D.PIE));
                
        g2d.dispose();
        return image;
    }
    
    public static Image createPacmanLeftImage(double mouthAngle) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(255, 255, 0)); // Ren gul
        double startAngle = 180.0 - (mouthAngle / 2.0);
        double arcAngle = 360.0 - mouthAngle;
        
        g2d.fill(new Arc2D.Double(4.0, 4.0, IMAGE_SIZE-8, IMAGE_SIZE-8, 
                startAngle, arcAngle, Arc2D.PIE));
        
        // Lägg till en subtil gradient för mer visuellt djup
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Double(IMAGE_SIZE/2, IMAGE_SIZE/2),
            IMAGE_SIZE/2,
            new float[] { 0.0f, 0.8f },
            new Color[] { new Color(255, 255, 100), new Color(255, 220, 0) }
        );
        g2d.setPaint(paint);
        g2d.fill(new Arc2D.Double(8.0, 8.0, IMAGE_SIZE-16, IMAGE_SIZE-16, 
                startAngle, arcAngle, Arc2D.PIE));
        
        g2d.dispose();
        return image;
    }
    
    public static Image createPacmanUpImage(double mouthAngle) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(255, 255, 0)); // Ren gul
        double startAngle = 90.0 - (mouthAngle / 2.0);
        double arcAngle = 360.0 - mouthAngle;
        
        g2d.fill(new Arc2D.Double(4.0, 4.0, IMAGE_SIZE-8, IMAGE_SIZE-8, 
                startAngle, arcAngle, Arc2D.PIE));
        
        // Lägg till en subtil gradient för mer visuellt djup
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Double(IMAGE_SIZE/2, IMAGE_SIZE/2),
            IMAGE_SIZE/2,
            new float[] { 0.0f, 0.8f },
            new Color[] { new Color(255, 255, 100), new Color(255, 220, 0) }
        );
        g2d.setPaint(paint);
        g2d.fill(new Arc2D.Double(8.0, 8.0, IMAGE_SIZE-16, IMAGE_SIZE-16, 
                startAngle, arcAngle, Arc2D.PIE));
        
        g2d.dispose();
        return image;
    }
    
    public static Image createPacmanDownImage(double mouthAngle) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(255, 255, 0)); // Ren gul
        double startAngle = 270.0 - (mouthAngle / 2.0);
        double arcAngle = 360.0 - mouthAngle;
        
        g2d.fill(new Arc2D.Double(4.0, 4.0, IMAGE_SIZE-8, IMAGE_SIZE-8, 
                startAngle, arcAngle, Arc2D.PIE));
        
        // Lägg till en subtil gradient för mer visuellt djup
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Double(IMAGE_SIZE/2, IMAGE_SIZE/2),
            IMAGE_SIZE/2,
            new float[] { 0.0f, 0.8f },
            new Color[] { new Color(255, 255, 100), new Color(255, 220, 0) }
        );
        g2d.setPaint(paint);
        g2d.fill(new Arc2D.Double(8.0, 8.0, IMAGE_SIZE-16, IMAGE_SIZE-16, 
                startAngle, arcAngle, Arc2D.PIE));
        
        g2d.dispose();
        return image;
    }
    
    // Spök-bilder
    public static Image createGhostImage(Color mainColor) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Skapa en ljusare version av huvudfärgen för gradient
        Color lighterColor = lighten(mainColor, 0.3f);
        
        // Övre del (rundad rektangel) med gradient
        GradientPaint gradient = new GradientPaint(
            0, 0, lighterColor,
            0, IMAGE_SIZE, mainColor
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(4, 4, IMAGE_SIZE-8, (int)((IMAGE_SIZE/3)*2), IMAGE_SIZE/2, IMAGE_SIZE/2);
        
        // Undre del (vågig kant)
        int waveHeight = IMAGE_SIZE / 6;
        int numWaves = 3;
        int waveWidth = (IMAGE_SIZE - 8) / numWaves;
        
        // Skapa en "vågig" underkant för spöket
        Path2D path = new Path2D.Double();
        path.moveTo(4, (IMAGE_SIZE/3)*2);
        
        for (int i = 0; i < numWaves; i++) {
            double x1 = 4 + i * waveWidth;
            double x2 = 4 + (i + 0.5) * waveWidth;
            double x3 = 4 + (i + 1) * waveWidth;
            
            path.lineTo(x1, (IMAGE_SIZE/3)*2);
            path.lineTo(x2, (IMAGE_SIZE/3)*2 + waveHeight);
            path.lineTo(x3, (IMAGE_SIZE/3)*2);
        }
        
        path.lineTo(IMAGE_SIZE-4, (IMAGE_SIZE/3)*2);
        path.lineTo(IMAGE_SIZE-4, IMAGE_SIZE-4);
        path.lineTo(4, IMAGE_SIZE-4);
        path.closePath();
        
        g2d.fill(path);
        
        // Ögon (större och mer detaljerade)
        // Vit del av ögonen
        g2d.setColor(Color.WHITE);
        g2d.fillOval(IMAGE_SIZE/5, IMAGE_SIZE/3, IMAGE_SIZE/4, IMAGE_SIZE/4);
        g2d.fillOval(IMAGE_SIZE*3/5, IMAGE_SIZE/3, IMAGE_SIZE/4, IMAGE_SIZE/4);
        
        // Blå iris
        g2d.setColor(new Color(70, 130, 255));
        g2d.fillOval(IMAGE_SIZE/5 + IMAGE_SIZE/16, IMAGE_SIZE/3 + IMAGE_SIZE/16, 
                    IMAGE_SIZE/6, IMAGE_SIZE/6);
        g2d.fillOval(IMAGE_SIZE*3/5 + IMAGE_SIZE/16, IMAGE_SIZE/3 + IMAGE_SIZE/16, 
                    IMAGE_SIZE/6, IMAGE_SIZE/6);
        
        // Svart pupill
        g2d.setColor(Color.BLACK);
        g2d.fillOval(IMAGE_SIZE/5 + IMAGE_SIZE/10, IMAGE_SIZE/3 + IMAGE_SIZE/10, 
                    IMAGE_SIZE/12, IMAGE_SIZE/12);
        g2d.fillOval(IMAGE_SIZE*3/5 + IMAGE_SIZE/10, IMAGE_SIZE/3 + IMAGE_SIZE/10, 
                    IMAGE_SIZE/12, IMAGE_SIZE/12);
        
        // Ljusreflektion i ögonen (liten vit prick)
        g2d.setColor(Color.WHITE);
        g2d.fillOval(IMAGE_SIZE/5 + IMAGE_SIZE/9, IMAGE_SIZE/3 + IMAGE_SIZE/12, 
                    IMAGE_SIZE/30, IMAGE_SIZE/30);
        g2d.fillOval(IMAGE_SIZE*3/5 + IMAGE_SIZE/9, IMAGE_SIZE/3 + IMAGE_SIZE/12, 
                    IMAGE_SIZE/30, IMAGE_SIZE/30);
        
        g2d.dispose();
        return image;
    }
    
    // Vägg-bild
    public static Image createWallImage() {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Basstruktur (mörkare blå)
        Color darkBlue = new Color(0, 20, 80);
        g2d.setColor(darkBlue);
        g2d.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
        
        // Lägg till textur (tegelmönster)
        int brickHeight = IMAGE_SIZE / 4;
        int brickWidth = IMAGE_SIZE / 2;
        
        // Rita tegelstenar
        g2d.setColor(new Color(0, 50, 150)); // Ljusare blå
        
        for (int y = 0; y < IMAGE_SIZE; y += brickHeight) {
            int offset = (y % (brickHeight * 2) == 0) ? 0 : brickWidth / 2;
            
            for (int x = -brickWidth / 2; x < IMAGE_SIZE; x += brickWidth) {
                g2d.fillRect(x + offset, y, brickWidth - 2, brickHeight - 2);
            }
        }
        
        // Lägg till skugga och ljus för 3D-effekt
        Color highlight = new Color(30, 144, 255, 120); // Ljusblå halvgenomskinlig
        g2d.setColor(highlight);
        
        // Övre och vänster kant (ljus)
        g2d.fillRect(0, 0, IMAGE_SIZE, 2);
        g2d.fillRect(0, 0, 2, IMAGE_SIZE);
        
        Color shadow = new Color(0, 0, 40, 120); // Mörk halvgenomskinlig
        g2d.setColor(shadow);
        
        // Undre och höger kant (skugga)
        g2d.fillRect(0, IMAGE_SIZE - 2, IMAGE_SIZE, 2);
        g2d.fillRect(IMAGE_SIZE - 2, 0, 2, IMAGE_SIZE);
        
        g2d.dispose();
        return image;
    }
    
    // Matprickar (pellets)
    public static Image createFoodImage() {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Rita en liten gul cirkel med gradient
        int pelletsSize = IMAGE_SIZE / 6;
        
        // Skapa en radiell gradient för mer visuellt intresse
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Double(IMAGE_SIZE/2, IMAGE_SIZE/2),
            pelletsSize,
            new float[] { 0.0f, 1.0f },
            new Color[] { new Color(255, 255, 200), new Color(255, 255, 0) }
        );
        
        g2d.setPaint(paint);
        g2d.fillOval(
            IMAGE_SIZE/2 - pelletsSize/2, 
            IMAGE_SIZE/2 - pelletsSize/2, 
            pelletsSize, 
            pelletsSize
        );
        
        g2d.dispose();
        return image;
    }
    
    // Power pellet (stor matbit)
    public static Image createPowerPelletImage() {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Rita en större gul cirkel med pulsliknande effekt
        int powerPelletSize = IMAGE_SIZE / 3;
        
        // Använd en radiell gradient för glödeffekt
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Double(IMAGE_SIZE/2, IMAGE_SIZE/2),
            powerPelletSize,
            new float[] { 0.0f, 0.7f, 1.0f },
            new Color[] { 
                new Color(255, 255, 255), 
                new Color(255, 255, 0), 
                new Color(255, 200, 0, 100) 
            }
        );
        
        g2d.setPaint(paint);
        g2d.fillOval(
            IMAGE_SIZE/2 - powerPelletSize/2, 
            IMAGE_SIZE/2 - powerPelletSize/2, 
            powerPelletSize, 
            powerPelletSize
        );
        
        // Lägg till en yttre cirkel för glödeffekt
        g2d.setColor(new Color(255, 255, 0, 30));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(
            IMAGE_SIZE/2 - powerPelletSize/2 - 3, 
            IMAGE_SIZE/2 - powerPelletSize/2 - 3, 
            powerPelletSize + 6, 
            powerPelletSize + 6
        );
        
        g2d.dispose();
        return image;
    }
    
    // Hjälpmetod för att skapa ljusare versioner av färger
    private static Color lighten(Color color, float amount) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[1] = Math.max(0, Math.min(1, hsb[1] - amount)); // Minska mättnad
        hsb[2] = Math.min(1, hsb[2] + amount); // Öka ljusstyrka
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
}