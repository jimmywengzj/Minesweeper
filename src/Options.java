package src;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import  java.util.ResourceBundle;
import java.util.Properties;

public class Options{
    public static int row;
    public static int col;
    public static int nbBomb;
    public static String resource = "";
    public static int scale;
    public static String lang = "";

    public static String path = "MINESWEEPER/options.properties";
    
    //read properties file
    public static void loadOptions(){
        ResourceBundle rb = ResourceBundle.getBundle("options");

        row = Integer.parseInt(rb.getString("row"));
        col = Integer.parseInt(rb.getString("col"));
        nbBomb = Integer.parseInt(rb.getString("nbbomb"));
        resource = rb.getString("resource");
        scale = Integer.parseInt(rb.getString("scale"));
        lang = rb.getString("lang");
        Language.setLanguage(lang);
    }
    
    //write properties file
    public static void writeOptions() throws FileNotFoundException, IOException{
        try(
            OutputStream outputStream = new FileOutputStream(path);
        ){
            Properties properties = new Properties();

            properties.setProperty("row", String.valueOf(row));
            properties.setProperty("col", String.valueOf(col));
            properties.setProperty("nbBomb", String.valueOf(nbBomb));
            properties.setProperty("resource", resource);
            properties.setProperty("scale", String.valueOf(scale));
            properties.setProperty("lang", lang);

            properties.store(outputStream, "new options");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}