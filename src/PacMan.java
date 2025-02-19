import java.awt.*;
import java.awt.event.*;
import java.util.HasgSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel {
    
    private int rowCount = 42;
    private int columnCount = 84;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    

    PacMan() {

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);

        
    }
    
    
}
