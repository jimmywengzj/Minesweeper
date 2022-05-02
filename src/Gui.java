package src;

import src.util.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.event.*;
import java.util.*;

public class Gui {
    
    public static final int STATUS_BAR_BORDER_SIZE = 4;

    public static boolean leftPressed = false;
    public static boolean leftExited = false;
    public static boolean facePressed = false;
    public static boolean faceExited = false;

    public static JFrame frame;

    public static JMenuBar menuBar;
    public static JMenu gameMenu, optionMenu, languageMenu, resourceMenu, aiMenu, guiScale;
    public static JMenuItem newgame, restart, beginner, intermediate, expert, custom, cn, fr, en, hint, scale1, scale2, scale3;

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
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                Options.writeOptions();
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }

    public static void reInitPanel() {
        frame.remove(mainPanel);
        boardInit();
        gameInit();
        frame.add(mainPanel);
        frame.pack();
        frame.revalidate();
    }

    public static void menuInit(){
        menuBar = new JMenuBar();
        //加入次级菜单
        newgame = new JMenuItem();
        restart = new JMenuItem();
        
        beginner = new JMenuItem();
        intermediate = new JMenuItem();
        expert = new JMenuItem();
        custom = new JMenuItem();
        cn = new JMenuItem("中文");
        fr = new JMenuItem("Français");
        en = new JMenuItem("English");
        hint = new JMenuItem();
        scale1 = new JMenuItem();
        scale2 = new JMenuItem();
        scale3 = new JMenuItem();

        newgame.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                reInitPanel();
            }
        });
        restart.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.remove(mainPanel);
                boardInit();
                frame.add(mainPanel);
                frame.pack();
                frame.revalidate();
                for(int i = 0; i < game.row; i++) {
                    for(int j = 0; j < game.col; j++) {
                        game.playerBoard[i][j] = Game.UNCHECKED;
                    }
                }
                game.numMinesLeft = game.numMines;
                game.numCoveredCellsLeft = game.row * game.col;
                game.status = Game.STATUS_STARTED;
            }
        });
        beginner.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Options.row = 9;
                Options.col = 9;
                Options.nbBomb = 10;
                reInitPanel();
            }
        });
        intermediate.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Options.row = 16;
                Options.col = 16;
                Options.nbBomb = 40;
                reInitPanel();
            }
        });
        expert.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Options.row = 16;
                Options.col = 30;
                Options.nbBomb = 99;
                reInitPanel();
            }
        });

        custom.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JFrame customFrame = new JFrame();

                JPanel customMainPanel = new JPanel();
                customMainPanel.setLayout(null);
                customMainPanel.setPreferredSize(new Dimension(235, 180));
                JLabel customrow = new JLabel();
                customrow.setText(Language.HEIGHT);
                customrow.setBounds(20, 20, 100, 20);
                JTextField customRow = new JTextField(Integer.toString(Options.row));
                customRow.setBounds(175, 20, 40, 20);
                JLabel customcol = new JLabel();
                customcol.setText(Language.WIDTH);
                customcol.setBounds(20, 60, 100, 20);
                JTextField customCol = new JTextField(Integer.toString(Options.col));
                customCol.setBounds(175, 60, 40, 20);
                JLabel customnbBomb = new JLabel();
                customnbBomb.setText(Language.MINES);
                customnbBomb.setBounds(20, 100, 100, 20);
                JTextField customNbBomb = new JTextField(Integer.toString(Options.nbBomb));
                customNbBomb.setBounds(175, 100, 40, 20);
                JButton ok = new JButton(Language.OK);
                ok.setBounds(120, 140, 95, 20);
                JButton cancel = new JButton(Language.CANCEL);
                cancel.setBounds(20, 140, 95, 20);

                ok.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        customFrame.setVisible(false);
                        String NbRow = customRow.getText();
                        String NbCol = customCol.getText();
                        String NbBomb = customNbBomb.getText();
                        int r = Integer.parseInt(NbRow);
                        int c = Integer.parseInt(NbCol);
                        int b = Integer.parseInt(NbBomb);
                        if (r < 9) r = 9;
                        if (c < 9) c = 9;
                        if (r > 24) r = 24;
                        if (c > 30) c = 30;
                        if (b > (int)(0.9 * r * c)) b = (int)(0.9 * r * c);
                        if (b < 10) b = 10;
                        Options.row = r;
                        Options.col = c;
                        Options.nbBomb = b;
                        reInitPanel();
                    }
                });
                cancel.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        customFrame.setVisible(false);
                    }
                });

                customMainPanel.add(cancel);
                customMainPanel.add(ok);
                customMainPanel.add(customnbBomb);
                customMainPanel.add(customcol);
                customMainPanel.add(customrow);
                customMainPanel.add(customRow);
                customMainPanel.add(customCol);
                customMainPanel.add(customNbBomb);
                customFrame.add(customMainPanel);

                customFrame.pack();
                customFrame.setTitle(Language.CUSTOM_TITLE);
                customFrame.setLocation(100, 100);
                //customFrame.setSize(100,100);
                customFrame.setResizable(false);
                //customFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                customFrame.setVisible(true);

                
            }
        });

        cn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Language.setLanguage("Zh");
                updateMenuLanguage();
            }
        });
        fr.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Language.setLanguage("Fr");
                updateMenuLanguage();
            }
        });
        en.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Language.setLanguage("En");
                updateMenuLanguage();
            }
        });

        scale1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Options.scale = 1;
                reInitPanel();
            }
        });
        scale2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Options.scale = 2;
                reInitPanel();
            }
        });
        scale3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Options.scale = 3;
                reInitPanel();
            }
        });

        hint.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(game.status != Game.STATUS_STARTED) {
                    JOptionPane.showMessageDialog(frame, Language.HINT_GAME_NOT_STARTED);
                } else {
                    Integer[][] prob = new Integer[game.row][game.col];
                    Pair<Integer, Point> res = Solver.getHint(game, prob);
                    int x = res.getValue().x;
                    int y = res.getValue().y;
                    switch (res.getKey()) {
                        case Solver.WRONG_FLAG :    JOptionPane.showMessageDialog(frame, Language.HINT_WRONG_FLAG);
                                                    changeJButtonImageToColor(cell[x][y], Color.GREEN);
                                                    break;
                        case Solver.BASIC_MINE :    JOptionPane.showMessageDialog(frame, Language.HINT_BASIC_MINE);
                                                    setJButtonColoredBorder(cell[x][y], Color.RED);
                                                    break;
                        case Solver.BASIC_SAFE :    JOptionPane.showMessageDialog(frame, Language.HINT_BASIC_SAFE);
                                                    setJButtonColoredBorder(cell[x][y], Color.GREEN);
                                                    break;
                        case Solver.LOGIC_MINE :    JOptionPane.showMessageDialog(frame, Language.HINT_LOGIC_MINE);
                                                    changeJButtonImageToColor(cell[x][y], Color.RED);
                                                    break;
                        case Solver.LOGIC_SAFE :    JOptionPane.showMessageDialog(frame, Language.HINT_LOGIC_SAFE);
                                                    changeJButtonImageToColor(cell[x][y], Color.GREEN);
                                                    break;
                        case Solver.PROBABILITY :   JOptionPane.showMessageDialog(frame, Language.HINT_PROBABILITY);

                                                    int imageDimension = new ImageIcon("resources/" + Options.resource + "/0.png").getIconWidth();
                                                    JFrame probFrame = new JFrame(Language.HINT_PROBABILITY_TITLE);
                                                    JTable jTable = new JTable(prob, prob[0]);
                                                    jTable.setTableHeader(null);
                                                    jTable.setFont(new Font("Serif", Font.PLAIN, 13 * Options.scale));
                                                    jTable.setRowHeight(imageDimension * Options.scale);

                                                    JScrollPane jScrollPane = new JScrollPane(jTable);
                                                    jScrollPane.setPreferredSize(new Dimension(imageDimension * game.col * Options.scale + 3, imageDimension * game.row * Options.scale + 3));
                                                    
                                                    probFrame.add(jScrollPane);
                                                    probFrame.pack();
                                                    probFrame.setLocation(400, 180);
                                                    probFrame.setVisible(true);
                                                    break;
                        default : break;
                    }
                }
            }
        });

        //加入菜单
        gameMenu = new JMenu();
        optionMenu = new JMenu();
        languageMenu = new JMenu();
        resourceMenu = new JMenu();
        aiMenu = new JMenu();
        guiScale = new JMenu();
        
        //创建File对象
        File resourcesFolder = new File("../Minesweeper/resources");
        //获取该目录下的所有文件
        if(resourcesFolder != null){
            String[] resourcePacks = resourcesFolder.list();

            if(resourcePacks != null){
                for(final String resourceName: resourcePacks){
                    JMenuItem a = new JMenuItem(resourceName);
                    a.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                            Options.resource = resourceName;
                            reInitPanel();
                        }
                    });
                    resourceMenu.add(a);
                }
            }
        }

        updateMenuLanguage();

        //将次级菜单加入Game
        gameMenu.add(newgame); gameMenu.add(restart);
        gameMenu.addSeparator();       //分割线
        gameMenu.add(beginner); gameMenu.add(intermediate); gameMenu.add(expert); gameMenu.add(custom);

        //将次级菜单加入option
        languageMenu.add(cn); languageMenu.add(fr); languageMenu.add(en);
        guiScale.add(scale1); guiScale.add(scale2); guiScale.add(scale3);
        optionMenu.add(languageMenu); optionMenu.add(resourceMenu); optionMenu.add(guiScale); 

        aiMenu.add(hint);

        menuBar.add(gameMenu); menuBar.add(optionMenu); menuBar.add(aiMenu);     //将菜单加入菜单栏
    }

    public static void updateMenuLanguage(){
        frame.setTitle(Language.TITLE);
        gameMenu.setText(Language.GAME);
        newgame.setText(Language.NEW);    
        optionMenu.setText(Language.OPTION);
        restart.setText(Language.RESTART);
        beginner.setText(Language.BEGINNER);
        intermediate.setText(Language.INTERMEDIATE);
        expert.setText(Language.EXPERT);
        custom.setText(Language.CUSTOM);
        languageMenu.setText(Language.LANGUAGE);
        resourceMenu.setText(Language.RESOURCE);
        aiMenu.setText(Language.AI);
        hint.setText(Language.HINT);
        guiScale.setText(Language.GUI_SCALE);
        scale1.setText(Language.GUI_SCALE1);
        scale2.setText(Language.GUI_SCALE2);
        scale3.setText(Language.GUI_SCALE3);
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
        statusBar.setLocation(x1 * Options.scale, y1 * Options.scale);
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
        face.setBorder(null);
        face.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    facePress();
                }
            }
            public void mouseReleased(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    faceRelease();
                }
            }
            public void mouseExited(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    faceExit();
                }
            }
        });

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

        cell = new JButton[Options.row][Options.col];
        int cellWidth = getImageWidth("covered.png");
        int cellHeight = getImageHeight("covered.png");
        for (int i = 0; i < Options.row; i++) {
            for (int j = 0; j < Options.col; j++) {
                cell[i][j] = setJButtonImage("covered.png", x1 + cellWidth * j, y3 + cellHeight * i, 1, 1);
                cell[i][j].setBorder(null);
                final int i2 = i;
                final int j2 = j;
                cell[i][j].addMouseListener(new MouseAdapter() { 
                    public void mousePressed(MouseEvent e) { 
                        if(SwingUtilities.isRightMouseButton(e)) {
                            System.out.println("alright");
                            rightPress(i2,j2);
                        } else if(SwingUtilities.isLeftMouseButton(e)) {
                            System.out.println("hey you");
                            leftPress(i2,j2);
                        }
                    };

                    public void mouseExited(MouseEvent e) {
                        if(SwingUtilities.isRightMouseButton(e)) {
                            System.out.println("alright I'm out");
                            rightExit(i2,j2);
                        }
                        if(SwingUtilities.isLeftMouseButton(e)) {
                            leftExit(i2,j2);
                        }
                    }

                    public void mouseReleased(MouseEvent e) {
                        if(SwingUtilities.isRightMouseButton(e)) {
                            rightRelease(i2,j2);
                        }
                        if(SwingUtilities.isLeftMouseButton(e)) {
                            leftRelease(i2,j2);
                        }
                    }

                    public void mouseClicked(MouseEvent e) {
                        if(SwingUtilities.isRightMouseButton(e)) {
                            rightClick(i2,j2);
                        }
                        if(SwingUtilities.isLeftMouseButton(e)) {
                            leftClick(i2,j2);
                        }
                    }
                });

                mainPanel.add(cell[i][j]);
            }
        }

    }

    public static void facePress() {
        facePressed = true;
    }

    public static void faceRelease() {
        if(facePressed && !faceExited) {
            changeFaceImage("faceSmiley");
            reInitPanel();
        }

        facePressed = false;
        faceExited = false;
    }

    public static void faceExit() {
        faceExited = true;
    }

    public static void rightExit(int i, int j) {
        if(game.playerBoard[i][j] <= 8 && game.playerBoard[i][j] > 0) {
            for(Point p : game.getSurroundingCells(i,j)) {
                if(game.playerBoard[p.x][p.y] == Game.UNCHECKED) {
                    changeJButtonImage(cell[p.x][p.y], Game.UNCHECKED);
                }
            }
        }
    }

    public static void rightRelease(int i, int j) {
        if(game.playerBoard[i][j] <= 8 && game.playerBoard[i][j] > 0 && game.status == Game.STATUS_STARTED) {
            int numFlags = 0;
            for(Point p : game.getSurroundingCells(i,j)) {
                if(game.playerBoard[p.x][p.y] == Game.FLAG) numFlags++;
            }

            if(numFlags == game.playerBoard[i][j]) {
                for(Point p : game.getSurroundingCells(i,j)) {
                    if(game.playerBoard[p.x][p.y] == Game.FLAG || game.playerBoard[p.x][p.y] != Game.QUESTION) {
                        leftPress(p.x, p.y);
                        leftRelease(p.x, p.y);
                    }
                }
            } else {
                for(Point p : game.getSurroundingCells(i,j)) {
                    if(game.playerBoard[p.x][p.y] == Game.UNCHECKED) {
                        changeJButtonImage(cell[p.x][p.y], Game.UNCHECKED);
                    } 
                }       
            }
        }
    }

    public static void rightPress(int i, int j) {
        if(game.status == Game.STATUS_STARTED) {
            if(game.playerBoard[i][j] == Game.UNCHECKED) { // if right click on unchecked block
                game.playerBoard[i][j] = Game.FLAG;
                changeJButtonImage(cell[i][j], Game.FLAG);
                game.numMinesLeft --;
            
            // update image
            } else if(game.playerBoard[i][j] == Game.FLAG) { // if right click on flag
                game.playerBoard[i][j] = Game.QUESTION;
                changeJButtonImage(cell[i][j], Game.QUESTION);
            } else if(game.playerBoard[i][j] == Game.QUESTION) { // if right click on question mark
                game.playerBoard[i][j] = Game.UNCHECKED;
                changeJButtonImage(cell[i][j], Game.UNCHECKED);
            } else if(game.playerBoard[i][j] <= 8 && game.playerBoard[i][j] > 0) { // if right click on checked and numbered block
                for(Point p : game.getSurroundingCells(i,j)) {
                    if(game.playerBoard[p.x][p.y] == Game.UNCHECKED) {
                        changeJButtonImage(cell[p.x][p.y], 0);
                    }
                }
            }
        }
        
    }

    public static void rightClick(int i, int j) {

    }


    public static void leftExit(int i, int j) {
        if(game.playerBoard[i][j] == Game.UNCHECKED && leftPressed) {
            changeJButtonImage(cell[i][j], Game.UNCHECKED);
        }
        
        leftExited = true;
    }

    public static void leftPress(int i, int j) {
        if(game.status == Game.STATUS_LOST) {
            changeFaceImage("faceSad");
        } else if(game.status != Game.STATUS_WON) {
            changeFaceImage("faceCurious");
        }
        

        if(game.playerBoard[i][j] == Game.UNCHECKED && (game.status == Game.STATUS_STARTED)) {
            changeJButtonImage(cell[i][j], 0);
        }
        
        leftPressed = true;
    }

    public static void leftRelease(int i, int j) {
        if(game.status == Game.STATUS_STARTED || game.status == Game.STATUS_NOT_STARTED) {
            changeFaceImage("faceSmiley");
        }
        
        if(game.playerBoard[i][j] == Game.UNCHECKED && !leftExited && leftPressed) { // if left click on unchecked block
            
            HashSet<Point> revealedCells = new HashSet<Point>();
            if(game.status == Game.STATUS_NOT_STARTED) {
                game.status = Game.STATUS_STARTED;
                game.initBoard(i, j);
                System.out.println("yes");
            }
            if(game.status == Game.STATUS_STARTED) {
                if(game.revealCell(i, j, revealedCells)) {
                    for(Point p : revealedCells) {
                        int info = game.getPlayerBoard(p.x, p.y);
                        changeJButtonImage(cell[p.x][p.y], info);
                        System.out.println(p.x + " " + p.y);
                    }
                    
                    if(game.numCoveredCellsLeft == game.numMines) {
                        game.status = Game.STATUS_WON;
                        changeFaceImage("faceCool");
                        for(int m = 0; m < game.row; m++) {
                            for(int n = 0; n < game.col; n++) {
                                if(game.playerBoard[m][n] == Game.UNCHECKED) {
                                    changeJButtonImage(cell[m][n], Game.FLAG);
                                }
                            }
                        }

                    }

                } else {
                    
                    changeFaceImage("faceSad");
                    game.status = Game.STATUS_LOST;
                    for(int m = 0; m < game.row; m++) {
                        for(int n = 0; n < game.col; n++) {
                            switch(game.playerBoard[m][n]) {
                                case Game.FLAG: 

                                    if(game.infoBoard[m][n] != Game.MINE) {
                                        changeJButtonImage(cell[m][n], Game.WRONG_FLAG);
                                    } 
                                    break;
                                
                                default:
                                    break;
                            }
                            switch(game.infoBoard[m][n]) {
                                case Game.MINE:
                                    changeJButtonImage(cell[m][n], Game.MINE);
                            }
                        }
                    }
                    changeJButtonImage(cell[i][j], Game.WRONG_MINE);
                }
            } 


        } 
        
        leftExited = false;
    }

    public static void leftClick(int i, int j) {
        
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

    public static void changeJButtonImage(JButton jButton, int n) {
        String fileName;
        switch (n) {
            case 1002:  fileName = "covered.png";
                        break;
            case 1003:  fileName = "flag.png";
                        break;
            case 1004:  fileName = "questionMark.png";
                        break;
            case 1005:  fileName = "bomb.png";
                        break;
            case 1007:  fileName = "exploded.png";
                        break;         
            case 1008:  fileName = "wrongGuess.png";
                        break;
            default:    fileName = Integer.toString(n) + ".png";
                        break;
        }
        ImageIcon icon = new ImageIcon("resources/" + Options.resource + "/" + fileName);
        int width = icon.getIconWidth() * Options.scale;
        int height = icon.getIconHeight() * Options.scale;
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        jButton.setIcon(new ImageIcon(scaledImage));
    }

    public static void changeJButtonImageToColor(JButton jButton, Color c) {
        Icon icon = jButton.getIcon();
        
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();

        graphics.setPaint(c);
        graphics.fillRect(0, 0, icon.getIconWidth(), icon.getIconWidth());
        graphics.dispose();

        jButton.setIcon(new ImageIcon(bufferedImage));
    }

    public static void setJButtonColoredBorder(JButton jButton, Color c) {
        Icon icon = jButton.getIcon();
        
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();

        icon.paintIcon(null, graphics, 0, 0);

        graphics.setPaint(c);
        graphics.setStroke(new BasicStroke(2 * Options.scale));
        graphics.drawRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graphics.dispose();

        jButton.setIcon(new ImageIcon(bufferedImage));
    }

    public static void changeFaceImage(String faceType) {
        String fileName;
        switch (faceType) {
            case "faceCool":    fileName = "face_Cool.png";
                                break;
            case "faceCurious": fileName = "face_Curious.png";
                                break;
            case "faceSad":     fileName = "face_Sad.png";
                                break;
            case "faceSmiley":  fileName = "face_Smiley.png";
                                break;
            default:            fileName = "faceSmiley";
                                break;
        }
        ImageIcon icon = new ImageIcon("resources/" + Options.resource + "/" + fileName);
        int width = icon.getIconWidth() * Options.scale;
        int height = icon.getIconHeight() * Options.scale;
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        face.setIcon(new ImageIcon(scaledImage));
    }

    public static void gameInit(){
        game = new Game();
    }

}
