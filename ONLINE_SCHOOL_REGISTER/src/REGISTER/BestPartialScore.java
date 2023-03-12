package REGISTER;

import java.util.ArrayList;

public class BestPartialScore implements Strategy{
    @Override
    public Student getBest(ArrayList<Grade> grades) {
        Double maxim = 0.0;
        Student student = null;
        for(Grade grade : grades)
            if(grade.getPartialScore() > maxim) {
                maxim = grade.getPartialScore();
                student = grade.getStudent();
            }
        return student;
    }
}
