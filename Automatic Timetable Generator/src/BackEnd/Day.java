package BackEnd;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gauravpunjabi
 */
public class Day {
    public Day()
    {
        lectures = new LinkedHashMap<>();
        for(int i=0;i<6;i++)
            lectures.put(i+1, new Lecture());
    }
    public boolean addLecture(int key,Lecture lecture)
    {
        if(lectures.put(key, lecture) == null)
        {
            return true;
        }
        return false;
    }
    public Lecture getLecture(int key) {
        return this.lectures.get(key);
    }
    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "Day{" + "lectures=" + lectures + '}';
    }
    public LinkedHashMap<Integer,Lecture> getLectures() {
        return this.lectures;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Day other = (Day) obj;
        if (!Objects.equals(this.lectures, other.lectures)) {
            return false;
        }
        return true;
    }
    private LinkedHashMap <Integer,Lecture> lectures;
}
