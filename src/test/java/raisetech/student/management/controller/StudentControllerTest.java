package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


  @Nested
  class バリデーションテスト {

    static Stream<Arguments> studentData() {
      return Stream.of(
          Arguments.of(1, "佐藤太郎", "さとうたろう", "taro@gmail.com", "東京都", 22, "男")
      );
    }

    static Stream<Arguments> courseData() {
      return Stream.of(
          Arguments.of(1, 1, "Java")
      );
    }

    @ParameterizedTest
    @MethodSource("studentData")
    void 受講生詳細の受講生で入力チェックに異常がないこと(
        int id, String name, String furigana, String email, String area, int age, String gender
    ) throws Exception {
      Student student = Student.builder()
          .id(id)
          .name(name)
          .furigana(furigana)
          .email(email)
          .area(area)
          .age(age)
          .gender(gender)
          .build();

      Set<ConstraintViolation<Student>> violations = validator.validate(student);

      assertThat(violations.size()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("courseData")
    void 受講生詳細のコース情報の入力チェックに異常がないこと(int courseId, int studentId,
        String course) throws Exception {
      StudentCourses courseInfo = StudentCourses.builder()
          .courseId(courseId)
          .studentId(studentId)
          .course(course)
          .build();

      Set<ConstraintViolation<StudentCourses>> violations = validator.validate(courseInfo);

      assertThat(violations.size()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("studentData")
    void 受講生詳細の受講生の入力チェックで名前のnullチェックで掛かること(
        int id, String name, String furigana, String email, String area, int age, String gender
    ) throws Exception {
      Student student = Student.builder()
          .id(id)
          .furigana(furigana)
          .email(email)
          .area(area)
          .age(age)
          .gender(gender)
          .build();

      Set<ConstraintViolation<Student>> violations = validator.validate(student);

      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).extracting("message").containsOnly("名前を入力してください");
    }

    @ParameterizedTest
    @MethodSource("studentData")
    void 受講生詳細の受講生の入力チェックで名前の文字数チェックで掛かること(
        int id, String name, String furigana, String email, String area, int age, String gender
    ) throws Exception {
      Student student = Student.builder()
          .id(id)
          .name("a")
          .furigana(furigana)
          .email(email)
          .area(area)
          .age(age)
          .gender(gender)
          .build();

      Set<ConstraintViolation<Student>> violations = validator.validate(student);

      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).extracting("message")
          .containsOnly("2文字以上50文字以下で入力してください");
    }

    @ParameterizedTest
    @MethodSource("studentData")
    void 受講生詳細の受講生の入力チェックでメールアドレスのnullチェックで掛かること(
        int id, String name, String furigana, String email, String area, int age, String gender
    ) throws Exception {
      Student student = Student.builder()
          .id(id)
          .name(name)
          .furigana(furigana)
          .area(area)
          .age(age)
          .gender(gender)
          .build();

      Set<ConstraintViolation<Student>> violations = validator.validate(student);

      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).extracting("message").containsOnly("メールアドレスを入力してください");
    }

    @ParameterizedTest
    @MethodSource("courseData")
    void 受講生詳細のコース情報の入力チェックのコース名のnullチェックで掛かること(
        int courseId, int studentId, String course) throws Exception {
      StudentCourses courseInfo = StudentCourses.builder()
          .courseId(courseId)
          .studentId(studentId)
          .build();

      Set<ConstraintViolation<StudentCourses>> violations = validator.validate(courseInfo);

      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).extracting("message")
          .containsOnly("コース名を入力してください");
    }

    @ParameterizedTest
    @MethodSource("courseData")
    void 受講生詳細のコース情報の入力チェックのコース名の文字数チェックで掛かること(
        int courseId, int studentId, String course) throws Exception {
      StudentCourses courseInfo = StudentCourses.builder()
          .courseId(courseId)
          .studentId(studentId)
          .course("abcd-abcd-abcd-abcd-a")
          .build();

      Set<ConstraintViolation<StudentCourses>> violations = validator.validate(courseInfo);

      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).extracting("message")
          .containsOnly("1文字以上20文字以下で入力してください");
    }
  }

  @Nested
  class 正常動作テスト {

    static Stream<Arguments> studentData() {
      return Stream.of(
          Arguments.of(1, "佐藤太郎", "さとうたろう", "taro@gmail.com", "東京都", 22, "男")
      );
    }

    static Stream<Arguments> courseData() {
      return Stream.of(
          Arguments.of(1, 1, "Java")
      );
    }

    @Test
    void 受講生一覧検索が実行できて空のリストが返ってくること() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders.get("/students"))//検証する内容。getの検証
          .andExpect(status().isOk())
          .andExpect(content().json("[]"));

      verify(service, times(1)).searchStudentList();
    }

    @Test
    void 受講生コース情報検索が実行できて空のリストが返ってくること() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders.get("/students/courses"))
          .andExpect(status().isOk())
          .andExpect(content().json("[]"));

      verify(service, times(1)).searchStudentsCourseList();
    }

    @ParameterizedTest
    @MethodSource("studentData")
    void 受講生登録が実行できてレスポンスが返ってくること(int id, String name, String furigana,
        String email, String area, int age, String gender
    ) throws Exception {
      Student student = Student.builder()
          .id(id)
          .name(name)
          .furigana(furigana)
          .email(email)
          .area(area)
          .age(age)
          .gender(gender)
          .build();

      StudentDetail studentDetail = StudentDetail.builder()
          .student(student).build();

      mockMvc.perform(MockMvcRequestBuilders.post("/student")
              .contentType(MediaType.APPLICATION_JSON) // JSONを送信
              .content(new ObjectMapper().writeValueAsString(studentDetail))) // JSONデータを追加
          .andExpect(status().isOk())
          .andExpect(content().string("登録処理完了"));

      verify(service, times(1)).registerStudent(any(Student.class));
    }

    @ParameterizedTest
    @MethodSource("courseData")
    void 受講生コース情報登録が実行できてレスポンスが返ってくること(
        int courseId, int studentId, String course) throws Exception {
      StudentCourses courseInfo = StudentCourses.builder()
          .courseId(courseId)
          .studentId(studentId)
          .course(course)
          .build();

      List<StudentCourses> courses = List.of(courseInfo);

      StudentDetail studentDetail = StudentDetail.builder()
          .studentCourses(courses).build();

      mockMvc.perform(MockMvcRequestBuilders.post("/student/course")
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(studentDetail)))
          .andExpect(status().isOk())
          .andExpect(content().string("登録処理完了"));

      verify(service, times(1)).registerCourse(anyList());
    }

    @ParameterizedTest
    @MethodSource("studentData")
    void 受講生ID検索が実行できて受講生情報が返ってくること(int id, String name, String furigana,
        String email, String area, int age, String gender
    ) throws Exception {
      Student student = Student.builder()
          .id(id)
          .name(name)
          .furigana(furigana)
          .email(email)
          .area(area)
          .age(age)
          .gender(gender)
          .build();

      StudentDetail studentDetail = StudentDetail.builder()
          .student(student).build();

      when(service.searchIdStudentInfo(1)).thenReturn(studentDetail);

      mockMvc.perform(MockMvcRequestBuilders.get("/student/{id}", id))
          .andExpect(status().isOk())
          .andExpect(content().json(new ObjectMapper().writeValueAsString(studentDetail)));

      verify(service, times(1)).searchIdStudentInfo(id);
    }

    @ParameterizedTest
    @MethodSource("studentData")
    void 受講生情報の更新が実行できてレスポンスが返ってくること(
        int id, String name, String furigana, String email, String area, int age, String gender
    ) throws Exception {
      Student student = Student.builder()
          .id(id)
          .name(name)
          .furigana(furigana)
          .email(email)
          .area(area)
          .age(age)
          .gender(gender)
          .build();

      StudentDetail studentDetail = StudentDetail.builder()
          .student(student).build();

      mockMvc.perform(MockMvcRequestBuilders.put("/student/{id}", id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(studentDetail)))
          .andExpect(status().isOk())
          .andExpect(content().string("更新処理完了"));

      verify(service, times(1)).updateStudent(any(Student.class));
    }

    @ParameterizedTest
    @MethodSource("courseData")
    void コース情報ID検索が実行できてコース情報が返ってくること(
        int courseId, int studentId, String course) throws Exception {
      StudentCourses courseInfo = StudentCourses.builder()
          .courseId(courseId)
          .studentId(studentId)
          .course(course)
          .build();

      when(service.searchCourses(1)).thenReturn(courseInfo);

      mockMvc.perform(MockMvcRequestBuilders.get("/student/course/{id}", courseId))
          .andExpect(status().isOk())
          .andExpect(content().json(new ObjectMapper().writeValueAsString(courseInfo)));

      verify(service, times(1)).searchCourses(courseId);
    }

    @ParameterizedTest
    @MethodSource("courseData")
    void コース情報の更新が実行できてレスポンスが返ってくること(
        int courseId, int studentId, String course) throws Exception {
      StudentCourses courseInfo = StudentCourses.builder()
          .courseId(courseId)
          .studentId(studentId)
          .course(course)
          .build();

      mockMvc.perform(MockMvcRequestBuilders.put("/student/course/{id}", courseId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(courseInfo)))
          .andExpect(status().isOk())
          .andExpect(content().string("更新処理完了"));

      verify(service, times(1)).updateStudentCourse(any(StudentCourses.class));
    }
  }


  @Nested
  class エラー動作テスト {

    static Stream<Arguments> studentData() {
      return Stream.of(
          Arguments.of(1, "佐藤太郎", "さとうたろう", "taro@gmail.com", "東京都", 22, "男")
      );
    }

    static Stream<Arguments> courseData() {
      return Stream.of(
          Arguments.of(1, 1, "Java")
      );
    }

    @ParameterizedTest
    @MethodSource("studentData")
    void 受講生情報の更新で誤ったidを入力したらエラーメッセージが返ってくること(
        int id, String name, String furigana, String email, String area, int age, String gender
    ) throws Exception {
      int errId = 2;

      Student student = Student.builder()
          .id(id)
          .name(name)
          .furigana(furigana)
          .email(email)
          .area(area)
          .age(age)
          .gender(gender)
          .build();

      StudentDetail studentDetail = StudentDetail.builder()
          .student(student).build();

      mockMvc.perform(MockMvcRequestBuilders.put("/student/{id}", errId)
              .contentType(MediaType.APPLICATION_JSON) // JSONを送信
              .content(new ObjectMapper().writeValueAsString(studentDetail))) // JSONデータを追加
          .andExpect(status().isOk())
          .andExpect(content().string("受講生IDを正しく指定してください"));

      verify(service, times(0)).updateStudent(any(Student.class));
    }

    @ParameterizedTest
    @MethodSource("courseData")
    void コース情報の更新で誤ったidを入力したらエラーメッセージが返ってくること(
        int courseId, int studentId, String course
    ) throws Exception {
      int errId = 2;

      StudentCourses courseInfo = StudentCourses.builder()
          .courseId(courseId)
          .studentId(studentId)
          .course(course)
          .build();

      mockMvc.perform(MockMvcRequestBuilders.put("/student/course/{id}", errId)
              .contentType(MediaType.APPLICATION_JSON) // JSONを送信
              .content(new ObjectMapper().writeValueAsString(courseInfo))) // JSONデータを追加
          .andExpect(status().isOk())
          .andExpect(content().string("コースIDを正しく指定してください"));

      verify(service, times(0)).updateStudentCourse(any(StudentCourses.class));
    }

    @ParameterizedTest
    @MethodSource("studentData")
    void 受講生情報の名前にエラーがありBadRequestが返ってくること(
        int id, String name, String furigana, String email, String area, int age, String gender
    ) throws Exception {
      Student student = Student.builder()
          .id(id)
          .furigana(furigana)
          .email(email)
          .area(area)
          .age(age)
          .gender(gender)
          .build();

      StudentDetail studentDetail = StudentDetail.builder()
          .student(student).build();

      mockMvc.perform(MockMvcRequestBuilders.post("/student")
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(studentDetail)))
          .andExpect(status().isBadRequest());

      verify(service, times(0)).registerStudent(any(Student.class));
    }

    @Test
    void 受講生ID検索で誤ったIDを渡してエラーメッセージが返ってくること() throws Exception {
      int errId = 0;

      mockMvc.perform(MockMvcRequestBuilders.get("/student/{id}", errId))
          .andExpect(status().isBadRequest())
          .andExpect(content().string("リクエストが不正です"));

      verify(service, times(0)).searchIdStudentInfo(errId);
    }

    @Test
    void コース情報ID検索で誤ったIDを渡してエラーメッセージが返ってくること() throws Exception {
      int errId = 0;

      mockMvc.perform(MockMvcRequestBuilders.get("/student/course/{id}", errId))
          .andExpect(status().isBadRequest())
          .andExpect(content().string("リクエストが不正です"));

      verify(service, times(0)).searchCourses(errId);
    }
  }
}
