import javax.swing.JFrame;


public class App {
    public static void main(String[] args) throws Exception {
       
    int rowCount = 42;
    int columnCount = 80;
    int tileSize = 32;
    int boardWidth = columnCount * tileSize;
    int boardHeight = rowCount * tileSize;

    JFrame frame = new JFrame("Pac");
    frame.setVisible(true);
    frame.setSize(boardWidth, boardHeight);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        
        
    }
}