package raisetech.student.management;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

  @Autowired
  private StudentRepository repository;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  //	生徒情報を表示する
  @GetMapping("/studentList")
  public List<Student> getStudentList() {
    return repository.searchStudent();
  }

  //  受講コース情報を表示する
  @GetMapping("/studentCourseList")
  public List<Course> getStudentCourseList() {
    return repository.searchCourse();
  }
}