package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;


@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  private static final Logger log = LoggerFactory.getLogger(StudentServiceTest.class);
  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = List.of(
        new Student(1, "佐藤太郎", "さとうたろう", "たろう", "taro@gmail.com", "東京都", 22, "男",
            "", false)
    );

    List<StudentCourses> studentCourses = List.of(
        new StudentCourses(1, 1, "Java", LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1))
    );

    when(repository.searchStudent()).thenReturn(studentList);
    when(repository.searchStudentsCourses()).thenReturn(studentCourses);

    List<StudentDetail> actual = sut.searchStudentList();

    verify(repository, times(1)).searchStudent();
    verify(repository, times(1)).searchStudentsCourses();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourses);

    assertEquals(actual, converter.convertStudentDetails(studentList, studentCourses));
  }

  @Test
  void コース情報一覧検索_リポジトリの処理が適切に呼び出されていること() {
    List<StudentCourses> studentCourses = List.of(
        new StudentCourses(1, 1, "Java", LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1))
    );

    when(repository.searchStudentsCourses()).thenReturn(studentCourses);

    List<StudentCourses> actual = sut.searchStudentsCourseList();

    verify(repository, times(1)).searchStudentsCourses();

    assertEquals(actual, repository.searchStudentsCourses());
  }

  @Test
  void 受講生詳細情報のID検索_リポジトリの処理が適切に呼び出されていること() {
    int studentId = 1;

    Student student = new Student(1, "佐藤太郎", "さとうたろう", "たろう", "taro@gmail.com",
        "東京都", 22, "男", "", false);

    List<StudentCourses> studentCourses = List.of(
        new StudentCourses(1, 1, "Java", LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1))
    );

    when(repository.searchIdStudent(studentId)).thenReturn(student);
    when(repository.searchIdStudentCourses(studentId)).thenReturn(studentCourses);

    StudentDetail actual = sut.searchIdStudentInfo(studentId);

    verify(repository, times(1)).searchIdStudent(studentId);
    verify(repository, times(1)).searchIdStudentCourses(studentId);

    assertEquals(student, actual.getStudent());
    assertEquals(studentCourses, actual.getStudentCourses());
  }

  @Test
  void コース情報ID検索_リポジトリの処理が適切に呼び出されていること() {
    int courseId = 1;

    StudentCourses course = new StudentCourses(1, 1, "Java", LocalDate.of(2025, 1, 1),
        LocalDate.of(2026, 1, 1));

    when(repository.searchIdCourse(courseId)).thenReturn(course);

    StudentCourses actual = sut.searchCourses(courseId);

    verify(repository, times(1)).searchIdCourse(courseId);

    assertEquals(actual, repository.searchIdCourse(courseId));
  }

  @Test
  void 受講生情報の登録_リポジトリの処理が適切に呼び出されていること() {
    Student student = new Student(1, "佐藤太郎", "さとうたろう", "たろう", "taro@gmail.com",
        "東京都", 22, "男", "", false);

    sut.registerStudent(student);

    verify(repository, times(1)).registerNewStudent(student);
  }

  @Test
  void コース情報の登録_リポジトリの処理が適切に呼び出されていること() {
    int studentId = 1;

    List<StudentCourses> studentCourses = List.of(
        new StudentCourses(1, 1, "Java", LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1))
    );

    when(repository.newStudentId()).thenReturn(studentId);

    sut.registerCourse(studentCourses);

    verify(repository, times(1)).registerNewCourse(studentId, "Java");
  }

  @Test
  void 受講生情報の更新_リポジトリの処理が適切に呼び出されていること() {
    Student student = new Student(1, "佐藤太郎", "さとうたろう", "たろう", "taro@gmail.com",
        "東京都", 22, "男", "", false);

    sut.updateStudent(student);

    verify(repository, times(1)).updateStudentInfo(student);
  }

  @Test
  void コース情報の更新_リポジトリの処理が適切に呼び出されていること() {
    StudentCourses course = new StudentCourses(1, 1, "Java", LocalDate.of(2025, 1, 1),
        LocalDate.of(2026, 1, 1));

    sut.updateStudentCourse(course);

    verify(repository, times(1)).updateStudentCourseInfo(course);
  }
}
