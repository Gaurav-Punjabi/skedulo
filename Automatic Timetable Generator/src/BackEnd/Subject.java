package BackEnd;


import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gaurav_Punjabi
 */
public class Subject implements Comparable<Subject> {
    /**
     * @author : Gaurav_Punjabi
     * @param name
     * @param lectureNo
     * @param practicalNo
     * @param noOfBatches 
     * Initializes the variables with the passed arguments to th respective
     * parameters.
     */
    public Subject(String name,Staff staff,int lectureNo,int practicalNo,int noOfBatches)
    {
        this.name = name;
        this.lectureNo = lectureNo;
        this.a = this.b = this.c= practicalNo;
        this.noOfBatches = noOfBatches;
        this.staff = staff;
    }

    public boolean decrementPractical()
    {
        return decrementPractical(1);
    }
    
    public boolean decrementPractical(int n)
    {
        if(this.a < n)
            return false;
        this.a = this.b = this.c -= n;
        return true;
    }
    
    public boolean decrementLecture()
    {
        return decrementLecture(1);
    }
    
    private boolean decrementLecture(int n)
    {
        if(n > lectureNo)
            return false;
        lectureNo -= n;
        return true;
    }
    
    /**
     * *************************************************************************
     *                          GETTERS AND SETTERS.
     * *************************************************************************
     */
    public void setNoOfBatches(int noOfBatches) {
        this.noOfBatches = noOfBatches;
    }

    public int getNoOfBatches() {
        return noOfBatches;
    }
    public void setA(int a) {
        this.a = a;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    @Override
    public String toString() {
        return "\nSubject{" + "name=" + name + ",staff = " + this.staff.getName() + ", lectureNo=" + lectureNo + ", a=" + a + ", b=" + b + ", c=" + c + '}';
    }
    public String getName() {
        return name;
    }
    public int getLectureNo() {
        return lectureNo;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLectureNo(int lectureNo) {
        this.lectureNo = lectureNo;
    }
    
    public int compareTo(Subject s)
    {
        return this.name.compareTo(s.getName());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((Subject)obj).getName());
    }
    private String name;
    private int lectureNo;
    private int a,b,c;
    private int noOfBatches;
    private Staff staff;
}
