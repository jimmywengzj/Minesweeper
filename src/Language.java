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
    public static String RESOURCE="";

    public static void setLanguage(String source){
        // read "changeLANGUAGE_fr.properties" and change the LANGUAGE
        ResourceBundle rb = ResourceBundle.getBundle("language/" + source);
        Options.lang = source;
        updateLanguage(rb);
    }

    public static void updateLanguage(ResourceBundle rb){
        GAME = rb.getString("GAME");
        OPTION = rb.getString("OPTION");
        NEW = rb.getString("NEW");
        RESTART = rb.getString("RESTART");
        BEGINNER = rb.getString("BEGINNER");
        INTERMEDIATE = rb.getString("INTERMEDIATE");
        EXPERT = rb.getString("EXPERT");
        CUSTOM = rb.getString("CUSTOM");
        LANGUAGE = rb.getString("LANGUAGE");
        RESOURCE = rb.getString("RESOURCE");
    }
}
