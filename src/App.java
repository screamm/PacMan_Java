// /src/App.java

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        // Skapa instans av PacMan-spelet
        PacMan pacmanGame = new PacMan();
        
        // Skapa och konfigurera JFrame
        JFrame frame = new JFrame("PacMan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(pacmanGame);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centrera fönstret på skärmen
        frame.setVisible(true);
    }
}