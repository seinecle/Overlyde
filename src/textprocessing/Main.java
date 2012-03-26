/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textprocessing;

import com.google.common.collect.HashMultiset;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author C. Levallois
 */
public class Main implements Runnable {

    static HashMap<String, HashMultiset<String>> map = new HashMap();
    static int minYear = 5000;
    static int maxYear = 0;
    static private String wk;
    static private String file;
    static public String SCorWC = "WC";
    //static String file = "overlay_nude_2010_with_clusters.gexf";
    //static String file = "overlay_nude_2007.gexf";
    //static String output = "dynamic_overlay_2010_"+StringUtils.left(IsiParser.file, IsiParser.file.length()-4)+".gexf";
    static String output;
    static BufferedReader br;
    static StringBuilder sb = new StringBuilder();
    static String newLine = "\n";
    static int count = 0;
    static InputStream nudeMap;
    static int nbLines;
    private static String webOrList;


    public Main(String wk, String file,String SCorWC, String webOrList) {

        Main.wk = wk+"\\";
        Main.file = file;
        Main.SCorWC = SCorWC;
        Main.webOrList = webOrList;
        System.out.println(SCorWC);

    }

    @Override
    public void run() {
        try {
            if (webOrList.equals("web")){
            IsiParser ip = new IsiParser();
           
            map = ip.run();}
            else{
                
                map = SubjectCategoryImporter.run();
            
            }

            DynamicSizeForGexf dyn = new DynamicSizeForGexf(map);
            Thread t = new Thread(dyn, "Code Executer");
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
                Screen1.jLabel3.setText("<html>"+dyn.output+"<br>=> this file has been created in the directory of your ISI records.<br>watch this very simple tutorial on youtube on how to use it.</html>");
            Screen1.jLabel3.setVisible(true);
            Screen1.jLabel4.setText("<html><u>http://youtu.be/jU5Gbh8MNM4</u></html>");
            Screen1.jLabel4.setVisible(true);
            Screen1.jButton2.setVisible(true);
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static public String getWorkingDirectory() {

        return wk;
    }

    static public String getInputFile() {

        return file;
    }


   


}

