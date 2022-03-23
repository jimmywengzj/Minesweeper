package src;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import java.awt.event.*;
import java.beans.Customizer; 

public class MinesweeperGui extends JFrame implements ActionListener {
    
    JFrame f;
    JMenuBar mb; 
    JMenu gameMenu, optionMenu,languageMenu;
    JMenuItem newGameItem, restartItem, beginnerItem, intermediateItem, expertItem, customItem, cnLangItem, frLangItem, enLangItem;

    public MinesweeperGui() {
        f=new JFrame();
        mb=new JMenuBar();
        
        //加入次级菜单 add secondary menu elements
        newGameItem = new JMenuItem("New");
        restartItem = new JMenuItem("Restart");
        beginnerItem = new JMenuItem("Beginner");
        intermediateItem = new JMenuItem("Intermediate");
        expertItem = new JMenuItem("Expert");
        customItem = new JMenuItem("Custom Board");
        cnLangItem = new JMenuItem("中文");
        frLangItem = new JMenuItem("français");
        enLangItem = new JMenuItem("English");

        newGameItem.addActionListener(this);
        restartItem.addActionListener(this);
        beginnerItem.addActionListener(this);
        intermediateItem.addActionListener(this);
        expertItem.addActionListener(this);
        customItem.addActionListener(this);
        cnLangItem.addActionListener(this);
        frLangItem.addActionListener(this);
        enLangItem.addActionListener(this);

        //加入菜单 add menu elements
        gameMenu = new JMenu("Game");
        optionMenu=new JMenu("Option");
        languageMenu=new JMenu("Language");
        gameMenu.addActionListener(this);
        optionMenu.addActionListener(this);
        languageMenu.addActionListener(this);

        //将次级菜单加入gameMenu   add secondary elements to gameMenu
        gameMenu.add(newGameItem);
        gameMenu.add(restartItem);
        gameMenu.addSeparator();       //分割线
        gameMenu.add(beginnerItem);
        gameMenu.add(intermediateItem);
        gameMenu.add(expertItem);
        gameMenu.add(customItem);

        //将次级菜单加入optionMenu    add secondary elements to optionMenu 
        languageMenu.add(cnLangItem); 
        languageMenu.add(frLangItem); 
        languageMenu.add(enLangItem);
        optionMenu.add(languageMenu);

        mb.add(gameMenu);mb.add(optionMenu);      //将两个菜单加入菜单栏
        f.add(mb);                                //在窗口中加入menu


        

        
        f.setJMenuBar(mb);  
        f.setLayout(null);
        f.setVisible(true);      
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newGameItem) {

        }
        if(e.getSource() == restartItem) {

        }
        if(e.getSource() == beginnerItem) {

        }
        if(e.getSource() == intermediateItem) {

        }
        if(e.getSource() == expertItem) {

        }
        if(e.getSource() == customItem) {

        }
        if(e.getSource() == languageMenu) {
            if(e.getSource() == cnLangItem){

            }
            if(e.getSource() == frLangItem){

            }
            if(e.getSource() == enLangItem){

            }
        }
    
    }
    
}

//Game.addSeparator(); //分割线
