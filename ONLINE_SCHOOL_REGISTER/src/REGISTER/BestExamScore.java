package REGISTER;

import java.util.ArrayList;

public class BestExamScore implements Strategy{
    @Override
    public Student getBest(ArrayList<Grade> grades) {
        Double maxim = 0.0;
        Student student = null;
        for(Grade grade : grades)
            if(grade.getExamScore() > maxim) {
                maxim = grade.getExamScore();
                student = grade.getStudent();
            }
        return student;
    }
}
