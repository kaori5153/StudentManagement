package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

@WebMvcTest(StudentController.class)  //StudentControllerインスタンス生成される
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;  //spring bootが用意してるMockの仕組み

  @MockBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


  @Nested
  class バリデーションテスト {

    @Test
    void 受講生詳細の受講生で入力チェックに異常がないこと() {
      Student student = new Student(1, "佐藤太郎", "さとうたろう", "たろう", "taro@gmail.com",
          "東京都", 22, "男", "", false);

      Set<ConstraintViolation<Student>> violations = validator.validate(student);

      assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void 受講生詳細のコース情報の入力チェックに異常がないこと() {
      StudentCourses course = new StudentCourses(1, 1, "Java", LocalDate.of(2025, 1, 1),
          LocalDate.of(2026, 1, 1));

      Set<ConstraintViolation<StudentCourses>> violations = validator.validate(course);

      assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void 受講生詳細の受講生の入力チェックで名前のnullチェックで掛かること() {
      Student student = new Student(1, null, "さとうたろう", "たろう", "taro@gmail.com",
          "東京都", 22, "男", "", false);

      Set<ConstraintViolation<Student>> violations = validator.validate(student);

      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).extracting("message").containsOnly("名前を入力してください");
    }

    @Test
    void 受講生詳細の受講生の入力チェックで名前の文字数チェックで掛かること() {
      Student student = new Student(1, "a", "さとうたろう", "たろう", "taro@gmail.com",
          "東京都", 22, "男", "", false);

      Set<ConstraintViolation<Student>> violations = validator.validate(student);

      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).extracting("message")
          .containsOnly("2文字以上50文字以下で入力してください");
    }

    @Test
    void 受講生詳細の受講生の入力チェックでメールアドレスのnullチェックで掛かること() {
      Student student = new Student(1, "佐藤太郎", "さとうたろう", "たろう", null,
          "東京都", 22, "男", "", false);

      Set<ConstraintViolation<Student>> violations = validator.validate(student);

      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).extracting("message").containsOnly("メールアドレスを入力してください");
    }

    @Test
    void 受講生詳細のコース情報の入力チェックのコース名のnullチェックで掛かること() {
      StudentCourses course = new StudentCourses(1, 1, null, LocalDate.of(2025, 1, 1),
          LocalDate.of(2026, 1, 1));

      Set<ConstraintViolation<StudentCourses>> violations = validator.validate(course);

      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).extracting("message")
          .containsOnly("コース名を入力してください");
    }

    @Test
    void 受講生詳細のコース情報の入力チェックのコース名の文字数チェックで掛かること() {
      StudentCourses course = new StudentCourses(1, 1, "abcd-abcd-abcd-abcd-a",
          LocalDate.of(2025, 1, 1),
          LocalDate.of(2026, 1, 1));

      Set<ConstraintViolation<StudentCourses>> violations = validator.validate(course);

      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).extracting("message")
          .containsOnly("1文字以上20文字以下で入力してください");
    }
  }

  @Nested
  class 正常動作テスト {

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

    @Test
    void 受講生登録が実行できてレスポンスが返ってくること() throws Exception {
      Student student = Student.builder()
          .id(1)
          .name("佐藤太郎")
          .furigana("さとうたろう")
          .nickname("たろう")
          .email("taro@gmail.com")
          .area("東京都")
          .age(22)
          .gender("男")
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

    @Test
    void 受講生コース情報登録が実行できてレスポンスが返ってくること() throws Exception {
      StudentCourses course = StudentCourses.builder()
          .courseId(1)
          .studentId(1)
          .course("Java")
          .build();

      List<StudentCourses> courses = List.of(course);

      StudentDetail studentDetail = StudentDetail.builder()
          .studentCourses(courses).build();

      mockMvc.perform(MockMvcRequestBuilders.post("/student/course")
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(studentDetail)))
          .andExpect(status().isOk())
          .andExpect(content().string("登録処理完了"));

      verify(service, times(1)).registerCourse(anyList());
    }

    @Test
    void 受講生ID検索が実行できてレスポンスが返ってくること() throws Exception {
      int studentId = 1;

      mockMvc.perform(MockMvcRequestBuilders.get("/student/{id}", studentId))
          .andExpect(status().isOk());

      verify(service, times(1)).searchIdStudentInfo(studentId);
    }

    @Test
    void 受講生情報の更新が実行できてレスポンスが返ってくること() throws Exception {
      Student student = Student.builder()
          .id(1)
          .name("佐藤太郎")
          .furigana("さとうたろう")
          .nickname("たろう")
          .email("taro@gmail.com")
          .area("東京都")
          .age(22)
          .gender("男")
          .build();

      StudentDetail studentDetail = StudentDetail.builder()
          .student(student).build();

      mockMvc.perform(MockMvcRequestBuilders.put("/student/{id}", 1)
              .contentType(MediaType.APPLICATION_JSON) // JSONを送信
              .content(new ObjectMapper().writeValueAsString(studentDetail))) // JSONデータを追加
          .andExpect(status().isOk())
          .andExpect(content().string("更新処理完了"));

      verify(service, times(1)).updateStudent(any(Student.class));
    }

    @Test
    void コース情報ID検索が実行できてレスポンスが返ってくること() throws Exception {
      int courseId = 1;

      mockMvc.perform(MockMvcRequestBuilders.get("/student/course/{id}", courseId))
          .andExpect(status().isOk());

      verify(service, times(1)).searchCourses(courseId);
    }

    @Test
    void コース情報の更新が実行できてレスポンスが返ってくること() throws Exception {
      StudentCourses course = StudentCourses.builder()
          .courseId(1)
          .studentId(1)
          .course("Java")
          .build();

      mockMvc.perform(MockMvcRequestBuilders.put("/student/course/{id}", 1)
              .contentType(MediaType.APPLICATION_JSON) // JSONを送信
              .content(new ObjectMapper().writeValueAsString(course))) // JSONデータを追加
          .andExpect(status().isOk())
          .andExpect(content().string("更新処理完了"));

      verify(service, times(1)).updateStudentCourse(any(StudentCourses.class));
    }
  }


  @Nested
  class エラー動作テスト {

    @Test
    void 受講生情報の更新で誤ったidを入力したらエラーメッセージが返ってくること() throws Exception {
      Student student = Student.builder()
          .id(1)
          .name("佐藤太郎")
          .furigana("さとうたろう")
          .nickname("たろう")
          .email("taro@gmail.com")
          .area("東京都")
          .age(22)
          .gender("男")
          .build();

      StudentDetail studentDetail = StudentDetail.builder()
          .student(student).build();

      mockMvc.perform(MockMvcRequestBuilders.put("/student/{id}", 2)
              .contentType(MediaType.APPLICATION_JSON) // JSONを送信
              .content(new ObjectMapper().writeValueAsString(studentDetail))) // JSONデータを追加
          .andExpect(status().isOk())
          .andExpect(content().string("受講生IDを正しく指定してください"));

      verify(service, times(0)).updateStudent(any(Student.class));
    }

    @Test
    void コース情報の更新で誤ったidを入力したらエラーメッセージが返ってくること() throws Exception {
      StudentCourses course = StudentCourses.builder()
          .courseId(1)
          .studentId(1)
          .course("Java")
          .build();

      mockMvc.perform(MockMvcRequestBuilders.put("/student/course/{id}", 2)
              .contentType(MediaType.APPLICATION_JSON) // JSONを送信
              .content(new ObjectMapper().writeValueAsString(course))) // JSONデータを追加
          .andExpect(status().isOk())
          .andExpect(content().string("コースIDを正しく指定してください"));

      verify(service, times(0)).updateStudentCourse(any(StudentCourses.class));
    }

    @Test
    void 受講生情報の名前にエラーがありBadRequestが返ってくること() throws Exception {
      Student student = Student.builder()
          .id(1)
          .furigana("さとうたろう")
          .nickname("たろう")
          .email("taro@gmail.com")
          .area("東京都")
          .age(22)
          .gender("男")
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
      int studentId = 0;

      mockMvc.perform(MockMvcRequestBuilders.get("/student/{id}", studentId))
          .andExpect(status().isBadRequest())
          .andExpect(content().string("リクエストが不正です"));

      verify(service, times(0)).searchIdStudentInfo(studentId);
    }

    @Test
    void コース情報ID検索で誤ったIDを渡してエラーメッセージが返ってくること() throws Exception {
      int courseId = 0;

      mockMvc.perform(MockMvcRequestBuilders.get("/student/course/{id}", courseId))
          .andExpect(status().isBadRequest())
          .andExpect(content().string("リクエストが不正です"));

      verify(service, times(0)).searchCourses(courseId);
    }
  }
}
