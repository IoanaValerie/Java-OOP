package REGISTER;

public class Notification {
    Grade grade;
    Parent parent;
    public Notification(Grade grade, Parent parent) {
        this.grade = grade;
        this.parent = parent;
    }

    public String toString() {
        if(grade.getPartialScore() != 0d && grade.getExamScore() == 0d) {
            if (grade.getStudent().getMother() != null && grade.getStudent().getMother().equals(parent))
                return "Stimata doamna, " + grade.getStudent().getMother() + ", va anuntam ca fiul/fiica dvs. a obtinut nota: " + grade.getPartialScore() + " la examenul partial in cadrul cursului: " + grade.getCourse() + "\n";
            if (grade.getStudent().getFather() != null && grade.getStudent().getFather().equals(parent))
                return "Stimate domn, " + grade.getStudent().getFather() + ", va anuntam ca fiul/fiica dvs. a obtinut nota: " + grade.getPartialScore() + " la examenul partial in cadrul cursului: " + grade.getCourse() + "\n";
        }
        else if(grade.getExamScore() != 0d && grade.getPartialScore() == 0d) {
            if (grade.getStudent().getMother() != null && grade.getStudent().getMother().equals(parent))
                return "Stimata doamna, " + grade.getStudent().getMother() + ", va anuntam ca fiul/fiica dvs. a obtinut nota: " + grade.getExamScore() + " la examenul final in cadrul cursului: " + grade.getCourse() + "\n";
            if (grade.getStudent().getFather() != null && grade.getStudent().getFather().equals(parent))
                return "Stimate domn, " + grade.getStudent().getFather() + ", va anuntam ca fiul/fiica dvs. a obtinut nota: " + grade.getExamScore() + " la examenul final in cadrul cursului: " + grade.getCourse() + "\n";
        }
        return null;
    }
}
