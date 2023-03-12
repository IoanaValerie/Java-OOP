package REGISTER;

import java.util.*;

public class FullCourse extends Course {
    private FullCourse(CourseBuilder builder) {
        super(builder);
    }

    static class FullCourseBuilder extends Course.CourseBuilder {
        public FullCourseBuilder(String name) {
            super(name);
        }

        @Override
        public Course build() {
            return new FullCourse(this);
        }

    }

    @Override
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> list = new ArrayList<>();
        HashMap<Student, Grade> studentsDictionary = gettAllStudentGrades();
        ArrayList<Student> students = getAllStudents();
        for (Student student : students) {
            Grade grade = studentsDictionary.get(student);
            if (grade.getPartialScore() >= 3 && grade.getExamScore() >= 2)
                list.add(student);
        }
        return list;
    }
}
