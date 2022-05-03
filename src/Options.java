package src;
import java.io.*;
import  java.util.*;

public class Options{
    public static int row;
    public static int col;
    public static int nbBomb;
    public static String resource = "";
    public static int scale;
    public static String lang = "";
    public static int rule;

    //read properties file
    public static void loadOptions(){
        try (InputStream input = new FileInputStream("options.properties")) {
            Properties properties = new Properties();
            // load a properties file
            properties.load(input);

            row = Integer.parseInt(properties.getProperty("row"));
            col = Integer.parseInt(properties.getProperty("col"));
            nbBomb = Integer.parseInt(properties.getProperty("nbBomb"));
            resource = properties.getProperty("resource");
            scale = Integer.parseInt(properties.getProperty("scale"));
            lang = properties.getProperty("lang");
            Language.setLanguage(lang);
            rule = Integer.parseInt(properties.getProperty("rule"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //write properties file
    public static void writeOptions(){
        try(OutputStream outputStream = new FileOutputStream("options.properties");){
            Properties properties = new Properties();

            properties.setProperty("row", String.valueOf(row));
            properties.setProperty("col", String.valueOf(col));
            properties.setProperty("nbBomb", String.valueOf(nbBomb));
            properties.setProperty("resource", resource);
            properties.setProperty("scale", String.valueOf(scale));
            properties.setProperty("lang", lang);
            properties.setProperty("rule", String.valueOf(rule));

            properties.store(outputStream, "Game options");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}