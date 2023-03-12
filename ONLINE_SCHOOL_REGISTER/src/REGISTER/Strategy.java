package REGISTER;

import java.util.ArrayList;

public interface Strategy {
    Student getBest(ArrayList<Grade> grades);
}
