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

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;

        int prevX;
        int prevY;

        boolean inCage;
        int cageTimer;
        int ghostIndex;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
            this.prevX = x;
            this.prevY = y;
            this.inCage = false;
            this.cageTimer = 0;
            this.ghostIndex = 0;
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
        "XooooooooooooXXooooooooooooooX",
        "XXXXXXoXXXXXoXXoXXXXXoXXXXXXXX",
        "     XoXXXXXoXXoXXXXXoX       ",
        "     XoXX          XXoX       ",
        "     XoXX XX    XX XXoX       ",
        "XXXXXXoXX X      X XXoXXXXXXXX",
        "      o   XrbpO  X   o        ",
        "XXXXXXoXX X      X XXoXXXXXXXX",
        "     XoXX XXXXXXXX XXoX       ",
        "     XoXX          XXoX       ",
        "     XoXX XXXXXXXXoXXoX       ",
        "XXXXXXoXX XXXXXXXXoXXoXXXXXXXX",
        "XooooooooooooXXoooooooooooooooX",
        "XoXXXXoXXXXXoXXoXXXXXoXXXXXoXoX",
        "XoXXXXoXXXXXoXXoXXXXXoXXXXXoXoX",
        "XEoXXoooooooPooooooooXXoXXoEooX",
        "XXoXXoXXoXXXXXXXXXoXXoXXoXXXXoX",
        "XXoXXoXXoXXXXXXXXXoXXoXXoXXXXoX",
        "XooooooooooooXXoooooooooooooooX",
        "XoXXXXXXXXXXoXXoXXXXXXXXXXXoXoX",
        "XoXXXXXXXXXXoXXoXXXXXXXXXXXoXoX",
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

    private boolean ghostsScared = false;
    private int scaredTimer = 0;
    private int scaredDuration = 300; // Hur länge spöken är skrämda (i uppdateringar)

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
                    ghosts.add(ghost);
                    System.out.println("Lagt till rött spöke på position " + ghost.x + "," + ghost.y);
                } else if (tile == 'p') {
                    // Rosa spöke
                    System.out.println("Hittade p på rad " + row + ", kolumn " + col);
                    Block ghost = new Block(ghostImages[1], col * tileSize, row * tileSize, tileSize, tileSize);
                    ghosts.add(ghost);
                    System.out.println("Lagt till rosa spöke på position " + ghost.x + "," + ghost.y);
                } else if (tile == 'b') {
                    // Blått spöke
                    System.out.println("Hittade b på rad " + row + ", kolumn " + col);
                    Block ghost = new Block(ghostImages[2], col * tileSize, row * tileSize, tileSize, tileSize);
                    ghosts.add(ghost);
                    System.out.println("Lagt till blått spöke på position " + ghost.x + "," + ghost.y);
                } else if (tile == 'O') {
                    // Orange spöke - ändrat från 'o' till 'O' för att skilja från mat
                    System.out.println("Hittade O på rad " + row + ", kolumn " + col);
                    Block ghost = new Block(ghostImages[3], col * tileSize, row * tileSize, tileSize, tileSize);
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
                
                if (gameOver || gameWon) {
                    if (keyCode == KeyEvent.VK_ENTER) {
                        resetGame();
                        return;
                    }
                }
                
                // Kontrollera att pacman inte är null innan vi försöker ändra riktning
                if (pacman == null) return;
                
                if (keyCode == KeyEvent.VK_UP) {
                    pacmanVelocityX = 0;
                    pacmanVelocityY = -pacmanSpeed;
                    pacManDirection = 3; // Upp
                    pacman.image = pacManImages[pacManDirection*2]; // Öppen mun
                } 
                else if (keyCode == KeyEvent.VK_DOWN) {
                    pacmanVelocityX = 0;
                    pacmanVelocityY = pacmanSpeed;
                    pacManDirection = 1; // Ner
                    pacman.image = pacManImages[pacManDirection*2]; // Öppen mun
                } 
                else if (keyCode == KeyEvent.VK_LEFT) {
                    pacmanVelocityX = -pacmanSpeed;
                    pacmanVelocityY = 0;
                    pacManDirection = 2; // Vänster
                    pacman.image = pacManImages[pacManDirection*2]; // Öppen mun
                }
                else if (keyCode == KeyEvent.VK_RIGHT) {
                    pacmanVelocityX = pacmanSpeed;
                    pacmanVelocityY = 0;
                    pacManDirection = 0; // Höger
                    pacman.image = pacManImages[pacManDirection*2]; // Öppen mun
                }
                else if (keyCode == KeyEvent.VK_M) {
                    // Tryck M för att slå på/av ljud
                    soundManager.setSoundEnabled(!soundManager.isSoundEnabled());
                }
                else if (keyCode == KeyEvent.VK_P) {
                    // Lägg till paus-funktion
                    gamePaused = !gamePaused;
                }
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
        if (!gameOver && !gamePaused) {
            // Uppdatera Pac-Man
            updatePacMan();
            
            // Uppdatera spöken
            moveGhosts();
            
            // Kontrollera kollision med spöken
            checkGhostCollision();
            
            // Uppdatera skrämd-timer
            if (ghostsScared) {
                scaredTimer++;
                if (scaredTimer >= scaredDuration) {
                    ghostsScared = false;
                    
                    // Återställ spökbilder
                    int ghostIndex = 0;
                    for (Block ghost : ghosts) {
                        ghost.image = ghostImages[ghostIndex % 4];
                        ghostIndex++;
                    }
                }
            }
            
            // Kontrollera om alla pellets är uppätna
            checkLevelComplete();
            
            // Uppdatera animationer
            updateAnimation();
        }
        
        // Tvinga omritning
        repaint();
    }
    
    private void updatePacMan() {
        // Kontrollera att pacman inte är null
        if (pacman == null) return;
        
        // Uppdatera Pac-Man-position
        int newX = pacman.x + pacmanVelocityX;
        int newY = pacman.y + pacmanVelocityY;
        
        // Hantera tunnlar på sidorna - förbättrad tunnelhantering
        if (newX < -pacman.width) {
            newX = boardWidth;
        } else if (newX > boardWidth) {
            newX = -pacman.width;
        }
        
        // Hantera tunnlar vertikalt också
        if (newY < -pacman.height) {
            newY = boardHeight;
        } else if (newY > boardHeight) {
            newY = -pacman.height;
        }
        
        // Kontrollera kollision med väggar
        boolean collision = false;
        
        // Minskat toleransen för mer precis styrning
        int collisionTolerance = 5; 
        for (Block wall : walls) {
            // Förbättrad kollisionsdetektering med mindre tolerans
            Rectangle pacmanRect = new Rectangle(newX + collisionTolerance/2, newY + collisionTolerance/2, 
                                               pacman.width - collisionTolerance, pacman.height - collisionTolerance);
            Rectangle wallRect = new Rectangle(wall.x, wall.y, wall.width, wall.height);
            
            if (pacmanRect.intersects(wallRect)) {
                collision = true;
                break;
            }
        }
        
        // Uppdatera position om ingen kollision
        if (!collision) {
            pacman.x = newX;
            pacman.y = newY;
            
            // Animera Pac-Man om han rör sig
            if (pacmanVelocityX != 0 || pacmanVelocityY != 0) {
                animationCounter++;
                if (animationCounter >= animationSpeed) {
                    pacManMouthOpen = !pacManMouthOpen;
                    animationCounter = 0;
                    
                    // Uppdatera Pac-Man-bilden baserat på riktning och om munnen är öppen
                    int imageIndex = pacManDirection * 2;
                    if (!pacManMouthOpen) {
                        imageIndex += 1;
                    }
                    pacman.image = pacManImages[imageIndex];
                }
            } else {
                // Om Pac-Man står stilla, visa honom med stängd mun
                pacman.image = pacManImages[pacManDirection * 2 + 1];
            }
            
            // Automatisk ätning av mat och power pellets
            checkFoodCollision();
        } else {
            // Förbättrad automatisk justering vid kollision
            
            // Om vi kolliderar, försök att justera position till närmaste ruta
            int tileX = Math.round((float)pacman.x / tileSize) * tileSize;
            int tileY = Math.round((float)pacman.y / tileSize) * tileSize;
            
            // Beräkna avståndet mellan nuvarande position och rutnätets position
            int deltaX = Math.abs(pacman.x - tileX);
            int deltaY = Math.abs(pacman.y - tileY);
            
            if (pacmanVelocityX != 0) {
                // Vi rör oss horisontellt, justera vertikalt om nära
                if (deltaY < tileSize/3) {
                    newY = tileY;
                    newX = pacman.x;
                    
                    // Kolla kollision igen
                    collision = false;
                    for (Block wall : walls) {
                        Rectangle pacmanRect = new Rectangle(newX + collisionTolerance/2, newY + collisionTolerance/2, 
                                                            pacman.width - collisionTolerance, pacman.height - collisionTolerance);
                        Rectangle wallRect = new Rectangle(wall.x, wall.y, wall.width, wall.height);
                        
                        if (pacmanRect.intersects(wallRect)) {
                            collision = true;
                            break;
                        }
                    }
                    
                    if (!collision) {
                        pacman.y = newY;
                    }
                }
            } else if (pacmanVelocityY != 0) {
                // Vi rör oss vertikalt, justera horisontellt om nära
                if (deltaX < tileSize/3) {
                    newX = tileX;
                    newY = pacman.y;
                    
                    // Kolla kollision igen
                    collision = false;
                    for (Block wall : walls) {
                        Rectangle pacmanRect = new Rectangle(newX + collisionTolerance/2, newY + collisionTolerance/2, 
                                                            pacman.width - collisionTolerance, pacman.height - collisionTolerance);
                        Rectangle wallRect = new Rectangle(wall.x, wall.y, wall.width, wall.height);
                        
                        if (pacmanRect.intersects(wallRect)) {
                            collision = true;
                            break;
                        }
                    }
                    
                    if (!collision) {
                        pacman.x = newX;
                    }
                }
            }
        }
    }
    
    // Separerar kollisionslogiken för att äta mat för renare kod
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
                ghostsScared = true;
                scaredTimer = 0;
                // Återställ combo-räknaren när en ny power pellet tas
                ghostCombo = 1;
                soundManager.play("power");
                
                // Visuell feedback när power pellet äts upp
                scoreDisplayText = "+" + pelletPoints;
                scoreDisplayPosition.x = powerPellet.x;
                scoreDisplayPosition.y = powerPellet.y;
                scoreDisplayTimer = 30; // Visa lite längre
                
                // Ändra spökenas utseende
                for (Block ghost : ghosts) {
                    ghost.image = scaredGhostImage;
                }
                break;
            }
        }
    }
    
    private void moveGhosts() {
        if (ghostsScared && scaredTimer == 0) {
            // Återställ spökena till normalläge när timer slut
            ghostsScared = false;
            ghostCombo = 1; // Återställ kombo
        }
        
        // Uppdatera rädslotimern om aktiv
        if (ghostsScared && scaredTimer > 0) {
            scaredTimer--;
        }
        
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
            // Kontrollera om spöket är inne i spökgården
            boolean isInCage = isGhostInCage(ghost);
            
            if (isInCage && !ghostsReleased[ghostIndex]) {
                // Rör spöket upp och ner inuti buren om det inte är utsläppt
                moveGhostInCage(ghost);
                ghostIndex++;
                continue;
            } else if (isInCage && ghostsReleased[ghostIndex]) {
                // Om spöket är i buren men ska vara utsläppt, flytta det uppåt mot utgången
                releaseGhostFromCage(ghost);
                ghostIndex++;
                continue;
            }
            
            // Hantera rörelserna för de utsläppta spökena
            if (!ghostsScared) {
                // Uppdatera spökets ögonriktning baserat på dess rörelseriktning
                int eyeDirection = getGhostEyeDirection(ghost);
                
                if (ghostIndex == 0) { // Rött spöke (Blinky) - jagar Pac-Man direkt
                    // Blinky jagar Pac-Man direkt (target = Pac-Man's position)
                    moveGhostTowardTarget(ghost, pacman.x, pacman.y, 0.95);
                    if (!ghostsScared) {
                        ghost.image = GameImages.createRedGhostImage(eyeDirection);
                    }
                } else if (ghostIndex == 1) { // Rosa spöke (Pinky) - försöker hamna framför Pac-Man
                    // Pinky försöker förutse vart Pac-Man är på väg och skära av
                    int targetX = pacman.x;
                    int targetY = pacman.y;
                    
                    // Beräkna en position 4 rutor framför Pac-Man baserat på hans riktning
                    if (pacmanVelocityX > 0) { // Pacman rör sig åt höger
                        targetX += 4 * tileSize;
                    } else if (pacmanVelocityX < 0) { // Pacman rör sig åt vänster
                        targetX -= 4 * tileSize;
                    } else if (pacmanVelocityY > 0) { // Pacman rör sig nedåt
                        targetY += 4 * tileSize;
                    } else if (pacmanVelocityY < 0) { // Pacman rör sig uppåt
                        // Simulera originalspelets "bug" där Pinky också siktar 4 rutor åt vänster
                        targetY -= 4 * tileSize;
                        targetX -= 4 * tileSize;
                    }
                    
                    moveGhostTowardTarget(ghost, targetX, targetY, 0.90);
                    if (!ghostsScared) {
                        ghost.image = GameImages.createPinkGhostImage(eyeDirection);
                    }
                } else if (ghostIndex == 2) { // Blått spöke (Inky) - Kombination baserad på Blinky
                    // Inky använder en kombination av Pac-Man och Blinky för att bestämma målpunkt
                    // Först, hitta en punkt 2 rutor framför Pac-Man
                    int intermediateX = pacman.x;
                    int intermediateY = pacman.y;
                    
                    if (pacmanVelocityX > 0) { // Pacman rör sig åt höger
                        intermediateX += 2 * tileSize;
                    } else if (pacmanVelocityX < 0) { // Pacman rör sig åt vänster
                        intermediateX -= 2 * tileSize;
                    } else if (pacmanVelocityY > 0) { // Pacman rör sig nedåt
                        intermediateY += 2 * tileSize;
                    } else if (pacmanVelocityY < 0) { // Pacman rör sig uppåt
                        // Simulera samma "bug" som Pinky
                        intermediateY -= 2 * tileSize;
                        intermediateX -= 2 * tileSize;
                    }
                    
                    // Hitta Blinky (rött spöke)
                    Block blinky = null;
                    int blinkyIndex = 0;
                    for (Block g : ghosts) {
                        if (blinkyIndex == 0) {
                            blinky = g;
                            break;
                        }
                        blinkyIndex++;
                    }
                    
                    // Beräkna Inky's målpunkt genom att "dubbla" vektorn från Blinky till mellanpunkten
                    int targetX = intermediateX;
                    int targetY = intermediateY;
                    
                    if (blinky != null) {
                        // Beräkna vektorn från Blinky till mellanpunkten
                        int vectorX = intermediateX - blinky.x;
                        int vectorY = intermediateY - blinky.y;
                        
                        // Dubbla vektorn för att få målpunkten
                        targetX = intermediateX + vectorX;
                        targetY = intermediateY + vectorY;
                    }
                    
                    moveGhostTowardTarget(ghost, targetX, targetY, 0.85);
                    if (!ghostsScared) {
                        ghost.image = GameImages.createCyanGhostImage(eyeDirection);
                    }
                } else { // Orange spöke (Clyde) - växlar mellan jakt och att fly
                    // Clyde jagar Pac-Man när han är långt borta men flyr när han är nära
                    double distanceToPacman = Math.sqrt(
                        Math.pow(ghost.x - pacman.x, 2) + 
                        Math.pow(ghost.y - pacman.y, 2)
                    );
                    
                    if (distanceToPacman > 8 * tileSize) {
                        // När långt borta, jaga Pac-Man
                        moveGhostTowardTarget(ghost, pacman.x, pacman.y, 0.75);
                    } else {
                        // När nära, rör sig mot nedre vänstra hörnet
                        moveGhostTowardTarget(ghost, 0, boardHeight, 0.75);
                    }
                    if (!ghostsScared) {
                        ghost.image = GameImages.createOrangeGhostImage(eyeDirection);
                    }
                }
            } else {
                // När spökena är skrämda, rör de sig slumpmässigt och försöker undvika Pac-Man
                moveGhostRandomly(ghost);
                
                // Blinka när powerup håller på att ta slut (sista 1/3 av tiden)
                boolean shouldBlink = scaredTimer < scaredDuration / 3 && (scaredTimer / 5) % 2 == 0;
                ghost.image = GameImages.createScaredGhostImage(shouldBlink);
            }
            
            ghostIndex++;
        }
    }
    
    // Uppdaterad hjälpmetod för att avgöra om ett spöke är inne i spökgården
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
    
    // Ny hjälpmetod för att bestämma spökögonens riktning baserat på rörelse
    private int getGhostEyeDirection(Block ghost) {
        // 0=höger, 1=ner, 2=vänster, 3=upp
        if (ghost.x > ghost.prevX) return 0; // Rör sig åt höger
        if (ghost.y > ghost.prevY) return 1; // Rör sig nedåt
        if (ghost.x < ghost.prevX) return 2; // Rör sig åt vänster
        if (ghost.y < ghost.prevY) return 3; // Rör sig uppåt
        
        // Standard om ingen rörelse detekterats
        return 0; 
    }
    
    // Ny metod för att röra spöket inuti buren
    private void moveGhostInCage(Block ghost) {
        // Låt spöket röra sig långsamt upp och ner i buren
        // för att skapa illusionen av att de väntar på att släppas ut
        int ghostCenterY = ghost.y + ghost.height / 2;
        int cageTop = 14 * tileSize + 5;
        int cageBottom = 16 * tileSize - 5;
        
        // Om spöket rör sig uppåt
        if (ghost.prevY > ghost.y) {
            if (ghostCenterY <= cageTop) {
                // Byt riktning när spöket når toppen
                ghost.prevY = ghost.y;
                ghost.y += 1;
            } else {
                ghost.prevY = ghost.y;
                ghost.y -= 1;
            }
        }
        // Om spöket rör sig nedåt eller inte rör sig alls
        else {
            if (ghostCenterY >= cageBottom) {
                // Byt riktning när spöket når botten
                ghost.prevY = ghost.y;
                ghost.y -= 1;
            } else {
                ghost.prevY = ghost.y;
                ghost.y += 1;
            }
        }
    }
    
    // Uppdaterad metod för att släppa ut spöket från buren
    private void releaseGhostFromCage(Block ghost) {
        // Konstanter för spökgårdens utgång
        int exitX = 15 * tileSize + tileSize/2;
        int exitY = 12 * tileSize; // Utgången är en rad högre upp
        
        // Centrera spöket i x-led först för att sedan gå uppåt mot utgången
        int ghostCenterX = ghost.x + ghost.width / 2;
        
        // Om vi är tillräckligt nära utgången i y-led, fokusera på att centrera i x-led
        if (ghost.y <= 13 * tileSize) {
            // Om vi är vid eller över utgången, rör oss uppåt och ignorera x-centrering
            ghost.prevY = ghost.y;
            ghost.y -= ghostSpeed;
            
            // Uppdatera spökets riktning till uppåt (3)
            int ghostIndex = 0;
            for (Block g : ghosts) {
                if (g == ghost) {
                    if (!ghostsScared) {
                        ghost.image = GameImages.createGhostImage(getGhostColor(ghostIndex), 3);
                    }
                    break;
                }
                ghostIndex++;
            }
        } 
        else if (Math.abs(ghostCenterX - exitX) > ghostSpeed) {
            // Flytta mot mitten av utgången först
            ghost.prevX = ghost.x;
            ghost.x += (ghostCenterX < exitX) ? ghostSpeed : -ghostSpeed;
            
            // Uppdatera spökets riktning baserat på dess rörelse
            int ghostIndex = 0;
            for (Block g : ghosts) {
                if (g == ghost) {
                    if (!ghostsScared) {
                        int eyeDirection = (ghostCenterX < exitX) ? 0 : 2; // 0=höger, 2=vänster
                        ghost.image = GameImages.createGhostImage(getGhostColor(ghostIndex), eyeDirection);
                    }
                    break;
                }
                ghostIndex++;
            }
        } 
        else {
            // När spöket är centrerat, flytta det uppåt
            ghost.prevY = ghost.y;
            ghost.y -= ghostSpeed;
            
            // Uppdatera spökets riktning till uppåt (3)
            int ghostIndex = 0;
            for (Block g : ghosts) {
                if (g == ghost) {
                    if (!ghostsScared) {
                        ghost.image = GameImages.createGhostImage(getGhostColor(ghostIndex), 3);
                    }
                    break;
                }
                ghostIndex++;
            }
        }
    }
    
    private Color getGhostColor(int ghostIndex) {
        switch (ghostIndex % 4) {
            case 0: return new Color(255, 0, 0);      // Röd (Blinky)
            case 1: return new Color(255, 192, 203);  // Rosa (Pinky)
            case 2: return new Color(0, 255, 255);    // Cyan (Inky)
            case 3: return new Color(255, 165, 0);    // Orange (Clyde)
            default: return new Color(255, 0, 0);     // Default är röd
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
            
            Rectangle ghostRect = new Rectangle(ghost.x, ghost.y, ghost.width, ghost.height);
            
            if (pacmanRect.intersects(ghostRect)) {
                if (ghostsScared) {
                    // Beräkna poäng baserat på hur många spöken som ätits under samma power pellet
                    int ghostValue = 200 * (int)Math.pow(2, ghostCombo - 1);
                    ghostValue = Math.min(ghostValue, 1600); // Max 1600 poäng
                    addScore(ghostValue, ghost.x, ghost.y);
                    
                    // Återställ spöket till startposition
                    ghost.x = ghost.startX;
                    ghost.y = ghost.startY;
                    ghost.inCage = true;
                    ghost.cageTimer = random.nextInt(60) + 60; // 1-2 sekunder i buren
                    
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
    private void moveGhostTowardTarget(Block ghost, int targetX, int targetY, double chanceToFollow) {
        // Tillgängliga riktningar (inte tillåtet att vända om direkt för spöken)
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}}; // Höger, Ner, Vänster, Upp
        
        // Hitta aktuell riktning för spöket
        int currentDx = 0;
        int currentDy = 0;
        
        // Beräkna spökets nuvarande riktning baserat på föregående flyttning
        if (ghost.prevX < ghost.x) currentDx = 1;
        else if (ghost.prevX > ghost.x) currentDx = -1;
        else if (ghost.prevY < ghost.y) currentDy = 1;
        else if (ghost.prevY > ghost.y) currentDy = -1;
        
        // Motsatt riktning (inte tillåten)
        int oppositeX = -currentDx;
        int oppositeY = -currentDy;
        
        // Kontrollera om spöket befinner sig i mitten av en ruta
        // Detta förhindrar att spökena ändrar riktning mitt i en korridor och ger dem ett jämnare rörelsemönster
        boolean atIntersection = false;
        int tileX = Math.round((float)ghost.x / tileSize) * tileSize;
        int tileY = Math.round((float)ghost.y / tileSize) * tileSize;
        
        if (Math.abs(ghost.x - tileX) < ghostSpeed && Math.abs(ghost.y - tileY) < ghostSpeed) {
            // Justera positionen exakt till rutnätet om vi är nära
            ghost.x = tileX;
            ghost.y = tileY;
            atIntersection = true;
        }
        
        // Om spöket inte är i en korsning, fortsätt i samma riktning
        if (!atIntersection) {
            int dx = currentDx * ghostSpeed;
            int dy = currentDy * ghostSpeed;
            
            // Om vi inte rört oss tidigare, välj en standardriktning (höger)
            if (currentDx == 0 && currentDy == 0) {
                dx = ghostSpeed;
            }
            
            // Kontrollera om vi skulle kollidera med en vägg
            int newX = ghost.x + dx;
            int newY = ghost.y + dy;
            
            if (checkGhostWallCollision(ghost, newX, newY)) {
                // Vi har stött på en vägg, tvinga spöket att ta ett beslut vid nästa ruta
                atIntersection = true;
            } else {
                // Spara tidigare position och uppdatera position
                ghost.prevX = ghost.x;
                ghost.prevY = ghost.y;
                ghost.x = newX;
                ghost.y = newY;
                return;
            }
        }
        
        // Om vi kommit hit är spöket antingen i en korsning eller har stött på en vägg
        // Beräkna alla möjliga vägar vid nästa korsning
        List<int[]> possibleDirections = new ArrayList<>();
        
        for (int[] dir : directions) {
            // Hoppa över om detta är motsatt riktning (spöken får inte vända om)
            if (dir[0] == oppositeX && dir[1] == oppositeY) {
                continue;
            }
            
            // Testa om detta är en giltig riktning (ingen vägg)
            int newX = ghost.x + dir[0] * ghostSpeed;
            int newY = ghost.y + dir[1] * ghostSpeed;
            
            if (!checkGhostWallCollision(ghost, newX, newY)) {
                possibleDirections.add(dir);
            }
        }
        
        // Om det inte finns några möjliga riktningar, vänd om (sällsynt specialfall)
        if (possibleDirections.isEmpty()) {
            possibleDirections.add(new int[]{oppositeX, oppositeY});
        }
        
        // Beräkna avstånd för varje möjlig riktning
        int bestDirectionIndex = -1;
        double shortestDistance = Double.MAX_VALUE;
        
        for (int i = 0; i < possibleDirections.size(); i++) {
            int[] dir = possibleDirections.get(i);
            
            // Beräkna en position flera rutor framåt i denna riktning
            // för att bättre simulera spökenas framförhållning
            int lookaheadFactor = 5; // Titta 5 rutor framåt
            int newX = ghost.x;
            int newY = ghost.y;
            
            // Försök röra oss i denna riktning tills vi når en vägg eller lookaheadFactor
            for (int step = 0; step < lookaheadFactor; step++) {
                int tempX = newX + dir[0] * ghostSpeed;
                int tempY = newY + dir[1] * ghostSpeed;
                
                if (checkGhostWallCollision(ghost, tempX, tempY)) {
                    break;
                }
                
                newX = tempX;
                newY = tempY;
            }
            
            // Beräkna avstånd till målpunkt
            double distance = Math.sqrt(
                Math.pow(newX - targetX, 2) + 
                Math.pow(newY - targetY, 2)
            );
            
            if (distance < shortestDistance) {
                shortestDistance = distance;
                bestDirectionIndex = i;
            }
        }
        
        // Välj den bästa riktningen med viss sannolikhet, annars slumpmässig
        // Högre sannolikhet ger mer aggressiva spöken
        int[] chosenDir;
        if (bestDirectionIndex != -1 && random.nextDouble() < chanceToFollow) {
            chosenDir = possibleDirections.get(bestDirectionIndex);
        } else {
            // Ibland välj en slumpmässig riktning
            chosenDir = possibleDirections.get(random.nextInt(possibleDirections.size()));
        }
        
        // Spara tidigare position innan rörelse
        ghost.prevX = ghost.x;
        ghost.prevY = ghost.y;
        
        // Uppdatera position
        ghost.x += chosenDir[0] * ghostSpeed;
        ghost.y += chosenDir[1] * ghostSpeed;
        
        // Hantera tunnlar på sidorna
        if (ghost.x < -ghost.width) {
            ghost.x = boardWidth;
        } else if (ghost.x > boardWidth) {
            ghost.x = -ghost.width;
        }
    }
    
    private void moveGhostRandomly(Block ghost) {
        // Kontrollera om vi är i en korsning
        boolean atIntersection = false;
        int tileX = Math.round((float)ghost.x / tileSize) * tileSize;
        int tileY = Math.round((float)ghost.y / tileSize) * tileSize;
        
        if (Math.abs(ghost.x - tileX) < ghostSpeed && Math.abs(ghost.y - tileY) < ghostSpeed) {
            // Justera positionen exakt till rutnätet om vi är nära
            ghost.x = tileX;
            ghost.y = tileY;
            atIntersection = true;
        }
        
        // Hitta aktuell riktning för spöket
        int currentDx = 0;
        int currentDy = 0;
        
        // Beräkna spökets nuvarande riktning baserat på föregående flyttning
        if (ghost.prevX < ghost.x) currentDx = 1;
        else if (ghost.prevX > ghost.x) currentDx = -1;
        else if (ghost.prevY < ghost.y) currentDy = 1;
        else if (ghost.prevY > ghost.y) currentDy = -1;
        
        // Om vi inte är i en korsning, fortsätt i samma riktning om möjligt
        if (!atIntersection) {
            int newX = ghost.x + currentDx * ghostSpeed;
            int newY = ghost.y + currentDy * ghostSpeed;
            
            // Om ingen kollision, fortsätt samma riktning
            if (!checkGhostWallCollision(ghost, newX, newY)) {
                ghost.prevX = ghost.x;
                ghost.prevY = ghost.y;
                ghost.x = newX;
                ghost.y = newY;
                return;
            }
            
            // Annars, behandla som om vi var i en korsning
        }
        
        // Vi är i en korsning eller har stött på en vägg
        // Samla alla möjliga riktningar vi kan gå i (inte inklusive bakåt)
        List<int[]> possibleDirections = new ArrayList<>();
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}}; // Höger, Ner, Vänster, Upp
        
        for (int[] dir : directions) {
            // Hoppa över om detta är motsatt riktning (spöken får inte vända om)
            if (dir[0] == -currentDx && dir[1] == -currentDy) {
                continue;
            }
            
            // Testa om detta är en giltig riktning (ingen vägg)
            int newX = ghost.x + dir[0] * ghostSpeed;
            int newY = ghost.y + dir[1] * ghostSpeed;
            
            if (!checkGhostWallCollision(ghost, newX, newY)) {
                possibleDirections.add(dir);
            }
        }
        
        // Om det inte finns några möjliga riktningar, vänd om (sällsynt specialfall)
        if (possibleDirections.isEmpty()) {
            possibleDirections.add(new int[]{-currentDx, -currentDy});
        }
        
        // Välj en slumpmässig giltig riktning
        int[] chosenDir = possibleDirections.get(random.nextInt(possibleDirections.size()));
        
        // Spara tidigare position och uppdatera position
        ghost.prevX = ghost.x;
        ghost.prevY = ghost.y;
        ghost.x += chosenDir[0] * ghostSpeed;
        ghost.y += chosenDir[1] * ghostSpeed;
        
        // Hantera tunnlar på sidorna
        if (ghost.x < -ghost.width) {
            ghost.x = boardWidth;
        } else if (ghost.x > boardWidth) {
            ghost.x = -ghost.width;
        }
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
        
        // Visa "READY!" text igen
        showReady = true;
        readyTimer = 120;
    }

    // Uppdatera updateGame-metoden för att hantera timers för retro-element
    private void updateGame() {
        if (gameOver || gameWon || gamePaused) {
            return;
        }
        
        // Hantera "READY!" text vid start
        if (showReady) {
            readyTimer--;
            if (readyTimer <= 0) {
                showReady = false;
            }
            return; // Ingen rörelse medan "READY!" visas
        }
        
        // Uppdatera animationsräknare
        animationCounter++;
        if (animationCounter % animationSpeed == 0) {
            pacManMouthOpen = !pacManMouthOpen;
        }
        
        // Uppdatera frukttimer
        if (showFruit) {
            fruitTimer--;
            if (fruitTimer <= 0) {
                showFruit = false;
            }
        } else {
            // Slumpmässigt visa frukt var 10:e sekund
            if (random.nextInt(600) == 1) { // Ungefär var 10:e sekund med 60 FPS
                showFruit();
            }
        }
        
        // Uppdatera scared-timer för spöken
        if (scaredTimer > 0) {
            scaredTimer--;
        }
        
        // Uppdatera timer för poängvisning
        if (pointsDisplayTimer > 0) {
            pointsDisplayTimer--;
        }
        
        // ... (befintlig kod för rörelse och kollisioner) ...
    }
}