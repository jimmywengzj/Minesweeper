package src;

import javax.swing.*; 
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.event.*;

public class Gui implements MouseListener {
    
    public static final int STATUS_BAR_BORDER_SIZE = 4;

    public static JFrame frame;
    public static JMenuBar menuBar;
    
    public static JPanel mainPanel;
    public static JLabel corner_top_left, corner_top_right, corner_middle_left, corner_middle_right, corner_bottom_left, corner_bottom_right,
                         edge_upper_left, edge_upper_right, edge_lower_left, edge_lower_right, edge_top, edge_middle, edge_bottom;
    public static JPanel statusBar;
    public static JLabel mineCnt_background, mineCnt_hundreds, mineCnt_tens, mineCnt_ones, 
                           timer_background,   timer_hundreds,   timer_tens,   timer_ones;
    public static JButton face;
    public static JLabel message;

    public static JButton[][] cell;
    
    public static Game game;

    public static void init() {
        frame = new JFrame();
        menuInit();
        boardInit();
        gameInit();
        frame.setJMenuBar(menuBar);  
        frame.add(mainPanel);
        frame.pack();
        frame.setLocation(100, 100);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void menuInit(){
        menuBar = new JMenuBar();
    }

    // every coordinates in this method are the coordinates in gui scale 1 (except for the init of the panels). The scaling is done while creating each element
    public static void boardInit() {

        int x1 = getImageWidth("corner_top_left.png");
        int x2 = x1 + getImageWidth("0.png") * Options.col;
        int x3 = x2 + getImageWidth("corner_top_right.png");
        int y1 = getImageHeight("corner_top_left.png");
        int y2 = y1 + STATUS_BAR_BORDER_SIZE * 2 + Math.max(getImageHeight("number_backgroud.png"), getImageHeight("face_Smiley.png") - 1);
        int y3 = y2 + getImageHeight("corner_middle_left.png");
        int y4 = y3 + getImageHeight("0.png") * Options.row;
        int y5 = y4 + getImageHeight("corner_bottom_left.png");

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(x3 * Options.scale, y5 * Options.scale));

        // init status bar (mineCnt, face, timer) panel
        statusBar = new JPanel();
        statusBar.setLayout(null);
        statusBar.setLocation(x1, y1);
        statusBar.setSize((x2 - x1) * Options.scale, (y2 - y1) * Options.scale);
        // set status bar color from color.png
        try {
            BufferedImage colorImage = ImageIO.read(new File("resources/" + Options.resource + "/color.png"));
            statusBar.setBackground(new Color(colorImage.getRGB(0, 0)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // add numbers and the face to the status bar
        int number_width = getImageWidth("number0.png");
        mineCnt_background = setJLabelImage("number_background.png", STATUS_BAR_BORDER_SIZE, STATUS_BAR_BORDER_SIZE, 1, 1);
        mineCnt_hundreds = setJLabelImage("number0.png", STATUS_BAR_BORDER_SIZE + 1, STATUS_BAR_BORDER_SIZE + 1, 1, 1);
        mineCnt_tens = setJLabelImage("number0.png", STATUS_BAR_BORDER_SIZE + 1 + number_width, STATUS_BAR_BORDER_SIZE + 1, 1, 1);
        mineCnt_ones = setJLabelImage("number0.png", STATUS_BAR_BORDER_SIZE + 1 + number_width * 2, STATUS_BAR_BORDER_SIZE + 1, 1, 1);
        statusBar.add(mineCnt_hundreds);
        statusBar.add(mineCnt_tens);
        statusBar.add(mineCnt_ones);
        // the last added JLabel is placed at the bottom layer of overlapping
        statusBar.add(mineCnt_background);

        int timerX = (x2 - x1) - getImageWidth("number_background.png") - STATUS_BAR_BORDER_SIZE;
        timer_background = setJLabelImage("number_background.png", timerX, STATUS_BAR_BORDER_SIZE, 1, 1);
        timer_hundreds = setJLabelImage("number0.png", timerX + 1, STATUS_BAR_BORDER_SIZE + 1, 1, 1);
        timer_tens = setJLabelImage("number0.png", timerX + 1 + number_width, STATUS_BAR_BORDER_SIZE + 1, 1, 1);
        timer_ones = setJLabelImage("number0.png", timerX + 1 + number_width * 2, STATUS_BAR_BORDER_SIZE + 1, 1, 1);
        statusBar.add(timer_hundreds);
        statusBar.add(timer_tens);
        statusBar.add(timer_ones);
        statusBar.add(timer_background);

        face = setJButtonImage("face_Smiley.png", (x2 - x1 - getImageWidth("face_Smiley.png")) / 2, STATUS_BAR_BORDER_SIZE, 1, 1);
        statusBar.add(face);

        mainPanel.add(statusBar);

        // init borders
        corner_top_left = setJLabelImage("corner_top_left.png", 0, 0, 1, 1);
        corner_top_right = setJLabelImage("corner_top_right.png", x2, 0, 1, 1);
        corner_middle_left = setJLabelImage("corner_middle_left.png", 0, y2, 1, 1);
        corner_middle_right = setJLabelImage("corner_middle_right.png", x2, y2, 1, 1);
        corner_bottom_left = setJLabelImage("corner_bottom_left.png", 0, y4, 1, 1);
        corner_bottom_right = setJLabelImage("corner_bottom_right.png", x2, y4, 1, 1);
        edge_upper_left = setJLabelImage("edge_upper_left.png", 0, y1, 1, y2 - y1);
        edge_upper_right = setJLabelImage("edge_upper_right.png", x2, y1, 1, y2 - y1);
        edge_lower_left = setJLabelImage("edge_lower_left.png", 0, y3, 1, y4 - y3);
        edge_lower_right = setJLabelImage("edge_lower_right.png", x2, y3, 1, y4 - y3);
        edge_top = setJLabelImage("edge_top.png", x1, 0, x2 - x1, 1);
        edge_middle = setJLabelImage("edge_middle.png", x1, y2, x2 - x1, 1);
        edge_bottom = setJLabelImage("edge_bottom.png", x1, y4, x2 - x1, 1);
        
        mainPanel.add(corner_top_left);
        mainPanel.add(corner_top_right);
        mainPanel.add(corner_middle_left);
        mainPanel.add(corner_middle_right);
        mainPanel.add(corner_bottom_left);
        mainPanel.add(corner_bottom_right);
        mainPanel.add(edge_upper_left);
        mainPanel.add(edge_upper_right);
        mainPanel.add(edge_lower_left);
        mainPanel.add(edge_lower_right);
        mainPanel.add(edge_top);
        mainPanel.add(edge_middle);
        mainPanel.add(edge_bottom);

        // init buttons
        cell = new JButton[Options.row][Options.col];
        int cellWidth = getImageWidth("covered.png");
        int cellHeight = getImageHeight("covered.png");
        for (int i = 0; i < Options.row; i++) {
            for (int j = 0; j < Options.col; j++) {
                cell[i][j] = setJButtonImage("covered.png", x1 + cellWidth * j, y3 + cellHeight * i, 1, 1);
                mainPanel.add(cell[i][j]);
            }
        }
    }

    /***
     * get image height by its file name
     * @param fileName
     * @return height in pixels
     */
    public static int getImageHeight(String fileName) {
        return new ImageIcon("resources/" + Options.resource + "/" + fileName).getIconHeight();
    }

    /***
     * get image width by its file name
     * @param fileName
     * @return width in pixels
     */
    public static int getImageWidth(String fileName) {
        return new ImageIcon("resources/" + Options.resource + "/" + fileName).getIconWidth();
    }

    /***
     * set an image to a JLable
     * @param fileName file name of the image
     * @param x position of the JLable
     * @param y
     * @param xScale scale in width
     * @param yScale scale in height
     * @return the JLabel
     */
    public static JLabel setJLabelImage(String fileName, int x, int y, int xScale, int yScale) {
        ImageIcon icon = new ImageIcon("resources/" + Options.resource + "/" + fileName);
        int width = icon.getIconWidth() * Options.scale * xScale;
        int height = icon.getIconHeight() * Options.scale * yScale;
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        JLabel jLabel = new JLabel(new ImageIcon(scaledImage));
        jLabel.setBounds(x * Options.scale, y * Options.scale, width, height);
        return jLabel;
    }

    /***
     * set an image to a JButton
     * @param fileName file name of the image
     * @param x position of the JButton
     * @param y
     * @param xScale scale in width
     * @param yScale scale in height
     * @return the JButton
     */
    public static JButton setJButtonImage(String fileName, int x, int y, int xScale, int yScale) {
        ImageIcon icon = new ImageIcon("resources/" + Options.resource + "/" + fileName);
        int width = icon.getIconWidth() * Options.scale * xScale;
        int height = icon.getIconHeight() * Options.scale * yScale;
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        JButton jButton = new JButton(new ImageIcon(scaledImage));
        jButton.setBounds(x * Options.scale, y * Options.scale, width, height);
        return jButton;
    }

    public static void gameInit(){
        game = new Game();
    }

    


    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)) {
            for(int i = 0; i < cell.length; i++) {
                for(int j = 0; j < cell[0].length; j++) {
                    if(e.getSource() == cell[i][j]) {
                        if(game.playerBoard[i][j] == Game.UNCHECKED) { // if right click on unchecked block
                            game.playerBoard[i][j] = Game.FLAG;
                            cell[i][j] = setJButtonImage("flag", i, j, 1, 1);

                        // update image
                        } else if(game.playerBoard[i][j] == Game.FLAG) { // if right click on flag
                            game.playerBoard[i][j] = Game.QUESTION;
                            cell[i][j] = setJButtonImage("questiomMark", i, j, 1, 1);
                        } else if(game.playerBoard[i][j] == Game.QUESTION) { // if right click on question mark
                            game.playerBoard[i][j] = Game.UNCHECKED;
                            cell[i][j] = setJButtonImage("covered", i, j, 1, 1);
                        } else if(game.playerBoard[i][j] <= 8 && game.playerBoard[i][j] > 0) { // if right click on checked and numbered block
                            for(Point p : game.getSurroundingCells(i,j)) {
                                if(game.playerBoard[p.x][p.y] == Game.UNCHECKED) {
                                    cell[p.x][p.y] = setJButtonImage("0", i, j, 1, 1);
                                }
                            }
                        }
                    }
                }
            }
            
        } else if(SwingUtilities.isLeftMouseButton(e)) {
            for(int i = 0; i < cell.length; i++) {
                for(int j = 0; j < cell[0].length; j++) {
                    if(e.getSource() == cell[i][j]) {
                        if(game.playerBoard[i][j] == Game.UNCHECKED) { // if left click on unchecked block
                            game.revealCell(i, j);
                
                        } 
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(int i = 0; i < cell.length; i++) {
            for(int j = 0; j < cell[0].length; j++) {
                if(e.getSource() == cell[i][j]) {
                    for(Point p : game.getSurroundingCells(i,j)) {
                        if(game.playerBoard[p.x][p.y] == Game.UNCHECKED) {
                            cell[p.x][p.y] = setJButtonImage("covered", i, j, 1, 1);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    
    
}