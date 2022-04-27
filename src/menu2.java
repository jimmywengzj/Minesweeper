package src;
import javax.swing.*; 
import java.awt.event.*; 
import  java.util.ResourceBundle;

public class menu2 extends JFrame implements ActionListener{
    
    static JFrame f;
    JMenuBar mb; 
    JMenu game, option,language; 
    static JMenu resource;
    JMenuItem newgame,restart,beginner, intermediate, expert, custom,cn,fr,en;
    ResourceBundle op=ResourceBundle.getBundle("options");
    public static String l;
    //String l=op.getString("lang");
    //ResourceBundle lang=ResourceBundle.getBundle("changelanguage/changelanguage_"+l);

    public menu2(){
        f=new JFrame();
        mb=new JMenuBar();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //加入次级菜单
        newgame=new JMenuItem();
        restart=new JMenuItem();
        
        beginner=new JMenuItem();
        intermediate=new JMenuItem();
        expert=new JMenuItem();
        custom=new JMenuItem();
        cn=new JMenuItem("中文");
        fr=new JMenuItem("Français");
        en=new JMenuItem("English");
        

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
        game=new JMenu();
        option=new JMenu();
        language=new JMenu();
        resource = new JMenu();
        language.addActionListener(this);
        game.addActionListener(this);
        option.addActionListener(this);
        resource.addActionListener(this);
        
        Resources.getFilesNames();//程序在src文件运行，所以要返回到上一级文件
   

        updatename();

        //将次级菜单加入Game
        game.add(newgame);game.add(restart);
        game.addSeparator();       //分割线
        game.add(beginner);game.add(intermediate);game.add(expert);game.add(custom);

        //将次级菜单加入option
        language.add(cn); language.add(fr); language.add(en);
        option.add(language);option.add(resource);

        mb.add(game);mb.add(option);      //将两个菜单加入菜单栏
        f.add(mb);                        //在窗口中加入menu
        
        f.setJMenuBar(mb);  
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
            l="Zh";
            Language.change(l);
            updatename();
            

        }
        if(e.getSource()==fr){
            Language.change("Fr");
            updatename();
            
        }
        if(e.getSource()==en){
            l="En";
            Language.change(l);
            updatename();

        }
        
    
    } 
    
    //读取文件
    public void updatename(){
        game.setText(Language.GAME);
        newgame.setText(Language.NEW);    
        option.setText(Language.OPTION);
        restart.setText(Language.RESTART);
        beginner.setText(Language.BEGINNER);
        intermediate.setText(Language.INTERMEDIATE);
        expert.setText(Language.EXPERT);
        custom.setText(Language.CUSTOM);
        language.setText(Language.LANGUAGE);
        resource.setText(Language.RESOURCE);
       
    }


}

