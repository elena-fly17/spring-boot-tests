package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

// Благодаря этой аннотации будет сделана автоматическая инициализация моков в классе-тесте
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    // Создаем поддельный (мок) объект - имитацию реальных объектов
    @Mock
    private StudentRepository studentRepository;
    private StudentService underTest;

    // Этот метод выполнится перед каждым тестовым методом этого класса
    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetAllStudents() {
        // дано
        underTest.getAllStudents();
        // когда
        // Метод verify используется для проверки, что метод findAll() был вызван
        // у объекта studentRepository. Это гарантирует, что метод getAllStudents()
        // действительно обращается к studentRepository для получения.
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        // дано ------ создали студента, которого будем сохранять в БД
        Student student = new Student("Jamila", "jamila@gmail.com", Gender.FEMALE);
        // когда ------ вызываем метод сервиса для сохранения студента в БД
        underTest.addStudent(student);
        // в итоге
        // Создается ArgumentCaptor для класса Student - он предназначен для захвата аргументов,
        // переданных в методы мок-объектов (этот мок-объект здесь - репозиторий внутри сервиса)
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        // Проверяется, что метод save был вызван у объекта studentRepository
        // studentArgumentCaptor.capture() захватывает переданный в метод save аргумент.
        verify(studentRepository).save(studentArgumentCaptor.capture());
        // Получаем от захватчика студента, которого он перехватил в строке выше
        Student capturedStudent = studentArgumentCaptor.getValue();
        // Сравниваем полученного от захватчика студента с изначально созданным нами
        // в первой строке этого тестового метода студентом, чтобы убедиться, что в БД
        // действительно был сохранен созданный нами студент
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // дано ------ создали студента, которого будем сохранять в БД
        Student student = new Student("Jamila", "jamila@gmail.com", Gender.FEMALE);
        // когда
        // В библиотеке Mockito метод given(...) используется для настройки поведения mock-объекта
        // перед выполнением теста - позволяет определить, какой результат должен возвращать мок
        // при вызове конкретного метода с определенными аргументами
        // В нашем случае при вызове у мок-репозитория метода проверки, есть ли в БД
        // указанный email или нет, метод мок-репозитория должен вернуть true
        given(studentRepository.selectExistsEmail(anyString())).willReturn(true);
        // в итоге
        // Метод assertThatThrownBy проверяет, вызывает ли какой-то код
        // (блок кода, метод, лямбда-выражение) исключение определенного типа
        assertThatThrownBy(() -> underTest
                // Вызов метода сохранения студента в БД - проверяем: выбросит ли он исключение
                .addStudent(student))
                // Проверяем, соответствует ли класс выброшенного исключения тому, что нужно
                .isInstanceOf(BadRequestException.class)
                // Проверяем, какое сообщение содержит выброшенное исключение
                .hasMessageContaining("Email " + student.getEmail() + " taken");
        // Проверяем, чтобы репозиторий не сохранял студентов в БД после вылета исключения
        verify(studentRepository, never()).save(any());
    }

    @Test
    @Disabled // Временно отключили этот тестовый метод
    void deleteStudent() {
    }
}