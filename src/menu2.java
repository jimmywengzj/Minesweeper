package src;
import javax.swing.*; 
import java.awt.event.*; 

public class menu2 extends JFrame implements ActionListener{
    
    JFrame f;
    JMenuBar mb; 
    JMenu Game, option,language;
    JMenuItem New,restart,Beginner, intermediate, expert, Custom,中文,Français, English;

    public menu2(){
        f=new JFrame();
        mb=new JMenuBar();
        
        //加入次级菜单
        New=new JMenuItem("New");
        restart=new JMenuItem("Restart");
        
        Beginner=new JMenuItem("Beginner");
        intermediate=new JMenuItem("Intermediate");
        expert=new JMenuItem("Expert");
        Custom=new JMenuItem("Custom Board");
        中文=new JMenuItem("中文");
        Français=new JMenuItem("français");
        English=new JMenuItem("English");

        New.addActionListener(this);
        restart.addActionListener(this);
        Beginner.addActionListener(this);
        intermediate.addActionListener(this);
        expert.addActionListener(this);
        Custom.addActionListener(this);
        中文.addActionListener(this);
        Français.addActionListener(this);
        English.addActionListener(this);

        //加入菜单
        Game=new JMenu("Game");
        option=new JMenu("Option");
        language=new JMenu("Language");
        language.addActionListener(this);
        Game.addActionListener(this);
        option.addActionListener(this);

        //将次级菜单加入Game
        Game.add(New);Game.add(restart);
        Game.addSeparator();       //分割线
        Game.add(Beginner);Game.add(intermediate);Game.add(expert);Game.add(Custom);

        //将次级菜单加入option
        language.add(中文); language.add(Français); language.add(English);
        option.add(language);

        mb.add(Game);mb.add(option);      //将两个菜单加入菜单栏
        f.add(mb);                        //在窗口中加入menu
        
        f.setJMenuBar(mb);  
        f.setLayout(null);
        f.setVisible(true);      
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==New){

        }
        if(e.getSource()==restart){

        }
        if(e.getSource()==Beginner){

        }
        if(e.getSource()==intermediate){

        }
        if(e.getSource()==expert){

        }
        if(e.getSource()==Custom){

        }
        if(e.getSource()==language){
            if(e.getSource()==中文){

            }
            if(e.getSource()==Français){

            }
            if(e.getSource()==English){

            }
        }
    
    }
    
}

//Game.addSeparator(); //分割线
