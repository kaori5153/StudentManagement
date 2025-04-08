package raisetech.student.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
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
   * 生徒情報を表示する
   * @param model
   * @return
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList(Model model) {
    return service.searchStudentList();
  }

  //  受講コース情報を表示する
  @GetMapping("/studentsCourseList")
  public String getStudentsCourseList(Model model) {
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();
    model.addAttribute("studentCourseList", studentsCourses);
    return "studentsCourseList";
  }

  //  新しい生徒情報の登録
  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail) {
    service.registerStudent(studentDetail.getStudent());
    return "redirect:/newStudentCourse";
  }

  //  コース情報の登録
  @GetMapping("/newStudentCourse")
  public String newStudentCourse(@ModelAttribute("studentDetail") StudentDetail studentDetail,
      Model model) {
    model.addAttribute("studentDetail", studentDetail);
    return "registerCourse";
  }

  @PostMapping("/registerCourse")
  public String registerCourse(@ModelAttribute StudentDetail studentDetail) {
    service.registerCourse(studentDetail.getStudentsCourses());
    return "redirect:/studentList";
  }

  //  idから生徒情報の検索
  @GetMapping("/searchStudent/{id}")
  public StudentDetail searchStudent(@PathVariable("id") int studentId, Model model) {
    StudentDetail updatedStudent = service.searchIdStudentInfo(studentId);
    model.addAttribute("updatedStudent", updatedStudent);
    model.addAttribute("studentId", studentId);
    model.addAttribute("message", "更新する情報を入力してください");
    return updatedStudent;
  }

  /**
   * 指定した生徒idの登録情報を更新する。
   * @param studentId 生徒ID
   * @param updatedStudent 更新する生徒
   * @return
   */
  @PostMapping("/updateStudent/{id}")
  public ResponseEntity<String> updateStudentInformation(@PathVariable("id") int studentId,
      @RequestBody StudentDetail updatedStudent) {
    service.updateStudent(updatedStudent.getStudent());
    return ResponseEntity.ok("更新処理完了");
  }

  //   idからコース情報の検索
  @GetMapping("/searchStudentCourse/{id}")
  public String searchStudentCourse(@PathVariable("id") int courseId, Model model) {
    StudentsCourses updatedStudentCourse = service.searchCourses(courseId);
    model.addAttribute("updatedStudentCourse", updatedStudentCourse);
    model.addAttribute("courseId", courseId);
    model.addAttribute("message", "開始日または終了日を入力してください");
    return "updateStudentCourse";
  }

  //  コース情報を更新する
  @PostMapping("/updateStudentCourse/{id}")
  public String updateStudentCourseInformation(@PathVariable("id") int courseId,
      @ModelAttribute("updatedStudentCourse") StudentsCourses updatedStudentCourse,
      Model model) {
    service.updateStudentCourse(updatedStudentCourse);
    return "redirect:/studentList";
  }
}
