/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textprocessing;

import com.google.common.collect.HashMultiset;
import java.io.*;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class IsiParser {

    static String wk = Main.getWorkingDirectory();
    static String file = Main.getInputFile();
    static String currLine;
    static String currYear;
    static String[] currWCs;


    static HashMap<String, HashMultiset<String>> map = new HashMap();

    static boolean recordNotOver;

    IsiParser(){}
    
    HashMap run () throws FileNotFoundException, IOException {




        BufferedReader br = new BufferedReader(new FileReader(wk + file));
        LineNumberReader lnr = new LineNumberReader(br);



        while ((currLine = br.readLine()) != null) {

            if (currLine.startsWith("PY")) {

                currYear = StringUtils.right(currLine, 4);
                //System.out.println("current year is: "+currYear+". Line is: "+lnr.getLineNumber());
                if (Integer.valueOf(currYear)<Main.minYear)
                    Main.minYear = Integer.valueOf(currYear);

                if (Integer.valueOf(currYear)>Main.maxYear)
                    Main.maxYear = Integer.valueOf(currYear);
                
                
                
                recordNotOver = true;
                //currNode = StringUtils.replace(currLine, "(.*<node id=\")([0-9\,\.\+\-]+)(\" label=\".*\">)", "\2");
            }

            if (currLine.startsWith(Main.SCorWC)) {
                currWCs = currLine.split(";");
                currWCs[0] = StringUtils.right(currWCs[0],currWCs[0].length()-3);

                if (recordNotOver) {

                    for (int i = 0; i < currWCs.length; i++) {
                        HashMultiset<String> yearsForCurrWC = HashMultiset.create();
                        currWCs[i]= currWCs[i].trim();
                        if (map.containsKey(currWCs[i]))
                            yearsForCurrWC.addAll(map.get(currWCs[i]));                            
                        yearsForCurrWC.add(currYear);

                        map.put(currWCs[i],yearsForCurrWC);
                        //System.out.println(currWCs[i]);
                        

                    }
                
                recordNotOver = false;
                    
                } else {
                    System.out.println(Main.SCorWC+ " found outside of a record, line " + lnr.getLineNumber());
                }


            }


        }
        
    return map;    
        
    }
}
