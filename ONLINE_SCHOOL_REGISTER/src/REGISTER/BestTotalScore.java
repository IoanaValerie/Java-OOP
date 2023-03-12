package REGISTER;

import java.util.ArrayList;

public class BestTotalScore implements Strategy{
    @Override
    public Student getBest(ArrayList<Grade> grades) {
        Double maxim = 0.0;
        Student student = null;
        for(Grade grade : grades)
            if(grade.getExamScore() + grade.getPartialScore() > maxim) {
                maxim = grade.getExamScore() + grade.getPartialScore();
                student = grade.getStudent();
            }
        return student;
    }
}
