package src;
import java.io.File;
import java.time.chrono.ThaiBuddhistChronology;

import javax.swing.*; 
import java.awt.event.*; 
import  java.util.ResourceBundle;


public class Resources extends menu2 {
   
    public static void getFilesNames(){
        //创建File对象
		File file = new File("..\\Minesweeper\\resources");
		//获取该目录下的所有文件
		if(file != null){
			String[] files = file.list();

			if(files != null){
				for(String f: files){
					JMenuItem a= new JMenuItem(f);
					final String resourceName=f;
					a.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							if(e.getSource()==a){

							}
						}
					});

					resource.add(new JMenuItem(resourceName));

				}
			}else{
				System.out.println(file);
			}

			
		}
		
    }		
		
}
