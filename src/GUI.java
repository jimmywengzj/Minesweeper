package src;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI extends JFrame implements ActionListener {
    JFrame frame = new JFrame("Minesweeper");
    Container grid = new Container();
    int numMines;
    int cellSize = 20;

    public GUI(int x, int y, int numMines) {
        this.numMines = numMines;
        int width = y * cellSize;
        int height = x * cellSize;
        frame.setSize(width, height);
        frame.setLayout(null);
        
        JButton[][] buttons = new JButton[x][y];
        grid.setLayout(new GridLayout(x,y));
        for(int i = 0; i < buttons.length; i++) {
            for(int j = 0; j < buttons[0].length; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].addActionListener(this);
                buttons[i][j].addMouseListener(new MouseInputAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(e.getButton() == MouseEvent.BUTTON3) {

                        }
                    }
                });
                grid.add(buttons[x][y]);
            }
        }
        setVisible(true);
    }

    public void 
}
