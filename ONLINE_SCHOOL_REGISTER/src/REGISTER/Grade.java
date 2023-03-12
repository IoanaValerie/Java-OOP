package REGISTER;

public class Grade implements Comparable, Cloneable {
    private Double partialScore, examScore;
    private Student student;
    private String courseName;

    public Object clone() {
        Grade grade = new Grade();
        grade.setCourse(courseName);
        grade.setStudent(student);
        grade.setPartialScore(partialScore);
        grade.setExamScore(examScore);
        return grade;
    }

    @Override
    public int compareTo(Object o) {
        Grade grade = (Grade) o;
        return this.getTotal().compareTo(grade.getTotal());
    }

    public Double getTotal() {
        return partialScore + examScore;
    }

    public Double getPartialScore() {
        return partialScore;
    }

    public void setPartialScore(Double partialScore) {
        this.partialScore = partialScore;
    }

    public Double getExamScore() {
        return examScore;
    }

    public void setExamScore(Double examScore) {
        this.examScore = examScore;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getCourse() {
        return courseName;
    }

    public void setCourse(String course) {
        this.courseName = course;
    }

    public String toString() {
        if(partialScore == 0d)
            return student.toString() + ": partialScore( ), " + "examscore(" + examScore + ")";
        if(examScore == 0d)
            return student.toString() + ": partialScore(" + partialScore + "), " + "examscore( )";
        return student.toString() + ": partialScore(" + partialScore + "), " + "examscore(" + examScore + ")";
    }
}
