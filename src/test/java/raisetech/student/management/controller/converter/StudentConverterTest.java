package raisetech.student.management.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;


public class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before(){
    sut = new StudentConverter();
  }

  static Stream<Arguments> validData() {
    List<Student> studentsList = List.of(
        Student.builder()
            .id(1).name("佐藤太郎").furigana("さとうたろう")
            .email("taro@gmail.com").area("東京都").age(22).gender("男")
            .build()
    );
    List<StudentCourses> coursesList = List.of(
        StudentCourses.builder()
            .courseId(1)
            .studentId(1)
            .course("Java")
            .build()
    );

    return Stream.of(
        Arguments.of(studentsList, coursesList)
    );
  }

  @ParameterizedTest
  @MethodSource("validData")
  void 受講生情報とコース情報が受講生IDで紐づいて受講生詳細情報としてマッピングできていること(List<Student> students,
      List<StudentCourses> studentsCourses){

    List<StudentDetail> actual = sut.convertStudentDetails(students,studentsCourses);

    assertThat(actual.get(0).getStudent()).isEqualTo(students.get(0));
    assertThat(actual.get(0).getStudentCourses()).isEqualTo(studentsCourses);

  }static Stream<Arguments> invalidData() {
    List<Student> studentsList = List.of(
        Student.builder()
            .id(1).name("佐藤太郎").furigana("さとうたろう")
            .email("taro@gmail.com").area("東京都").age(22).gender("男")
            .build()
    );
    List<StudentCourses> coursesList = List.of(
        StudentCourses.builder()
            .courseId(1)
            .studentId(2)
            .course("Java")
            .build()
    );

    return Stream.of(
        Arguments.of(studentsList, coursesList)
    );
  }

  @ParameterizedTest
  @MethodSource("invalidData")
  void 受講生情報の受講生IDとコース情報の受講生IDが異なり受講生詳細情報としてマッピングされないこと(List<Student> students,
      List<StudentCourses> studentsCourses){

    List<StudentDetail> actual = sut.convertStudentDetails(students,studentsCourses);

    assertThat(actual.get(0).getStudent()).isEqualTo(students.get(0));
    assertThat(actual.get(0).getStudentCourses().isEmpty());

  }
}
