package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EventListener;

public class Board extends JFrame{
    public Board(){
        this.setTitle("Demineur");
        this.setSize(800,800);
        this.setLocation(300,200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panneauGlobal = new JPanel();
        panneauGlobal.setBounds(0,0,800,800);
        panneauGlobal.setLayout(null);
        panneauGlobal.setBackground(Color.gray);
        
        this.add(panneauGlobal);
        
        this.setVisible(true);
    }

}
