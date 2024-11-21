package raisetech.student.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@RestController
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
  public List<StudentDetail> getStudentList(@RequestParam String age,String course) {
    List<Student> students = service.searchStudentList(age);
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList(course);
    return converter.convertStudentDetails(students, studentsCourses);
  }

  //  受講コース情報を表示する
  @GetMapping("/studentsCourseList")
  public List<StudentsCourses> getStudentsCourseList(@RequestParam String course) {
    return service.searchStudentsCourseList(course);
  }

}
