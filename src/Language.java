package src;
import  java.util.ResourceBundle;

public class Language {
    //赋予初始值
    public static String TITLE="";
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
    public static String AI="";
    public static String HINT="";
    public static String HINT_GAME_NOT_STARTED="";
    public static String HINT_WRONG_FLAG="";
    public static String HINT_BASIC_MINE="";
    public static String HINT_BASIC_SAFE="";
    public static String HINT_LOGIC_MINE="";
    public static String HINT_LOGIC_SAFE="";
    public static String HINT_PROBABILITY="";
    public static String HINT_PROBABILITY_TITLE="";
    public static String GUI_SCALE="";
    public static String GUI_SCALE1="";
    public static String GUI_SCALE2="";
    public static String GUI_SCALE3="";

    public static void setLanguage(String source){
        // read "changeLANGUAGE_fr.properties" and change the LANGUAGE
        ResourceBundle rb = ResourceBundle.getBundle("language/" + source);
        Options.lang = source;
        updateLanguage(rb);
    }

    public static void updateLanguage(ResourceBundle rb){
        TITLE = rb.getString("TITLE");
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
        AI = rb.getString("AI");
        HINT = rb.getString("HINT");
        HINT_GAME_NOT_STARTED = rb.getString("HINT_GAME_NOT_STARTED");
        HINT_WRONG_FLAG = rb.getString("HINT_WRONG_FLAG");
        HINT_BASIC_MINE = rb.getString("HINT_BASIC_MINE");
        HINT_BASIC_SAFE = rb.getString("HINT_BASIC_SAFE");
        HINT_LOGIC_MINE = rb.getString("HINT_LOGIC_MINE");
        HINT_LOGIC_SAFE = rb.getString("HINT_LOGIC_SAFE");
        HINT_PROBABILITY = rb.getString("HINT_PROBABILITY");
        HINT_PROBABILITY_TITLE = rb.getString("HINT_PROBABILITY_TITLE");
        GUI_SCALE = rb.getString("GUI_SCALE");
        GUI_SCALE1 = rb.getString("GUI_SCALE1");
        GUI_SCALE2 = rb.getString("GUI_SCALE2");
        GUI_SCALE3 = rb.getString("GUI_SCALE3");
    }
}
