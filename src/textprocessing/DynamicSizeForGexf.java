/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textprocessing;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset.Entry;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

/**
 *
 * @author C. Levallois
 */
public class DynamicSizeForGexf implements Runnable {

    static HashMap<String, HashMultiset<String>> map = new HashMap();
    //static String file = "overlay_nude_2010_with_clusters.gexf";
    //static String file = "overlay_nude_2007.gexf";
    //static String output = "dynamic_overlay_2010_"+StringUtils.left(IsiParser.file, IsiParser.file.length()-4)+".gexf";
    public String output;
    static BufferedReader br;
    static String currLine;
    static String currNodeId;
    static String currNodeLabel;
    static StringBuilder sb = new StringBuilder();
    static String newLine = "\n";
    static int count = 0;
    static InputStream nudeMap;
    static int nbLines;

    DynamicSizeForGexf(HashMap map) {

        DynamicSizeForGexf.map = map;


    }

    @Override
    public void run() {
        
        
        try {
            if ("SC".equals(Main.SCorWC)){
                nudeMap = Main.class.getResourceAsStream("overlay_nude_2007.txt");
                output = "dynamic_overlay_2007_"+StringUtils.left(IsiParser.file, IsiParser.file.length()-4)+".gexf";
                nbLines = 22335;
                
            }
            else{ nudeMap = Main.class.getResourceAsStream("overlay_nude_2010.txt");
                output = "dynamic_overlay_2010_"+StringUtils.left(IsiParser.file, IsiParser.file.length()-4)+".gexf";
                nbLines =24711;
            
            }
            
            br = new BufferedReader(new InputStreamReader(nudeMap,"UTF-8"));

            while ((currLine = br.readLine()) != null) {
                    Screen1.jProgressBar1.setValue(Math.round((float)100*(float)count++/(float)nbLines));

                    //System.out.println(Math.round((float)100*(float)count/(float)nbLines));
                    
                if (currLine.contains("<node ")) {

                    currLine = currLine.replaceAll("&","and").toLowerCase();
                    Pattern p = Pattern.compile("(.*<node id=.)(\\d*)(. label=.)(.*)(..)");
                    Matcher m = p.matcher(currLine);
                    currNodeId = m.replaceFirst("$2").trim();
                    currNodeLabel = m.replaceFirst("$4").trim();
                    //System.out.println(currNodeLabel);
                    //System.out.println(currNodeLabel);

                    sb.append(StringUtils.left(currLine, currLine.length() - 1)).append(" start=\"").append(String.valueOf(Main.minYear)).append("-01-01\" end=\"").append(String.valueOf(Main.maxYear)).append("-12-31\">").append(newLine);


                } else {
                    if (currLine.contains("<attvalues>")) {
                        sb.append(currLine).append(newLine);

                        if (map.containsKey(currNodeLabel)) {
                            //System.out.println("true");
                            HashMultiset setYears = map.get(currNodeLabel);
                            //System.out.println(setYears.size());

                            //copies the set of currYears to a list of years (I feel more comfortable with a list here, don't ask why)
                            Iterator<Entry<String>> setYearsIt = setYears.entrySet().iterator();
                            TreeMap<LocalDate, Float> mapYears = new TreeMap();

                            while (setYearsIt.hasNext()) {
                                //System.out.println("in the loop of years");
                                Entry<String> entry = setYearsIt.next();
                                mapYears.put(new LocalDate(Integer.valueOf(entry.getElement().trim()), 6, 01), (float)entry.getCount());
                            }

                            //adds the missing years in his list. Missing years are those where no paper belonged to the current WC / node
                            
                            //first, adds a single date for each year missing.
                            for (int i = Main.minYear; i <= Main.maxYear; i++) {
                                if (!setYears.elementSet().contains(String.valueOf(i))) {
                                    mapYears.put(new LocalDate(i, 06, 01), (float) 1);

                                }
                            }
                             
                            //now, iterate through each year and the nxt
                            // if their corresponding frequencies are different,
                            //fills the intervening months with gradually increasing or decreasing values
                            for (int i = Main.minYear; i < Main.maxYear; i++) {

                                float freqCurrYear = mapYears.get(new LocalDate(i, 6, 01));
                                float freqNextYear = mapYears.get(new LocalDate(i + 1, 6, 01));

                                if (freqCurrYear != freqNextYear) {

                                    for (int j = 1; j <= 6; j++) {

                                        mapYears.put(new LocalDate(i, 6 + j, 01), freqCurrYear+((freqNextYear - freqCurrYear) /12)*j);


                                    }
                                    for (int j = 1; j < 6; j++) {

                                        mapYears.put(new LocalDate(i + 1, j, 01), freqNextYear-((freqNextYear - freqCurrYear) / 12)*(6-j));

                                    }


                                } else {mapYears.put(new LocalDate(i, 06, 01), freqCurrYear);}

                            }
                            //ok now the tweening of dates should work!
                            Iterator<LocalDate> mapYearsIt = mapYears.keySet().iterator();
//                            TreeMap<LocalDate, Float> mapYearsBis = new TreeMap();
//                                while (mapYearsIt.hasNext()) {
//
//                                LocalDate currDate1 = mapYearsIt.next();
//                                LocalDate nextDate1 = mapYearsIt.next();
//                                if (currDate1.plusMonths(5)==nextDate1){
//                                    
//                                    mapYearsBis.remove(map)
//                                    
//                                }
                                
//                                
//                                }

                                
                                while (mapYearsIt.hasNext()) {
                                
                                LocalDate currDate = mapYearsIt.next();
                                float freq = mapYears.get(currDate);

                                //System.out.println(currDate);

                                sb.append("<attvalue for=\"freq\" value=\"").append(freq).append("\"").append(" start =\"").append(currDate).append("\" end =\"").append(currDate.plusMonths(1).minusDays(1)).append("\"></attvalue>");
                                sb.append(newLine);
                            }

                        }

                    } else {

                        sb.append(currLine);
                        sb.append(newLine);

                    }
                }


            }

            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(Main.getWorkingDirectory() + output));
            bw.write(sb.toString());
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(DynamicSizeForGexf.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}