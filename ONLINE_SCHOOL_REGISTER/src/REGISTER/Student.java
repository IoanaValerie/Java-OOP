package REGISTER;

public class Student extends User implements Comparable{
    private Parent mother, father;

    public Student(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public void setMother(Parent mother) {
        this.mother = mother;
    }

    public void setFather(Parent father) {
        this.father = father;
    }

    public Parent getMother() {
        return mother;
    }

    public Parent getFather() {
        return father;
    }

    @Override
    public int compareTo(Object o) {
        if(this.getFirstName().compareTo(((Student) o).getFirstName()) != 0)
            return this.getFirstName().compareTo(((Student) o).getLastName());
        else return this.getLastName().compareTo(((Student) o).getLastName());
    }

}
