package src;
import java.util.Properties;
import  java.util.ResourceBundle;

public class Language {
    //赋予初始值
    public static String GAME="";
    public static String OPTION="";
    public static String NEW="";
    public static String RESTART="";
    public static String BEGINNER="";
    public static String INTERMEDIATE="";
    public static String EXPERT="";
    public static String CUSTOM="";
    public static String LANGUAGE="";

    public static void change(String source){
        
        if(source == "Fr"){
            //read "changeLANGUAGE_fr.properties" and change the LANGUAGE
            ResourceBundle rb=ResourceBundle.getBundle("changelanguage/changelanguage_fr");
            changename(rb);
        
        }else if(source == "Zh"){
            ResourceBundle rb=ResourceBundle.getBundle("changelanguage/changelanguage_zh");
            changename(rb);

        }else if(source == "En"){
            ResourceBundle rb=ResourceBundle.getBundle("changelanguage/changelanguage_en");
            changename(rb);

        }
    }

    public static void changename(ResourceBundle rb){
        GAME=rb.getString("GAME");
        OPTION=rb.getString("OPTION");
        NEW=rb.getString("NEW");
        RESTART=rb.getString("RESTART");
        BEGINNER=rb.getString("BEGINNER");
        INTERMEDIATE=rb.getString("INTERMEDIATE");
        EXPERT=rb.getString("EXPERT");
        CUSTOM=rb.getString("CUSTOM");
        LANGUAGE=rb.getString("LANGUAGE");
    }

    
}
