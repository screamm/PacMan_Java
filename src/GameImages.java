import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class GameImages {
    
    // Ökad bildstorlek för bättre visuellt uttryck
    private static final int IMAGE_SIZE = 40; // Större storlek för att matcha den nya banstorleken
    
    // Pacman-bilder i olika riktningar
    public static Image createPacmanRightImage(double mouthAngle) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Aktivera anti-aliasing för jämnare kanter
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Rita Pacman med ljusgul färg
        g2d.setColor(new Color(255, 255, 0)); // Ren gul
        
        // Räkna ut vinklar för öppning (i grader) - mer vågrät mun
        double startAngle = 30; // Större värde ger mer vågrät mun
        double arcAngle = 300; // Mindre värde ger större öppning
        
        // Rita en cirkel med en "bit" borttagen
        g2d.fill(new Arc2D.Double(2.0, 2.0, IMAGE_SIZE-4, IMAGE_SIZE-4, 
                startAngle, arcAngle, Arc2D.PIE));
        
        // Lägg till en subtil gradient för mer visuellt djup
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Double(IMAGE_SIZE/2, IMAGE_SIZE/2),
            IMAGE_SIZE/2,
            new float[] { 0.0f, 0.8f },
            new Color[] { new Color(255, 255, 100), new Color(255, 220, 0) }
        );
        g2d.setPaint(paint);
        g2d.fill(new Arc2D.Double(4.0, 4.0, IMAGE_SIZE-8, IMAGE_SIZE-8, 
                startAngle, arcAngle, Arc2D.PIE));
                
        g2d.dispose();
        return image;
    }
    
    public static Image createPacmanLeftImage(double mouthAngle) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(255, 255, 0)); // Ren gul
        
        // Mer vågrät mun
        double startAngle = 150;
        double arcAngle = 300;
        
        g2d.fill(new Arc2D.Double(2.0, 2.0, IMAGE_SIZE-4, IMAGE_SIZE-4, 
                startAngle, arcAngle, Arc2D.PIE));
        
        // Lägg till en subtil gradient för mer visuellt djup
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Double(IMAGE_SIZE/2, IMAGE_SIZE/2),
            IMAGE_SIZE/2,
            new float[] { 0.0f, 0.8f },
            new Color[] { new Color(255, 255, 100), new Color(255, 220, 0) }
        );
        g2d.setPaint(paint);
        g2d.fill(new Arc2D.Double(4.0, 4.0, IMAGE_SIZE-8, IMAGE_SIZE-8, 
                startAngle, arcAngle, Arc2D.PIE));
                
        g2d.dispose();
        return image;
    }
    
    public static Image createPacmanUpImage(double mouthAngle) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(255, 255, 0)); // Ren gul
        
        // Mer vågrät mun
        double startAngle = 240;
        double arcAngle = 300;
        
        g2d.fill(new Arc2D.Double(2.0, 2.0, IMAGE_SIZE-4, IMAGE_SIZE-4, 
                startAngle, arcAngle, Arc2D.PIE));
        
        // Lägg till en subtil gradient för mer visuellt djup
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Double(IMAGE_SIZE/2, IMAGE_SIZE/2),
            IMAGE_SIZE/2,
            new float[] { 0.0f, 0.8f },
            new Color[] { new Color(255, 255, 100), new Color(255, 220, 0) }
        );
        g2d.setPaint(paint);
        g2d.fill(new Arc2D.Double(4.0, 4.0, IMAGE_SIZE-8, IMAGE_SIZE-8, 
                startAngle, arcAngle, Arc2D.PIE));
                
        g2d.dispose();
        return image;
    }
    
    public static Image createPacmanDownImage(double mouthAngle) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(255, 255, 0)); // Ren gul
        
        // Mer vågrät mun
        double startAngle = 300;
        double arcAngle = 300;
        
        g2d.fill(new Arc2D.Double(2.0, 2.0, IMAGE_SIZE-4, IMAGE_SIZE-4, 
                startAngle, arcAngle, Arc2D.PIE));
        
        // Lägg till en subtil gradient för mer visuellt djup
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Double(IMAGE_SIZE/2, IMAGE_SIZE/2),
            IMAGE_SIZE/2,
            new float[] { 0.0f, 0.8f },
            new Color[] { new Color(255, 255, 100), new Color(255, 220, 0) }
        );
        g2d.setPaint(paint);
        g2d.fill(new Arc2D.Double(4.0, 4.0, IMAGE_SIZE-8, IMAGE_SIZE-8, 
                startAngle, arcAngle, Arc2D.PIE));
                
        g2d.dispose();
        return image;
    }
    
    // Generisk spökbild med anpassad färg
    public static Image createGhostImage(Color mainColor) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Kropp (övre halvan är en oval, nedre halvan är en rektangel med vågig botten)
        g2d.setColor(mainColor);
        
        // Rita huvuddelen av spöket (rektangel med rundade hörn)
        g2d.fillRoundRect(4, IMAGE_SIZE/2, IMAGE_SIZE-8, IMAGE_SIZE/2-4, 8, 8);
        
        // Rita den övre delen (oval)
        g2d.fillOval(4, 4, IMAGE_SIZE-8, IMAGE_SIZE/2+4);
        
        // Rita den vågiga botten
        int waveHeight = 4;
        int segments = 3;
        int segmentWidth = (IMAGE_SIZE-8) / segments;
        
        for (int i = 0; i < segments; i++) {
            int x = 4 + i * segmentWidth;
            g2d.fillArc(x, IMAGE_SIZE-waveHeight*2, segmentWidth, waveHeight*2, 0, 180);
        }
        
        // Ögon (vita)
        g2d.setColor(Color.WHITE);
        g2d.fillOval(IMAGE_SIZE/4-2, IMAGE_SIZE/3-2, 10, 10);
        g2d.fillOval(3*IMAGE_SIZE/4-8, IMAGE_SIZE/3-2, 10, 10);
        
        // Pupiller (blå)
        g2d.setColor(Color.BLUE);
        g2d.fillOval(IMAGE_SIZE/4, IMAGE_SIZE/3, 6, 6);
        g2d.fillOval(3*IMAGE_SIZE/4-6, IMAGE_SIZE/3, 6, 6);
        
        g2d.dispose();
        return image;
    }
    
    // Rött spöke (Blinky)
    public static Image createRedGhostImage() {
        return createGhostImage(new Color(255, 0, 0)); // Blinky - röd
    }
    
    // Rosa spöke (Pinky)
    public static Image createPinkGhostImage() {
        return createGhostImage(new Color(255, 182, 255)); // Pinky - rosa
    }
    
    // Blått spöke (Inky)
    public static Image createBlueGhostImage() {
        return createGhostImage(new Color(0, 255, 255)); // Inky - cyan
    }
    
    // Orange spöke (Clyde)
    public static Image createOrangeGhostImage() {
        return createGhostImage(new Color(255, 165, 0)); // Clyde - orange
    }
    
    // Cyan spöke (Inky)
    public static Image createCyanGhostImage() {
        return createGhostImage(new Color(0, 255, 255)); // Cyan färg
    }
    
    // Skrämt spöke
    public static Image createScaredGhostImage() {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Kropp (mörkblå)
        g2d.setColor(new Color(0, 0, 150));
        
        // Rita huvuddelen av spöket (rektangel med rundade hörn)
        g2d.fillRoundRect(4, IMAGE_SIZE/2, IMAGE_SIZE-8, IMAGE_SIZE/2-4, 8, 8);
        
        // Rita den övre delen (oval)
        g2d.fillOval(4, 4, IMAGE_SIZE-8, IMAGE_SIZE/2+4);
        
        // Rita den vågiga botten
        int waveHeight = 4;
        int segments = 3;
        int segmentWidth = (IMAGE_SIZE-8) / segments;
        
        for (int i = 0; i < segments; i++) {
            int x = 4 + i * segmentWidth;
            g2d.fillArc(x, IMAGE_SIZE-waveHeight*2, segmentWidth, waveHeight*2, 0, 180);
        }
        
        // Ögon (vita)
        g2d.setColor(Color.WHITE);
        g2d.fillOval(IMAGE_SIZE/4-2, IMAGE_SIZE/3-2, 10, 10);
        g2d.fillOval(3*IMAGE_SIZE/4-8, IMAGE_SIZE/3-2, 10, 10);
        
        // Mun (vit)
        g2d.setColor(Color.WHITE);
        int mouthY = IMAGE_SIZE/2 + 4;
        g2d.drawLine(IMAGE_SIZE/3, mouthY, IMAGE_SIZE/3+2, mouthY+2);
        g2d.drawLine(IMAGE_SIZE/3+2, mouthY+2, IMAGE_SIZE/3+4, mouthY);
        g2d.drawLine(IMAGE_SIZE/3+4, mouthY, IMAGE_SIZE/3+6, mouthY+2);
        g2d.drawLine(IMAGE_SIZE/3+6, mouthY+2, IMAGE_SIZE/3+8, mouthY);
        g2d.drawLine(IMAGE_SIZE/3+8, mouthY, IMAGE_SIZE/3+10, mouthY+2);
        
        g2d.dispose();
        return image;
    }
    
    // Vägg-bild
    public static Image createWallImage() {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Skapa en mörkblå färg för väggarna (mer lik originalspelet)
        Color darkBlue = new Color(0, 0, 128);
        Color lightBlue = new Color(0, 0, 255);
        
        // Fyll hela rutan med mörkblå
        g2d.setColor(darkBlue);
        g2d.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
        
        // Lägg till en ljusblå kant för att ge väggarna ett glödande utseende
        g2d.setColor(lightBlue);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRect(1, 1, IMAGE_SIZE-2, IMAGE_SIZE-2);
        
        // Lägg till en subtil gradient för mer djup
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(0, 0, 180, 150),
            IMAGE_SIZE, IMAGE_SIZE, new Color(0, 0, 100, 150)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(3, 3, IMAGE_SIZE-6, IMAGE_SIZE-6);
        
        g2d.dispose();
        return image;
    }
    
    // Mat-bild (pellet)
    public static Image createFoodImage() {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Skapa en liten vit prick i mitten (som i originalspelet)
        int dotSize = IMAGE_SIZE / 8; // Mindre prick
        int dotX = (IMAGE_SIZE - dotSize) / 2;
        int dotY = (IMAGE_SIZE - dotSize) / 2;
        
        // Rita en vit cirkel
        g2d.setColor(Color.WHITE);
        g2d.fillOval(dotX, dotY, dotSize, dotSize);
        
        // Lägg till en subtil glöd
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillOval(dotX - 1, dotY - 1, dotSize + 2, dotSize + 2);
        
        g2d.dispose();
        return image;
    }
    
    // Power Pellet-bild
    public static Image createPowerPelletImage() {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Skapa en större vit prick i mitten (som i originalspelet)
        int dotSize = IMAGE_SIZE / 3; // Större prick för power pellet
        int dotX = (IMAGE_SIZE - dotSize) / 2;
        int dotY = (IMAGE_SIZE - dotSize) / 2;
        
        // Rita en vit cirkel
        g2d.setColor(Color.WHITE);
        g2d.fillOval(dotX, dotY, dotSize, dotSize);
        
        // Lägg till en subtil glöd
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillOval(dotX - 2, dotY - 2, dotSize + 4, dotSize + 4);
        
        g2d.dispose();
        return image;
    }
    
    // Pac-Man-bild med olika munöppningar
    public static Image createPacManImage(int direction, boolean mouthOpen) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Skapa en gul cirkel för Pac-Man
        int pacManSize = (int)(IMAGE_SIZE * 0.8);
        int x = (IMAGE_SIZE - pacManSize) / 2;
        int y = (IMAGE_SIZE - pacManSize) / 2;
        
        // Gul färg för Pac-Man
        Color pacManColor = new Color(255, 255, 0);
        g2d.setColor(pacManColor);
        
        // Startvinkel och vinkelomfång för munnen
        int startAngle = 0;
        int arcAngle = mouthOpen ? 300 : 360; // Om munnen är öppen: 300 grader, annars hel cirkel
        
        // Justera startvinkel baserat på riktning
        if (mouthOpen) {
            switch (direction) {
                case 0: // Höger
                    startAngle = 330;
                    break;
                case 1: // Ner
                    startAngle = 60;
                    break;
                case 2: // Vänster
                    startAngle = 150;
                    break;
                case 3: // Upp
                    startAngle = 240;
                    break;
            }
        }
        
        // Rita Pac-Man-kroppen
        g2d.fillArc(x, y, pacManSize, pacManSize, startAngle, arcAngle);
        
        g2d.dispose();
        return image;
    }
    
    // Överlagrad metod för bakåtkompatibilitet
    public static Image createPacManImage(int direction) {
        return createPacManImage(direction, true); // Standard är öppen mun
    }
}