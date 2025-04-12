import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;

public class PacMan extends JPanel {

    // Enum för spökets olika lägen
    private enum GhostMode {
        CHASE, SCATTER, FRIGHTENED, EATEN
    }

    private class Block {
        public int x, y, width, height, prevX, prevY;
        public int startX, startY;
        public int direction = 0; // 0=upp, 1=höger, 2=ner, 3=vänster
        public int ghostIndex = -1; // För att spåra vilket spöke det är
        public boolean isActive = true;
        public boolean isScared = false;
        public int scaredTimer = 0;
        public boolean inCage = false;
        public int cageTimer = 0;
        public boolean isEaten = false; // Ny flagga för uppätna spöken
        public Image image;  // Bilden som ska ritas
        
        // För kollisionshantering och rörelsebegränsning
        public Block(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;    
            this.prevX = x;
            this.prevY = y;
        }
        
        // Konstruktor med bild
        public Block(Image image, int x, int y, int width, int height) {
            this(x, y, width, height);
            this.image = image;
        }
    }

    private int rowCount = 31;
    private int columnCount = 31;
    private int tileSize = 35; // Mindre rutor för att passa hela banan på skärmen
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    
    private Image wallImage;
    private Image foodImage;
    private Image powerPelletImage;

    private int pacmanVelocityX = 0;
    private int pacmanVelocityY = 0;
    private int pacmanSpeed = 4; // Ändrat från 3 till 4 för snabbare Pac-Man
    private int pacManDirection = 0; // 0=höger, 1=ner, 2=vänster, 3=upp
    private int nextDirection = -1; // Buffrad riktning (-1 = ingen)
    private boolean pacManMouthOpen = true;
    private int animationCounter = 0;
    private int animationSpeed = 5; // Lite långsammare för tydligare animationer
    private Timer gameTimer;
    private Random random = new Random();
    private int ghostSpeed = 3; // Ändrat från 2 till 3 för snabbare spöken
    private boolean gameOver = false;
    private boolean gameWon = false;
    private int score = 0;
    private int lives = 3;
    
    // Ljudhanterare
    private SoundManager soundManager;
    
    private boolean gamePaused = false;
    
    // Visuella förbättringar
    private Color backgroundColor = new Color(0, 0, 0); // Helt svart bakgrund för mer retrokänsla
    private Font gameFont = new Font("Arial", Font.BOLD, 30);
    private Font scoreFont = new Font("Arial", Font.BOLD, 20); // Font för poängräkning
    private Font titleFont = new Font("Arial", Font.BOLD, 60);

    // Retro-funktioner
    private int highScore = 10000; // Standardvärde för high score
    private int level = 1;
    private int fruitBonus = 100; // Bonuspoäng för frukter
    private boolean showReady = true; // Om "READY!" ska visas vid start
    private int readyTimer = 120; // Timer för "READY!" text (i frames)
    private boolean showFruit = false; // Om frukt ska visas
    private int fruitTimer = 0; // Timer för hur länge frukten visas
    private int fruitX, fruitY; // Position för frukt
    private Image fruitImage; // Bild för aktuell frukt
    private int pointsDisplayTimer = 0; // Timer för visning av poäng när spöke äts
    private int pointsDisplayX = 0, pointsDisplayY = 0; // Position för poängvisning
    private int pointsDisplayValue = 0; // Poängvärde att visa

    // Klassisk Pac-Man-layout med symmetrisk design och förbättrad spökgård
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        "XooooooooooooXXoooooooooooooooX",
        "XoXXXXoXXXXXoXXoXXXXXoXXXXXoXoX",
        "XEXXXXoXXXXXoXXoXXXXXoXXXXXoEoX",
        "XoXXXXoXXXXXoXXoXXXXXoXXXXXoXoX",
        "XoooooooooooooooooooooooooooooX",
        "XoXXXXoXXoXXXXXXXXXoXXoXXXXoXoX",
        "XoXXXXoXXoXXXXXXXXXoXXoXXXXoXoX",
        "XooooooooooooXXoooooooooooooooX",
        "XXXXXXoXXXXXoXXoXXXXXoXXXXXXXXX",
        "X    XoXXXXXoXXoXXXXXoX       X",
        "X    XoXX          XXoX       X",
        "X    XoXX X      X XXoX       X",
        "XXXXXXoXX X      X XXoXXXXXXXXX",
        "      o    rbpO      o        ",
        "XXXXXXoXX X      X XXoXXXXXXXXX",
        "X    XoXX XXXXXXXX XXoX       X",
        "X    XoXX          XXoX       X",
        "X    XoXX XXXXXXXXoXXoX       X",
        "XXXXXXoXX XXXXXXXXoXXoXXXXXXXXX",
        "XooooooooooooXXoooooooooooooooX",
        "XoXXXXoXXXXXoXXoXXXXXoXXXXXXXoX",
        "XoXXXXoXXXXXoXXoXXXXXoXXXXXXXoX",
        "XEoXXoooooooPooooooooooooooEooX",
        "XXoXXoXXoXXXXXXXXXoXXoXXoXXXXoX",
        "XXoXXoXXoXXXXXXXXXoXXoXXoXXXXoX",
        "XooooooooooooXXoooooooooooooooX",
        "XoXXXXXXXXXXoXXoXXXXXXXXXXXXXoX",
        "XoXXXXXXXXXXoXXoXXXXXXXXXXXXXoX",
        "XoooooooooooooooooooooooooooooX",
        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> ghosts;
    HashSet<Block> foods;
    HashSet<Block> powerPellets;
    Block pacman;    

    private boolean[] ghostsReleased = new boolean[10]; // För att hålla reda på vilka spöken som släppts ut
    private int ghostReleaseTimer = 0;
    private int ghostReleaseInterval = 40; // Ytterligare minskat intervall för att släppa ut spöken snabbare

    private Image[] ghostImages;
    private Image[] pacManImages;
    private Image scaredGhostImage;
    private Image eatenGhostImage; // Bild för uppätna spöken (bara ögon)

    private boolean ghostsScared = false;
    private int scaredTimer = 0;
    private int scaredDuration = 300; // Hur länge spöken är skrämda (i uppdateringar)

    // Chase/Scatter-läge
    private GhostMode currentGhostMode = GhostMode.SCATTER; // Starta i SCATTER
    private int modeTimer = 0;
    private final int SCATTER_DURATION = 420; // 7 sekunder (7 * 60)
    private final int CHASE_DURATION = 1200; // 20 sekunder (20 * 60)
    private int modeChangeCounter = 0; // För att hantera kortare chase/scatter senare i spelet

    // Scatter-målkoordinater (utanför spelplanen för att simulera hörn)
    private final Point[] scatterTargets = {
        new Point(boardWidth + tileSize * 2, -tileSize * 2),    // Röd (Blinky) - Övre högra
        new Point(-tileSize * 2, -tileSize * 2),               // Rosa (Pinky) - Övre vänstra
        new Point(boardWidth + tileSize * 2, boardHeight + tileSize * 2), // Blå (Inky) - Nedre högra
        new Point(-tileSize * 2, boardHeight + tileSize * 2)    // Orange (Clyde) - Nedre vänstra
    };

    // Spökhusets position (används som mål för uppätna spöken)
    private final Point ghostHouseEntry = new Point(15 * tileSize + tileSize / 2, 13 * tileSize + tileSize / 2);

    // Förbättrad poängräkning och visuell feedback
    private int foodPoints = 10;    // Poäng för vanlig mat
    private int pelletPoints = 50;  // Poäng för power pellet
    private int ghostPoints = 200;  // Grundpoäng för att äta spöken
    private int ghostCombo = 1;     // Multiplikator för spökpoäng
    
    private int scoreDisplayTimer = 0;  // Timer för att visa poängökning
    private String scoreDisplayText = ""; // Text att visa
    private Point scoreDisplayPosition = new Point(0, 0); // Position för texten

    public PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(backgroundColor);

        // Initiera ljudhanterare
        soundManager = new SoundManager();

        // Skapa bilder med vår GameImages-hjälpklass
        loadImages();
        
        // Ladda spelplanen
        loadMap();
        
        // Sätt upp spelets kontroller
        setupGameControls();
        
        // Spela startljud
        soundManager.play("start");
    }

    private void loadImages() {
        wallImage = GameImages.createWallImage();
        foodImage = GameImages.createFoodImage();
        powerPelletImage = GameImages.createPowerPelletImage();
        
        // Ladda spökbilder
        ghostImages = new Image[4];
        ghostImages[0] = GameImages.createRedGhostImage();    // Blinky
        ghostImages[1] = GameImages.createPinkGhostImage();   // Pinky
        ghostImages[2] = GameImages.createCyanGhostImage();   // Inky
        ghostImages[3] = GameImages.createOrangeGhostImage(); // Clyde
        
        // Skapa Pac-Man-bilder för olika riktningar och munöppningar
        pacManImages = new Image[8]; // 4 riktningar x 2 munöppningar
        for (int i = 0; i < 4; i++) {
            pacManImages[i*2] = GameImages.createPacManImage(i, true);     // Öppen mun
            pacManImages[i*2+1] = GameImages.createPacManImage(i, false);  // Stängd mun
        }
        
        // Skapa skrämd spökbild
        scaredGhostImage = GameImages.createScaredGhostImage();
        eatenGhostImage = GameImages.createEatenGhostImage(0); // Skapa bild för uppätna spöken
    }

    public void loadMap() {
        System.out.println("Laddar karta. rowCount: " + rowCount + ", tileMap rader: " + tileMap.length);
        
        walls = new HashSet<>();
        ghosts = new HashSet<>();
        foods = new HashSet<>();
        powerPellets = new HashSet<>();
        pacman = null;
        
        for (int row = 0; row < tileMap.length; row++) {
            for (int col = 0; col < tileMap[row].length(); col++) {
                char tile = tileMap[row].charAt(col);
                
                if (tile == 'X') {
                    // Vägg
                    walls.add(new Block(wallImage, col * tileSize, row * tileSize, tileSize, tileSize));
                } else if (tile == 'o') {
                    // Mat
                    foods.add(new Block(foodImage, col * tileSize + tileSize/4, row * tileSize + tileSize/4, tileSize/2, tileSize/2));
                } else if (tile == 'E') {
                    // Power pellet
                    powerPellets.add(new Block(powerPelletImage, col * tileSize + tileSize/4, row * tileSize + tileSize/4, tileSize/2, tileSize/2));
                } else if (tile == 'r') {
                    // Rött spöke
                    System.out.println("Hittade r på rad " + row + ", kolumn " + col);
                    Block ghost = new Block(ghostImages[0], col * tileSize, row * tileSize, tileSize, tileSize);
                    ghost.ghostIndex = 0;
                    ghosts.add(ghost);
                    System.out.println("Lagt till rött spöke på position " + ghost.x + "," + ghost.y);
                } else if (tile == 'p') {
                    // Rosa spöke
                    System.out.println("Hittade p på rad " + row + ", kolumn " + col);
                    Block ghost = new Block(ghostImages[1], col * tileSize, row * tileSize, tileSize, tileSize);
                    ghost.ghostIndex = 1;
                    ghosts.add(ghost);
                    System.out.println("Lagt till rosa spöke på position " + ghost.x + "," + ghost.y);
                } else if (tile == 'b') {
                    // Blått spöke
                    System.out.println("Hittade b på rad " + row + ", kolumn " + col);
                    Block ghost = new Block(ghostImages[2], col * tileSize, row * tileSize, tileSize, tileSize);
                    ghost.ghostIndex = 2;
                    ghosts.add(ghost);
                    System.out.println("Lagt till blått spöke på position " + ghost.x + "," + ghost.y);
                } else if (tile == 'O') {
                    // Orange spöke - ändrat från 'o' till 'O' för att skilja från mat
                    System.out.println("Hittade O på rad " + row + ", kolumn " + col);
                    Block ghost = new Block(ghostImages[3], col * tileSize, row * tileSize, tileSize, tileSize);
                    ghost.ghostIndex = 3;
                    ghosts.add(ghost);
                    System.out.println("Lagt till orange spöke på position " + ghost.x + "," + ghost.y);
                } else if (tile == 'P') {
                    // Pacman
                    System.out.println("Hittade P på rad " + row + ", kolumn " + col);
                    pacman = new Block(pacManImages[0], col * tileSize, row * tileSize, tileSize, tileSize);
                    System.out.println("Lagt till Pacman på position " + pacman.x + "," + pacman.y);
                }
            }
        }
        
        System.out.println("Antal väggar: " + walls.size());
        System.out.println("Antal mat: " + foods.size());
        System.out.println("Antal power pellets: " + powerPellets.size());
        System.out.println("Antal spöken: " + ghosts.size());
        System.out.println("Pacman hittad: " + (pacman != null));
        
        // Om Pacman inte hittades, skapa en på en standardposition
        if (pacman == null) {
            pacman = new Block(pacManImages[0], 13 * tileSize, 13 * tileSize, tileSize, tileSize);
            System.out.println("Pacman inte hittad i kartan, skapad på standardposition: " + pacman.x + "," + pacman.y);
        }
        
        // Begränsa antalet spöken till 4
        if (ghosts.size() > 4) {
            System.out.println("För många spöken (" + ghosts.size() + "), begränsar till 4");
            HashSet<Block> limitedGhosts = new HashSet<>();
            int count = 0;
            for (Block ghost : ghosts) {
                if (count < 4) {
                    limitedGhosts.add(ghost);
                    count++;
                } else {
                    break;
                }
            }
            ghosts = limitedGhosts;
            System.out.println("Antal spöken efter begränsning: " + ghosts.size());
        }
    }


    private void setupGameControls() {
        // Keyboard input
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                // System.out.println("Key Pressed: " + KeyEvent.getKeyText(keyCode)); // DEBUG REMOVED
                
                if (gameOver || gameWon) {
                    if (keyCode == KeyEvent.VK_ENTER) {
                        resetGame();
                        return;
                    }
                }
                
                // Kontrollera att pacman inte är null innan vi försöker ändra riktning
                if (pacman == null || gamePaused || showReady) {
                    // System.out.println("Input ignored (pacman=" + pacman + ", paused=" + gamePaused + ", ready=" + showReady + ")"); // DEBUG REMOVED
                    return; // Tillåt inte input under paus/ready
                }
                
                // int oldNextDirection = nextDirection; // DEBUG REMOVED
                if (keyCode == KeyEvent.VK_UP) {
                    nextDirection = 3; // Sätt nästa riktning istället för direkt hastighet
                } 
                else if (keyCode == KeyEvent.VK_DOWN) {
                    nextDirection = 1; // Sätt nästa riktning istället för direkt hastighet
                } 
                else if (keyCode == KeyEvent.VK_LEFT) {
                    nextDirection = 2; // Sätt nästa riktning istället för direkt hastighet
                }
                else if (keyCode == KeyEvent.VK_RIGHT) {
                    nextDirection = 0; // Sätt nästa riktning istället för direkt hastighet
                }
                else if (keyCode == KeyEvent.VK_M) {
                    // Tryck M för att slå på/av ljud
                    soundManager.setSoundEnabled(!soundManager.isSoundEnabled());
                }
                else if (keyCode == KeyEvent.VK_P) {
                    // Lägg till paus-funktion
                    gamePaused = !gamePaused;
                }
                // if (oldNextDirection != nextDirection) { // DEBUG REMOVED
                //     System.out.println("Next Direction set to: " + nextDirection); // DEBUG REMOVED
                // } // DEBUG REMOVED
            }
        };
        
        this.setFocusable(true);
        this.addKeyListener(keyAdapter);
        
        // Game timer - mer balanserad uppdateringsfrekvens
        gameTimer = new Timer(1000/60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        gameTimer.start();
    }

    // Återställ spelet för omstart
    private void resetGame() {
        // Återställ spelet
        loadMap();
        score = 0;
        lives = 3;
        gameOver = false;
        gameWon = false;
        pacmanVelocityX = 0;
        pacmanVelocityY = 0;
        
        // Återställ spökutgång
        ghostReleaseTimer = 0;
        for (int i = 0; i < ghostsReleased.length; i++) {
            ghostsReleased[i] = false;
        }
        
        // Starta speltimern
        if (gameTimer != null) {
            gameTimer.start();
        }
        
        // Spela startljudet
        soundManager.play("start");
    }

    private void update() {
        // Hantera "READY!" text vid start
        if (showReady) {
            readyTimer--;
            if (readyTimer <= 0) {
                showReady = false;
            }
            repaint(); // Rita om för att visa nedräkning eller när texten försvinner
            return; // Ingen speluppdatering medan "READY!" visas
        }

        // Kör resten av uppdateringen endast om spelet inte är över, pausat, eller i "READY!"-läge
        if (!gameOver && !gamePaused) {

            // --- Hantera Chase/Scatter-timer --- 
            modeTimer++;
            boolean switchMode = false;

            if (currentGhostMode == GhostMode.SCATTER) {
                if (modeTimer >= SCATTER_DURATION) {
                    currentGhostMode = GhostMode.CHASE;
                    modeTimer = 0;
                    switchMode = true;
                    System.out.println("Switching to CHASE mode");
                }
            } else if (currentGhostMode == GhostMode.CHASE) {
                if (modeTimer >= CHASE_DURATION) {
                    currentGhostMode = GhostMode.SCATTER;
                    modeTimer = 0;
                    switchMode = true;
                    modeChangeCounter++; // Räkna upp antal byten
                    System.out.println("Switching to SCATTER mode");
                }
            }

            // Om läget byttes, tvinga spöken att vända håll (om de inte är FRIGHTENED/EATEN)
            if (switchMode) {
                for (Block ghost : ghosts) {
                    if (!ghost.isScared && !ghost.isEaten) {
                         // Tvinga omvänd riktning (om möjligt)
                         int oppositeDirection = (ghost.direction + 2) % 4;
                         if(canMove(ghost.x, ghost.y, oppositeDirection)) {
                              ghost.direction = oppositeDirection;
                         }
                    }
                }
            }
            // --- Slut på Chase/Scatter-logik ---

            // Uppdatera Pac-Man
            updatePacMan();
            
            // Uppdatera spöken
            moveGhosts();
            
            // Kontrollera kollision med spöken
            checkGhostCollision();
            
            // Uppdatera animationer (kanske flytta till egen metod?)
            // updateAnimation(); // Anropas separat nedanför

            // Kontrollera om nivån är klar
            checkLevelComplete(); // Lade till detta anrop
        }
        
        // Tvinga omritning oavsett paus/game over för att visa meddelanden
        repaint();
    }
    
    private void updatePacMan() {
        // System.out.println("updatePacMan - Current Direction: " + pacManDirection + ", Next Direction: " + nextDirection); // DEBUG REMOVED
        // Spara föregående position för kollisionsupplösning
        pacman.prevX = pacman.x;
        pacman.prevY = pacman.y;
        
        // Hantera buffrad styrningsinmatning
        if (nextDirection != -1) {
            // Testa om vi kan svänga i den buffrade riktningen
            int testX = pacman.x;
            int testY = pacman.y;
            
            if (nextDirection == 0) testX += pacmanSpeed; // Höger
            else if (nextDirection == 1) testY += pacmanSpeed; // Ner
            else if (nextDirection == 2) testX -= pacmanSpeed; // Vänster
            else if (nextDirection == 3) testY -= pacmanSpeed; // Upp
            
            if (!checkPacManWallCollision(testX, testY)) {
                // Om vi kan svänga, uppdatera huvudriktningen
                pacManDirection = nextDirection;
                nextDirection = -1; // Nollställ bufferten
            }
        }
        
        // Försök att flytta Pac-Man i den nuvarande huvudriktningen
        boolean moved = false;
        if (pacManDirection == 0) { // Höger
            if (!checkPacManWallCollision(pacman.x + pacmanSpeed, pacman.y)) {
                pacman.x += pacmanSpeed;
                moved = true;
            }
        } else if (pacManDirection == 1) { // Ner
            if (!checkPacManWallCollision(pacman.x, pacman.y + pacmanSpeed)) {
                pacman.y += pacmanSpeed;
                moved = true;
            }
        } else if (pacManDirection == 2) { // Vänster
            if (!checkPacManWallCollision(pacman.x - pacmanSpeed, pacman.y)) {
                pacman.x -= pacmanSpeed;
                moved = true;
            }
        } else if (pacManDirection == 3) { // Upp
            if (!checkPacManWallCollision(pacman.x, pacman.y - pacmanSpeed)) {
                pacman.y -= pacmanSpeed;
                moved = true;
            }
        }
        
        // Uppdatera hastighetsvariablerna baserat på om vi rörde oss
        pacmanVelocityX = pacman.x - pacman.prevX;
        pacmanVelocityY = pacman.y - pacman.prevY;
        
        // Kontrollera tunnel-passage
        handleTunnel(pacman);
        
        // Kontrollera matkollision
        checkFoodCollision();
        
        // Uppdatera animation
        if (moved) { // Använd 'moved' istället för att kolla velocity
            animationCounter++;
            if (animationCounter >= animationSpeed) {
                pacManMouthOpen = !pacManMouthOpen;
                animationCounter = 0;
            }
        }
        
        // Uppdatera Pac-Man-bilden baserat på riktning och animation
        int imageIndex = pacManDirection * 2; // Basindex för riktning (öppen mun)
        if (!pacManMouthOpen || !moved) { // Använd stängd mun om munnen är stängd eller om vi står stilla
            imageIndex += 1;  // Index för stängd mun
        }
        pacman.image = pacManImages[imageIndex];
    }

    // checkPacManWallCollision, canMovePacMan, checkFoodCollision etc. remain the same...

    private void moveGhosts() {
        // Uppdatera global scared timer (används inte längre per spöke)
        // if (ghostsScared) { scaredTimer++; ... }

        // Släpp ut spökena från burens centrum med jämna mellanrum
        ghostReleaseTimer++;
        if (ghostReleaseTimer >= ghostReleaseInterval) {
            ghostReleaseTimer = 0;
            
            // Försök hitta ett ospelat spöke att släppa ut
            for (int i = 0; i < ghostsReleased.length; i++) {
                if (!ghostsReleased[i]) {
                    ghostsReleased[i] = true;
                    System.out.println("Släpper ut spöke " + i);
                    break;
                }
            }
        }
        
        int ghostIndex = 0;
        for (Block ghost : ghosts) {

            // Om spöket är uppätet och inne i buren, återställ det
            if (ghost.isEaten && isGhostInCage(ghost)) {
                 if (Math.abs(ghost.x - ghost.startX) < ghostSpeed && Math.abs(ghost.y - ghost.startY) < ghostSpeed) {
                      ghost.isEaten = false;
                      ghost.inCage = true; // Se till att det är i buren
                      ghost.cageTimer = 0; // Nollställ timer så det kan börja röra sig i buren
                      updateGhostImage(ghost); // Uppdatera till normal bild
                      System.out.println("Ghost " + ghost.ghostIndex + " respawned.");
                      ghostIndex++;
                      continue; // Gå till nästa spöke
                 }
            }

            // Kontrollera om spöket är inne i spökgården
            boolean isInCage = isGhostInCage(ghost);
            
            if (isInCage && !ghostsReleased[ghostIndex % ghostsReleased.length]) {
                // Rör spöket upp och ner inuti buren om det inte är utsläppt
                moveGhostInCage(ghost);
                ghostIndex++;
                continue;
            } else if (isInCage && ghostsReleased[ghostIndex % ghostsReleased.length]) {
                // Om spöket är i buren men ska vara utsläppt, flytta det uppåt mot utgången
                releaseGhostFromCage(ghost);
                ghostIndex++;
                continue;
            }
            
            // --- HÄMTA MÅLKOORDINAT BASERAT PÅ LÄGE --- 
            int targetX = 0;
            int targetY = 0;
            GhostMode effectiveMode = currentGhostMode; // Starta med globalt läge

            if (ghost.isEaten) {
                effectiveMode = GhostMode.EATEN;
            } else if (ghost.isScared) {
                effectiveMode = GhostMode.FRIGHTENED;
            }

            switch (effectiveMode) {
                case FRIGHTENED:
                    moveGhostRandomly(ghost);
                    break; // Slumpmässig rörelse

                case EATEN:
                    targetX = ghostHouseEntry.x;
                    targetY = ghostHouseEntry.y;
                    moveGhostTowardTarget(ghost, targetX, targetY, true); // Tillåt att vända direkt
                    break;

                case SCATTER:
                    targetX = scatterTargets[ghost.ghostIndex % 4].x;
                    targetY = scatterTargets[ghost.ghostIndex % 4].y;
                    moveGhostTowardTarget(ghost, targetX, targetY, false);
                    break;

                case CHASE:
                    // Här kommer den specifika AI:n för varje spöke
                    int ghostTypeIndex = ghost.ghostIndex;
                    Block blinky = findGhostByType(0); // Hitta Blinky för Inkys logik

                    if (ghostTypeIndex == 0) { // Rött spöke (Blinky)
                        targetX = pacman.x;
                        targetY = pacman.y;
                    } else if (ghostTypeIndex == 1) { // Rosa spöke (Pinky)
                        targetX = pacman.x;
                        targetY = pacman.y;
                        // Sikta 4 rutor framför Pac-Man i hans riktning
                        int lookAhead = 4 * tileSize;
                        if (pacManDirection == 3) targetY -= lookAhead;      // UPP
                        else if (pacManDirection == 0) targetX += lookAhead; // HÖGER
                        else if (pacManDirection == 1) targetY += lookAhead; // NER
                        else if (pacManDirection == 2) targetX -= lookAhead; // VÄNSTER
                    } else if (ghostTypeIndex == 2) { // Blått spöke (Inky)
                        if (blinky != null) {
                             // Beräkna punkt 2 rutor framför Pac-Man
                             int pivotX = pacman.x;
                             int pivotY = pacman.y;
                             int lookAhead = 2 * tileSize;
                             if (pacManDirection == 3) pivotY -= lookAhead;      // UPP
                             else if (pacManDirection == 0) pivotX += lookAhead; // HÖGER
                             else if (pacManDirection == 1) pivotY += lookAhead; // NER
                             else if (pacManDirection == 2) pivotX -= lookAhead; // VÄNSTER

                             // Beräkna vektor från Blinky till pivot
                             int vecX = pivotX - blinky.x;
                             int vecY = pivotY - blinky.y;

                             // Dubbla vektorn och sätt målet från Blinkys position
                             targetX = blinky.x + 2 * vecX;
                             targetY = blinky.y + 2 * vecY;
                        } else { // Fallback om Blinky inte hittas (bör inte hända)
                            targetX = pacman.x;
                            targetY = pacman.y;
                        }
                    } else { // Orange spöke (Clyde)
                        double distance = Math.sqrt(Math.pow(ghost.x - pacman.x, 2) + Math.pow(ghost.y - pacman.y, 2));
                        if (distance < 8 * tileSize) { // Om inom 8 rutor, gå till scatter-mål
                            targetX = scatterTargets[ghost.ghostIndex % 4].x;
                            targetY = scatterTargets[ghost.ghostIndex % 4].y;
                        } else { // Annars, jaga Pac-Man
                            targetX = pacman.x;
                            targetY = pacman.y;
                        }
                    }
                    moveGhostTowardTarget(ghost, targetX, targetY, false);
                    break;
            }
            // --- SLUT PÅ MÅLBERÄKNING --- 
            
            // Uppdatera spökets utseende baserat på dess tillstånd och riktning
            updateGhostImage(ghost);
            
            ghostIndex++;
        }
    }
    
    // Lägg till denna hjälpmetod för att uppdatera spökbilden baserat på typ och riktning
    private void updateGhostImage(Block ghost) {
        if (ghost.isEaten) {
            ghost.image = eatenGhostImage;
        } else if (ghost.isScared) {
            // Skrämt spöke
            boolean shouldBlink = ghost.scaredTimer < scaredDuration / 3 && (ghost.scaredTimer / 5) % 2 == 0;
            ghost.image = GameImages.createScaredGhostImage(shouldBlink);
        } else {
            // Normalt spöke, uppdatera utseende baserat på typ och riktning
            int eyeDirection = getGhostEyeDirection(ghost);
            
            switch (ghost.ghostIndex) {
                case 0: // Rött
                    ghost.image = GameImages.createRedGhostImage(eyeDirection);
                    break;
                case 1: // Rosa
                    ghost.image = GameImages.createPinkGhostImage(eyeDirection);
                    break;
                case 2: // Blått
                    ghost.image = GameImages.createCyanGhostImage(eyeDirection);
                    break;
                case 3: // Orange
                    ghost.image = GameImages.createOrangeGhostImage(eyeDirection);
                    break;
                default: // Standard
                    ghost.image = GameImages.createRedGhostImage(eyeDirection);
                    break;
            }
        }
    }
    
    // Uppdaterad metod för att avgöra om spöket kolliderar med en vägg
    private boolean checkGhostWallCollision(Block ghost, int newX, int newY) {
        // Spöken ska kunna gå ut genom burens topp
        if (isExitPath(newX, newY)) {
            return false;
        }
        
        // Normal kollisionskontroll mot väggar
        for (Block wall : walls) {
            if (newX < wall.x + wall.width && 
                newX + ghost.width > wall.x && 
                newY < wall.y + wall.height && 
                newY + ghost.height > wall.y) {
                return true;
            }
        }
        return false;
    }
    
    // Uppdaterad hjälpmetod för att identifiera utgångsvägen från buren
    private boolean isExitPath(int x, int y) {
        // Definiera den exakta utgången från spökgården - göra den bredare och högre
        int exitX = 14 * tileSize; // Bredare utgång börjar längre till vänster
        int exitY = 12 * tileSize; // Börjar högre upp
        int exitWidth = tileSize * 3;  // Mycket bredare utgång (3 rutor)
        int exitHeight = 3 * tileSize; // Högre utgång (3 rutor)
        
        return x >= exitX && x < exitX + exitWidth &&
               y >= exitY && y < exitY + exitHeight;
    }
    
    // Uppdaterad animeringsmetod för att få jämnare och mer responsiv animation
    private void updateAnimation() {
        // Uppdatera animationsräknare
        animationCounter++;
        
        // Beräkna animationsfas (0-3) baserat på animation counter
        // Detta ger en jämnare animation med 4 steg:
        // 0: helt öppen mun, 1: halvöppen, 2: nästan stängd, 3: stängd
        int animationPhase = (animationCounter / animationSpeed) % 4;
        
        // Uppdatera Pac-Man's bild baserat på riktning och aktuell animationsfas
        pacman.image = GameImages.createPacManImage(pacManDirection, animationPhase);
        
        // Uppdatera poängvisning om aktiv
        if (scoreDisplayTimer > 0) {
            scoreDisplayTimer--;
        }
    }
    
    private void checkGhostCollision() {
        if (pacman == null) return;
        
        Rectangle pacmanRect = new Rectangle(pacman.x, pacman.y, pacman.width, pacman.height);
        
        for (Iterator<Block> it = ghosts.iterator(); it.hasNext();) {
            Block ghost = it.next();
            
            // Ignorera kollision om spöket är uppätet
            if (ghost.isEaten) continue;

            Rectangle ghostRect = new Rectangle(ghost.x, ghost.y, ghost.width, ghost.height);
            
            if (pacmanRect.intersects(ghostRect)) {
                if (ghost.isScared) {
                    // Beräkna poäng baserat på hur många spöken som ätits under samma power pellet
                    int ghostValue = 200 * (int)Math.pow(2, ghostCombo - 1);
                    ghostValue = Math.min(ghostValue, 1600); // Max 1600 poäng
                    addScore(ghostValue, ghost.x, ghost.y);
                    
                    // Öka kombon för nästa spöke
                    ghostCombo++;
                    
                    // Sätt spöket till EATEN-läge istället för att återställa direkt
                    ghost.isScared = false; // Inte längre skrämd
                    ghost.isEaten = true;   // Nu uppäten
                    ghost.scaredTimer = 0;
                    updateGhostImage(ghost); // Visa bara ögon

                    soundManager.play("ghost"); // Spela ljudeffekt för att äta spöke
                } else {
                    if (!gameOver) {
                        lives--;
                        
                        if (lives <= 0) {
                            gameOver = true;
                            soundManager.play("gameover");
                        } else {
                            // Återställ Pac-Man och spöken till startposition
                            restartLevel();
                            soundManager.play("death");
                        }
                    }
                }
                break;
            }
        }
    }
    
    private void checkLevelComplete() {
        if (foods.isEmpty() && powerPellets.isEmpty()) {
            gameWon = true;
            gameOver = true;
            gameTimer.stop();
            
            // Spela vinstljud
            soundManager.play("win");
        }
    }
    
    // Återställer positioner för Pac-Man och spöken när ett liv förloras
    private void resetPositions() {
        // Återställ Pac-Man till startposition
        if (pacman != null) {
            pacman.x = pacman.startX;
            pacman.y = pacman.startY;
        }
        
        // Stoppa rörelse
        pacmanVelocityX = 0;
        pacmanVelocityY = 0;
        
        // Återställ spökenas position till deras startpositioner
        for (Block ghost : ghosts) {
            ghost.x = ghost.startX;
            ghost.y = ghost.startY;
        }
        
        // Återställ spökutgång
        ghostReleaseTimer = 0;
        for (int i = 0; i < ghostsReleased.length; i++) {
            ghostsReleased[i] = false;
        }
        
        // Återställ skräck-status för spöken
        ghostsScared = false;
        scaredTimer = 0;
        ghostCombo = 1;
    }
    
    // Metod för att röra spökena mot en specifik målpunkt
    // Lägg till en parameter allowReverse för att styra om spöket får vända 180 grader direkt
    private void moveGhostTowardTarget(Block ghost, int targetX, int targetY, boolean allowReverseOverride) {
        // Spara gamla positionen
        int oldX = ghost.x;
        int oldY = ghost.y;
        
        // Beräkna riktning till målet
        int dx = targetX - ghost.x;
        int dy = targetY - ghost.y;
        
        // Nuvarande riktning
        int currentDirection = ghost.direction;
        
        // Kontrollera om vi är på en korsning
        boolean isAtIntersection = isAtIntersection(ghost.x, ghost.y);
        
        // Möjliga riktningar, utom motsatt mot nuvarande (förhindrar att vända)
        ArrayList<Integer> possibleDirections = new ArrayList<>();
        int oppositeDirection = (currentDirection + 2) % 4;
        
        // Om spöket får vända (t.ex. om det sitter fast eller är uppätet) lägger vi till motsatt riktning
        boolean allowReverse = allowReverseOverride; // Använd override om den är sann
        
        // Kontrollera om vi har fastnat (ingen framkomlig väg)
        boolean isStuck = true;
        for (int dir = 0; dir < 4; dir++) {
            if (dir != oppositeDirection && canMove(ghost.x, ghost.y, dir)) {
                isStuck = false;
                break;
            }
        }
        
        // Om spöket är fast, tillåt det att vända (om inte override redan tillåter det)
        if (isStuck && !allowReverse) {
            allowReverse = true;
        }
        
        // Samla möjliga riktningar
        for (int dir = 0; dir < 4; dir++) {
            if ((dir != oppositeDirection || allowReverse) && canMove(ghost.x, ghost.y, dir)) {
                possibleDirections.add(dir);
            }
        }
        
        // Om vi är på en korsning eller måste ändra riktning
        if (isAtIntersection || possibleDirections.size() == 1 || isStuck) {
            int bestDirection = oppositeDirection; // Default till motsatt riktning om inget annat fungerar
            int bestDistance = Integer.MAX_VALUE;
            
            // Hitta bästa riktningen baserat på avstånd till målet
            for (Integer dir : possibleDirections) {
                int newX = ghost.x;
                int newY = ghost.y;
                
                // Beräkna ny position baserat på riktning
                if (dir == 0) newY -= ghostSpeed;      // UPP
                else if (dir == 1) newX += ghostSpeed; // HÖGER
                else if (dir == 2) newY += ghostSpeed; // NER
                else if (dir == 3) newX -= ghostSpeed; // VÄNSTER
                
                // Beräkna det nya avståndet till målet
                int newDistance = (int) Math.sqrt(Math.pow(newX - targetX, 2) + Math.pow(newY - targetY, 2));
                
                // Om vi hittar en bättre riktning, spara den
                if (newDistance < bestDistance && canMove(ghost.x, ghost.y, dir)) {
                    bestDistance = newDistance;
                    bestDirection = dir;
                }
            }
            
            ghost.direction = bestDirection;
        }
        
        // Flytta spöket i den valda riktningen
        if (ghost.direction == 0) ghost.y -= ghostSpeed;      // UPP
        else if (ghost.direction == 1) ghost.x += ghostSpeed; // HÖGER
        else if (ghost.direction == 2) ghost.y += ghostSpeed; // NER
        else if (ghost.direction == 3) ghost.x -= ghostSpeed; // VÄNSTER
        
        // Kontrollera om den nya positionen är giltig
        if (!isValidPosition(ghost.x, ghost.y)) {
            // Återställ positionen om spöket skulle gå genom en vägg
            ghost.x = oldX;
            ghost.y = oldY;
            
            // Välj en annan slumpmässig riktning
            moveGhostRandomly(ghost);
        }
        
        // Hantera tunnel
        handleTunnel(ghost);
        
        // Uppdatera tidigare position
        ghost.prevX = oldX;
        ghost.prevY = oldY;
    }

    // Kontrollmetod för att se om en position är giltig (inte på en vägg)
    private boolean isValidPosition(int x, int y) {
        // Konvertera pixel-koordinater till rutnätspositioner
        int row = y / tileSize;
        int col = x / tileSize;
        
        // Säkerhetskontroll att vi är inom gränserna
        if (row < 0 || row >= tileMap.length || col < 0 || col >= tileMap[0].length()) {
            return true; // Hantera som tunnel om utanför gränserna
        }
        
        // Kontrollera om det finns vägg på denna position
        return tileMap[row].charAt(col) != 'X';
    }

    // Förbättrad metod för att kontrollera om spöket kan röra sig i en viss riktning
    private boolean canMove(int x, int y, int direction) {
        int newX = x;
        int newY = y;
        
        // Beräkna ny position baserat på riktning
        if (direction == 0) newY -= ghostSpeed;      // UPP
        else if (direction == 1) newX += ghostSpeed; // HÖGER
        else if (direction == 2) newY += ghostSpeed; // NER
        else if (direction == 3) newX -= ghostSpeed; // VÄNSTER
        
        // Beräkna rutnätsposition för varje hörn av spöket (med en mindre hitbox för bättre rörelse)
        int offset = tileSize / 4; // Mindre hitbox
        
        // Kontrollera varje hörn
        int[][] corners = {
            {newX + offset, newY + offset},         // Övre vänstra
            {newX + tileSize - offset, newY + offset},  // Övre högra
            {newX + offset, newY + tileSize - offset},  // Nedre vänstra
            {newX + tileSize - offset, newY + tileSize - offset}   // Nedre högra
        };
        
        for (int[] corner : corners) {
            int row = corner[1] / tileSize;
            int col = corner[0] / tileSize;
            
            // Kontrollera om hörnet är på en vägg
            if (row >= 0 && row < tileMap.length && col >= 0 && col < tileMap[0].length() && 
                tileMap[row].charAt(col) == 'X') {
                return false;
            }
        }
        
        return true;
    }

    // Hjälpmetod för att avgöra om en position är en korsning
    private boolean isAtIntersection(int x, int y) {
        int count = 0;
        
        // Beräkna centerpositionen för spöket i rutnätet
        int centerX = x + tileSize / 2;
        int centerY = y + tileSize / 2;
        
        // Kontrollera om vi är centrerade på rutnätet (för att förhindra överdrivet byte av riktning)
        if (centerX % tileSize > ghostSpeed && centerX % tileSize < tileSize - ghostSpeed) return false;
        if (centerY % tileSize > ghostSpeed && centerY % tileSize < tileSize - ghostSpeed) return false;
        
        // Räkna möjliga riktningar från denna position
        for (int dir = 0; dir < 4; dir++) {
            if (canMove(x, y, dir)) {
                count++;
            }
        }
        
        // Om vi kan gå åt mer än 2 riktningar, är det en korsning
        return count > 2;
    }

    private void moveGhostRandomly(Block ghost) {
        // Spara gamla positionen
        int oldX = ghost.x;
        int oldY = ghost.y;
        
        // Nuvarande riktning
        int currentDirection = ghost.direction;
        
        // Kontrollera om vi är på en korsning
        boolean isAtIntersection = isAtIntersection(ghost.x, ghost.y);
        
        // Möjliga riktningar, utom motsatt mot nuvarande
        ArrayList<Integer> possibleDirections = new ArrayList<>();
        int oppositeDirection = (currentDirection + 2) % 4;
        
        // Kontrollera om vi har fastnat (ingen framkomlig väg)
        boolean isStuck = true;
        for (int dir = 0; dir < 4; dir++) {
            if (dir != oppositeDirection && canMove(ghost.x, ghost.y, dir)) {
                isStuck = false;
                break;
            }
        }
        
        // Samla möjliga riktningar
        for (int dir = 0; dir < 4; dir++) {
            if ((dir != oppositeDirection || isStuck) && canMove(ghost.x, ghost.y, dir)) {
                possibleDirections.add(dir);
            }
        }
        
        // Om vi är på en korsning eller om vi inte kan fortsätta i samma riktning
        if (isAtIntersection || !canMove(ghost.x, ghost.y, currentDirection) || Math.random() < 0.1) {
            if (possibleDirections.size() > 0) {
                // Välj slumpmässig riktning från möjliga
                int randomIndex = (int) (Math.random() * possibleDirections.size());
                ghost.direction = possibleDirections.get(randomIndex);
            } else if (isStuck) {
                // Om vi ändå sitter fast, tillåt riktningen som är motsatt
                ghost.direction = oppositeDirection;
            }
        }
        
        // Flytta spöket i den valda riktningen
        if (ghost.direction == 0) ghost.y -= ghostSpeed;      // UPP
        else if (ghost.direction == 1) ghost.x += ghostSpeed; // HÖGER
        else if (ghost.direction == 2) ghost.y += ghostSpeed; // NER
        else if (ghost.direction == 3) ghost.x -= ghostSpeed; // VÄNSTER
        
        // Kontrollera om den nya positionen är giltig
        if (!isValidPosition(ghost.x, ghost.y)) {
            // Återställ positionen om spöket skulle gå genom en vägg
            ghost.x = oldX;
            ghost.y = oldY;
            
            // Välj annan riktning
            ArrayList<Integer> newDirections = new ArrayList<>();
            for (int dir = 0; dir < 4; dir++) {
                if (canMove(ghost.x, ghost.y, dir)) {
                    newDirections.add(dir);
                }
            }
            
            if (newDirections.size() > 0) {
                int randomIndex = (int) (Math.random() * newDirections.size());
                ghost.direction = newDirections.get(randomIndex);
            }
        }
        
        // Hantera tunnel
        handleTunnel(ghost);
        
        // Uppdatera tidigare position
        ghost.prevX = oldX;
        ghost.prevY = oldY;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Rita bakgrund
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Rita väggar
        for (Block wall : walls) {
            g2d.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        
        // Rita mat
        for (Block food : foods) {
            g2d.drawImage(food.image, food.x, food.y, food.width, food.height, null);
        }
        
        // Rita power pellets med pulseffekt
        for (Block powerPellet : powerPellets) {
            // Skapa en pulserande skalning baserat på speltiden
            double pulseScale = 1.0 + 0.2 * Math.sin(System.currentTimeMillis() / 200.0);
            int adjustedSize = (int)(powerPellet.width * pulseScale);
            int adjustedX = powerPellet.x - (adjustedSize - powerPellet.width) / 2;
            int adjustedY = powerPellet.y - (adjustedSize - powerPellet.height) / 2;
            
            g2d.drawImage(powerPellet.image, adjustedX, adjustedY, adjustedSize, adjustedSize, null);
        }
        
        // Rita frukt om synlig
        if (showFruit) {
            g2d.drawImage(fruitImage, fruitX, fruitY, tileSize, tileSize, null);
        }
        
        // Rita spöken
        for (Block ghost : ghosts) {
            g2d.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }
        
        // Rita Pac-Man
        if (pacman != null) {
            g2d.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
        }
        
        // Rita HUD med poäng och liv
        drawHUD(g2d);
        
        // Rita Game Over/Win skärm
        if (gameOver) {
            g2d.setColor(new Color(0, 0, 0, 200)); // Halvgenomskinlig svart
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(Color.RED);
            g2d.setFont(titleFont);
            g2d.drawString("GAME OVER", boardWidth/2 - 150, boardHeight/2);
            
            g2d.setColor(Color.YELLOW);
            g2d.setFont(gameFont);
            g2d.drawString("TRYCK ENTER FÖR ATT SPELA IGEN", boardWidth/2 - 240, boardHeight/2 + 60);
            g2d.drawString("POÄNG: " + score, boardWidth/2 - 80, boardHeight/2 + 120);
        } else if (gameWon) {
            g2d.setColor(new Color(0, 0, 0, 200)); // Halvgenomskinlig svart
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(Color.GREEN);
            g2d.setFont(titleFont);
            g2d.drawString("DU VANN!", boardWidth/2 - 130, boardHeight/2);
            
            g2d.setColor(Color.YELLOW);
            g2d.setFont(gameFont);
            g2d.drawString("TRYCK ENTER FÖR NÄSTA NIVÅ", boardWidth/2 - 210, boardHeight/2 + 60);
            g2d.drawString("POÄNG: " + score, boardWidth/2 - 80, boardHeight/2 + 120);
        } else if (gamePaused) {
            g2d.setColor(new Color(100, 100, 255, 200));
            g2d.setFont(titleFont);
            String text = "PAUSAD";
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            g2d.drawString(text, (boardWidth - textWidth) / 2, boardHeight / 2);
            
            g2d.setFont(gameFont);
            String subText = "Tryck på P för att fortsätta";
            int subTextWidth = g2d.getFontMetrics().stringWidth(subText);
            g2d.drawString(subText, (boardWidth - subTextWidth) / 2, boardHeight / 2 + 50);
        }
    }

    // Metod för att rita poäng och livräknare i retrostil
    private void drawHUD(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Rita poäng
        g2d.setFont(scoreFont);
        g2d.setColor(Color.WHITE);
        g2d.drawString("SCORE: " + score, 30, 30);
        
        // Rita high score
        g2d.drawString("HIGH SCORE: " + highScore, boardWidth/2 - 80, 30);
        
        // Rita livräknare med små Pac-Man-ikoner
        for (int i = 0; i < lives; i++) {
            g2d.drawImage(pacManImages[0], boardWidth - 150 + i * 40, 15, 20, 20, null);
        }
        
        // Rita aktuell nivå
        g2d.drawString("LEVEL: " + level, boardWidth - 120, 30);
        
        // Visa "READY!" text vid start
        if (showReady) {
            g2d.setColor(Color.YELLOW);
            g2d.setFont(gameFont);
            g2d.drawString("READY!", boardWidth/2 - 60, boardHeight/2 + 20);
        }
        
        // Visa poäng när spöke äts
        if (pointsDisplayTimer > 0) {
            g2d.setColor(Color.CYAN);
            g2d.setFont(scoreFont);
            g2d.drawString("" + pointsDisplayValue, pointsDisplayX, pointsDisplayY);
        }
    }

    // Uppdatera poängberäkning och visa poäng på skärmen
    private void addScore(int points, int x, int y) {
        score += points;
        
        // Uppdatera high score om applicerbart
        if (score > highScore) {
            highScore = score;
        }
        
        // Visa poäng på skärmen temporärt
        if (points >= 200) { // Bara visa för spöken och större bonusar
            pointsDisplayValue = points;
            pointsDisplayX = x;
            pointsDisplayY = y;
            pointsDisplayTimer = 60; // Visa i 1 sekund (60 frames)
        }
    }

    // Lägg till metod för att visa frukt
    private void showFruit() {
        showFruit = true;
        fruitTimer = 300; // Visa i 5 sekunder (300 frames)
        
        // Placera frukten någonstans i mitten av banan
        fruitX = 14 * tileSize;
        fruitY = 17 * tileSize;
        
        // Skapa fruktbilden direkt utan att anropa saknade metoder
        BufferedImage cherryImage = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) cherryImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (level == 1) {
            // Rita körsbär
            g.setColor(Color.RED);
            g.fillOval(5, 15, 10, 10);
            g.fillOval(15, 15, 10, 10);
            
            // Stjälkar
            g.setColor(new Color(139, 69, 19));
            g.setStroke(new BasicStroke(2));
            g.drawLine(10, 15, 15, 5);
            g.drawLine(20, 15, 15, 5);
            
            fruitBonus = 100;
        } else if (level == 2) {
            // Rita jordgubbe
            g.setColor(Color.RED);
            g.fillOval(5, 10, 20, 20);
            
            // Blad
            g.setColor(Color.GREEN);
            g.fillOval(10, 5, 10, 10);
            
            // Prickar (frön)
            g.setColor(Color.YELLOW);
            for(int i = 0; i < 8; i++) {
                g.fillOval(8 + (i % 3) * 7, 12 + (i / 3) * 6, 2, 2);
            }
            
            fruitBonus = 300;
        } else {
            // Standardfrukt (körsbär)
            g.setColor(Color.RED);
            g.fillOval(5, 15, 10, 10);
            g.fillOval(15, 15, 10, 10);
            
            // Stjälkar
            g.setColor(new Color(139, 69, 19));
            g.setStroke(new BasicStroke(2));
            g.drawLine(10, 15, 15, 5);
            g.drawLine(20, 15, 15, 5);
            
            fruitBonus = 100 * level;
        }
        
        g.dispose();
        fruitImage = cherryImage;
    }

    // Skapa en restartLevel-metod
    private void restartLevel() {
        // Återställ positioner för alla spelare och spöken
        resetPositions();
        
        // Återställ timers och spelläge
        ghostsScared = false;
        scaredTimer = 0;
        ghostCombo = 1;
        currentGhostMode = GhostMode.SCATTER; // Starta i scatter efter dödsfall
        modeTimer = 0;
        
        // Visa "READY!" text igen
        showReady = true;
        readyTimer = 120; // Återställ timern!
    }

    // Metod för att hantera tunnlar för spöken och pacman
    private void handleTunnel(Block entity) {
        // Hantera tunnlar på sidorna
        if (entity.x < -entity.width) {
            entity.x = boardWidth;
        } else if (entity.x > boardWidth) {
            entity.x = -entity.width;
        }
    }

    // Uppdaterad metod för att avgöra om ett spöke är inne i spökgården
    private boolean isGhostInCage(Block ghost) {
        // Definiera spökgårdens bounding box baserat på den nya layouten
        int cageX1 = 13 * tileSize;
        int cageY1 = 14 * tileSize;
        int cageX2 = 18 * tileSize;
        int cageY2 = 16 * tileSize;
        
        // Kontrollera om spöket är inom denna area
        return ghost.x >= cageX1 && ghost.x <= cageX2 && 
               ghost.y >= cageY1 && ghost.y <= cageY2;
    }

    // Uppdaterad metod för att släppa ut spöket från buren
    private void releaseGhostFromCage(Block ghost) {
        // Konstanter för spökgårdens utgång
        int exitX = 15 * tileSize + tileSize/2;
        int exitY = 12 * tileSize + tileSize/2; // Utgångens mittpunkt
        
        // Centrera spöket i x-led först för att sedan gå uppåt mot utgången
        int ghostCenterX = ghost.x + ghost.width / 2;
        int ghostCenterY = ghost.y + ghost.height / 2;
        
        // Spara gamla positionen
        int oldX = ghost.x;
        int oldY = ghost.y;
        
        // Beräkna riktning till utgången
        boolean moveVertically = Math.abs(ghostCenterY - exitY) > ghostSpeed;
        boolean moveHorizontally = Math.abs(ghostCenterX - exitX) > ghostSpeed;
        
        // Prioritera vertikal rörelse om vi är nära utgången
        if (ghostCenterY > exitY + tileSize) {
            // Vi är under utgången, rör oss uppåt
            ghost.y -= ghostSpeed;
            ghost.direction = 0; // Upp
        } else if (moveHorizontally) {
            // Flytta i x-led för att centrera med utgången
            if (ghostCenterX < exitX) {
                ghost.x += ghostSpeed;
                ghost.direction = 1; // Höger
            } else {
                ghost.x -= ghostSpeed;
                ghost.direction = 3; // Vänster
            }
        } else {
            // När spöket är centrerat, flytta det uppåt
            ghost.y -= ghostSpeed;
            ghost.direction = 0; // Upp
        }
        
        // Kontrollera för kollision med väggar
        if (checkGhostWallCollision(ghost, ghost.x, ghost.y)) {
            // Återställ position om kollision uppstår
            ghost.x = oldX;
            ghost.y = oldY;
            
            // Prova en annan riktning
            if (ghost.direction == 0 || ghost.direction == 2) { // Om vi rörde oss vertikalt
                // Prova horisontell rörelse
                if (ghostCenterX < exitX) {
                    ghost.x += ghostSpeed;
                    ghost.direction = 1; // Höger
                } else {
                    ghost.x -= ghostSpeed;
                    ghost.direction = 3; // Vänster
                }
            } else { // Om vi rörde oss horisontellt
                // Prova vertikal rörelse
                ghost.y -= ghostSpeed;
                ghost.direction = 0; // Upp
            }
            
            // Kontrollera igen för kollision
            if (checkGhostWallCollision(ghost, ghost.x, ghost.y)) {
                // Återställ om det fortfarande är kollision
                ghost.x = oldX;
                ghost.y = oldY;
            }
        }
        
        // Uppdatera tidigare position
        ghost.prevX = oldX;
        ghost.prevY = oldY;
    }

    // Ny metod för att röra spöket inuti buren
    private void moveGhostInCage(Block ghost) {
        // Låt spöket röra sig långsamt upp och ner i buren
        // för att skapa illusionen av att de väntar på att släppas ut
        int ghostCenterY = ghost.y + ghost.height / 2;
        int cageTop = 14 * tileSize + 5;
        int cageBottom = 16 * tileSize - 5;
        
        // Spara tidigare position
        int oldX = ghost.x;
        int oldY = ghost.y;
        
        // Om spöket rör sig uppåt
        if (ghost.direction == 0) {
            ghost.y -= ghost.height / 4;
            if (ghostCenterY <= cageTop) {
                // Byt riktning när spöket når toppen
                ghost.direction = 2;
            }
        } else {
            // Rör spöket nedåt
            ghost.y += ghost.height / 4;
            if (ghostCenterY >= cageBottom) {
                // Byt riktning när spöket når botten
                ghost.direction = 0;
            }
        }
        
        // Kontrollera kollision med väggar
        if (checkGhostWallCollision(ghost, ghost.x, ghost.y)) {
            // Återställ position om kollision
            ghost.x = oldX;
            ghost.y = oldY;
            // Vänd riktning
            ghost.direction = (ghost.direction == 0) ? 2 : 0;
        }
        
        // Uppdatera tidigare position
        ghost.prevX = oldX;
        ghost.prevY = oldY;
    }

    // Ny hjälpmetod för att bestämma spökögonens riktning baserat på rörelse
    private int getGhostEyeDirection(Block ghost) {
        // 0=höger, 1=ner, 2=vänster, 3=upp
        if (ghost.x > ghost.prevX) return 0; // Rör sig åt höger
        if (ghost.y > ghost.prevY) return 1; // Rör sig nedåt
        if (ghost.x < ghost.prevX) return 2; // Rör sig åt vänster
        if (ghost.y < ghost.prevY) return 3; // Rör sig uppåt
        
        // Använd spökets riktning om ingen rörelse detekterats
        return ghost.direction;
    }

    // Ny hjälpmetod för att hitta ett spöke baserat på dess typindex
    private Block findGhostByType(int typeIndex) {
        for (Block ghost : ghosts) {
            if (ghost.ghostIndex == typeIndex) {
                return ghost;
            }
        }
        return null; // Hittades inte
    }

    // --- LÄGG TILL DESSA TVÅ NYA HJÄLPMETODER --- 

    // Kontrollerar om Pac-Man kolliderar med en vägg vid given position
    private boolean checkPacManWallCollision(int x, int y) {
        // Använd en lite mindre rektangel för PacMan för att undvika att fastna på hörn
        int tolerance = 2;
        Rectangle pacmanRect = new Rectangle(x + tolerance, y + tolerance,
                                          pacman.width - 2 * tolerance, pacman.height - 2 * tolerance);
        for (Block wall : walls) {
            Rectangle wallRect = new Rectangle(wall.x, wall.y, wall.width, wall.height);
            if (pacmanRect.intersects(wallRect)) {
                return true; // Kollision!
            }
        }
        return false; // Ingen kollision
    }

    // Kontrollerar om Pac-Man *kan* röra sig till nästa ruta i en given riktning
    private boolean canMovePacMan(int currentX, int currentY, int direction) {
        int testX = currentX;
        int testY = currentY;
        // Beräkna positionen en pixel in i nästa ruta för att testa
        int testDistance = 1;

        if (direction == 0) testX += testDistance; // Höger
        else if (direction == 1) testY += testDistance; // Ner
        else if (direction == 2) testX -= testDistance; // Vänster
        else if (direction == 3) testY -= testDistance; // Upp

        // Använd kollisionskontrollen för att se om vägen är fri
        return !checkPacManWallCollision(testX, testY);
    }
    // --- SLUT PÅ NYA HJÄLPMETODER ---

    // --- LÄGG TILLBAKA checkFoodCollision HÄR --- 
    private void checkFoodCollision() {
        // Kontrollera om Pac-Man äter mat
        Iterator<Block> foodIterator = foods.iterator();
        while (foodIterator.hasNext()) {
            Block food = foodIterator.next();
            if (pacman.x < food.x + food.width && 
                pacman.x + pacman.width > food.x && 
                pacman.y < food.y + food.height && 
                pacman.y + pacman.height > food.y) {
                foodIterator.remove();
                score += foodPoints;
                soundManager.play("eat");
                
                // Visuell feedback när mat äts upp
                scoreDisplayText = "+" + foodPoints;
                scoreDisplayPosition.x = food.x;
                scoreDisplayPosition.y = food.y;
                scoreDisplayTimer = 20; // Visa i 20 frames
                
                break;
            }
        }
        
        // Kontrollera om Pac-Man äter power pellet
        Iterator<Block> powerPelletIterator = powerPellets.iterator();
        while (powerPelletIterator.hasNext()) {
            Block powerPellet = powerPelletIterator.next();
            if (pacman.x < powerPellet.x + powerPellet.width && 
                pacman.x + pacman.width > powerPellet.x && 
                pacman.y < powerPellet.y + powerPellet.height && 
                pacman.y + pacman.height > powerPellet.y) {
                powerPelletIterator.remove();
                score += pelletPoints;
                
                // Viktigt: Aktivera skrämt läge för spökena
                // ghostsScared = true; // Används ej globalt
                // scaredTimer = 0; // Används ej globalt
                
                // Återställ kombo-räknaren när en ny power pellet tas
                ghostCombo = 1; // Korrigerat från ghostCombo++ i en tidigare feltolkad edit
                
                // Uppdatera spökens utseende till skrämda
                for (Block ghost : ghosts) {
                    if (!ghost.isEaten && !ghost.inCage) { // Påverka bara spöken utanför buren/ej uppätna
                        ghost.isScared = true;  
                        ghost.scaredTimer = scaredDuration; 
                        updateGhostImage(ghost); // Uppdatera bilden direkt
                        // Tvinga vändning
                        int oppositeDirection = (ghost.direction + 2) % 4;
                        if(canMove(ghost.x, ghost.y, oppositeDirection)) {
                             ghost.direction = oppositeDirection;
                        }
                    }
                }
                
                soundManager.play("power");
                
                // Visuell feedback när power pellet äts upp
                scoreDisplayText = "+" + pelletPoints;
                scoreDisplayPosition.x = powerPellet.x;
                scoreDisplayPosition.y = powerPellet.y;
                scoreDisplayTimer = 30; // Visa lite längre
                
                break;
            }
        }
    }
    // --- SLUT PÅ checkFoodCollision --- 
}