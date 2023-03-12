package REGISTER;

import java.util.*;

public class ScoreVisitor implements Visitor{
    private final HashMap<Assistant, ArrayList<Tuple>> partialScores = new HashMap<>();
    private final HashMap<Teacher, ArrayList<Tuple>> examScores = new HashMap<>();
    private final Catalog catalog = Catalog.getInstance();

    public void addTupleAssistant(Assistant assistant, Student student, String name, Double grade) {
        Tuple<Student, String, Double> tupleAssistant = new Tuple<>();
        tupleAssistant.setGrade(grade);
        tupleAssistant.setStudent(student);
        tupleAssistant.setName(name);
        if(partialScores.containsKey(assistant))
                partialScores.get(assistant).add(tupleAssistant);
        else {
            ArrayList<Tuple> list = new ArrayList<>();
            list.add(tupleAssistant);
            partialScores.put(assistant, list);
        }

    }

    public void addTupleTeacher(Teacher teacher, Student student, String name, Double grade) {
        Tuple<Student, String, Double> tupleTeacher = new Tuple<>();
        tupleTeacher.setGrade(grade);
        tupleTeacher.setStudent(student);
        tupleTeacher.setName(name);
        if(examScores.containsKey(teacher))
            examScores.get(teacher).add(tupleTeacher);
        else {
            ArrayList<Tuple> list = new ArrayList<>();
            list.add(tupleTeacher);
            examScores.put(teacher, list);
        }
    }

    @Override
    public void visit(Assistant assistant) {
        if(partialScores.containsKey(assistant))
            for (int i = 0; i < partialScores.get(assistant).size(); i++) {
                boolean ok = false;
                int index_course = 0;
                for (int j = 0; j < catalog.courses.size(); j++)
                    if(catalog.courses.get(j).getName().equals(partialScores.get(assistant).get(i).getName()) && catalog.courses.get(j).getAllStudents().contains((Student) partialScores.get(assistant).get(i).getStudent())) {
                        if ((catalog.courses.get(j).getGrade((Student) partialScores.get(assistant).get(i).getStudent())) != null) {
                            if ((catalog.courses.get(j).getGrade((Student) partialScores.get(assistant).get(i).getStudent()).getExamScore()) != null)
                                catalog.courses.get(j).getGrade((Student) partialScores.get(assistant).get(i).getStudent()).setPartialScore((Double) partialScores.get(assistant).get(i).getGrade());
                            else
                                catalog.courses.get(j).getGrade((Student) partialScores.get(assistant).get(i).getStudent()).setPartialScore(0d);
                            ok = true;
                            Double exam_score = catalog.courses.get(j).getGrade((Student) partialScores.get(assistant).get(i).getStudent()).getExamScore();
                            catalog.courses.get(j).getGrade((Student) partialScores.get(assistant).get(i).getStudent()).setExamScore(0d);
                            catalog.notifyObservers(catalog.courses.get(j).getGrade((Student) partialScores.get(assistant).get(i).getStudent()));
                            catalog.courses.get(j).getGrade((Student) partialScores.get(assistant).get(i).getStudent()).setExamScore(exam_score);
                        }
                        else index_course = j;
                    }
                if(!ok) {
                    Grade grade = new Grade();
                    grade.setStudent((Student) partialScores.get(assistant).get(i).getStudent());
                    grade.setPartialScore((Double) partialScores.get(assistant).get(i).getGrade());
                    grade.setCourse((String) partialScores.get(assistant).get(i).getName());
                    grade.setExamScore(0d);
                    catalog.courses.get(index_course).addGrade(grade);
                    catalog.notifyObservers(grade);
                }
            }
    }

    @Override
    public void visit(Teacher teacher) {
        if(examScores.containsKey(teacher))
            for (int i = 0; i < examScores.get(teacher).size(); i++) {
                boolean ok = false;
                int index_course = 0;
                for (int j = 0; j < catalog.courses.size(); j++)
                    if(catalog.courses.get(j).getName().equals(examScores.get(teacher).get(i).getName()) && catalog.courses.get(j).getAllStudents().contains((Student) examScores.get(teacher).get(i).getStudent())) {
                            if ((catalog.courses.get(j).getGrade((Student) examScores.get(teacher).get(i).getStudent())) != null) {
                               if((catalog.courses.get(j).getGrade((Student) examScores.get(teacher).get(i).getStudent()).getPartialScore()) != 0d)
                                   catalog.courses.get(j).getGrade((Student) examScores.get(teacher).get(i).getStudent()).setExamScore((Double) examScores.get(teacher).get(i).getGrade());
                               else
                                   catalog.courses.get(j).getGrade((Student) examScores.get(teacher).get(i).getStudent()).setPartialScore(0d);
                               ok = true;
                               Double partial_score = catalog.courses.get(j).getGrade((Student) examScores.get(teacher).get(i).getStudent()).getPartialScore();
                               catalog.courses.get(j).getGrade((Student) examScores.get(teacher).get(i).getStudent()).setPartialScore(0d);
                               catalog.notifyObservers(catalog.courses.get(j).getGrade((Student) examScores.get(teacher).get(i).getStudent()));
                               catalog.courses.get(j).getGrade((Student) examScores.get(teacher).get(i).getStudent()).setPartialScore(partial_score);
                            }
                            else index_course = j;
                    }
                if(!ok) {
                    Grade grade = new Grade();
                    grade.setStudent((Student) examScores.get(teacher).get(i).getStudent());
                    grade.setExamScore((Double) examScores.get(teacher).get(i).getGrade());
                    grade.setCourse((String) examScores.get(teacher).get(i).getName());
                    grade.setPartialScore(0d);
                    catalog.courses.get(index_course).addGrade(grade);
                    catalog.notifyObservers(grade);
                }
            }
    }

    public ArrayList<String> getAssistantGrades(Assistant assistant, String name) {
        ArrayList<String> Grades = new ArrayList<>();
        Student student;
        Double grade;
        for (int i = 0; i < partialScores.get(assistant).size(); i++)
            if(partialScores.get(assistant).get(i).getName().equals(name)) {
                student = (Student) partialScores.get(assistant).get(i).getStudent();
                grade = (Double) partialScores.get(assistant).get(i).getGrade();
                String gradeString = Double.toString(grade);
                Grades.add(student.getFirstName() + " " + student.getLastName() + ": " + gradeString);
            }
        return Grades;
    }

    public ArrayList<String> getTeacherGrades(Teacher teacher, String name) {
        ArrayList<String> Grades = new ArrayList<>();
        Student student;
        Double grade;
        for (int i = 0; i < examScores.get(teacher).size(); i++)
            if(examScores.get(teacher).get(i).getName().equals(name)) {
                student = (Student) examScores.get(teacher).get(i).getStudent();
                grade = (Double) examScores.get(teacher).get(i).getGrade();
                String gradeString = Double.toString(grade);
                Grades.add(student.getFirstName() + " " + student.getLastName() + ": " + gradeString);
            }
        return Grades;
    }

    private static class Tuple<U, R, S> {
        private U student; //Student
        private R name; //String
        private S grade; //Double

        public U getStudent() {
            return student;
        }

        public void setStudent(U student) {
            this.student = student;
        }

        public R getName() {
            return name;
        }

        public void setName(R name) {
            this.name = name;
        }

        public S getGrade() {
            return grade;
        }

        public void setGrade(S grade) {
            this.grade = grade;
        }
    }
}
