import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;


/**
 *
 * @author Brandon
 */
public class Main {

    //private static ArrayList<Student> students = new ArrayList<>();
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";
    static final String username = "root";
    static final String password = "BP061512-81";
    
    private static boolean addStudentToDB(String id, String name, String mobile) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, username, password);
            stmt = conn.createStatement();
            String sql = "INSERT INTO students (id, name, phone) VALUES ('" + id + "', '" + name + "', '" + mobile + "')";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException | ClassNotFoundException se) {
            se.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private static String viewAllStudents() {
        Connection conn = null;
        Statement stmt = null;
        StringBuilder students = new StringBuilder();
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, username, password);
            stmt = conn.createStatement();
            String sql = "SELECT id, name, phone FROM students";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String mobile = rs.getString("phone");
                students.append("ID: ").append(id).append(", Name: ").append(name).append(", Mobile: ").append(mobile).append("\n");
            }
            rs.close();
        } catch (SQLException | ClassNotFoundException se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return students.toString();
    }

    private static String searchStudentById(String id) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, username, password);
            stmt = conn.createStatement();
            String sql = "SELECT id, name, phone FROM students WHERE id='" + id + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String name = rs.getString("name");
                String mobile = rs.getString("phone");
                rs.close();
                return "ID: " + id + ", Name: " + name + ", Mobile: " + mobile;
            } else {
                rs.close();
                return null;
            }
        } catch (SQLException | ClassNotFoundException se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private static boolean deleteStudentById(String id) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, username, password);
            stmt = conn.createStatement();
            String sql = "DELETE FROM students WHERE id='" + id + "'";
            int rowsAffected = stmt.executeUpdate(sql);
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException se) {
            se.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(null);

        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(0, 40, 50, 20);

        JTextField idBox = new JTextField();
        idBox.setBounds(60, 40, 200, 20);

        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(0, 80, 50, 20);

        JTextField nameBox = new JTextField();
        nameBox.setBounds(60, 80, 200, 20);

        JLabel mobileLabel = new JLabel("Mobile: ");
        mobileLabel.setBounds(0, 120, 50, 20);

        JTextField mobileBox = new JTextField();
        mobileBox.setBounds(60, 120, 200, 20);

        JButton add = new JButton("Add");
        add.setBounds(0, 170, 100, 20);

        JButton viewStudent = new JButton("View Student");
        viewStudent.setBounds(120, 170, 100, 20);

        JButton search = new JButton("Search");
        search.setBounds(240, 170, 100, 20);

        JButton delete = new JButton("Delete");
        delete.setBounds(360, 170, 100, 20);

        // Additions
        panel.add(idLabel);
        panel.add(idBox);
        panel.add(nameLabel);
        panel.add(nameBox);
        panel.add(mobileLabel);
        panel.add(mobileBox);
        panel.add(add);
        panel.add(search);
        panel.add(viewStudent);
        panel.add(delete);

        frame.add(panel);
        frame.setVisible(true);

        // Button action listeners
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idBox.getText();
                String name = nameBox.getText();
                String mobile = mobileBox.getText();

                boolean success = addStudentToDB(id, name, mobile);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Student added: " + name, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Error adding student", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        viewStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String students = viewAllStudents();
                JOptionPane.showMessageDialog(frame, students, "All Students", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idBox.getText();
                String student = searchStudentById(id);
                if (student != null) {
                    JOptionPane.showMessageDialog(frame, student, "Student Found", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Student not found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idBox.getText();
                boolean success = deleteStudentById(id);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Deleted student with ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Student not found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

