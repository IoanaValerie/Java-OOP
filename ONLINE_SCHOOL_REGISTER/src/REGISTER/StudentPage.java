package REGISTER;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentPage extends JFrame implements ListSelectionListener {
    JList<String> list;
    JPanel rb1, rb2;
    JScrollPane scroll;
    JLabel label1, label2, label3, label4;
    JTextField text1, text2, text3, text4;
    Student student;
    ArrayList<Course> courses, coursesStudent;
    Course courseListener;
    Assistant assistant;
    ScoreVisitor v;
    public StudentPage(ArrayList<Course> courses, Student student, ScoreVisitor v) {
        super(student.toString());
        this.student = student;
        this.courses = courses;
        this.v = v;

        coursesStudent = new ArrayList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Course course : courses)
            if (course.getAllStudents().contains(student)) {
                coursesStudent.add(course);
                listModel.addElement(course.getName());
            }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        list = new JList<>(listModel);
        list.addListSelectionListener(this);

        scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVisible(true);

        rb1 = new JPanel();
        rb1.add(scroll);
        add(rb1, BorderLayout.WEST);

        label1 = new JLabel("Titular: ");
        label2 = new JLabel("Assistants list: ");
        label3 = new JLabel("Assistant :");
        label4 = new JLabel("Grades: ");

        text1 = new JTextField(40);
        text2 = new JTextField(40);
        text3 = new JTextField(40);
        text4 = new JTextField(40);

        rb2 = new JPanel();
        rb2.setLayout(new GridLayout(4,2));
        rb2.add(label1);
        rb2.add(text1);
        rb2.add(label2);
        rb2.add(text2);
        rb2.add(label3);
        rb2.add(text3);
        rb2.add(label4);
        rb2.add(text4);

        add(rb2, BorderLayout.EAST);

        pack();
        setVisible(true);

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(!list.isSelectionEmpty()) {
            String courseName = list.getSelectedValue();
            for(Course course : coursesStudent)
                if(course.getName().equals(courseName))
                    courseListener = course;

            HashMap<String, Group> dictionary = courseListener.getDictionary();
            for(Map.Entry<String, Group> entry : dictionary.entrySet())
                if(entry.getValue().contains(student))
                    assistant = entry.getValue().getAssistant();


            text1.setText(courseListener.getTitular().toString());
            text2.setText(courseListener.getAssistants().toString());
            text3.setText(assistant.toString());
            if(courseListener.getGrade(student) != null)
                text4.setText(courseListener.getGrade(student).toString());
            else text4.setText("Nota nu a fost validata!");
        }
    }
}
