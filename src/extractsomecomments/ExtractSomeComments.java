/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package extractsomecomments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apple
 */
public class ExtractSomeComments {

    
    public static void extractComments(File inputFile, File outputFile) {
        
        boolean commentsStart = false;
        char currentChar = '$';
        char lastChar = '$';
        char lastLastChar = '$';
        StringBuffer outputStr = new StringBuffer();
        StringBuffer lastOutputStr = new StringBuffer();
        
        if(!inputFile.exists()) {
            System.out.println("File doesn't exist!");
        } else if(inputFile.isDirectory()) {
            System.out.println(inputFile.getName() + " is a directory. Please input a file.");
        } else {
            try {
                //get input file name
                String inputFilePath = inputFile.getPath();
                
                try(
                    InputStream in = new FileInputStream(inputFilePath);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in)); //prepare to read a file
                    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile)) //prepare to write a file
                ){
                    //read a line from the file
                    String lineString = null;
                    lineString = reader.readLine();
                    while(lineString != null) {
                        int strLength = lineString.length();
                        if(strLength == 0) {
                        }
                        if(strLength == 1) {
                            if(commentsStart) {
                                currentChar = lineString.charAt(0);
                                outputStr.append(currentChar);
                            }
                        } else if(strLength == 2) {
                            currentChar = lineString.charAt(1);
                            lastChar = lineString.charAt(0);
                            if(lastChar == '/' && currentChar == '*' ) {
                                commentsStart = true;
                            } else if(lastChar == '*' && currentChar == '/') {
                                commentsStart = false;
                            }
                        } else {
                            boolean doubleSlashStart = false;//Whether comments starting with "//"
                            for(int i = 2; i < strLength; i ++) {
                                //get current char, last char and the char before last char.
                                currentChar = lineString.charAt(i);
                                lastChar = lineString.charAt(i - 1);
                                lastLastChar = lineString.charAt(i - 2);
                                
                                //If the multipul lines comments start, put the char into outputStr.
                                if(commentsStart || doubleSlashStart) {
                                    if(i == 2) {
                                        outputStr.append(lastLastChar);
                                        outputStr.append(lastChar);
                                    }
                                    outputStr.append(currentChar);
                                }
                                
                                //special condition: comments start from the first char(lastLastChar),
                                //including /*..., //..., */...
                                if(i == 2 && lastLastChar == '/' && lastChar =='*') {
                                    commentsStart = true;
                                    outputStr.append(currentChar);
                                }
                                
                                if(i == 2 && lastLastChar == '/' && lastChar =='/') {
                                    doubleSlashStart =true;
                                    outputStr.append(currentChar);
                                }
                                
                                if(i == 2 && lastLastChar == '*' && lastChar =='/') {
                                    commentsStart = false;
                                    outputStr.delete(outputStr.length() - 3, outputStr.length());
                                }
                                
                                //Check whether ".../*...*/" will start
                                if(currentChar == '*' && lastChar == '/') {
                                    commentsStart = true;
                                }
                                
                                //Don't extract comments like ".../**...*/".
                                if(currentChar == '*' && lastChar == '*' && lastLastChar == '/') {
                                    commentsStart = false;
                                    outputStr.deleteCharAt(outputStr.length() - 1);
                                }
                                
                                //Check whether multipul lines comments will end with "...*/" except "/*/"
                                //Delete last two chars if it ends.
                                if(currentChar == '/' && lastChar =='*' && lastLastChar != '/') {
                                    commentsStart = false;
                                    outputStr.delete(outputStr.length() - 2, outputStr.length());
                                }
                                
                                //Extract comments starting with "...//". Pay attention to condition "*//*".
                                if(currentChar == '/' && lastChar == '/' && lastLastChar != '*') {
                                    doubleSlashStart = true;
                                }
                                
                                
                            }
                            
                        }
                        
                        //Whether the line includes comments
                        if(!outputStr.toString().equals(lastOutputStr.toString())) {
                            outputStr.append("\r\n");
                        }
                        lastOutputStr.replace(0, outputStr.length(), outputStr.toString());
                        //read next line
                        lineString = reader.readLine();
                    }
                    
                    writer.write(outputStr.toString());
                } 
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ExtractSomeComments.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ExtractSomeComments.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
