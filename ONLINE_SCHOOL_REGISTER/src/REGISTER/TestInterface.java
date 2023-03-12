package REGISTER;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TestInterface {
    public static void main(String[] args) throws IOException, ParseException {
        /**
         *   Se face citirea din fisierul test.json si se introduc datele in catalog si in cele 2 dictionare.
         */

        Catalog catalog = Catalog.getInstance();
        ArrayList<Student> studentsList = new ArrayList<>();
        HashSet<Assistant> assistants = new HashSet<>();
        HashSet<Teacher> teachers = new HashSet<>();
        ArrayList<Assistant> assistantsInterface = new ArrayList<>();
        UserFactory user = new UserFactory();

        JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader("test.json"));
        for (Object coursesObj : (JSONArray) jsonObject.get("courses")) {
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
                assistantsInterface.add(assistant);
            }

            HashMap<String, Group> dictionary = new HashMap<>();
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
                    if (!ok) {
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
                        .build();
                catalog.addCourse(fullCourse);
            } else if (type.equals("PartialCourse")) {
                PartialCourse partialCourse = (PartialCourse) new PartialCourse.PartialCourseBuilder(name)
                        .setTitular(teacher)
                        .setAssistants(assistantsDictionary)
                        .setDictionary(dictionary)
                        .setStrategy(strategy)
                        .setGrades(gradesList)
                        .build();
                catalog.addCourse(partialCourse);
            }
        }

        ScoreVisitor v = new ScoreVisitor();

        for (Object examScoresObj : (JSONArray) jsonObject.get("examScores")) {
            JSONObject examScore = (JSONObject) examScoresObj;

            JSONObject teacherName = (JSONObject) examScore.get("teacher");
            Teacher teacher = (Teacher) user.getUser("Teacher", (String) teacherName.get("firstName"), (String) teacherName.get("lastName"));
            Teacher searchedTeacher = teachers.stream().filter(teacher1 -> teacher1.getLastName().equals(teacher.getLastName()) &&
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

        for (Object partialScoresObj : (JSONArray) jsonObject.get("partialScores")) {
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
         *   Se testeaza cele 3 pagini pentru un student din catalog.
         */

        TeacherAssistantPage obj1 = new TeacherAssistantPage(catalog.courses, catalog.courses.get(0).getTitular(), v);

        for (Assistant assistant : assistantsInterface) {
            if (catalog.courses.get(0).getAssistants().contains(assistant)) {
                for(Map.Entry<String , Group> entry : catalog.courses.get(0).getDictionary().entrySet())
                    if(entry.getValue().getAssistant().equals(assistant) && entry.getValue().contains(catalog.courses.get(0).getAllStudents().get(0))) {
                        TeacherAssistantPage obj2 = new TeacherAssistantPage(catalog.courses, assistant, v);
                    }
            }
        }
        StudentPage obj3 = new StudentPage(catalog.courses, catalog.courses.get(0).getAllStudents().get(0), v);
        if (catalog.courses.get(0).getAllStudents().get(0).getFather() != null) {
            ParentPage obj4 = new ParentPage(catalog.courses, catalog.courses.get(0).getAllStudents().get(0), catalog.courses.get(0).getAllStudents().get(0).getFather(), v);
        }
        if (catalog.courses.get(0).getAllStudents().get(0).getMother() != null) {
            ParentPage obj5 = new ParentPage(catalog.courses, catalog.courses.get(0).getAllStudents().get(0), catalog.courses.get(0).getAllStudents().get(0).getMother(), v);
        }
    }
}

