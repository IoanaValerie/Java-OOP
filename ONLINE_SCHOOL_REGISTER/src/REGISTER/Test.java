package REGISTER;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Test {
    public static void main(String[] args) throws IOException, ParseException {
        /**
         *   Se face citirea din fisierul test.json si se introduc datele in catalog si in cele 2 dictionare.
         */

        Catalog catalog = Catalog.getInstance();

        ArrayList<Student> studentsList = new ArrayList<>();
        HashSet<Assistant> assistants = new HashSet<>();
        HashSet<Teacher> teachers = new HashSet<>();

        UserFactory user = new UserFactory();

        JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader("test.json"));
        for(Object coursesObj : (JSONArray) jsonObject.get("courses")) {
            JSONObject course = (JSONObject) coursesObj;

            String type = (String) course.get("type");

            Strategy strategy;
            String strategyName = (String) course.get("strategy");
            if (strategyName.equals("BestTotalScore"))
                strategy = new BestTotalScore();
            else if (strategyName.equals("BestExamScore"))
                strategy = new BestExamScore();
            else strategy = new BestPartialScore();

            String name = (String) course.get("name");

            JSONObject teacherName = (JSONObject) course.get("teacher");
            Teacher teacher = (Teacher) user.getUser("Teacher", (String) teacherName.get("firstName"), (String) teacherName.get("lastName"));
            teachers.add(teacher);

            HashSet<Assistant> assistantsDictionary = new HashSet<>();
            for (Object assistantsObj : (JSONArray) course.get("assistants")) {
                JSONObject assistantName = (JSONObject) assistantsObj;
                Assistant assistant = (Assistant) user.getUser("Assistant", (String) assistantName.get("firstName"), (String) assistantName.get("lastName"));
                assistants.add(assistant);
                assistantsDictionary.add(assistant);
            }

            HashMap<String, Group> dictionary =new HashMap<>();
            for (Object groupsObj : (JSONArray) course.get("groups")) {
                JSONObject groupName = (JSONObject) groupsObj;
                String ID = (String) groupName.get("ID");
                JSONObject assistantName = (JSONObject) groupName.get("assistant");
                Assistant assistant = (Assistant) user.getUser("Assistant", (String) assistantName.get("firstName"), (String) assistantName.get("lastName"));
                Assistant searchedAssistant = assistantsDictionary.stream().filter(assistant1 -> assistant1.getLastName().equals(assistant.getLastName()) &&
                                                                                   assistant1.getFirstName().equals(assistant.getFirstName())).findFirst().get();

                Group group = new Group(ID, searchedAssistant);
                for (Object studentsObj : (JSONArray) groupName.get("students")) {
                    JSONObject studentName = (JSONObject) studentsObj;
                    Student student = (Student) user.getUser("Student", (String) studentName.get("firstName"), (String) studentName.get("lastName"));
                    boolean ok = false;
                    for (Student value : studentsList)
                        if (value.getFirstName().equals(student.getFirstName()) && value.getLastName().equals(student.getLastName())) {
                            group.add(value);
                            ok = true;
                        }
                    if(!ok) {
                        if (studentName.get("mother") != null) {
                            JSONObject motherName = (JSONObject) studentName.get("mother");
                            Parent mother = (Parent) user.getUser("Parent", (String) motherName.get("firstName"), (String) motherName.get("lastName"));
                            student.setMother(mother);
                            catalog.addObserver(mother);
                        }
                        if (studentName.get("father") != null) {
                            JSONObject fatherName = (JSONObject) studentName.get("father");
                            Parent father = (Parent) user.getUser("Parent", (String) fatherName.get("firstName"), (String) fatherName.get("lastName"));
                            student.setFather(father);
                            catalog.addObserver(father);
                        }
                        studentsList.add(student);
                        group.add(student);
                    }
                }
                dictionary.put(ID, group);
            }

            ArrayList<Grade> gradesList = new ArrayList<>();
            if (type.equals("FullCourse")) {
                FullCourse fullCourse = (FullCourse) new FullCourse.FullCourseBuilder(name)
                        .setTitular(teacher)
                        .setAssistants(assistantsDictionary)
                        .setDictionary(dictionary)
                        .setStrategy(strategy)
                        .setGrades(gradesList)
                        .setCreditNumber(5)
                        .build();
                catalog.addCourse(fullCourse);
            } else if (type.equals("PartialCourse")) {
                PartialCourse partialCourse = (PartialCourse) new PartialCourse.PartialCourseBuilder(name)
                        .setTitular(teacher)
                        .setAssistants(assistantsDictionary)
                        .setDictionary(dictionary)
                        .setStrategy(strategy)
                        .setGrades(gradesList)
                        .setCreditNumber(5)
                        .build();
                catalog.addCourse(partialCourse);
            }
        }

        ScoreVisitor v = new ScoreVisitor();

        for(Object examScoresObj : (JSONArray) jsonObject.get("examScores")) {
            JSONObject examScore = (JSONObject) examScoresObj;

            JSONObject teacherName = (JSONObject) examScore.get("teacher");
            Teacher teacher = (Teacher) user.getUser("Teacher", (String) teacherName.get("firstName"), (String) teacherName.get("lastName"));
            Teacher searchedTeacher = teachers.stream().filter(teacher1 -> teacher1.getLastName().equals(teacher.getLastName())  &&
                                                                           teacher1.getFirstName().equals(teacher.getFirstName())).findFirst().get();
            int index_Student = -1;
            JSONObject studentName = (JSONObject) examScore.get("student");
            Student student = (Student) user.getUser("Student", (String) studentName.get("firstName"), (String) studentName.get("lastName"));
            for (int i = 0; i < studentsList.size(); i++)
                if (studentsList.get(i).getFirstName().equals(student.getFirstName()) && studentsList.get(i).getLastName().equals(student.getLastName())) {
                   index_Student = i;
                   break;
                }

            String name = (String) examScore.get("course");

            Double grade = (((Number) examScore.get("grade")).doubleValue());

            v.addTupleTeacher(searchedTeacher, studentsList.get(index_Student), name, grade);
        }

        for(Object partialScoresObj : (JSONArray) jsonObject.get("partialScores")) {
            JSONObject partialScore = (JSONObject) partialScoresObj;

            JSONObject assistantName = (JSONObject) partialScore.get("assistant");
            Assistant assistant = (Assistant) user.getUser("Assistant", (String) assistantName.get("firstName"), (String) assistantName.get("lastName"));
            Assistant searchedAssistant = assistants.stream().filter(assistant1 -> assistant1.getLastName().equals(assistant.getLastName()) &&
                                                                                   assistant1.getFirstName().equals(assistant.getFirstName())).findFirst().get();
            int index_Student = -1;

            JSONObject studentName = (JSONObject) partialScore.get("student");
            Student student = (Student) user.getUser("Student", (String) studentName.get("firstName"), (String) studentName.get("lastName"));
            for (int i = 0; i < studentsList.size(); i++)
                if (studentsList.get(i).getFirstName().equals(student.getFirstName()) && studentsList.get(i).getLastName().equals(student.getLastName())) {
                    index_Student = i;
                    break;
                }

            String name = (String) partialScore.get("course");

            Double grade = (((Number) partialScore.get("grade")).doubleValue());

            v.addTupleAssistant(searchedAssistant, studentsList.get(index_Student), name, grade);
        }

        /**
         *    Introduc notele in catalog.
         */
        for(int i = 0; i < catalog.courses.size(); i++) {
            catalog.courses.get(i).getTitular().accept(v);
            for(Assistant assistant : catalog.courses.get(i).getAssistants())
                assistant.accept(v);
        }

        /**
         *    Afisez catalogul.
         */
        catalog.show();

        /**
         *    Adaug o grupa noua cu notele sale in primul curs din catalog pentru a testa Design
         *    Pattern-ul Memento.
         */
        catalog.courses.get(0).makeBackup();

        Student student1 = (Student) user.getUser("Student", "Ana", "Blandiana");
        Student student2 = (Student) user.getUser("Student", "Robert", "Neagu");
        Student student3 = (Student) user.getUser("Student", "Ioana", "Ciobotea");
        Student student4 = (Student) user.getUser("Student", "Valerie", "Neagu");

        String ID = "315CC";
        Assistant assistant1 = (Assistant) user.getUser("Assistant", "Ionut", "Aldea");
        catalog.courses.get(0).addGroup(ID, assistant1);

        catalog.courses.get(0).addStudent(ID, student1);
        catalog.courses.get(0).addStudent(ID, student2);
        catalog.courses.get(0).addStudent(ID, student3);
        catalog.courses.get(0).addStudent(ID, student4);

        Grade grade1 = new Grade();
        Grade grade2 = new Grade();
        Grade grade3 = new Grade();
        Grade grade4 = new Grade();
        grade1.setStudent(student1);
        grade1.setCourse(catalog.courses.get(0).getName());
        grade1.setPartialScore(4.5);
        grade1.setExamScore(2.5);
        grade2.setStudent(student2);
        grade2.setCourse(catalog.courses.get(0).getName());
        grade2.setPartialScore(3.0);
        grade2.setExamScore(3.5);
        grade3.setStudent(student3);
        grade3.setCourse(catalog.courses.get(0).getName());
        grade3.setPartialScore(3.9);
        grade3.setExamScore(2.8);
        grade4.setStudent(student4);
        grade4.setCourse(catalog.courses.get(0).getName());
        grade4.setPartialScore(1.7);
        grade4.setExamScore(1.5);

        catalog.courses.get(0).addGrade(grade1);
        catalog.courses.get(0).addGrade(grade2);
        catalog.courses.get(0).addGrade(grade3);
        catalog.courses.get(0).addGrade(grade4);

        /**
         *    Afiseaza notele dupa ce au fost adaugate cele noi
         */
        for(Grade grade : catalog.courses.get(0).getGrades())
            System.out.println(grade.toString());
        System.out.println();

        catalog.courses.get(0).undo();

        /**
         *    Afiseaza notele dupa ce a fost utilizat Memento
         */
        for(Grade grade : catalog.courses.get(0).getGrades())
            System.out.println(grade.toString());
    }
}
