import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

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

    private int rowCount = 21;
    private int columnCount = 31;
    private int tileSize = 64; // Större rutor
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image foodImage;
    private Image powerPelletImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    private int pacmanVelocityX = 0;
    private int pacmanVelocityY = 0;
    private int pacmanSpeed = 8;
    private Timer gameTimer;
    private Random random = new Random();
    private int ghostSpeed = 4;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private int score = 0;
    private int lives = 3;
    
    // Ljudhanterare
    private SoundManager soundManager;
    
    // Animeringsvariabler
    private int animationFrame = 0;
    private int animationDelay = 5;
    private int animationCounter = 0;
    private double[] mouthAngles = {20, 40, 60, 40, 20, 0};

    // Visuella förbättringar
    private Color backgroundColor = new Color(0, 0, 20); // Mycket mörk blå, nästan svart
    private Font gameFont = new Font("Arial", Font.BOLD, 30);
    private Font titleFont = new Font("Arial", Font.BOLD, 60);
    
    // Halverad spelplan för bättre proportioner med större rutor
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        "XOOOOOOOOOOOOOXOoOOOOOOOOOOOOOX",
        "XOXXXXXXXXXOXOXOXXXXXXXXXOXOXOX",
        "XEXOOOOOOOOXOXOXOOOOOOOOXOXEXOX",
        "XOXOXXXXXXOXOXOXOXXXXXXOXOXOXOX",
        "XOXOXOOOOOXOXOXOXOOOOOXOXOXOXOX",
        "XOXOXOXXOXOXOXOXOXXOXOXOXOXOXOX",
        "XOXOXOrXOXOXOXOXOXOOOOXOXOXOXOX",
        "XOXOXXXXOXOXOXOXOXXXXXOXOXOXOXOX",
        "XOXOOOOOXOXOXOXOXOOOOOXOXOXOXOX",
        "XOXXXXXXXOXOXOXOXXXXXXXOXOXOXOX",
        "XOOOOOOOOOXOXOXOXOOOOOOXOXOXbXOX",
        "XXXXXXXXXXXXOXOXOXXXXXXXOXOXOXOX",
        "OOOOOOOOOOOOXOXOXOOOOOOOOXOXOXOX",
        "XXXXXXXXXXXXOXOXOXXXXXXXOXOXOXOX",
        "XOOOOOOOOOXOXOXOXOOOOOOXOXOXpXOX",
        "XOXXXXXXXOXOXOXOXXXXXXXOXOXOXOXOX",
        "XOXOOOOOOXOXOXOXOOOOOOXOXOXOXOXOX",
        "XOXOXXXXXOXOXOXOXXXXXXXOXOXOXOXOX",
        "XOXOOOOOXOXOXOXOXOOOOOOXOXOXOXOXOX",
        "XOXOXXXOXOXOXOXOXXXXXXXOXOXOXOXOX", 
        "XOXOOOOOXOXOXOXOOOOOOOOXOXOXOXOXOX",
        "XOXXXXXXXOXOXOXOXXXXXXXXXOXOXOXOXOX",
        "XOOOOOOOOXOXOXOOOOOOOOOOXOXOXOXOXOX",
        "XXXXXXXXOXOXOXXXXXXXXXXXXXOXOXOXOX",
        "XXXXXXXXOXOXOOOOOOOOOOOOOOXOXOXOX",
        "XXXXXXXXOXOXOXXXXXXXXXXXXXXXOXOXOX",
        "XOOOOOOOOXOXOXOOOOOOOOOOOOXOXOXOX",
        "XOXXXXXXXOXOXOXOXXXXXXXXXXXOXOXOX",
        "XOXEOOOOXOXOXOXOOOOOOOOOOOOXOXOX",
        "XOXOXXXXXOXOXOXOXXXXXXXXXXXOXOXOX",
        "XOXOOOOXOXOXOXOXOOOOOOOOOOXOXOX", 
        "XOXOXXXOXOXOXOXOXXXXXXXXXXXXXOXOX",
        "XOXOOOOXOXOXOXOXOOOOOOOOOOOOXOXOX",
        "XOXXXXXOXOXOXOXOXXXXXXXXXXXOXOXOX",
        "XOOOOOOXOXOXOXOXOOOOOOOOOOXOXOXOX",
        "XXXXXXXXOXOXOXOXXXXXXXXXXXXXOXOX",
        "XOOOOOOOOXOXOXOXOOOOOOOOOOXOXOXOX",
        "XOXXXXXXXOXOXOXOXXXXXXXXXXXOXOXOX",
        "XOXOOOOOOXOXOXOXOOOOPOOOOOOXOXOXOX",
        "XOXXXXXXXOXXXOXXXXXXXXXXXXXXXOXOXOX",
        "XOOOOOOOOXOOOOOOOOOOOOOOOOOOXOXOXOX",
        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> ghosts;
    HashSet<Block> foods;
    HashSet<Block> powerPellets;
    Block pacman;    

    public PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(backgroundColor);

        // Initiera ljudhanterare
        soundManager = new SoundManager();

        // Skapa bilder med vår GameImages-hjälpklass
        wallImage = GameImages.createWallImage();
        blueGhostImage = GameImages.createGhostImage(Color.CYAN);
        orangeGhostImage = GameImages.createGhostImage(new Color(255, 165, 0)); // Orange
        pinkGhostImage = GameImages.createGhostImage(Color.PINK);
        redGhostImage = GameImages.createGhostImage(Color.RED);
        foodImage = GameImages.createFoodImage();
        powerPelletImage = GameImages.createPowerPelletImage();

        // Skapa Pacman-bilder med mun öppen 40 grader
        pacmanUpImage = GameImages.createPacmanUpImage(40);
        pacmanDownImage = GameImages.createPacmanDownImage(40);
        pacmanLeftImage = GameImages.createPacmanLeftImage(40);
        pacmanRightImage = GameImages.createPacmanRightImage(40);
        
        // Ladda spelplanen
        loadMap();
        
        // Sätt upp spelets kontroller
        setupGameControls();
        
        // Spela startljud
        soundManager.play("start");
    }

    public void loadMap() {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        powerPellets = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        
        System.out.println("Laddar karta. rowCount: " + rowCount + ", tileMap rader: " + tileMap.length);
        
        for (int r = 0; r < tileMap.length; r++) {
            String row = tileMap[r];
            
            for (int c = 0; c < row.length(); c++) {
                char tileMapChar = row.charAt(c);
                
                // Skriv ut Pacman och spökpositioner för felsökning
                if (tileMapChar == 'P' || tileMapChar == 'b' || tileMapChar == 'o' || 
                    tileMapChar == 'p' || tileMapChar == 'r') {
                    System.out.println("Hittade " + tileMapChar + " på rad " + r + ", kolumn " + c);
                }
                
                int x = c*tileSize;
                int y = r*tileSize;
    
                if (tileMapChar == 'X') { // block wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'b') { //blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                    System.out.println("Lagt till blått spöke på position " + x + "," + y);
                }
                else if (tileMapChar == 'o') { //orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                    System.out.println("Lagt till orange spöke på position " + x + "," + y);
                }
                else if (tileMapChar == 'p') { //pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                    System.out.println("Lagt till rosa spöke på position " + x + "," + y);
                }
                else if (tileMapChar == 'r') { //red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                    System.out.println("Lagt till rött spöke på position " + x + "," + y);
                }
                else if (tileMapChar == 'P') { //pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                    System.out.println("Lagt till Pacman på position " + x + "," + y);
                }
                else if (tileMapChar == ' ' || tileMapChar == 'O') { // vanlig mat (pellet)
                    Block food = new Block(foodImage, x, y, tileSize, tileSize);
                    foods.add(food);
                }
                else if (tileMapChar == 'E') { // power pellet
                    Block powerPellet = new Block(powerPelletImage, x, y, tileSize, tileSize);
                    powerPellets.add(powerPellet);
                }
            }
        }
        
        // Utskrifter för att verifiera laddat innehåll
        System.out.println("Antal väggar: " + walls.size());
        System.out.println("Antal mat: " + foods.size());
        System.out.println("Antal power pellets: " + powerPellets.size());
        System.out.println("Antal spöken: " + ghosts.size());
        System.out.println("Pacman hittad: " + (pacman != null));
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
                
                if (keyCode == KeyEvent.VK_UP) {
                    pacmanVelocityX = 0;
                    pacmanVelocityY = -pacmanSpeed;
                    pacman.image = pacmanUpImage;
                } 
                else if (keyCode == KeyEvent.VK_DOWN) {
                    pacmanVelocityX = 0;
                    pacmanVelocityY = pacmanSpeed;
                    pacman.image = pacmanDownImage;
                } 
                else if (keyCode == KeyEvent.VK_LEFT) {
                    pacmanVelocityX = -pacmanSpeed;
                    pacmanVelocityY = 0;
                    pacman.image = pacmanLeftImage;
                }
                else if (keyCode == KeyEvent.VK_RIGHT) {
                    pacmanVelocityX = pacmanSpeed;
                    pacmanVelocityY = 0;
                    pacman.image = pacmanRightImage;
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
                updateAnimation();
                repaint();
            }
        });
        gameTimer.start();
    }

    // Återställ spelet för omstart
    private void resetGame() {
        score = 0;
        lives = 3;
        gameOver = false;
        gameWon = false;
        
        // Ladda spelplanen på nytt
        loadMap();
        
        // Återställ hastigheter
        pacmanVelocityX = 0;
        pacmanVelocityY = 0;
        
        // Spela startljudet
        soundManager.play("start");
        
        // Starta timern igen
        gameTimer.start();
    }

    private void update() {
        if (gameOver || gameWon) return;
        
        // Förflyttning av Pacman
        int newX = pacman.x + pacmanVelocityX;
        int newY = pacman.y + pacmanVelocityY;
        
        // Kolla kollision med väggar
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
        }
        
        // Ät mat (vanliga pellets)
        HashSet<Block> foodToRemove = new HashSet<>();
        for (Block food : foods) {
            if (pacman.x < food.x + food.width && 
                pacman.x + pacman.width > food.x && 
                pacman.y < food.y + food.height && 
                pacman.y + pacman.height > food.y) {
                foodToRemove.add(food);
                score += 10; // 10 poäng per pellet
                
                // Spela ätljud
                soundManager.play("eat");
            }
        }
        foods.removeAll(foodToRemove);
        
        // Ät power pellets
        HashSet<Block> powerPelletsToRemove = new HashSet<>();
        for (Block powerPellet : powerPellets) {
            if (pacman.x < powerPellet.x + powerPellet.width && 
                pacman.x + pacman.width > powerPellet.x && 
                pacman.y < powerPellet.y + powerPellet.height && 
                pacman.y + pacman.height > powerPellet.y) {
                powerPelletsToRemove.add(powerPellet);
                score += 50; // 50 poäng per power pellet
                
                // Spela power-pellet ljud
                soundManager.play("power");
                
                // Här kan du implementera logik för att göra spöken sårbara
                // t.ex. ändra deras färg och beteende
            }
        }
        powerPellets.removeAll(powerPelletsToRemove);
        
        // Kontrollera om all mat är uppäten för vinst
        if (foods.isEmpty() && powerPellets.isEmpty()) {
            gameWon = true;
            gameTimer.stop();
            soundManager.play("win");
        }
        
        // Flytta spöken
        moveGhosts();
        
        // Kontrollera kollision med spöken
        checkGhostCollision();
    }
    
    private void updateAnimation() {
        // Uppdatera animationsramarna med en viss fördröjning
        animationCounter++;
        if (animationCounter >= animationDelay) {
            animationCounter = 0;
            animationFrame = (animationFrame + 1) % mouthAngles.length;
            
            // Uppdatera Pacman-bilderna med nya munvinklar
            if (pacmanVelocityX > 0 || (pacmanVelocityX == 0 && pacmanVelocityY == 0)) {
                pacmanRightImage = GameImages.createPacmanRightImage(mouthAngles[animationFrame]);
                if (pacmanVelocityX > 0) pacman.image = pacmanRightImage;
            }
            if (pacmanVelocityX < 0) {
                pacmanLeftImage = GameImages.createPacmanLeftImage(mouthAngles[animationFrame]);
                pacman.image = pacmanLeftImage;
            }
            if (pacmanVelocityY < 0) {
                pacmanUpImage = GameImages.createPacmanUpImage(mouthAngles[animationFrame]);
                pacman.image = pacmanUpImage;
            }
            if (pacmanVelocityY > 0) {
                pacmanDownImage = GameImages.createPacmanDownImage(mouthAngles[animationFrame]);
                pacman.image = pacmanDownImage;
            }
        }
    }
    
    private void moveGhosts() {
        for (Block ghost : ghosts) {
            // Avgör riktning (mot pacman eller slumpmässigt)
            int dirX = 0;
            int dirY = 0;
            
            // 70% chans att spöken rör sig mot Pacman, 30% slumpmässigt
            if (random.nextDouble() < 0.7) {
                // Rörelse mot Pacman
                if (ghost.x < pacman.x) dirX = 1;
                else if (ghost.x > pacman.x) dirX = -1;
                
                if (ghost.y < pacman.y) dirY = 1;
                else if (ghost.y > pacman.y) dirY = -1;
            } else {
                // Slumpmässig rörelse
                int randomDir = random.nextInt(4);
                if (randomDir == 0) dirX = 1;      // höger
                else if (randomDir == 1) dirX = -1; // vänster
                else if (randomDir == 2) dirY = 1;  // ner
                else dirY = -1;                    // upp
            }
            
            // Om både x och y har rörelse, välj endast en riktning (för att undvika diagonal rörelse)
            if (dirX != 0 && dirY != 0) {
                if (random.nextBoolean()) dirX = 0;
                else dirY = 0;
            }
            
            // Beräkna ny position
            int newX = ghost.x + dirX * ghostSpeed;
            int newY = ghost.y + dirY * ghostSpeed;
            
            // Kontrollera kollision med väggar
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
        }
    }
    
    private void checkGhostCollision() {
        if (gameOver) return;
        
        for (Block ghost : ghosts) {
            if (pacman.x < ghost.x + ghost.width && 
                pacman.x + pacman.width > ghost.x && 
                pacman.y < ghost.y + ghost.height && 
                pacman.y + pacman.height > ghost.y) {
                
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