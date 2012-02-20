/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textprocessing;

import com.google.common.collect.HashMultiset;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author C. Levallois
 */
public class SubjectCategoryImporter {

    static String wk = "D:\\Docs Pro Clement\\E-projects\\OverlayToolkit\\";
    static String file = "erasmus.txt";
    static String currLine;
    static String currYear;
    static String[] currWCs;
    static HashMap<String, HashMultiset<String>> map = new HashMap();

    static HashMap run() throws FileNotFoundException, IOException {

        BufferedReader br = new BufferedReader(new FileReader(wk + file));

        while ((currLine = br.readLine()) != null) {
//
            currWCs = currLine.split("[;,]");
            currYear = currWCs[0];
            if (currYear.length() == 5) {
                currYear = currYear.substring(1);
            }

            if (Integer.valueOf(currYear) < Main.minYear) {
                Main.minYear = Integer.valueOf(currYear);
            }

            if (Integer.valueOf(currYear) > Main.maxYear) {
                Main.maxYear = Integer.valueOf(currYear);
            }


            
            for (int i = 1; i < currWCs.length; i++) {
                //System.out.println(currWCs[i]);
                HashMultiset<String> yearsForCurrWC = HashMultiset.create();
                currWCs[i] = currWCs[i].trim().toLowerCase();
                if (map.containsKey(currWCs[i])) {
                    yearsForCurrWC.addAll(map.get(currWCs[i]));
                }
                yearsForCurrWC.add(currYear);

                map.put(currWCs[i], yearsForCurrWC);
                //System.out.println(currWCs[i]);

            }


        }
        return map;

    }
}
