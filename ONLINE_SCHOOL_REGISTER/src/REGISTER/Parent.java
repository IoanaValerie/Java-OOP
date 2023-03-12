package REGISTER;

import java.util.Vector;

public class Parent extends User {
    public Parent(String firstName, String lastName) {
        super(firstName, lastName);
    }

    private Vector<String> notificationsVector = new Vector<>();

    void update(Notification notification) {
        if(notification.toString() != null)
            getNotificationsVector().add(notification.toString());
    }

    public Vector<String> getNotificationsVector() {
        return notificationsVector;
    }

    public void setNotificationsVector(Vector<String> notificationsVector) {
        this.notificationsVector = notificationsVector;
    }
}
