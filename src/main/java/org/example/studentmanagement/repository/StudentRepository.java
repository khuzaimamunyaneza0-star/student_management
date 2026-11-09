package org.example.studentmanagement.repository;

import org.example.studentmanagement.model.Student;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {

    private final DataSource dataSource;

    public StudentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Student student) {
        String sql = "INSERT INTO students (full_name, age, email) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String fullName = student.getFirstName() + " " + student.getLastName();

            statement.setString(1, fullName);
            statement.setInt(2, student.getAge());
            statement.setString(3, student.getEmail());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not save student", e);
        }
    }

    public List<Student> findAll() {
        String sql = "SELECT id, full_name, age, email FROM students ORDER BY id";
        List<Student> students = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setAge(resultSet.getInt("age"));
                student.setEmail(resultSet.getString("email"));

                String[] names = resultSet.getString("full_name").split(" ", 2);
                student.setFirstName(names[0]);
                student.setLastName(names.length > 1 ? names[1] : "");

                students.add(student);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not load students", e);
        }
        return students;
    }
}