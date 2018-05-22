/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import BackEnd.Day;
import BackEnd.Lecture;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author gauravpunjabi
 */
public class XmlParser {
    public XmlParser(File path) {
        this.path = path;
    }
    public ArrayList<Day> parseTimetable() {
        ArrayList<Day> timeTable = new ArrayList<>();
        String practicalName = "";
        try {
            Pattern xmlPattern = Pattern.compile("<timeTable>([\\w\\d<>/]*?)</timeTable>");
            Matcher xmlMatcher = xmlPattern.matcher(new String(Files.readAllBytes(this.path.toPath())).replaceAll("\\s",""));
            while(xmlMatcher.find()) {
               Pattern dayPattern = Pattern.compile("<day>([\\w\\d<>/]*?)</day>");
               Matcher dayMatcher = dayPattern.matcher(xmlMatcher.group(1));
               Day day = new Day();
               int lectureCount = 0;
               while(dayMatcher.find()) {
                   String temp = dayMatcher.group(1);
                   Matcher theory = Pattern.compile("<theory>(\\w*?)</theory>").matcher(temp);
                   Matcher practical = Pattern.compile("<practical>([\\w<>/]*?)</practical>").matcher(temp);
                    for(int i=0;i<6;i++) {
                        if(theory.find()) {
                            String subjectName = theory.group(1);
                            day.addLecture(lectureCount++,new Lecture(subjectName, true));
                            System.out.println(subjectName);
                        } 
                        else if(practical.find()) {
                            practicalName = "";
                            Matcher batch = Pattern.compile("<batch>([\\w]*?)</batch>").matcher(practical.group(1));
                            int batchCount = 0;
                            while(batch.find()) {
                                practicalName += batch.group(1) + ",";
                                batchCount++;
                            }
                            day.addLecture(lectureCount++, new Lecture(practicalName, false));
                            day.addLecture(lectureCount++, new Lecture(practicalName, false));
                        }
                    }
                   timeTable.add(day);
               }
            }
        }catch(IOException ioe) {
            JOptionPane.showMessageDialog(null, "Some error in reading the xml file ; " + ioe.getMessage());
        }
        return timeTable;
    }
    public static void main(String[] args) {
        XmlParser parser = new XmlParser(new File("/Users/gauravpunjabi/Desktop/Timetable.xml"));
        System.out.println("Hello : " + parser.parseTimetable());
    }
    private File path;
}
