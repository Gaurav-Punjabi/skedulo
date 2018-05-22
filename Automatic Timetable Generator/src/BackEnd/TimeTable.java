package BackEnd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author Gaurav_Punjabi
 *      This class is used to generate a time table with the help of specific 
 *      subjects in the DB.
 */
public class TimeTable implements Constants {    
    /**
     * @author : Gaurav_Punjabi
     *           This is the default constructor which initializes all the variables 
     *              to their default values.
     */
    public TimeTable()
    {
        initVariables();
    }
    /**
     * @author : Gaurav_Punjabi
     *      This is method which generates a random order timetable with the 
     *      help of values retrieved from the Database.
     * @param path
     * @throws java.io.IOException
     * @return : It returns the XML Format of the generated TimeTable in the 
     *            of string.
     */
    public ArrayList<Day> generateTimetable(File path) throws IOException
    {
        //Simple variable declarations
        int lectureType,lectureNo;
        Random random = new Random();
        
        String xmlString =  "<timeTable>";
        int dayCount = 0;
        /**
         *    Running an for-each loop on timetable to traverse each and every day
         *    i.e from MONDAY to SATURDAY.
         */
        for(Day day : timeTable)
        {
            dayCount++;
            xmlString += "\n\t<day>";
            filled = new ArrayList<>();
            /**
             *  This loop runs until every period of day is filled either with a
             *  lecture or practical lab.
             */
            while(filled.size() < noOfLectures)
            {
                /**
                 * Generating a random number to pick a random number in the 
                 * current context.
                 */
                int subjectIndex = random.nextInt(subjects.size());
                Subject subject = subjects.get(subjectIndex);
                /**
                 * Generating a random number to pick a random period in the 
                 * current Day day.
                 */
                lectureNo = random.nextInt(noOfLectures) + 1;
                /**
                 * Sorting the filled array for BinarySearching the ArrayList.
                 */
                Collections.sort(filled);
                /**
                 * Checking if the randomly generated period is filled with a 
                 * lecture or an period if it is empty then it enters the if 
                 * clause.
                 */
                if(Collections.binarySearch(filled,lectureNo) < 0)
                {
                   lectureType = random.nextInt(2);
                   /**
                    * We generate a random number between the range of 0 to 1
                    * if the lectureType is 0 then we place a lecture in the 
                    * current lectureNo if the following conditions meet
                    *   -: the current subject has any lectures left
                    *   -: there is only one period left to fill in the current 
                    *      day.
                    */
                   if(lectureConditions(subject, lectureType,dayCount,lectureNo))
                   {
                        String subjectName = subject.getName();
                        Lecture lecture = new Lecture(subjectName,true);
                        day.addLecture(lectureNo, lecture);
                        subject.decrementLecture();
                        filled.add(lectureNo);
                        getStaffByName(subject.getStaff().getName()).addLecture(dayCount, lectureNo, lecture);
                        xmlString += "\n\t\t<theory>" + subject.getName().toUpperCase() +
                                "</theory>";
                   }
                   /**
                    * The current practical will be entered into to generated slot
                    * with 2 batches if the following conditions are met
                    *   -: The lecture no is even
                    *   -: The concurrent period slot is empty
                    *   -: There are 2 batches for the current subject.
                    */
                   else if(specialPracticalConditions(subject,lectureNo,dayCount))
                   {
                       String subjectName = subject.getName().toUpperCase();
                       Lecture lecture = new Lecture(subjectName,false);
                       day.addLecture(lectureNo, lecture);
                       day.addLecture(lectureNo + 1, lecture);
                       subject.decrementPractical();
                       getStaffByName(subject.getStaff().getName()).addLecture(dayCount, lectureNo, lecture);
                       getStaffByName(subject.getStaff().getName()).addLecture(dayCount, lectureNo + 1, lecture);
                       filled.add(lectureNo);
                       filled.add(lectureNo + 1);
                       
                       xmlString += "\n\t\t\t<batch>" + subjectName + "</batch>";
                       xmlString += "\n\t\t\t<batch>" + subjectName + "</batch>";
                   }
                   /**
                    * if the lectureType is 1 then we place the two concurrent
                    * periods with the current practical subject if the following
                    * conditions meet
                    *   -: the lectureNo is an odd number
                    *   -: the subject has any practical labs left
                    *   -: the concurrent period is not filled
                    *   -: the noOfBatches for the subject is greater than 2.
                    */
                   else if(practicalConditions(subject,lectureNo,dayCount))
                   { 
                        /**
                         * Simple Variable declaration.
                         */
                        String s = "";
                        Subject subjectB;
                        Random randomBatch = new Random(lectureNo);
                        int randomC;
                        
                        s += subject.getName() + ",";
                        
                        xmlString += "\n\t\t<practical>";
                        xmlString += "\n\t\t\t<batch>" + subject.getName().toUpperCase() + "</batch>";
                        //Updating the staffs timetable...
                        getStaffByName(subject.getStaff().getName()).addLecture(dayCount, lectureNo, new Lecture(subject.getName(),false));
                        getStaffByName(subject.getStaff().getName()).addLecture(dayCount, lectureNo + 1, new Lecture(subject.getName(),false));
                        
                        /**
                         * Picking up a random subject for B batch that meets the
                         * following conditions
                         *      -: the subject has any practical labs left
                         *      -: the subject is not the same as of batch A.
                         */
                        subjectB = subjects.get(randomBatch.nextInt(subjects.size()));
                        System.out.println("Check B : " + subjectB.getNoOfBatches());
                        while(subjectB.equals(subject) || 
                              subjectB.getB() == 0 || 
                              subjectB.getNoOfBatches() == 2 ||
                              !getStaffByName(subjectB.getStaff().getName()).isAvailable(dayCount, lectureNo) ||
                              !getStaffByName(subjectB.getStaff().getName()).isAvailable(dayCount, lectureNo + 1)) {
                            subjectB = subjects.get(randomBatch.nextInt(subjects.size()));
                        }
                        
                        xmlString += "\n\t\t\t<batch>" + subjectB.getName().toUpperCase() + "</batch>";
                        //Updating the staffs timetable...
                        getStaffByName(subjectB.getStaff().getName()).addLecture(dayCount, lectureNo, new Lecture(subjectB.getName(),false));
                        getStaffByName(subjectB.getStaff().getName()).addLecture(dayCount, lectureNo + 1, new Lecture(subjectB.getName(),false));
                        
                        s += subjectB.getName() + ",";
                        /**
                         * Picking up a random subject for C batch that meets the
                         * following conditions
                         *      -: the subject has any practical labs left
                         *      -: the subject is not the same as of batch A
                         *      -: the subject is not the same as of batch B.
                         */
                        randomC = randomBatch.nextInt(subjects.size());
                        System.out.println("Check C :" + subjects.get(randomC).getNoOfBatches());
                        while(subjects.get(randomC).getC() == 0 || 
                              subjects.get(randomC).equals(subject) || 
                              subjects.get(randomC).equals(subjectB) || 
                              subjects.get(randomC).getNoOfBatches() == 2 ||
                              !getStaffByName(subjects.get(randomC).getStaff().getName()).isAvailable(dayCount, lectureNo) ||
                              !getStaffByName(subjects.get(randomC).getStaff().getName()).isAvailable(dayCount, lectureNo + 1))
                            randomC = randomBatch.nextInt(subjects.size());
                        subjects.get(randomC).setC(subjects.get(randomC).getC() - 1);
                        
                        xmlString += "\n\t\t\t<batch>" + subjects.get(randomC).getName().toUpperCase() + "</batch>";
                        //Updating the staffs timetable...
                        getStaffByName(subjects.get(randomC).getStaff().getName()).addLecture(dayCount, lectureNo, new Lecture(subjects.get(randomC).getName(),false));
                        getStaffByName(subjects.get(randomC).getStaff().getName()).addLecture(dayCount, lectureNo + 1, new Lecture(subjects.get(randomC).getName(),false));
                        
                        s += subjects.get(randomC).getName() + ",";
                        
                        day.addLecture(lectureNo, new Lecture(s,false));
                        day.addLecture(lectureNo + 1,new Lecture(s,false));
                        filled.add(lectureNo);
                        filled.add(lectureNo + 1);
                        subject.decrementPractical();
                        
                        xmlString += "\n\t\t</practical>";
                   }              
                }  
            }
            xmlString += "\n\t</day>";
        }
        xmlString += "\n</timeTable>";
        
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(xmlString.getBytes());
        System.out.println("Timetable : " + timeTable);
        staffs.get(0).addLecture(0, 0, new Lecture("STE",true));
        for(Staff staff : staffs) {
            if(staff.getName().length() != 0)
                System.out.println("staff = " + staff);
        }
        initSubjects();
        return timeTable;
    }
    private boolean practicalConditions(Subject subject,int lectureNo,int dayCount) {
        return (lectureNo%2 != 0  && subject.getA() > 0 && 
                Collections.binarySearch(filled,lectureNo + 1) < 0) &&
                getStaffByName(subject.getStaff().getName()).isAvailable(dayCount, lectureNo) &&
                getStaffByName(subject.getStaff().getName()).isAvailable(dayCount, lectureNo+1); 
    }
    private boolean lectureConditions(Subject subject,int lectureType,int dayCount,int lectureNo) {
        return (((filled.size() == 4 && subject.getLectureNo() > 0 ) ||
                (lectureType == LECTURE && subject.getLectureNo() > 0)) &&
                getStaffByName(subject.getStaff().getName()).isAvailable(dayCount, lectureNo));
    }
    private boolean specialPracticalConditions(Subject subject,int lectureNo,int dayCount) {
        return (lectureNo%2 != 0 && subject.getA() > 0 &&
                Collections.binarySearch(filled, lectureNo + 1) < 0 &&
                subject.getNoOfBatches() == 2  &&
                getStaffByName(subject.getStaff().getName()).isAvailable(dayCount, lectureNo) &&
                getStaffByName(subject.getStaff().getName()).isAvailable(dayCount, lectureNo+1));
    }
    /**
     * @author : Gaurav_Punjabi
     *      This method overrides the toString() provided from Object class and 
     *      returns a string which best describes the TimeTable object.
     * @return : String : description of TimeTable object.
     */
    @Override
    public String toString() {
        String s  = "";
        for(Day day : timeTable)
            s += "\n\n Day : " + day;
        return s;
    }
    /**
     * @author : Gaurav_Punjabi
     *      This method initializes all the variables of this class required 
     *      during the construction of the object.
     */
    private void initVariables() {
        this.timeTable = new ArrayList<>();
        this.filled = new ArrayList<>();
        this.subjects = new ArrayList<>();
        for(int i=0;i<6;i++)
            timeTable.add(new Day());
        this.connection = initConnection();
        if(connection != null) {
            this.staffs = initStaffs();
            this.subjects = initSubjects();
            this.noOfLectures = 6;
        }
        System.out.println("Staff  : " + subjects);
    }
    /**
     * @author : Gaurav_Punjabi
     *      This method the object of connection object for database 
     *      communication with the help of static method connectDb in 
     *      MySqlConnect.
     * @return : Connection : it returns connection's object established
     *           Returns null if the object is not created.
     */
    private Connection initConnection()
    {
        connection = MySqlConnect.connectDb();
        if(connection == null)
            JOptionPane.showMessageDialog(null,"Connection unsucessful");
        return connection;
    }
    /**
     * @author : Gaurav_Punjabi
     *      This method initializes an ArrayList of Subjects and inserts all the
     *      subject from the database.
     */
    private ArrayList<Subject> initSubjects() {
        ArrayList<Subject> subject = new ArrayList<>();
        try
        {
            resultSet = connection.createStatement().executeQuery("SELECT * FROM subjects");
            while(resultSet.next()) {
                Collections.sort(this.staffs);
                int index = Collections.binarySearch(this.staffs, new Staff(resultSet.getString("staff")));
                if(index >= 0)
                    subjects.add(new Subject(resultSet.getString("name"),this.staffs.get(index),resultSet.getInt("lecture_no"),resultSet.getInt("practical_no"),resultSet.getInt("batch_no")));
            }    
            return subjects;
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Sql Problem  : " + e);
            return null;
        }
    }
    private ArrayList<Staff> initStaffs() {
        ArrayList<Staff> staffs = new ArrayList<>();
        try {
            resultSet = connection.createStatement().executeQuery("SELECT * FROM staff");
            while(resultSet.next()) {
                staffs.add(new Staff(resultSet.getString("name"),resultSet.getString("prefix"),resultSet.getInt("priority")));
            }
//            System.out.println("Testing : " + Collections.binarySearch(staffs, new Staff("Asavari")));
        } catch (Exception e) {
        }
        return staffs;
                
    }
    private Staff getStaffByName(String name) {
        Collections.sort(staffs);
        int index = Collections.binarySearch(staffs, new Staff(name));
        if(index >= 0)
            return staffs.get(index);
        return null;
    }
    /**
     * @author : Gaurav_Punjabi
     * @param args
     * @throws java.io.IOException
     * ****************************************************
     *      Void main used for Unit Testing Purpose Only...
     * ****************************************************
     * 
     */
    public static void main(String []args) throws IOException
    {
        TimeTable table = new TimeTable();
        table.generateTimetable(new File("/Users/gauravpunjabi/Desktop/Test.xml"));
    }
    private ArrayList<Day> timeTable;
    private ArrayList<Integer> filled;
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    private ArrayList<Subject> subjects;
    private ArrayList<Staff> staffs;
    private int noOfLectures;
}
