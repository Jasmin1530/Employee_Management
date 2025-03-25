import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EmployeeManagement extends JFrame implements ActionListener {
    JLabel l1, l2, l3, l4, l5;
    JTextField tf1, tf2, tf4, tf5;
    JRadioButton maleBtn, femaleBtn;
    ButtonGroup genderGroup;
    JButton bookBtn, cancelBtn, updateBtn, displayBtn;
    Connection con;

    EmployeeManagement() {
        setTitle("Employee Management System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        l1 = new JLabel("Employee ID:");
        l2 = new JLabel("Name:");
        l3 = new JLabel("Gender:");
        l4 = new JLabel("Department:");
        l5 = new JLabel("Salary:");

        tf1 = new JTextField();
        tf2 = new JTextField();
        tf4 = new JTextField();
        tf5 = new JTextField();

        maleBtn = new JRadioButton("Male");
        femaleBtn = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleBtn);
        genderGroup.add(femaleBtn);

        bookBtn = new JButton("Register");
        cancelBtn = new JButton("Delete");
        updateBtn = new JButton("Update");
        displayBtn = new JButton("Display");

        bookBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        displayBtn.addActionListener(this);

        l1.setBounds(50, 30, 100, 30);
        tf1.setBounds(150, 30, 150, 30);
        l2.setBounds(50, 70, 100, 30);
        tf2.setBounds(150, 70, 150, 30);
        l3.setBounds(50, 110, 100, 30);
        maleBtn.setBounds(150, 110, 70, 30);
        femaleBtn.setBounds(230, 110, 80, 30);
        l4.setBounds(50, 150, 100, 30);
        tf4.setBounds(150, 150, 150, 30);
        l5.setBounds(50, 190, 100, 30);
        tf5.setBounds(150, 190, 150, 30);

        bookBtn.setBounds(50, 250, 100, 50);
        cancelBtn.setBounds(150, 250, 100, 50);
        updateBtn.setBounds(250, 250, 100, 50);
        displayBtn.setBounds(350, 250, 100, 50);

        add(l1); add(tf1);
        add(l2); add(tf2);
        add(l3); add(maleBtn); add(femaleBtn);
        add(l4); add(tf4);
        add(l5); add(tf5);
        add(bookBtn); add(cancelBtn); add(updateBtn); add(displayBtn);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_db", "root", " ");
            System.out.println("Database Connected Successfully!");
        } catch (Exception e) {
            System.out.println("Database Connection Error: " + e.getMessage());
            e.printStackTrace();
        }
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bookBtn) {
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed. Restart the program.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String gender = maleBtn.isSelected() ? "Male" : (femaleBtn.isSelected() ? "Female" : "");

                PreparedStatement stmt = con.prepareStatement("INSERT INTO employees VALUES(?, ?, ?, ?, ?)");
                stmt.setInt(1, Integer.parseInt(tf1.getText()));
                stmt.setString(2, tf2.getText());
                stmt.setString(3, gender);
                stmt.setString(4, tf4.getText());
                stmt.setDouble(5, Double.parseDouble(tf5.getText()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Employee record added successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == cancelBtn) {
            try {
                PreparedStatement stmt = con.prepareStatement("DELETE FROM employees WHERE emp_id=?");
                stmt.setInt(1, Integer.parseInt(tf1.getText()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Employee record deleted.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == updateBtn) {
            try {
                String gender = maleBtn.isSelected() ? "Male" : (femaleBtn.isSelected() ? "Female" : "");
                PreparedStatement stmt = con.prepareStatement("UPDATE employees SET name=?, gender=?, department=?, salary=? WHERE emp_id=?");
                stmt.setString(1, tf2.getText());
                stmt.setString(2, gender);
                stmt.setString(3, tf4.getText());
                stmt.setDouble(4, Double.parseDouble(tf5.getText()));
                stmt.setInt(5, Integer.parseInt(tf1.getText()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Employee record updated.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == displayBtn) {
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM employees");
                StringBuilder data = new StringBuilder();
                while (rs.next()) {
                    data.append("ID: ").append(rs.getInt(1))
                        .append(", Name: ").append(rs.getString(2))
                        .append(", Gender: ").append(rs.getString(3))
                        .append(", Department: ").append(rs.getString(4))
                        .append(", Salary: ").append(rs.getDouble(5))
                        .append("\n");
                }
                JOptionPane.showMessageDialog(this, data.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new EmployeeManagement();
    }
}
