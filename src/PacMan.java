import java.awt.*;
import java.awt.event.*;
import java.util.HasgSet;
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

    
    private int rowCount = 42;
    private int columnCount = 64;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;





 //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXXXXX                             XXXXXX    XXXXXXXXXXXX",
        "X XX XXX X XXX XX XXXXXXXXXXXXXXXX    XXXXXXXXXXXXXXXXXXXXXXXXXX",
        "X                 XXXXXXXXXXXXXXXX    XXXXXXXXXXXXXXXXXXXXXXXXXX",
        "X XX X XXXXX X XX XXXXXXXXXXXXXXXX     XXXXXXXXXXXXXXXXXXXXXXXXX",
        "X    X       X       XXXXXXXXXXXXX     XXXXXXXXXXXXXXXXXXXXXXXXX",
        "XXXX XXXX XXXX XXXX   XXXXXXXXXXX      XXXXXXXXXXXXXXXXXXXXXXXXX",
        "OOOX X       X XOOO   XXXXXXXXXXX      XXXXXXXXXXXXXXXXXXXXXXXXX",
        "XXXX X XXrXX X XXXX   XXXXXXXXXXX      XXXXXXXXXXXXXXXXXXXXXXXXX",
        "O       bpo                                                    O",
        "XXXX X XXXXX X                                              XXXX",
        "OOOX X       X                                              XOOO",
        "XXXX X XXXXX X                                              XXXX",
        "X        X                                                     X",
        "X XX XXX X XXX XX                                              X",
        "X  X     P     X                                               X",
        "XX X X XXXXX X X                                              XX",
        "X    X   X   X                                                 X",
        "X XXXXXX X XXXXXX                                              X",
        "X                                                              X",
        "XXXXXXXXXXXXXXXXXXX                  XXXXXXXX                  X", 
        "XXXXXXXXXXXXXXXXXXX                   xxxxxxx                  X",
        "XXXXXXXXXXXXXXXXXXX                                            X",
        "XXXXXXXXXXXXXXXXXXX               xxxxxxxxxxxxxxxxxxx          X",
        "XXXXXXXXXXXXXXXXXXX                                            X",
        "XXXXXXXXXXXXXXXXXXX                                            X",
        "XXXXXXXXXXXXXXXXXXX                   xxxxxxxxxxxxxx           X",
        "XXXXXXXXXXXXXXXXXXX                                            X",
        "XXXXXXXXXXXXXXXXXXX          xxxxxxxx                          X",
        "XXXXXXXXXXXXXXXXXXX                 x                          X",
        "XXXXXXXXXXXXXXXXXXX                 x          xxxxxxxxxxx     X",
        "XXXXXXXXXXXXXXXXXXX                 x                          X", 
        "XXXXXXXXXXXXXXXXXXX                 x                          X",
        "XXXXXXXXXXXXXXXXXXX                 x                          X",
        "XXXXXXXXXXXXXXXXXXX               xxxxxxxxxxxxxxxxxxxx         X",
        "XXXXXXXXXXXXXXXXXXX                                            X",
        "XXXXXXXXXXXXXXXXXXX                                            X",
        "XXXXXXXXXXXXXXXXXXX                                            X",
        "XXXXXXXXXXXXXXXXXXX                                            X",
        "XXXXXXXXXXXXXXXXXXX                                            X",
        "XXXXXXXXXXXXXXXXXXX                                            X",
        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",

    };



HashSet<Block> walls;
HashSet<Block> ghosts;
HashSet<Block> foods;
Block pacman;    




    PacMan() {

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);

        //load images
        wallImage = new ImageIcon(getClass().getResource("/images/wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("/images/blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("/images/orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("/images/pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("/images/redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("/images/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("/images/pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("/images/pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("/images/pacmanRight.png")).getImage();
        
    }

    public void loadMap() {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        
        for (int row = 0; row < rowCount; row++) {
            for (int c = 0; c < columnCount; c++) {
              String row = tileMap[r];
              char tile = row.charAt(c);
            
                int x = c*tileSize;
                int y = r*tileSize;

                if (tileMapChar == "X") { // block wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'b') //blue ghost
                Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                ghosts.add(ghost);
                }
                else if (tileMapChar == 'o') //orange ghost
                Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                ghosts.add(ghost);
                }
                else if (tileMapChar == 'p') //pink ghost
                Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                ghosts.add(ghost);
                }
                else if (tileMapChar == 'r') //red ghost
                Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') //pacman
                pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
         
                else if (tileMapChar == 'P') { //pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }   


            
}
