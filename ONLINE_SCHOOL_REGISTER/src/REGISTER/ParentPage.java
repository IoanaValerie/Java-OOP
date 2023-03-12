package REGISTER;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class ParentPage extends JFrame implements ActionListener {
    Student student;
    ScoreVisitor visitor;
    ArrayList<Course> courses;
    JPanel rb1, rb2;
    Parent parent;
    JButton button;
    JTextArea text;
    Vector<String> vector = new Vector<>();
    ParentPage(ArrayList<Course> courses, Student student, Parent parent, ScoreVisitor v) {
        super(parent.toString());
        this.courses = courses;
        this.visitor = v;
        this.student = student;
        this.parent = parent;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        text = new JTextArea(4, 100);

        rb1 = new JPanel();
        rb1.add(text);
        add(rb1, BorderLayout.CENTER);

        button = new JButton("Refresh");
        button.addActionListener(this);
        button.setVisible(true);

        rb2 = new JPanel();
        rb2.add(button);
        add(rb2, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button) {
            for(String string : parent.getNotificationsVector())
                if(!vector.contains(string)) {
                    text.append(string);
                    vector.add(string);
                }
        }
    }
}
