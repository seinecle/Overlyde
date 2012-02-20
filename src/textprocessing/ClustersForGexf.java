/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textprocessing;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author C. Levallois
 */
public class ClustersForGexf {

    static String wk = "D:\\Docs Pro Clement\\E-projects\\OverlayToolkit\\";
    static String file = "overlay_nude_2010.gexf";
    static String output= "overlay_nude_2010_with_clusters.gexf";

    static String currLine;
    static String currNode;
    static String currCluster19;
    static String currCluster6;
    static String currCluster4;
    static StringBuilder sb = new StringBuilder();
    static HashMap<String, String> clusters19 = new HashMap();
    static HashMap<String, String> clusters6 = new HashMap();
    static HashMap<String, String> clusters4 = new HashMap();
    static int count = 0;
    
    static String newLine = "\n";

    
    void run() throws FileNotFoundException, IOException {




        BufferedReader br = new BufferedReader(new FileReader(wk + file));
        BufferedReader br19 = new BufferedReader(new FileReader(wk + "clusters19.txt"));
        BufferedReader br6 = new BufferedReader(new FileReader(wk + "clusters6.txt"));
        BufferedReader br4 = new BufferedReader(new FileReader(wk + "clusters4.txt"));


        while ((currLine = br19.readLine()) != null) {
            count++;
            clusters19.put(String.valueOf(count), currLine);
        }
        count = 0;
        while ((currLine = br6.readLine()) != null) {
            count++;
            clusters6.put(String.valueOf(count), currLine);
        }
        count = 0;
        while ((currLine = br4.readLine()) != null) {
            count++;
            clusters4.put(String.valueOf(count), currLine);
        }
        count = 0;

        br19.close();
        br6.close();
        br4.close();

        while ((currLine = br.readLine()) != null) {

            if (currLine.contains("<node ")) {
                Pattern p = Pattern.compile("(\\s*<node id=.v)(\\d*)(.*)");
                Matcher m = p.matcher(currLine);
                currNode = m.replaceFirst("$2");

            }
            
            if (!currLine.contains("<attvalues></attvalues>")) {
                sb.append(currLine).append(newLine);
            } else {

                currCluster19 = clusters19.get(currNode);
                currCluster6 = clusters6.get(currNode);
                currCluster4 = clusters4.get(currNode);

                sb.append("        <attvalues>\n");
                sb.append("                <attvalue for=\"clusters19\" value=\"").append(currCluster19).append("\"/>\n");
                sb.append("                <attvalue for=\"clusters6\" value=\"").append(currCluster6).append("\"/>\n");
                sb.append("                <attvalue for=\"clusters4\" value=\"").append(currCluster4).append("\"/>\n");
                sb.append("        </attvalues>\n");


            }

        }
        br.close();
        BufferedWriter bw = new BufferedWriter(new FileWriter(wk +output));
        bw.write(sb.toString());
        bw.close();
        
    }
}
