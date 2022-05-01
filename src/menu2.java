package src;
import javax.swing.*; 
import java.awt.event.*; 
import  java.util.ResourceBundle;

public class menu2 extends JFrame implements ActionListener{
    
    static JFrame f;
    JMenuBar menuBar; 
    JMenu gameMenu, optionMenu, languageMenu; 
    static JMenu resourceMenu;
    JMenuItem newgame, restart, beginner, intermediate, expert, custom, cn, fr, en;
    //ResourceBundle op = ResourceBundle.getBundle("options");

    public menu2(){
        f = new JFrame();
        menuBar = new JMenuBar();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
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
        
        newgame.addActionListener(this);
        restart.addActionListener(this);
        beginner.addActionListener(this);
        intermediate.addActionListener(this);
        expert.addActionListener(this);
        custom.addActionListener(this);
        cn.addActionListener(this);
        fr.addActionListener(this);
        en.addActionListener(this);

        //加入菜单
        gameMenu = new JMenu();
        optionMenu = new JMenu();
        languageMenu = new JMenu();
        resourceMenu = new JMenu();
        languageMenu.addActionListener(this);
        gameMenu.addActionListener(this);
        optionMenu.addActionListener(this);
        resourceMenu.addActionListener(this);
        
        Resources.getFilesNames();//程序在src文件运行，所以要返回到上一级文件

        updateMenuLanguage();

        //将次级菜单加入Game
        gameMenu.add(newgame); gameMenu.add(restart);
        gameMenu.addSeparator();       //分割线
        gameMenu.add(beginner); gameMenu.add(intermediate); gameMenu.add(expert); gameMenu.add(custom);

        //将次级菜单加入option
        languageMenu.add(cn); languageMenu.add(fr); languageMenu.add(en);
        optionMenu.add(languageMenu); optionMenu.add(resourceMenu);

        menuBar.add(gameMenu); menuBar.add(optionMenu);      //将两个菜单加入菜单栏
        f.add(menuBar);                        //在窗口中加入menu
        
        f.setJMenuBar(menuBar);  
        f.setLayout(null);
        f.setVisible(true);      
    }
    
    //Locale cn=Locale.CHINA;
    //Locale en=Locale.ENGLISH;
    
    public  void actionPerformed(ActionEvent e) {
        if(e.getSource()==newgame){

        }
        if(e.getSource() == restart){

        }

        if(e.getSource()==beginner){

        }
        if(e.getSource() == intermediate){

        }
        if(e.getSource() == expert){

        }
        if(e.getSource()==custom){

        }
        
        if(e.getSource()==cn){
            Language.setLanguage("Zh");
            updateMenuLanguage();
        }
        if(e.getSource()==fr){
            Language.setLanguage("Fr");
            updateMenuLanguage();
        }
        if(e.getSource()==en){
            Language.setLanguage("En");
            updateMenuLanguage();
        }
    } 
    
    //读取文件
    public void updateMenuLanguage(){
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
    }
}

