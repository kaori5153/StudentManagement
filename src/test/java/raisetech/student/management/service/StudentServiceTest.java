package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;


@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
    //テスト毎度newしてくれる
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> student = new ArrayList<>();
    List<StudentCourses> courses = new ArrayList<>();

    when(repository.searchStudent()).thenReturn(
        student);
    when(repository.searchStudentsCourses()).thenReturn(courses);

    sut.searchStudentList();

    verify(repository, times(1)).searchStudent();
    verify(repository, times(1)).searchStudentsCourses();
    verify(converter, times(1)).convertStudentDetails(student, courses);
  }

  @Test
  void コース情報一覧検索_リポジトリの処理が適切に呼び出されていること() {
    List<StudentCourses> courses = new ArrayList<>();

    when(repository.searchStudentsCourses()).thenReturn(courses);

    sut.searchStudentsCourseList();

    verify(repository, times(1)).searchStudentsCourses();
  }

  @Test
  void 受講生詳細情報のID検索_リポジトリの処理が適切に呼び出されていること() {
    Student student = new Student();
    List<StudentCourses> studentCourses = new ArrayList<>();
    int id = 1;

    when(repository.searchIdStudent(id)).thenReturn(student);
    when(repository.searchIdStudentCourses(id)).thenReturn(studentCourses);

    StudentDetail actual = sut.searchIdStudentInfo(id);

    verify(repository, times(1)).searchIdStudent(id);
    verify(repository, times(1)).searchIdStudentCourses(id);

    assertEquals(student, actual.getStudent());
    assertEquals(studentCourses, actual.getStudentCourses());
  }

  @Test
  void コース情報ID検索_リポジトリの処理が適切に呼び出されていること() {
    StudentCourses courses = new StudentCourses();
    int id = 1;

    when(repository.searchIdCourse(id)).thenReturn(courses);

    sut.searchCourses(id);

    verify(repository, times(1)).searchIdCourse(id);
  }
}