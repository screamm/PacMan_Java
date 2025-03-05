import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;
import java.util.Iterator;

public class PacMan extends JPanel {

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }
    }

    private int rowCount = 23;
    private int columnCount = 31;
    private int tileSize = 40; // Större rutor för bättre spelupplevelse
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    
    private Image wallImage;
    private Image foodImage;
    private Image powerPelletImage;

    private int pacmanVelocityX = 0;
    private int pacmanVelocityY = 0;
    private int pacmanSpeed = 3; // Anpassad hastighet
    private int pacManDirection = 0; // 0=höger, 1=ner, 2=vänster, 3=upp
    private boolean pacManMouthOpen = true;
    private int animationCounter = 0;
    private int animationSpeed = 3; // Lägre värde = snabbare animation
    private Timer gameTimer;
    private Random random = new Random();
    private int ghostSpeed = 3; // Ökad hastighet för att spökena ska röra sig bättre
    private boolean gameOver = false;
    private boolean gameWon = false;
    private int score = 0;
    private int lives = 3;
    
    // Ljudhanterare
    private SoundManager soundManager;
    
    private boolean gamePaused = false;
    
    // Visuella förbättringar
    private Color backgroundColor = new Color(0, 0, 20); // Mycket mörk blå, nästan svart
    private Font gameFont = new Font("Arial", Font.BOLD, 30);
    private Font titleFont = new Font("Arial", Font.BOLD, 60);
    
    // Halverad spelplan för bättre proportioner med större rutor
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        "XooooooooooooXXoooooooooooooooX",
        "XoXXXXoXXXXXoXXoXXXXXoXXXXXoXoX",
        "XEXoXXoXXXXXoXXoXXXXXoXXXXXoEoX",
        "XoXoXXoXXXXXoXXoXXXXXoXXXXXoXoX",
        "XoooooooooooooooooooooooooooooX",
        "XoXXXXoXXoXXXXXXXXXoXXoXXXXoXoX",
        "XoXXXXoXXooooXXooooXXoXXXXoXoX",
        "XoXXXXoXXoXXoXXoXXoXXoXXXXoXoX",
        "XooooooXXoXXrbpoXXoXXooooooooX",
        "XXXXXXoXXoXXXXXXXXXoXXoXXXXXXX",
        "XXXXXXoXXoooooooooooXXoXXXXXXX",
        "XXXXXXoXXoXXXXXXXXXoXXoXXXXXXX",
        "ooooooooooXXX   XXXoooooooooo",
        "XXXXXXoXXoXXX   XXXoXXoXXXXXXX",
        "XXXXXXoXXoooooooooooXXoXXXXXXX",
        "XXXXXXoXXoXXXXXXXXXoXXoXXXXXXX",
        "XooooooXXooooXXooooXXooooooooX",
        "XoXXXXoXXXXXoXXoXXXXXoXXXXoXoX",
        "XoXXXXoXXXXXoXXoXXXXXoXXXXoXoX",
        "XEoXXXooooooPooooooooXXXXXoEoX",
        "XoXXXXoXXXXXoXXoXXXXXoXXXXoXoX",
        "XooooooooooooXXoooooooooooooooX",
        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> ghosts;
    HashSet<Block> foods;
    HashSet<Block> powerPellets;
    Block pacman;    

    private boolean[] ghostsReleased = new boolean[10]; // För att hålla reda på vilka spöken som släppts ut
    private int ghostReleaseTimer = 0;
    private int ghostReleaseInterval = 100; // Minskat intervall för att släppa ut spöken snabbare

    private Image[] ghostImages;
    private Image[] pacManImages;
    private Image scaredGhostImage;

    private boolean ghostsScared = false;
    private int scaredTimer = 0;
    private int scaredDuration = 300; // Hur länge spöken är skrämda (i uppdateringar)

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
                } else if (tile == 'o') {
                    // Orange spöke - ändra detta till 'O' (stor bokstav) för att skilja från mat
                    System.out.println("Hittade o på rad " + row + ", kolumn " + col);
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
            }
        };
        
        this.setFocusable(true);
        this.addKeyListener(keyAdapter);
        
        // Game timer
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
            updateGhosts();
            
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
        
        // Hantera tunnlar på sidorna
        if (newX < -pacman.width/2) {
            newX = boardWidth - pacman.width/2;
        } else if (newX > boardWidth - pacman.width/2) {
            newX = -pacman.width/2;
        }
        
        // Kontrollera kollision med väggar
        boolean collision = false;
        for (Block wall : walls) {
            if (newX < wall.x + wall.width && 
                newX + pacman.width > wall.x && 
                newY < wall.y + wall.height && 
                newY + pacman.height > wall.y) {
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
                    if (pacManMouthOpen) {
                        pacman.image = pacManImages[pacManDirection*2]; // Öppen mun
                    } else {
                        pacman.image = pacManImages[pacManDirection*2 + 1]; // Stängd mun
                    }
                }
            }
            
            // Kontrollera om Pac-Man äter mat
            Iterator<Block> foodIterator = foods.iterator();
            while (foodIterator.hasNext()) {
                Block food = foodIterator.next();
                if (pacman.x < food.x + food.width && 
                    pacman.x + pacman.width > food.x && 
                    pacman.y < food.y + food.height && 
                    pacman.y + pacman.height > food.y) {
                    foodIterator.remove();
                    score += 10;
                    soundManager.play("eat");
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
                    score += 50;
                    ghostsScared = true;
                    scaredTimer = 0;
                    soundManager.play("power");
                    
                    // Ändra spökenas utseende
                    for (Block ghost : ghosts) {
                        ghost.image = scaredGhostImage;
                    }
                    break;
                }
            }
        }
    }
    
    private void updateAnimation() {
        // Uppdatera animationsramen
        animationCounter++;
        if (animationCounter >= animationSpeed) {
            pacManMouthOpen = !pacManMouthOpen;
            animationCounter = 0;
            
            // Uppdatera Pac-Man-bilden baserat på riktning och munöppning
            if (pacmanVelocityX != 0 || pacmanVelocityY != 0) {
                int imageIndex = pacManDirection * 2;
                if (!pacManMouthOpen) {
                    imageIndex += 1;
                }
                pacman.image = pacManImages[imageIndex];
            }
        }
    }
    
    private void updateGhosts() {
        // Uppdatera timer för att släppa ut spöken
        ghostReleaseTimer++;
        
        // Begränsa antalet spöken till 4
        int maxGhosts = 4;
        int ghostIndex = 0;
        
        for (Block ghost : ghosts) {
            // Hoppa över spöken om vi redan har 4 aktiva
            if (ghostIndex >= maxGhosts) {
                break;
            }
            
            // Kontrollera om spöket ska vara kvar i spökgården
            if (ghostIndex < ghostsReleased.length && !ghostsReleased[ghostIndex]) {
                // Släpp ut spöken med jämna mellanrum
                if (ghostReleaseTimer >= ghostReleaseInterval * (ghostIndex + 1)) {
                    ghostsReleased[ghostIndex] = true;
                }
                
                // Låt spöket röra sig lite upp och ner i spökgården medan det väntar
                if (ghostReleaseTimer % 40 < 20) {
                    ghost.y += 1;
                } else {
                    ghost.y -= 1;
                }
                
                ghostIndex++;
                continue; // Gå till nästa spöke
            }
            
            // Tvinga ut spöken från spökgården om de fastnat
            if (isGhostInCage(ghost)) {
                // Flytta spöket uppåt för att komma ut ur spökgården
                ghost.y -= ghostSpeed * 2;
                ghostIndex++;
                continue;
            }
            
            // Spökrörelser när de är ute ur spökgården
            if (!ghostsScared) {
                // Olika beteende baserat på spökfärg
                if (ghostIndex == 0) { // Rött spöke (Blinky) - jagar Pac-Man direkt
                    moveGhostTowardsPacman(ghost, 0.9);
                } else if (ghostIndex == 1) { // Rosa spöke (Pinky) - försöker gå framför Pac-Man
                    moveGhostAhead(ghost, 0.8);
                } else if (ghostIndex == 2) { // Blått spöke (Inky) - kombination av Blinky och slumpmässighet
                    if (random.nextInt(100) < 70) {
                        moveGhostTowardsPacman(ghost, 0.7);
                    } else {
                        moveGhostRandomly(ghost);
                    }
                } else { // Orange spöke (Clyde) - mest slumpmässigt
                    if (random.nextInt(100) < 50) {
                        moveGhostTowardsPacman(ghost, 0.5);
                    } else {
                        moveGhostRandomly(ghost);
                    }
                }
            } else {
                // När spökena är skrämda, rör de sig slumpmässigt
                moveGhostRandomly(ghost);
            }
            
            ghostIndex++;
        }
    }
    
    // Kontrollera om spöket är i spökgården
    private boolean isGhostInCage(Block ghost) {
        // Definiera spökgårdens område baserat på kartan
        int cageMinX = 13 * tileSize;
        int cageMaxX = 17 * tileSize;
        int cageMinY = 8 * tileSize;
        int cageMaxY = 10 * tileSize;
        
        return ghost.x >= cageMinX && ghost.x <= cageMaxX && 
               ghost.y >= cageMinY && ghost.y <= cageMaxY;
    }
    
    private void moveGhostTowardsPacman(Block ghost, double chanceToFollow) {
        if (pacman == null) return;
        
        // Beräkna riktning mot Pac-Man
        int dx = 0;
        int dy = 0;
        
        // Bestäm riktning baserat på Pac-Man's position
        if (random.nextDouble() < chanceToFollow) {
            if (ghost.x < pacman.x) dx = ghostSpeed;
            else if (ghost.x > pacman.x) dx = -ghostSpeed;
            else if (ghost.y < pacman.y) dy = ghostSpeed;
            else if (ghost.y > pacman.y) dy = -ghostSpeed;
        } else {
            // Slumpmässig rörelse ibland
            moveGhostRandomly(ghost);
            return;
        }
        
        // Kontrollera kollision med väggar innan rörelse
        int newX = ghost.x + dx;
        int newY = ghost.y + dy;
        
        boolean collision = false;
        for (Block wall : walls) {
            if (newX < wall.x + wall.width && 
                newX + ghost.width > wall.x && 
                newY < wall.y + wall.height && 
                newY + ghost.height > wall.y) {
                collision = true;
                break;
            }
        }
        
        // Uppdatera position om ingen kollision
        if (!collision) {
            ghost.x = newX;
            ghost.y = newY;
        } else {
            // Om kollision, prova att röra sig i en annan riktning
            moveGhostRandomly(ghost);
        }
        
        // Hantera tunnlar på sidorna
        if (ghost.x < 0) {
            ghost.x = boardWidth - ghost.width;
        } else if (ghost.x > boardWidth - ghost.width) {
            ghost.x = 0;
        }
    }
    
    private void moveGhostAhead(Block ghost, double chanceToPredict) {
        if (pacman == null) return;
        
        // Beräkna en position framför Pac-Man baserat på hans riktning
        int targetX = pacman.x;
        int targetY = pacman.y;
        
        // Förutse Pac-Man's position
        if (random.nextDouble() < chanceToPredict) {
            int predictionDistance = 4 * tileSize; // Hur långt framför vi siktar
            
            if (pacmanVelocityX > 0) targetX += predictionDistance;
            else if (pacmanVelocityX < 0) targetX -= predictionDistance;
            else if (pacmanVelocityY > 0) targetY += predictionDistance;
            else if (pacmanVelocityY < 0) targetY -= predictionDistance;
        }
        
        // Beräkna riktning mot målet
        int dx = 0;
        int dy = 0;
        
        if (ghost.x < targetX) dx = ghostSpeed;
        else if (ghost.x > targetX) dx = -ghostSpeed;
        else if (ghost.y < targetY) dy = ghostSpeed;
        else if (ghost.y > targetY) dy = -ghostSpeed;
        
        // Kontrollera kollision med väggar innan rörelse
        int newX = ghost.x + dx;
        int newY = ghost.y + dy;
        
        boolean collision = false;
        for (Block wall : walls) {
            if (newX < wall.x + wall.width && 
                newX + ghost.width > wall.x && 
                newY < wall.y + wall.height && 
                newY + ghost.height > wall.y) {
                collision = true;
                break;
            }
        }
        
        // Uppdatera position om ingen kollision
        if (!collision) {
            ghost.x = newX;
            ghost.y = newY;
        } else {
            // Om kollision, prova att röra sig i en annan riktning
            moveGhostRandomly(ghost);
        }
        
        // Hantera tunnlar på sidorna
        if (ghost.x < 0) {
            ghost.x = boardWidth - ghost.width;
        } else if (ghost.x > boardWidth - ghost.width) {
            ghost.x = 0;
        }
    }
    
    private void moveGhostRandomly(Block ghost) {
        // Slumpmässig riktning
        int direction = random.nextInt(4);
        int dx = 0;
        int dy = 0;
        
        switch (direction) {
            case 0: dx = ghostSpeed; break;  // Höger
            case 1: dy = ghostSpeed; break;  // Ner
            case 2: dx = -ghostSpeed; break; // Vänster
            case 3: dy = -ghostSpeed; break; // Upp
        }
        
        // Kontrollera kollision med väggar innan rörelse
        int newX = ghost.x + dx;
        int newY = ghost.y + dy;
        
        boolean collision = false;
        for (Block wall : walls) {
            if (newX < wall.x + wall.width && 
                newX + ghost.width > wall.x && 
                newY < wall.y + wall.height && 
                newY + ghost.height > wall.y) {
                collision = true;
                break;
            }
        }
        
        // Uppdatera position om ingen kollision
        if (!collision) {
            ghost.x = newX;
            ghost.y = newY;
        }
        
        // Hantera tunnlar på sidorna
        if (ghost.x < 0) {
            ghost.x = boardWidth - ghost.width;
        } else if (ghost.x > boardWidth - ghost.width) {
            ghost.x = 0;
        }
    }
    
    private void checkGhostCollision() {
        if (gameOver || pacman == null) return;
        
        HashSet<Block> ghostsToRemove = new HashSet<>();
        
        for (Block ghost : ghosts) {
            if (pacman.x < ghost.x + ghost.width && 
                pacman.x + pacman.width > ghost.x && 
                pacman.y < ghost.y + ghost.height && 
                pacman.y + pacman.height > ghost.y) {
                
                if (ghostsScared) {
                    // Pacman äter spöket
                    ghostsToRemove.add(ghost);
                    score += 200;
                    
                    // Spela spökätningsljud
                    soundManager.play("ghost");
                } else {
                    // Spöket äter Pacman
                    lives--;
                    
                    // Spela dödsljud
                    soundManager.play("death");
                    
                    if (lives <= 0) {
                        gameOver = true;
                        gameTimer.stop();
                        soundManager.play("gameover");
                    } else {
                        // Återställ Pacman till startposition
                        pacman.x = pacman.startX;
                        pacman.y = pacman.startY;
                        pacmanVelocityX = 0;
                        pacmanVelocityY = 0;
                    }
                    
                    break;
                }
            }
        }
        
        // Ta bort uppätna spöken
        ghosts.removeAll(ghostsToRemove);
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
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Rita bakgrund - mer elegant mörk bakgrund
        g.setColor(backgroundColor);
        g.fillRect(0, 0, boardWidth, boardHeight);
        
        // Rita rutnät för visuell klarhet (väldigt subtilt)
        g.setColor(new Color(20, 20, 40, 30)); // Nästan osynligt rutnät
        for (int x = 0; x < boardWidth; x += tileSize) {
            g.drawLine(x, 0, x, boardHeight);
        }
        for (int y = 0; y < boardHeight; y += tileSize) {
            g.drawLine(0, y, boardWidth, y);
        }
        
        // Rita väggar
        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, null);
        }
        
        // Rita mat (pellets)
        for (Block food : foods) {
            g.drawImage(food.image, food.x, food.y, null);
        }
        
        // Rita power pellets (med lite pulsation för visuellt intresse)
        for (Block powerPellet : powerPellets) {
            g.drawImage(powerPellet.image, powerPellet.x, powerPellet.y, null);
        }
        
        // Rita spöken
        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, null);
        }
        
        // Rita Pacman
        if (pacman != null) {
            g.drawImage(pacman.image, pacman.x, pacman.y, null);
        }
        
        // Visa poäng och liv
        g.setColor(Color.WHITE);
        g.setFont(gameFont);
        g.drawString("POÄNG: " + score, 20, 40);
        g.drawString("LIV: " + lives, boardWidth - 150, 40);
        
        // Visa ljudstatus i höger hörn
        String soundStatus = soundManager.isSoundEnabled() ? "LJUD: PÅ" : "LJUD: AV";
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString(soundStatus + " (M)", boardWidth - 120, 70);
        
        // Visa "Game Over" om spelet är slut
        if (gameOver) {
            // Halvgenomskinlig bakgrund
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, boardWidth, boardHeight);
            
            g.setColor(Color.RED);
            g.setFont(titleFont);
            String gameOverText = "GAME OVER";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(gameOverText);
            g.drawString(gameOverText, (boardWidth - textWidth) / 2, boardHeight / 2);
            
            g.setFont(gameFont);
            String scoreText = "Slutpoäng: " + score;
            fm = g.getFontMetrics();
            textWidth = fm.stringWidth(scoreText);
            g.drawString(scoreText, (boardWidth - textWidth) / 2, boardHeight / 2 + 70);
            
            String restartText = "Tryck på ENTER för att spela igen";
            fm = g.getFontMetrics();
            textWidth = fm.stringWidth(restartText);
            g.setColor(Color.WHITE);
            g.drawString(restartText, (boardWidth - textWidth) / 2, boardHeight / 2 + 120);
        }
        
        // Visa "Du vann!" om spelaren har vunnit
        if (gameWon) {
            // Halvgenomskinlig bakgrund
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, boardWidth, boardHeight);
            
            g.setColor(Color.GREEN);
            g.setFont(titleFont);
            String winText = "DU VANN!";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(winText);
            g.drawString(winText, (boardWidth - textWidth) / 2, boardHeight / 2);
            
            g.setFont(gameFont);
            String scoreText = "Slutpoäng: " + score;
            fm = g.getFontMetrics();
            textWidth = fm.stringWidth(scoreText);
            g.drawString(scoreText, (boardWidth - textWidth) / 2, boardHeight / 2 + 70);
            
            String restartText = "Tryck på ENTER för att spela igen";
            fm = g.getFontMetrics();
            textWidth = fm.stringWidth(restartText);
            g.setColor(Color.WHITE);
            g.drawString(restartText, (boardWidth - textWidth) / 2, boardHeight / 2 + 120);
        }
    }
}