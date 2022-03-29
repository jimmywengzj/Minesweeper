package src;
import javax.swing.*; 
import java.awt.event.*; 
import  java.util.Locale;
import  java.util.ResourceBundle;

public class menu2 extends JFrame implements ActionListener{
    
    JFrame f;
    JMenuBar mb; 
    JMenu gameMenu, optionMenu,langMenu;
    JMenuItem newGame,restartGame, beginner, intermediate, expert, custom, cn, fr, en;

    public menu2(){
        f=new JFrame();
        mb=new JMenuBar();
        
        //加入次级菜单
        newGame = new JMenuItem("New");
        restartGame = new JMenuItem("Restart");
        
        beginner = new JMenuItem("Beginner");
        intermediate = new JMenuItem("Intermediate");
        expert = new JMenuItem("Expert");
        custom = new JMenuItem("Custom");
        cn = new JMenuItem("中文");
        fr = new JMenuItem("Français");
        en = new JMenuItem("English");

        newGame.addActionListener(this);
        restartGame.addActionListener(this);
        beginner.addActionListener(this);
        intermediate.addActionListener(this);
        expert.addActionListener(this);
        custom.addActionListener(this);
        cn.addActionListener(this);
        fr.addActionListener(this);
        en.addActionListener(this);

        //加入菜单
        gameMenu=new JMenu("Game");
        optionMenu=new JMenu("Option");
        langMenu=new JMenu("Language");
        langMenu.addActionListener(this);
        gameMenu.addActionListener(this);
        optionMenu.addActionListener(this);

        //将次级菜单加入Game
        gameMenu.add(newGame);gameMenu.add(restartGame);
        gameMenu.addSeparator();       //分割线
        gameMenu.add(beginner);gameMenu.add(intermediate);gameMenu.add(expert);gameMenu.add(custom);

        //将次级菜单加入option
        langMenu.add(cn); langMenu.add(fr); langMenu.add(en);
        optionMenu.add(langMenu);

        mb.add(gameMenu);mb.add(optionMenu);      //将两个菜单加入菜单栏
        f.add(mb);                        //在窗口中加入menu
        
        f.setJMenuBar(mb);  
        f.setLayout(null);
        f.setVisible(true);      
    }
    
    //Locale cn=Locale.CHINA;
    //Locale en=Locale.ENGLISH;
    private static String G="";
    

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newGame){

        }
        if(e.getSource() == restartGame){

        }
        if(e.getSource() == beginner){

        }
        if(e.getSource() == intermediate){

        }
        if(e.getSource() == expert){

        }
        if(e.getSource() == custom){

        }
        if(e.getSource() == langMenu){
            if(e.getSource() == cn){

            }
            if(e.getSource() == fr){
                
                
            }
            if(e.getSource() == en){
                
            }
        }
    
    } 
    
    
    public static String getPathfr(){
        ResourceBundle rb=ResourceBundle.getBundle("resources/changelanguage_fr");
        G=rb.getString("Game");
        return G;
    }
}



//Game.addSeparator(); //分割线
