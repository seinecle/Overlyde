/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textprocessing;

import com.google.common.collect.HashMultiset;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    public Main(String wk, String file,String SCorWC) {

        Main.wk = wk+"\\";
        Main.file = file;
        Main.SCorWC = SCorWC;

    }

    @Override
    public void run() {
        try {
            
            IsiParser ip = new IsiParser();
           
            map = SubjectCategoryImporter.run();

            DynamicSizeForGexf dyn = new DynamicSizeForGexf(map);

            dyn.run();
        } catch (FileNotFoundException ex) {
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
