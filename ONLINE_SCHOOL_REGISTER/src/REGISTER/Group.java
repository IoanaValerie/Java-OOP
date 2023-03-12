package REGISTER;

import java.util.*;

public class Group extends HashSet<Student> {
    private Assistant assistant;
    private String ID;

    public Group(String ID, Assistant assistant, Comparator<Student> comp) {
        super((Collection<? extends Student>) comp);
        this.assistant = assistant;
        this.ID = ID;
    }

    public Group(String ID, Assistant assistant) {
        this.assistant = assistant;
        this.ID = ID;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
