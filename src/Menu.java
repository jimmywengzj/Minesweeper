package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EventListener;

import javax.swing.JFrame;

public class Menu extends JFrame implements ActionListener {
    
    private JPanel mainPanel;
    private JPanel aboutPanel;
    private JPanel optionPanel;
    private JButton startButton;
    private JButton resumeButton;
    private JButton optionButton;
    private JButton aboutButton;
    private JButton exitButton;
    private JButton aboutBackButton;
    private JButton optionBackButton;
    private JButton optionResetButton;
    
    private JTextArea textHere;

    private JLabel optionLangLabel;
    private JLabel optionFontSizeLabel;
    private JLabel optionThemeLabel;
    

    private JComboBox optionLangComboBox;
    private JComboBox optionFontSizeComboBox;
    private JComboBox optionThemeComboBox;
    

    private String[] langStrings = { "English", "français", "italiano", "简体中文", "日本語" };
    private String[] fontSizeStrings = {"Small", "Medium", "Large"};    // to be changed later
    private String[] themeStrings = {"Classic", "Marine", "Underground", "Aerial"};

    public Menu() {
        
        this.setSize(500,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Main Menu");
        this.setVisible(true);
        this.setLocationRelativeTo(null);   // keep position centered

        // create mainPanel and its components
        mainPanel = new JPanel();
        mainPanel.setBounds(0,0,500,800);
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.gray);

        startButton = new JButton("START"); // to be changed later depending on languages
        startButton.setBounds(100,0,300,100);
        startButton.setLayout(null);
        startButton.setBackground(Color.white);
        startButton.addActionListener(this);

        resumeButton = new JButton("RESUME");
        resumeButton.setBounds(100,150,300,100);
        resumeButton.setLayout(null);
        resumeButton.setBackground(Color.white);
        resumeButton.addActionListener(this);

        optionButton = new JButton("OPTION");
        optionButton.setBounds(100,300,300,100);
        optionButton.setLayout(null);
        optionButton.setBackground(Color.white);
        optionButton.addActionListener(this);

        aboutButton = new JButton("ABOUT");
        aboutButton.setBounds(100,450,300,100);
        aboutButton.setLayout(null);
        aboutButton.setBackground(Color.white);
        aboutButton.addActionListener(this);

        exitButton = new JButton("EXIT");
        exitButton.setBounds(100,600,300,100);
        exitButton.setLayout(null);
        exitButton.setBackground(Color.white);
        exitButton.addActionListener(this);

        mainPanel.add(startButton);
        mainPanel.add(resumeButton);
        mainPanel.add(optionButton);
        mainPanel.add(aboutButton);
        mainPanel.add(exitButton);
       
        // create aboutPanel and its components
        aboutPanel = new JPanel();
        aboutPanel.setBounds(0,0,800,800);
        aboutPanel.setLayout(null);
        aboutPanel.setBackground(new Color(0,200,255));

        textHere = new JTextArea("I Have a Big Text and I want to show it now!"); 
        textHere.setBounds(0,150,500,800);

        aboutBackButton = new JButton("Back");
        aboutBackButton.setBounds(50,50,50,50);
        aboutBackButton.addActionListener(this);

        aboutPanel.add(textHere);
        aboutPanel.add(aboutBackButton);

        // create optionPanel and its components
        optionPanel = new JPanel();
        optionPanel.setBounds(0,0,500,800);
        optionPanel.setLayout(null);
        optionPanel.setBackground(new Color(0,200,255));

        optionBackButton = new JButton("Back");
        optionBackButton.setBounds(100,50,50,50);
        optionBackButton.setLayout(null);
        optionBackButton.setBackground(Color.DARK_GRAY);
        optionBackButton.addActionListener(this);

        optionLangLabel = new JLabel("Language");
        optionLangLabel.setBounds(50,150,80,50);
        optionLangLabel.setLayout(null);
        optionLangLabel.setLayout(null);
        optionLangLabel.setBackground(Color.DARK_GRAY);
        
        optionFontSizeLabel = new JLabel("Font size");
        optionFontSizeLabel.setBounds(50,250,80,50);
        optionFontSizeLabel.setLayout(null);
        optionFontSizeLabel.setBackground(Color.DARK_GRAY);

        optionThemeLabel = new JLabel("Theme");
        optionThemeLabel.setBounds(50,350,80,50);
        optionThemeLabel.setLayout(null);
        optionThemeLabel.setBackground(Color.DARK_GRAY);

        
        // ComboBox hiding, not sure what it is
        optionLangComboBox = new JComboBox(langStrings);
        optionLangComboBox.setSelectedIndex(0);
        optionLangComboBox.setBounds(100,150,100,100);
        optionLangComboBox.setLayout(null);
        optionLangComboBox.setBackground(Color.DARK_GRAY);
        optionLangComboBox.addActionListener(this);

        optionFontSizeComboBox = new JComboBox(fontSizeStrings);
        optionFontSizeComboBox.setSelectedIndex(0);
        optionFontSizeComboBox.setBounds(100,250,100,100);
        optionFontSizeComboBox.setLayout(null);
        optionFontSizeComboBox.setBackground(Color.DARK_GRAY);
        optionFontSizeComboBox.addActionListener(this);

        optionThemeComboBox = new JComboBox(themeStrings);
        optionThemeComboBox.setSelectedIndex(0);
        optionThemeComboBox.setBounds(100,350,100,100);
        optionThemeComboBox.setLayout(null);
        optionThemeComboBox.setBackground(Color.DARK_GRAY);
        optionThemeComboBox.addActionListener(this);


        optionResetButton = new JButton("RESET TO DEFAULT");
        optionResetButton.setBounds(50,600,50,50);
        optionResetButton.setLayout(null);
        optionResetButton.setBackground(Color.ORANGE);
        optionResetButton.addActionListener(this);
        
        optionPanel.add(optionBackButton);
        optionPanel.add(optionLangLabel);
        optionPanel.add(optionFontSizeLabel);
        optionPanel.add(optionThemeLabel);
        
        
        optionPanel.add(optionLangComboBox);
        optionPanel.add(optionFontSizeComboBox);
        optionPanel.add(optionThemeComboBox);
        optionPanel.add(optionResetButton);
        
        // add mainPanel to the JFrame
        this.add(mainPanel);
        this.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton) {
            Board board = new Board();
        } else if(e.getSource() == resumeButton) {
            
            JOptionPane.showMessageDialog(this, "Are you sure to continue?");
            // back to the board to be implemented


        } else if(e.getSource() == optionButton) {
            this.getContentPane().removeAll();
            this.getContentPane().add(optionPanel);
            repaint();
            

        } else if(e.getSource() == aboutButton) {
            /*this.remove(mainPanel);
            this.add(aboutPanel);
            this.repaint();
            this.revalidate(); **/

            //mainPanel.setVisible(false);
            //aboutPanel.setVisible(true);

            //mountain of crap :)

            this.getContentPane().removeAll();
            this.getContentPane().add(aboutPanel);
            repaint();
            System.out.println("yes");
            
            
        } else if(e.getSource() == exitButton) {
            // Code below aborted because cannot set default button, would have to manually create options
            /*  
            int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?",
            "Confirm Quit", JOptionPane.YES_NO_OPTION); 
            if (result == JOptionPane.YES_OPTION) System.exit(0);
            **/
            
            // Warning message seems to work...
            Object[] options = { "Wait, no!", "Yes!" };
            int result = JOptionPane.showOptionDialog(null, "Are you sure you want to quit? All progress will be lost", 
                "Confirm Quit", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if(result == 1) System.exit(0);

        } else if(e.getSource() == aboutBackButton) {
            // back to the mainPanel
            this.getContentPane().removeAll();
            this.getContentPane().add(mainPanel);
            repaint();
            revalidate();
            System.out.println("back");
        } else if(e.getSource() == optionBackButton) {
            // back to the mainPanel
            this.getContentPane().removeAll();
            this.getContentPane().add(mainPanel);
            repaint();
            revalidate();
        } else if(e.getSource() == optionResetButton) {
            // reset all properties
            optionLangComboBox.setSelectedIndex(0);
            optionFontSizeComboBox.setSelectedIndex(0);
            optionThemeComboBox.setSelectedIndex(0);
        }
    }

}
