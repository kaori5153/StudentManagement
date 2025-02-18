package raisetech.student.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  //	生徒情報を表示する
  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();
    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    return "studentList";
  }

  //  受講コース情報を表示する
  @GetMapping("/studentsCourseList")
  public String getStudentsCourseList(Model model) {
    List<Student> students = service.searchStudentList();
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
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      result.getAllErrors().forEach(error -> System.out.println(error.toString()));
      return "registerStudent";
    }
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
  public String registerCourse(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      result.getAllErrors().forEach(error -> System.out.println(error.toString()));
      return "registerStudent";
    }
    service.registerCourse(studentDetail.getStudentsCourses());
    return "redirect:/studentList";
  }

  //  idから生徒情報の検索
  @GetMapping("/searchStudent/{id}")
  public String searchStudent(@PathVariable("id") int studentId, Model model) {
    List<Student> student = service.searchIdStudentInfo(studentId);
    List<StudentsCourses> studentCourses = service.searchStudentsCourseList();
    StudentDetail updatedStudent = converter.convertStudentDetails(student, studentCourses).get(0);
    model.addAttribute("updatedStudent", updatedStudent);
    model.addAttribute("studentId", studentId);
    model.addAttribute("message", "更新する情報を入力してください");
    return "updateStudent";
  }

  //  生徒情報を更新する
  @PostMapping("/updateStudent/{id}")
  public String updateStudentInformation(@PathVariable("id") int studentId,
      @ModelAttribute("updatedStudent") StudentDetail updatedStudent, BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("updatedStudent", updatedStudent);
      model.addAttribute("studentId", studentId);
      if (result.hasFieldErrors("student.name")) {
        model.addAttribute("errorMessage", "名前にエラーがあります。入力情報を確認してください");
      } else if (result.hasFieldErrors("student.email")) {
        model.addAttribute("errorMessage", "メールアドレスにエラーがあります。入力情報を確認してください");
      } else if (result.hasFieldErrors("student.age")) {
        model.addAttribute("errorMessage", "年齢にエラーがあります。入力情報を確認してください");
      } else {
        model.addAttribute("errorMessage", "入力情報を確認してください");
      }
      return "updateStudent";
    }
    service.updateStudent(updatedStudent.getStudent());
    return "redirect:/studentList";
  }
}
