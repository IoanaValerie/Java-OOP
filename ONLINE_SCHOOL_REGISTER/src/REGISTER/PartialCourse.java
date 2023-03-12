package REGISTER;

import java.util.*;

public class PartialCourse extends Course {
    private PartialCourse(CourseBuilder builder) {
        super(builder);
    }

    static class PartialCourseBuilder extends Course.CourseBuilder {
        public PartialCourseBuilder(String name) {
            super(name);
        }

        @Override
        public Course build() {
            return new PartialCourse(this);
        }
    }

    @Override
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> list = new ArrayList<>();
        HashMap<Student, Grade> dictionarStudenti = gettAllStudentGrades();
        ArrayList<Student> students = getAllStudents();
        for (Student student : students) {
            Grade grade = dictionarStudenti.get(student);
            if (grade.getTotal() >= 5)
                list.add(student);
        }
        return list;
    }
}
