package src;
import  java.util.Locale;
import  java.util.ResourceBundle;

import src.util.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Minesweeper {
    
    public static void main(String[] args) {
        Options.loadOptions();
        new menu2();
        System.out.println(Language.GAME);
        //Gui.init();
    }
   
    
}
