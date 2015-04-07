/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package extractsomecomments;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apple
 */
public class Main {
    
    public static boolean createDir(String dirPath) {
        File dir = new File(dirPath);
        if(dir.exists()) {
            System.out.println("The folder has existed");
            return false;
        }
        if(!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        if(dir.mkdirs()) {
            System.out.println("create successful: " + dirPath);
            return true;
        } else {
            System.out.println("create fail...");
            return false;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Add timestamp to folder name
        Timestamp ts = new Timestamp(System.currentTimeMillis()); 
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        String timeStampStr = sdf.format(ts);
        
        //Create a new folder
        String folderPath = "/Users/apple/NetBeansProjects/ExtractComments/output/all-" + timeStampStr + "/";
        createDir(folderPath);
        
        File inputRootFile = new File("/Users/apple/NetBeansProjects/ExtractComments/sourceCode");
        ArrayList<String> path = new ArrayList<>();
        if(!inputRootFile.isDirectory()) {
            System.out.println("Please input a directory.");
        } else {
            TraversalFiles.fileList(inputRootFile, 0, path, folderPath);
        }
        
        
    }
}
