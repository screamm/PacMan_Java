import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class GameImages {
    
    // Bildstorlek för spelelement
    private static final int IMAGE_SIZE = 30; // Standard bildstorlek för att passa banan bättre
    
    // ======================= PAC-MAN BILDER =======================
    
    // Skapa en Pac-Man som tittar åt höger med specifik munöppning
    public static Image createPacmanRightImage(double mouthAngle) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Aktivera anti-aliasing för jämnare kanter
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Klassisk klar gul färg för Pac-Man
        Color pacManColor = new Color(255, 255, 0);
        
        // Lägg till en ljus gradient för mer djup
        RadialGradientPaint gradient = new RadialGradientPaint(
            new Point2D.Float(IMAGE_SIZE/2, IMAGE_SIZE/2),
            IMAGE_SIZE/2,
            new float[] {0.0f, 0.8f},
            new Color[] {new Color(255, 255, 128), pacManColor}
        );
        
        g2d.setPaint(gradient);
        
        // Centrera Pac-Man i bilden
        int centerX = IMAGE_SIZE / 2;
        int centerY = IMAGE_SIZE / 2;
        int radius = (IMAGE_SIZE - 4) / 2; // Lite mindre än hela rutan
        
        // Beräkna vinklar för mun (i grader)
        double startAngle = mouthAngle / 2.0;
        double arcAngle = 360 - mouthAngle;
        
        // Rita en cirkel med en "bit" borttagen
        g2d.fill(new Arc2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2, 
                startAngle, arcAngle, Arc2D.PIE));
        
        g2d.dispose();
        return image;
    }
    
    // Skapa en Pac-Man som tittar åt vänster med specifik munöppning
    public static Image createPacmanLeftImage(double mouthAngle) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Klassisk klar gul färg för Pac-Man
        Color pacManColor = new Color(255, 255, 0);
        
        // Lägg till en ljus gradient för mer djup
        RadialGradientPaint gradient = new RadialGradientPaint(
            new Point2D.Float(IMAGE_SIZE/2, IMAGE_SIZE/2),
            IMAGE_SIZE/2,
            new float[] {0.0f, 0.8f},
            new Color[] {new Color(255, 255, 128), pacManColor}
        );
        
        g2d.setPaint(gradient);
        
        // Centrera Pac-Man i bilden
        int centerX = IMAGE_SIZE / 2;
        int centerY = IMAGE_SIZE / 2;
        int radius = (IMAGE_SIZE - 4) / 2;
        
        // För vänster riktning börjar munnen från motsatt håll (180 grader)
        double startAngle = 180 - mouthAngle / 2.0;
        double arcAngle = 360 - mouthAngle;
        
        g2d.fill(new Arc2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2, 
                startAngle, arcAngle, Arc2D.PIE));
        
        g2d.dispose();
        return image;
    }
    
    // Skapa en Pac-Man som tittar uppåt med specifik munöppning
    public static Image createPacmanUpImage(double mouthAngle) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Klassisk klar gul färg för Pac-Man
        Color pacManColor = new Color(255, 255, 0);
        
        // Lägg till en ljus gradient för mer djup
        RadialGradientPaint gradient = new RadialGradientPaint(
            new Point2D.Float(IMAGE_SIZE/2, IMAGE_SIZE/2),
            IMAGE_SIZE/2,
            new float[] {0.0f, 0.8f},
            new Color[] {new Color(255, 255, 128), pacManColor}
        );
        
        g2d.setPaint(gradient);
        
        // Centrera Pac-Man i bilden
        int centerX = IMAGE_SIZE / 2;
        int centerY = IMAGE_SIZE / 2;
        int radius = (IMAGE_SIZE - 4) / 2;
        
        // För uppåtriktad, vinkeln börjar från 270 grader
        double startAngle = 270 - mouthAngle / 2.0;
        double arcAngle = 360 - mouthAngle;
        
        g2d.fill(new Arc2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2, 
                startAngle, arcAngle, Arc2D.PIE));
        
        g2d.dispose();
        return image;
    }
    
    // Skapa en Pac-Man som tittar nedåt med specifik munöppning
    public static Image createPacmanDownImage(double mouthAngle) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Klassisk klar gul färg för Pac-Man
        Color pacManColor = new Color(255, 255, 0);
        
        // Lägg till en ljus gradient för mer djup
        RadialGradientPaint gradient = new RadialGradientPaint(
            new Point2D.Float(IMAGE_SIZE/2, IMAGE_SIZE/2),
            IMAGE_SIZE/2,
            new float[] {0.0f, 0.8f},
            new Color[] {new Color(255, 255, 128), pacManColor}
        );
        
        g2d.setPaint(gradient);
        
        // Centrera Pac-Man i bilden
        int centerX = IMAGE_SIZE / 2;
        int centerY = IMAGE_SIZE / 2;
        int radius = (IMAGE_SIZE - 4) / 2;
        
        // För nedåtriktad, vinkeln börjar från 90 grader
        double startAngle = 90 - mouthAngle / 2.0;
        double arcAngle = 360 - mouthAngle;
        
        g2d.fill(new Arc2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2, 
                startAngle, arcAngle, Arc2D.PIE));
        
        g2d.dispose();
        return image;
    }
    
    // ======================= SPÖKBILDER =======================
    
    /**
     * Skapar ett spöke med den klassiska designen
     * @param mainColor Huvudfärgen på spöket
     * @param eyeDirection Ögonriktning: 0=höger, 1=ner, 2=vänster, 3=upp
     * @return Bilden av spöket
     */
    public static Image createGhostImage(Color mainColor, int eyeDirection) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int padding = 2;
        
        // Spökens kropp
        int ghostWidth = IMAGE_SIZE - 2*padding;
        int ghostHeight = (int)(ghostWidth * 0.9); // Lite lägre höjd
        
        // Övre delen är en halvcirkel
        int topDiameter = ghostWidth;
        int topRadius = topDiameter / 2;
        int topX = padding;
        int topY = padding;
        
        // Rita övre halvcirkel
        g2d.setColor(mainColor);
        g2d.fillArc(topX, topY, topDiameter, topDiameter, 0, 180);
        
        // Rita rektangulär del av kroppen
        int rectHeight = ghostHeight / 2;
        int rectX = padding;
        int rectY = padding + topRadius;
        g2d.fillRect(rectX, rectY, ghostWidth, rectHeight);
        
        // Rita den "vågiga" bottenkanten (klassisk design med 3 vågor)
        int waveCount = 3;
        int waveWidth = ghostWidth / waveCount;
        int waveHeight = ghostHeight / 6;
        
        for (int i = 0; i < waveCount; i++) {
            int[] xPoints = {
                rectX + i*waveWidth,
                rectX + i*waveWidth + waveWidth/2,
                rectX + (i+1)*waveWidth
            };
            
            int[] yPoints = {
                rectY + rectHeight,
                rectY + rectHeight + waveHeight,
                rectY + rectHeight
            };
            
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
        
        // Rita ögonen (vita oval)
        g2d.setColor(Color.WHITE);
        int eyeWidth = ghostWidth / 4;
        int eyeHeight = (int)(eyeWidth * 1.2);
        int eyeSpacing = ghostWidth / 5;
        int eyeY = topY + topRadius - eyeHeight/4;
        int leftEyeX = padding + ghostWidth/2 - eyeSpacing - eyeWidth/2;
        int rightEyeX = padding + ghostWidth/2 + eyeSpacing - eyeWidth/2;
        
        g2d.fillOval(leftEyeX, eyeY, eyeWidth, eyeHeight);
        g2d.fillOval(rightEyeX, eyeY, eyeWidth, eyeHeight);
        
        // Rita pupillerna (blå)
        g2d.setColor(Color.BLUE);
        int pupilSize = eyeWidth / 2;
        
        // Olika ögonriktningar baserat på parameter
        int leftPupilX = leftEyeX + eyeWidth/2 - pupilSize/2;
        int rightPupilX = rightEyeX + eyeWidth/2 - pupilSize/2;
        int pupilY = eyeY + eyeHeight/2 - pupilSize/2;
        
        // Justera pupillernas position baserat på riktning
        if (eyeDirection == 0) { // Höger
            leftPupilX += pupilSize/2;
            rightPupilX += pupilSize/2;
        } else if (eyeDirection == 1) { // Ner
            pupilY += pupilSize/2;
        } else if (eyeDirection == 2) { // Vänster
            leftPupilX -= pupilSize/2;
            rightPupilX -= pupilSize/2;
        } else if (eyeDirection == 3) { // Upp
            pupilY -= pupilSize/2;
        }
        
        g2d.fillOval(leftPupilX, pupilY, pupilSize, pupilSize);
        g2d.fillOval(rightPupilX, pupilY, pupilSize, pupilSize);
        
        g2d.dispose();
        return image;
    }
    
    // Rött spöke (Blinky) - jagar Pac-Man direkt
    public static Image createRedGhostImage(int eyeDirection) {
        return createGhostImage(new Color(255, 0, 0), eyeDirection); // Klassisk röd
    }
    
    // Rosa spöke (Pinky) - försöker hamna framför Pac-Man
    public static Image createPinkGhostImage(int eyeDirection) {
        return createGhostImage(new Color(255, 192, 203), eyeDirection); // Ljusrosa
    }
    
    // Cyan spöke (Inky) - komplext rörelsemönster
    public static Image createCyanGhostImage(int eyeDirection) {
        return createGhostImage(new Color(0, 255, 255), eyeDirection); // Klassisk cyan
    }
    
    // Orange spöke (Clyde) - slumpmässigt beteende
    public static Image createOrangeGhostImage(int eyeDirection) {
        return createGhostImage(new Color(255, 165, 0), eyeDirection); // Klassisk orange
    }
    
    // Skrämt spöke (blått)
    public static Image createScaredGhostImage(boolean flashing) {
        Color ghostColor;
        if (flashing) {
            // Blinkar vitt/blått när powerup håller på att ta slut
            ghostColor = new Color(255, 255, 255);
        } else {
            // Normalt skrämt spöke - mörkblått
            ghostColor = new Color(0, 0, 196);
        }
        
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int padding = 2;
        
        // Spökens kropp
        int ghostWidth = IMAGE_SIZE - 2*padding;
        int ghostHeight = (int)(ghostWidth * 0.9);
        
        // Övre delen är en halvcirkel
        int topDiameter = ghostWidth;
        int topRadius = topDiameter / 2;
        int topX = padding;
        int topY = padding;
        
        // Rita övre halvcirkel
        g2d.setColor(ghostColor);
        g2d.fillArc(topX, topY, topDiameter, topDiameter, 0, 180);
        
        // Rita rektangulär del av kroppen
        int rectHeight = ghostHeight / 2;
        int rectX = padding;
        int rectY = padding + topRadius;
        g2d.fillRect(rectX, rectY, ghostWidth, rectHeight);
        
        // Rita den "vågiga" bottenkanten
        int waveCount = 3;
        int waveWidth = ghostWidth / waveCount;
        int waveHeight = ghostHeight / 6;
        
        for (int i = 0; i < waveCount; i++) {
            int[] xPoints = {
                rectX + i*waveWidth,
                rectX + i*waveWidth + waveWidth/2,
                rectX + (i+1)*waveWidth
            };
            
            int[] yPoints = {
                rectY + rectHeight,
                rectY + rectHeight + waveHeight,
                rectY + rectHeight
            };
            
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
        
        // Rita ögonen (vita)
        g2d.setColor(Color.WHITE);
        int eyeSize = ghostWidth / 5;
        int eyeSpacing = ghostWidth / 3;
        int eyeY = topY + topRadius;
        
        g2d.fillOval(padding + ghostWidth/2 - eyeSpacing - eyeSize/2, 
                     eyeY, eyeSize, eyeSize);
        g2d.fillOval(padding + ghostWidth/2 + eyeSpacing - eyeSize/2, 
                     eyeY, eyeSize, eyeSize);
        
        // Rita munnen (blå)
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2.0f));
        
        // Skapa en böjd mun (vagga)
        int mouthWidth = ghostWidth / 2;
        int mouthHeight = ghostHeight / 6;
        int mouthX = rectX + ghostWidth/2 - mouthWidth/2;
        int mouthY = rectY + 2;
        
        g2d.drawArc(mouthX, mouthY, mouthWidth, mouthHeight * 2, 0, -180);
        
        g2d.dispose();
        return image;
    }
    
    // Ätna spöken (ögon som återgår till spökgården)
    public static Image createEatenGhostImage(int eyeDirection) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Rita bara ögonen
        g2d.setColor(Color.WHITE);
        int eyeWidth = IMAGE_SIZE / 6;
        int eyeHeight = (int)(eyeWidth * 1.2);
        int eyeSpacing = IMAGE_SIZE / 4;
        int eyeY = IMAGE_SIZE / 2 - eyeHeight / 2;
        int leftEyeX = IMAGE_SIZE/2 - eyeSpacing - eyeWidth/2;
        int rightEyeX = IMAGE_SIZE/2 + eyeSpacing - eyeWidth/2;
        
        g2d.fillOval(leftEyeX, eyeY, eyeWidth, eyeHeight);
        g2d.fillOval(rightEyeX, eyeY, eyeWidth, eyeHeight);
        
        // Rita pupillerna (blå)
        g2d.setColor(Color.BLUE);
        int pupilSize = eyeWidth / 2;
        
        // Olika ögonriktningar baserat på parameter
        int leftPupilX = leftEyeX + eyeWidth/2 - pupilSize/2;
        int rightPupilX = rightEyeX + eyeWidth/2 - pupilSize/2;
        int pupilY = eyeY + eyeHeight/2 - pupilSize/2;
        
        // Justera pupillernas position baserat på riktning
        if (eyeDirection == 0) { // Höger
            leftPupilX += pupilSize/2;
            rightPupilX += pupilSize/2;
        } else if (eyeDirection == 1) { // Ner
            pupilY += pupilSize/2;
        } else if (eyeDirection == 2) { // Vänster
            leftPupilX -= pupilSize/2;
            rightPupilX -= pupilSize/2;
        } else if (eyeDirection == 3) { // Upp
            pupilY -= pupilSize/2;
        }
        
        g2d.fillOval(leftPupilX, pupilY, pupilSize, pupilSize);
        g2d.fillOval(rightPupilX, pupilY, pupilSize, pupilSize);
        
        g2d.dispose();
        return image;
    }
    
    // ======================= SPELPLAN OCH ANDRA OBJEKT =======================
    
    // Vägg-bild (klassiskt blå)
    public static Image createWallImage() {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Klassisk mörk marinblå från originalspelet
        Color wallColor = new Color(33, 33, 255);
        
        // Rita en fylld rektangel med rundade hörn
        g2d.setColor(wallColor);
        g2d.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
        
        // Lägg till en ljusare ytterlinje för 3D-effekt
        g2d.setColor(new Color(144, 144, 255));
        g2d.drawRect(0, 0, IMAGE_SIZE-1, IMAGE_SIZE-1);
        
        g2d.dispose();
        return image;
    }
    
    // Mat (små prickar)
    public static Image createFoodImage() {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Ljusrosa mat-prickar
        g2d.setColor(new Color(255, 255, 180));
        
        // Liten prick i mitten av rutan
        int dotSize = IMAGE_SIZE / 6;
        g2d.fillOval(IMAGE_SIZE/2 - dotSize/2, IMAGE_SIZE/2 - dotSize/2, dotSize, dotSize);
        
        g2d.dispose();
        return image;
    }
    
    // Power pellet (större prickar)
    public static Image createPowerPelletImage() {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Ljusrosa färg för power pellet
        g2d.setColor(new Color(255, 255, 180));
        
        // Större prick i mitten av rutan
        int dotSize = IMAGE_SIZE / 2;
        g2d.fillOval(IMAGE_SIZE/2 - dotSize/2, IMAGE_SIZE/2 - dotSize/2, dotSize, dotSize);
        
        g2d.dispose();
        return image;
    }
    
    // ======================= HJÄLPMETODER FÖR PACMAN =======================
    
    // Skapa alla Pac-Man-bilder i olika riktningar och munöppningar
    public static Image createPacManImage(int direction, boolean mouthOpen) {
        // Munöppningsvinkel är bredare när den är öppen, nästan helt stängd annars
        double openAngle = 60.0; // Klassisk munöppningsvinkel
        double closedAngle = 5.0; // Nästan stängd mun
        
        double angle = mouthOpen ? openAngle : closedAngle;
        
        switch (direction) {
            case 0: // Höger
                return createPacmanRightImage(angle);
            case 1: // Ner
                return createPacmanDownImage(angle);
            case 2: // Vänster
                return createPacmanLeftImage(angle);
            case 3: // Upp
                return createPacmanUpImage(angle);
            default:
                return createPacmanRightImage(angle);
        }
    }
    
    // Överlagrad metod som tar emot en animationsfas för mer detaljerad animation
    public static Image createPacManImage(int direction, int animationPhase) {
        // Animationsfas: 0 = helt öppen, 1 = halvöppen, 2 = nästan stängd, 3 = stängd
        double[] mouthAngles = {60.0, 40.0, 20.0, 5.0};
        double angle = mouthAngles[animationPhase % mouthAngles.length];
        
        switch (direction) {
            case 0: // Höger
                return createPacmanRightImage(angle);
            case 1: // Ner
                return createPacmanDownImage(angle);
            case 2: // Vänster
                return createPacmanLeftImage(angle);
            case 3: // Upp
                return createPacmanUpImage(angle);
            default:
                return createPacmanRightImage(angle);
        }
    }
    
    // Överlagrad metod för bakåtkompatibilitet
    public static Image createPacManImage(int direction) {
        return createPacManImage(direction, true); // Standard är öppen mun
    }
    
    // ======================= ÖVERLAGRADE METODER FÖR STANDARDRIKTNING =======================
    
    // Standardriktning för spökbilder är höger (eyeDirection = 0)
    public static Image createRedGhostImage() {
        return createRedGhostImage(0);
    }
    
    public static Image createPinkGhostImage() {
        return createPinkGhostImage(0);
    }
    
    public static Image createCyanGhostImage() {
        return createCyanGhostImage(0);
    }
    
    public static Image createOrangeGhostImage() {
        return createOrangeGhostImage(0);
    }
    
    public static Image createScaredGhostImage() {
        return createScaredGhostImage(false);
    }
}