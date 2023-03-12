package REGISTER;

public abstract class User {
    private final String firstName, lastName;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public User() {
        this.firstName = "";
        this.lastName = "";
    }

    public String toString() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
}
