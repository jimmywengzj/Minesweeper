package src;
import javax.swing.*; 
import java.awt.event.*; 
import  java.util.ResourceBundle;

public class menu2 extends JFrame implements ActionListener{
    
    JFrame f;
    JMenuBar mb; 
    JMenu game, option,language;
    JMenuItem newgame,restart,beginner, intermediate, expert, custom,cn,fr,en;
    ResourceBundle op=ResourceBundle.getBundle("options");
    String l=op.getString("lang");
    ResourceBundle lang=ResourceBundle.getBundle("changelanguage/changelanguage_"+l);

    public menu2(){
        f=new JFrame();
        mb=new JMenuBar();
        
        //加入次级菜单
        newgame=new JMenuItem(lang.getString("NEW"));
        restart=new JMenuItem(lang.getString("RESTART"));
        
        beginner=new JMenuItem(lang.getString("BEGINNER"));
        intermediate=new JMenuItem(lang.getString("INTERMEDIATE"));
        expert=new JMenuItem(lang.getString("EXPERT"));
        custom=new JMenuItem(lang.getString("CUSTOM"));
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
        game=new JMenu(lang.getString("GAME"));
        option=new JMenu(lang.getString("OPTION"));
        language=new JMenu(lang.getString("LANGUAGE"));
        language.addActionListener(this);
        game.addActionListener(this);
        option.addActionListener(this);

        //将次级菜单加入Game
        game.add(newgame);game.add(restart);
        game.addSeparator();       //分割线
        game.add(beginner);game.add(intermediate);game.add(expert);game.add(custom);

        //将次级菜单加入option
        language.add(cn); language.add(fr); language.add(en);
        option.add(language);

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
            Language.change("Zh");
            updatename();

        }
        if(e.getSource()==fr){
            Language.change("Fr");
            updatename();
        }
        if(e.getSource()==en){
            Language.change("En");
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


        
    }


}
//Game.addSeparator(); //分割线
