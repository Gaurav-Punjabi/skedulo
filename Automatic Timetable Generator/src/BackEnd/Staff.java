/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BackEnd;

import java.util.ArrayList;

/**
 *
 * @author gauravpunjabi
 */
public class Staff implements Comparable<Staff>{
    public Staff(String name,
                 String prefix,
                 int priority) {
        this.setName(name);
        this.setPrefix(prefix);
        this.setPriority(priority);
        this.schedule = new ArrayList<>();
        //Initializing the schedule for this staff.
        for(int i=0;i<7;i++)
            this.schedule.add(new Day());
    }
    public Staff(String name) { this(name,"",0); }
    public Staff() { this("","",0); }

    public boolean isAvailable(int day,
                               int lectureNo) {
        return schedule.get(day).getLecture(lectureNo).getName().equals("");
    }
    public boolean addLecture(int day,
                              int lectureNo,
                              Lecture lecture) {
        return schedule.get(day).addLecture(lectureNo, lecture);
    }

    @Override
    public String toString() {
        return "\nStaff name : " + this.name + "\n\t TimeTable : " + this.schedule;
    }
    
    @Override
    public int compareTo(Staff other) {
        return this.getName().compareTo(other.getName());
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getPriority() {
        return priority;
    }
    private String name,prefix;
    private int priority;
    private ArrayList<Day> schedule;
}
