package REGISTER;

import java.util.*;

public abstract class Course {
    private String name;
    private Teacher titular;
    private HashSet<Assistant> assistants;
    private ArrayList<Grade> grades;
    private HashMap<String, Group> dictionary;
    private int creditNumber;
    private Strategy strategy;
    private Snapshot snapshot;

    protected Course(CourseBuilder builder) {
        this.name = builder.name;
        this.titular = builder.titular;
        this.creditNumber = builder.creditNumber;
        this.assistants = builder.assistants;
        this.grades = builder.grades;
        this.dictionary = builder.dictionary;
        this.strategy = builder.strategy;
    }

    public abstract static class CourseBuilder {
        private final String name;
        private Teacher titular;
        private HashSet<Assistant> assistants;
        private ArrayList<Grade> grades;
        private HashMap<String, Group> dictionary;
        private int creditNumber;
        private Strategy strategy;

        public CourseBuilder(String name) {
            this.name = name;
        }

        public CourseBuilder setTitular(Teacher titular) {
            this.titular = titular;
            return this;
        }

        public CourseBuilder setAssistants(HashSet<Assistant> assistants) {
            this.assistants = assistants;
            return this;
        }

        public CourseBuilder setGrades(ArrayList<Grade> grades) {
            this.grades = grades;
            return this;
        }

        public CourseBuilder setDictionary(HashMap<String, Group> dictionary) {
            this.dictionary = dictionary;
            return this;
        }

        public CourseBuilder setCreditNumber(int creditNumber) {
            this.creditNumber = creditNumber;
            return this;
        }

        public CourseBuilder setStrategy(Strategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public abstract Course build();
    }

    public void makeBackup() {
        snapshot = new Snapshot((ArrayList<Grade>) grades.clone());
    }

    public void undo() {
        if(snapshot == null)
            System.out.println("Nu s-a mai facut back-up");
        else grades = snapshot.grades;
    }

    private static class Snapshot {
        private ArrayList<Grade> grades;

        public Snapshot(ArrayList<Grade> grades) {
            this.grades = grades;
        }

        public ArrayList<Grade> getGrades() {
            return grades;
        }

        public void setGrades(ArrayList<Grade> grades) {
            this.grades = grades;
        }
    }

    public Student getBestStudent() {
        return strategy.getBest(grades);
    }

    public void addAssistant(String ID, Assistant assistant) {
        dictionary.get(ID).setAssistant(assistant);
        assistants.add(assistant);
    }

    public void addStudent(String ID, Student student) {
        dictionary.get(ID).add(student);
    }

    public void addGroup(Group group) {
        String ID = group.getID();
        dictionary.put(ID, group);
    }

    public void addGroup(String ID, Assistant assistant) {
        Group group = new Group(ID, assistant);
        dictionary.put(ID, group);
    }

    public void addGroup(String ID, Assistant assist, Comparator<Student> comp) {
        Group group = new Group(ID, assist, comp);
        dictionary.put(ID, group);
    }

    public Grade getGrade(Student student) {
        for (Grade grade : grades)
            if (student.equals(grade.getStudent()))
                return grade;
        return null;
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
        Collections.sort(grades);
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        for(Map.Entry<String, Group> entry : dictionary.entrySet())
            students.addAll(entry.getValue());
        return students;
    }

    public HashMap<Student, Grade> gettAllStudentGrades() {
        HashMap<Student, Grade> studentsDictionary = new HashMap<>();
        for (Grade grade : grades)
            studentsDictionary.put(grade.getStudent(), grade);
        return studentsDictionary;
    }

    public abstract ArrayList<Student> getGraduatedStudents();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTitular() {
        return titular;
    }

    public void setTitular(Teacher titular) {
        this.titular = titular;
    }

    public HashSet<Assistant> getAssistants() {
        return assistants;
    }

    public void setAssistants(HashSet<Assistant> assistants) {
        this.assistants = assistants;
    }

    public ArrayList<Grade> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Grade> grades) {
        this.grades = grades;
    }

    public HashMap<String, Group> getDictionary() {
        return dictionary;
    }

    public void setDictionary(HashMap<String, Group> dictionary) {
        this.dictionary = dictionary;
    }

    public int getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(int creditNumber) {
        this.creditNumber = creditNumber;
    }

    public String toString() {
        return name;
    }
}
