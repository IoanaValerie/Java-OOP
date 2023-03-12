package REGISTER;

import java.util.*;

public class Catalog {
    private static Catalog obj = null;
    ArrayList<Course> courses = new ArrayList<>();
    ArrayList<Parent> parents = new ArrayList<>();
    String name;

    private Catalog() {
        name = "Catalog online Facultatea de Automatica si Calculatoare";
    }

    public static Catalog getInstance() {
        // daca clasa nu a fost instantiata inainte, o facem acum
        if (obj == null)
            obj = new Catalog();
        return obj;
    }

    public void show() {
        for (Course cours : courses) {
            System.out.println("Curs: " + cours.getName());
            System.out.println("Titular: " + cours.getTitular());
            System.out.println("Asistenti: " + cours.getAssistants().toString());
            System.out.println("Dictionar: ");
            int nr = 0;
            for (Map.Entry<String, Group> entry : cours.getDictionary().entrySet()) {
                System.out.println("Grupa: " + ++nr);
                System.out.println("ID: " + entry.getValue().getID());
                System.out.println("Asistent: " + entry.getValue().getAssistant());
                System.out.println("Elevi: " + entry.getValue().toString());
            }
            System.out.println("Note: ");
            for (Grade grade : cours.getGrades())
                System.out.println(grade.toString());
            System.out.println("Numar credite: " + cours.getCreditNumber());
            System.out.println("Cel mai bun student este: " + cours.getBestStudent());
            System.out.println("Absolventi: " + cours.getGraduatedStudents().toString());
            System.out.println();
        }
    }

    // Adauga un curs în catalog
    public void addCourse(Course course) {
        if(!courses.contains(course))
            courses.add(course);
    }

    // Sterge un curs din catalog
    public void removeCourse(Course course) {
        courses.remove(course);
    }

    void addObserver(Parent observer) {
        if(!parents.contains(observer))
            parents.add(observer);
    }

    void removeObserver(Parent observer) {
        parents.remove(observer);
    }

    void notifyObservers(Grade grade) {
        Notification notification1 = new Notification(grade, grade.getStudent().getFather());
        Notification notification2 = new Notification(grade, grade.getStudent().getMother());
        for(Parent parent : parents) {
                if (grade.getStudent().getFather() != null && grade.getStudent().getFather().equals(parent))
                    parent.update(notification1);
                if (grade.getStudent().getMother() != null && grade.getStudent().getMother().equals(parent))
                    parent.update(notification2);
            }
    }
}
