/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BackEnd;

/**
 *
 * @author gauravpunjabi
 */
public class Lecture {
    public Lecture(String name,
                   int roomNo,
                   boolean isTheory) {
        this.name = name;
        this.isTheory = isTheory;
        this.roomNo = roomNo;
    }
    public Lecture() { this("",0,false); }
    public Lecture(String name,
                   boolean isTheory) {
        this(name,0,isTheory);
    }
    
    
    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public String getName() {
        return name;
    }

    public boolean isTheory() {
        return isTheory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTheory(boolean isTheory) {
        this.isTheory = isTheory;
    }

    @Override
    public String toString() {
        return "Lecture{" + "name=" + name + ", isTheory=" + isTheory + ", roomNo=" + roomNo + '}';
    }
    
    
    private String name;
    private boolean isTheory;
    private int roomNo;
}
