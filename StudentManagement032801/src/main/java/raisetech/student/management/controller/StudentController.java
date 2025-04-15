package raisetech.student.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新など行うREST APIとして実行されるController
 */
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
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * コース情報一覧を表示する
   *
   * @return コース情報一覧リスト
   */
  @GetMapping("/studentsCourseList")
  public List<StudentCourses> getStudentsCourseList() {
    return service.searchStudentsCourseList();
  }
  
  /**
   * 新規受講生情報の登録を行う
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<String> registerStudent(@RequestBody StudentDetail studentDetail) {
    service.registerStudent(studentDetail.getStudent());
    return ResponseEntity.ok("登録処理完了");
  }

  /**
   * 受講生コース情報の登録を行う
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @PostMapping("/registerCourse")
  public ResponseEntity<String> registerCourse(@RequestBody StudentDetail studentDetail) {
    service.registerCourse(studentDetail.getStudentCourses());
    return ResponseEntity.ok("登録処理完了");
  }

  /**
   * 指定した受講生IDの受講生詳細情報を表示する
   *
   * @param studentId 表示したい受講生のID
   * @return 受講生詳細情報
   */
  @GetMapping("/searchStudent/{id}")
  public StudentDetail searchStudent(@PathVariable("id") int studentId) {
    return service.searchIdStudentInfo(studentId);
  }

  /**
   * 指定した受講生IDの登録情報を更新する。 キャンセルフラグの更新も論理削除で行う。
   *
   * @param studentId      受講生ID
   * @param updatedStudent 更新する受講生
   * @return 実行結果
   */
  @PutMapping("/updateStudent/{id}")
  public ResponseEntity<String> updateStudentInformation(@PathVariable("id") int studentId,
      @RequestBody StudentDetail updatedStudent) {
    service.updateStudent(updatedStudent.getStudent());
    return ResponseEntity.ok("更新処理完了");
  }

  /**
   * 指定したコースIDの情報を表示する
   *
   * @param courseId コースID
   * @return コース情報
   */
  @GetMapping("/searchStudentCourse/{id}")
  public StudentCourses searchStudentCourse(@PathVariable("id") int courseId) {
    return service.searchCourses(courseId);
  }

  /**
   * 指定したコースIDの情報を更新する。開始日と終了日を更新できる。
   *
   * @param courseId       更新するコースID
   * @param updatedStudent コース情報を更新する受講生
   * @return 実行結果
   */
  @PutMapping("/updateStudentCourse/{id}")
  public ResponseEntity<String> updateStudentCourseInformation(@PathVariable("id") int courseId,
      @RequestBody StudentDetail updatedStudent) {
    service.updateStudentCourse(updatedStudent.getStudentCourses().get(courseId));
    return ResponseEntity.ok("更新処理完了");
  }
}
