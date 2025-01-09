package raisetech.student.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    model.addAttribute("studentList",converter.convertStudentDetails(students, studentsCourses));
    return "studentList";
  }

  //  受講コース情報を表示する
  @GetMapping("/studentsCourseList")
  public String getStudentsCourseList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();
    model.addAttribute("studentCourseList", studentsCourses);
    return "studentCourseList";
  }

  //  新しい生徒情報の登録
  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "resisterStudent";
  }

  @PostMapping("/resisterStudent")
  public String resisterStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "resisterStudent";
    }
    service.resisterStudent(studentDetail.getStudent());
    System.out.println(studentDetail.getStudent().getName());
    return "redirect:/newStudentCourse";
  }

  //  コース情報の登録
  @GetMapping("/newStudentCourse")
  public String newStudentCourse(@ModelAttribute("studentDetail") StudentDetail studentDetail,
      Model model) {
    model.addAttribute("studentDetail", studentDetail);
    return "resisterCourse";
  }

  @PostMapping("/resisterCourse")
  public String resisterCourse(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      result.getAllErrors().forEach(error -> System.out.println(error.toString()));
      return "resisterStudent";
    }
    service.resisterCourse(studentDetail.getStudentsCourses());
    return "redirect:/studentList";
  }
}
