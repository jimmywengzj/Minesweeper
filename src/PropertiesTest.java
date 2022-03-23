package src;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class PropertiesTest {
    public void testPropertiesReadFile(){
        try(
            InputStream inputStream=PropertiesTest.class.getClassLoader().getResourceAsStream("changelanguage_fr.properties")
        ){
            Properties properties=new Properties();
            properties.load(inputStream);

            java.util.Set<String> Name=properties.stringPropertyNames();
            
            for(String propertyName:Name){
                System.out.println("属性名"+propertyName);
            }

            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
