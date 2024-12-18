package raisetech.student.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
//attributeNameは<tr th:each="studentDetail : ${studentList}">より
    model.addAttribute("studentList",converter.convertStudentDetails(students, studentsCourses));
//templates名
    return "studentList";
  }

  //  受講コース情報を表示する
  @GetMapping("/studentsCourseList")
  public List<StudentsCourses> getStudentsCourseList(@RequestParam String course) {
    return service.searchStudentsCourseList();
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model){
    model.addAttribute("studentDetail", new StudentDetail());
    return "resisterStudent";
  }

  @PostMapping("/resisterStudent")
  public String resisterStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result){
    if(result.hasErrors()){
      return "resisterStudent";
    }
    System.out.println(studentDetail.getStudent().getName() + "さんが新規受講生としてとうろくされました");
    return "redirect:/studentList";
  }

}
