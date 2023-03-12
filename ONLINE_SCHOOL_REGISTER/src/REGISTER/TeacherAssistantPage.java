package REGISTER;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TeacherAssistantPage extends JFrame implements ListSelectionListener, ActionListener {
    JList<String> list;
    JPanel rb1, rb2, rb3;
    JScrollPane scroll;
    JTextField text;
    JButton button;
    Teacher teacher = null;
    Assistant assistant = null;
    ArrayList<Course> courses, coursesUser;
    Course courseListener;
    ArrayList<String> Grades;
    ScoreVisitor v;

    public TeacherAssistantPage(ArrayList<Course> courses, User user, ScoreVisitor v) {
        super(user.toString());
        if(user instanceof Teacher)
            this.teacher = (Teacher) user;
        else this.assistant = (Assistant) user;
        this.courses = courses;
        this.v = v;

        coursesUser = new ArrayList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Course course : courses)
            if (course.getTitular().equals(teacher) || course.getAssistants().contains(assistant)) {
                coursesUser.add(course);
                listModel.addElement(course.getName());
            }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        list = new JList<>(listModel);
        list.addListSelectionListener(this);

        scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVisible(true);

        rb1 = new JPanel();
        rb1.add(scroll);
        add(rb1, BorderLayout.NORTH);

        if(teacher != null)
            text = new JTextField(120);
        if(assistant != null)
            text = new JTextField(120);

        rb2 = new JPanel();
        rb2.setLayout(new GridLayout(1,2));
        rb2.add(text);
        add(rb2, BorderLayout.CENTER);

        button = new JButton("Validate grades");
        button.addActionListener(this);
        button.setVisible(true);

        rb3 = new JPanel();
        rb3.add(button);
        add(rb3, BorderLayout.SOUTH);

        pack();
        setVisible(true);

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(!list.isSelectionEmpty()) {
            String courseName = list.getSelectedValue();
            for(Course course : coursesUser)
                if(course.getName().equals(courseName))
                    courseListener = course;
            if(teacher != null) {
                Grades = v.getTeacherGrades(teacher, courseListener.getName());
                text.setText("Titular: " + Grades.toString());
            }
            else if(assistant != null) {
                Grades = v.getAssistantGrades(assistant, courseListener.getName());
                text.setText("Assistant: " + Grades.toString());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            if(teacher != null) {
                teacher.accept(v);
            }
            else if(assistant != null)
                assistant.accept(v);
            text.setText(null);
        }
    }
}