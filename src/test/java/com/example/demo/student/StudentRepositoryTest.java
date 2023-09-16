package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    // Метод сработает после каждого тестового метода
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckWhenStudentEmailExists() {
        // дано
        String email = "jamila@gmail.com";
        Student student = new Student("Jamila", email, Gender.FEMALE);
        underTest.save(student);
        // когда
        boolean expected = underTest.selectExistsEmail(email);
        // в итоге
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckWhenStudentEmailDoesNotExists() {
        // дано
        String email = "jamila@gmail.com";
        // когда
        boolean expected = underTest.selectExistsEmail(email);
        // в итоге
        assertThat(expected).isFalse();
    }
}