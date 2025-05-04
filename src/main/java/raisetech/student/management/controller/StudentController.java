package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.TestException;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新など行うREST APIとして実行されるController
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;


  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生情報を表示する
   *
   * @return 受講生情報一覧リスト
   */
  @Operation(summary = "受講生情報一覧", description = "受講生の一覧を検索します")
  @GetMapping("/students")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * コース情報一覧を表示する
   *
   * @return コース情報一覧リスト
   */
  @Operation(summary = "コース情報一覧", description = "コース情報一覧を表示します")
  @GetMapping("/students/courses")
  public List<StudentCourses> getStudentsCourseList() {
    return service.searchStudentsCourseList();
  }

  /**
   * 新規受講生情報の登録を行う
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生登録", description = "受講生を登録します")
  @PostMapping("/student")
  public ResponseEntity<String> registerStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.registerStudent(studentDetail.getStudent());
    return ResponseEntity.ok("登録処理完了");
  }

  /**
   * 受講生コース情報の登録を行う
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "コース情報登録", description = "コース情報を登録します")
  @PostMapping("/student/course")
  public ResponseEntity<String> registerCourse(@RequestBody @Valid
  StudentDetail studentDetail) {
    service.registerCourse(studentDetail.getStudentCourses());
    return ResponseEntity.ok("登録処理完了");
  }

  /**
   * 指定した受講生IDの受講生詳細情報を表示する
   *
   * @param studentId 表示したい受講生のID
   * @return 受講生詳細情報
   */
  @Operation(summary = "受講生ID検索", description = "IDで受講生を検索します")
  @GetMapping("/student/{id}")
  public StudentDetail searchStudent(
      @Parameter(description = "受講生ID", required = true, example = "1")
      @PathVariable("id") @Min(1) @Max(999) int studentId) {
    return service.searchIdStudentInfo(studentId);
  }

  /**
   * 指定した受講生IDの登録情報を更新する。 キャンセルフラグの更新も論理削除で行う。
   *
   * @param studentId      受講生ID
   * @param updatedStudent 更新する受講生
   * @return 実行結果
   */
  @Operation(summary = "受講生情報更新", description = "受講生の情報を更新します")
  @PutMapping("/student/{id}")
  public ResponseEntity<String> updateStudentInformation(
      @Parameter(description = "受講生ID", required = true, example = "1")
      @PathVariable("id") @Min(1) @Max(999) int studentId,
      @RequestBody @Valid StudentDetail updatedStudent) {
    String message;
    if (studentId == updatedStudent.getStudent().getId()) {
      service.updateStudent(updatedStudent.getStudent());
      message = "更新処理完了";
    } else {
      message = "受講生IDを正しく指定してください";
    }

    return ResponseEntity.ok(message);
  }

  /**
   * 指定したコースIDの情報を表示する
   *
   * @param courseId コースID
   * @return コース情報
   */
  @Operation(summary = "コースID検索", description = "コースIDでコース情報を検索します")
  @GetMapping("/student/course/{id}")
  public StudentCourses searchStudentCourse(
      @Parameter(description = "コースID", required = true, example = "1")
      @PathVariable("id") @Min(1) @Max(999) int courseId) {
    return service.searchCourses(courseId);
  }

  /**
   * 指定したコースIDの情報を更新する。開始日と終了日を更新できる。
   *
   * @param courseId             更新するコースID
   * @param updatedStudentCourse 更新するコース情報
   * @return 実行結果
   */
  @Operation(summary = "コース情報更新", description = "コース情報を更新します")
  @PutMapping("/student/course/{id}")
  public ResponseEntity<String> updateStudentCourseInformation(
      @Parameter(description = "コースID", required = true, example = "1")
      @PathVariable("id") @Min(1) @Max(999) int courseId,
      @RequestBody @Valid StudentCourses updatedStudentCourse) {
    String message;
    if (courseId == updatedStudentCourse.getCourseId()) {
      service.updateStudentCourse(updatedStudentCourse);
      message = "更新処理完了";
    } else {
      message = "コースIDを正しく指定してください";
    }
    return ResponseEntity.ok(message);
  }

  /**
   * コントローラーの例外処理の確認を行う。
   *
   * @return 例外発生時メッセージ
   */
  @Operation(summary = "例外処理確認", description = "例外処理の確認を行います")
  @GetMapping("/exception")
  public List<StudentDetail> getException() throws TestException {
    throw new TestException("このURLは利用できません。正しいURLはexceptionsです。");
  }

}
