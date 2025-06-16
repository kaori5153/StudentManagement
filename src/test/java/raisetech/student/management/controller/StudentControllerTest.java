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

    static Stream<Arguments> validStudentData() {
      return Stream.of(
          Arguments.of(Student.builder()
              .id(1).name("佐藤太郎").furigana("さとうたろう")
              .email("taro@gmail.com").area("東京都").age(22).gender("男")
              .build()
          )
      );
    }

    @ParameterizedTest
    @MethodSource("validStudentData")
    void 受講生詳細の受講生で入力チェックに異常がないこと(Student student) throws Exception {
      Set<ConstraintViolation<Student>> violations = validator.validate(student);
      assertThat(violations.size()).isEqualTo(0);
    }

    static Stream<Arguments> invalidStudentData() {
      return Stream.of(
          Arguments.of(Student.builder()
              .id(1).furigana("さとうたろう")
              .email("taro@gmail.com").area("東京都").age(22).gender("男")
              .build(), List.of("名前を入力してください")),
          Arguments.of(Student.builder()
              .id(1).name("a").furigana("さとうたろう")
              .email("taro@gmail.com").area("東京都").age(22).gender("男")
              .build(), List.of("2文字以上50文字以下で入力してください")),
          Arguments.of(Student.builder()
              .id(1).name("佐藤太郎").furigana("さとうたろう")
              .area("東京都").age(22).gender("男")
              .build(), List.of("メールアドレスを入力してください"))
      );
    }

    @ParameterizedTest
    @MethodSource("invalidStudentData")
    void 受講生詳細の受講生で入力チェックに異常メッセージが返ってくること(Student student,
        List<String> expectedMessages) {
      Set<ConstraintViolation<Student>> violations = validator.validate(student);
      assertThat(violations).hasSize(expectedMessages.size());
      assertThat(violations).extracting("message")
          .containsExactlyInAnyOrderElementsOf(expectedMessages);
    }

    static Stream<Arguments> validStudentCourseData() {
      return Stream.of(
          Arguments.of(StudentCourses.builder()
              .courseId(1).studentId(1).course("Java")
              .build()
          )
      );
    }

    @ParameterizedTest
    @MethodSource("validStudentCourseData")
    void 受講生詳細のコース情報の入力チェックに異常がないこと(StudentCourses courseInfo)
        throws Exception {
      Set<ConstraintViolation<StudentCourses>> violations = validator.validate(courseInfo);
      assertThat(violations.size()).isEqualTo(0);
    }

    static Stream<Arguments> invalidStudentCourseData() {
      return Stream.of(
          Arguments.of(StudentCourses.builder()
              .courseId(1).studentId(1)
              .build(), List.of("コース名を入力してください")),
          Arguments.of(StudentCourses.builder()
              .courseId(1).studentId(1).course("abcd-abcd-abcd-abcd-a")
              .build(), List.of("1文字以上20文字以下で入力してください"))
      );
    }

    @ParameterizedTest
    @MethodSource("invalidStudentCourseData")
    void 受講生詳細のコース情報で入力チェックに異常メッセージが返ってくること(
        StudentCourses courseInfo,
        List<String> expectedMessages) {
      Set<ConstraintViolation<StudentCourses>> violations = validator.validate(courseInfo);
      assertThat(violations).hasSize(expectedMessages.size());
      assertThat(violations).extracting("message")
          .containsExactlyInAnyOrderElementsOf(expectedMessages);
    }
  }

  @Nested
  class 正常動作テスト {

    static Stream<Arguments> validStudentDetailData() {
      return Stream.of(
          Arguments.of(StudentDetail.builder()
              .student(Student.builder()
                  .id(1).name("佐藤太郎").furigana("さとうたろう")
                  .email("taro@gmail.com").area("東京都").age(22).gender("男")
                  .build())
              .build()
          )
      );
    }

    static Stream<Arguments> validStudentCourseData() {
      return Stream.of(
          Arguments.of(StudentCourses.builder()
              .courseId(1).studentId(1).course("Java")
              .build()
          )
      );
    }

    @Test
    void 受講生一覧検索が実行できて空のリストが返ってくること() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders.get("/students"))
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
    @MethodSource("validStudentDetailData")
    void 受講生登録が実行できてレスポンスが返ってくること(StudentDetail studentDetail
    ) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders.post("/student")
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(studentDetail)))
          .andExpect(status().isOk())
          .andExpect(content().string("登録処理完了"));

      verify(service, times(1)).registerStudent(any(Student.class));
    }

    @ParameterizedTest
    @MethodSource("validStudentCourseData")
    void 受講生コース情報登録が実行できてレスポンスが返ってくること(
        StudentCourses courseInfo) throws Exception {
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
    @MethodSource("validStudentDetailData")
    void 受講生ID検索が実行できて受講生情報が返ってくること(StudentDetail studentDetail
    ) throws Exception {
      when(service.searchIdStudentInfo(1)).thenReturn(studentDetail);

      mockMvc.perform(
              MockMvcRequestBuilders.get("/student/{id}", studentDetail.getStudent().getId()))
          .andExpect(status().isOk())
          .andExpect(content().json(new ObjectMapper().writeValueAsString(studentDetail)));

      verify(service, times(1)).searchIdStudentInfo(studentDetail.getStudent().getId());
    }

    @ParameterizedTest
    @MethodSource("validStudentDetailData")
    void 受講生情報の更新が実行できてレスポンスが返ってくること(StudentDetail studentDetail
    ) throws Exception {
      mockMvc.perform(
              MockMvcRequestBuilders.put("/student/{id}", studentDetail.getStudent().getId())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(new ObjectMapper().writeValueAsString(studentDetail)))
          .andExpect(status().isOk())
          .andExpect(content().string("更新処理完了"));

      verify(service, times(1)).updateStudent(any(Student.class));
    }

    @ParameterizedTest
    @MethodSource("validStudentCourseData")
    void コース情報ID検索が実行できてコース情報が返ってくること(
        StudentCourses courseInfo) throws Exception {
      when(service.searchCourses(1)).thenReturn(courseInfo);

      mockMvc.perform(MockMvcRequestBuilders.get("/student/course/{id}", courseInfo.getCourseId()))
          .andExpect(status().isOk())
          .andExpect(content().json(new ObjectMapper().writeValueAsString(courseInfo)));

      verify(service, times(1)).searchCourses(courseInfo.getCourseId());
    }

    @ParameterizedTest
    @MethodSource("validStudentCourseData")
    void コース情報の更新が実行できてレスポンスが返ってくること(
        StudentCourses courseInfo) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders.put("/student/course/{id}", courseInfo.getCourseId())
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(courseInfo)))
          .andExpect(status().isOk())
          .andExpect(content().string("更新処理完了"));

      verify(service, times(1)).updateStudentCourse(any(StudentCourses.class));
    }
  }


  @Nested
  class エラー動作テスト {

    static Stream<Arguments> validStudentDetailData() {
      return Stream.of(
          Arguments.of(StudentDetail.builder()
              .student(Student.builder()
                  .id(1).name("佐藤太郎").furigana("さとうたろう")
                  .email("taro@gmail.com").area("東京都").age(22).gender("男")
                  .build())
              .build()
          )
      );
    }

    @ParameterizedTest
    @MethodSource("validStudentDetailData")
    void 受講生情報の更新で誤ったidを入力したらエラーメッセージが返ってくること(
        StudentDetail studentDetail
    ) throws Exception {
      int errId = 2;

      mockMvc.perform(MockMvcRequestBuilders.put("/student/{id}", errId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(studentDetail)))
          .andExpect(status().isOk())
          .andExpect(content().string("受講生IDを正しく指定してください"));

      verify(service, times(0)).updateStudent(any(Student.class));
    }

    static Stream<Arguments> validStudentCourseData() {
      return Stream.of(
          Arguments.of(StudentCourses.builder()
              .courseId(1).studentId(1).course("Java")
              .build()
          )
      );
    }

    @ParameterizedTest
    @MethodSource("validStudentCourseData")
    void コース情報の更新で誤ったidを入力したらエラーメッセージが返ってくること(
        StudentCourses courseInfo
    ) throws Exception {
      int errId = 2;

      mockMvc.perform(MockMvcRequestBuilders.put("/student/course/{id}", errId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(courseInfo)))
          .andExpect(status().isOk())
          .andExpect(content().string("コースIDを正しく指定してください"));

      verify(service, times(0)).updateStudentCourse(any(StudentCourses.class));
    }

    static Stream<Arguments> invalidStudentDetailData() {
      return Stream.of(
          Arguments.of(StudentDetail.builder()
              .student(Student.builder()
                  .id(1).furigana("さとうたろう")
                  .email("taro@gmail.com").area("東京都").age(22).gender("男")
                  .build())
              .build()
          )
      );
    }

    @ParameterizedTest
    @MethodSource("invalidStudentDetailData")
    void 受講生情報の名前にエラーがありBadRequestが返ってくること(StudentDetail studentDetail
    ) throws Exception {
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
