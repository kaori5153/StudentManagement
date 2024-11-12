package raisetech.student.management.service;

import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    List<Student> searchStudentResult = repository.searchStudent();
    Iterator<Student> iterator = searchStudentResult.iterator();
    while (iterator.hasNext()) {
      Student student = iterator.next();
      if (student.getAge() < 30 || student.getAge() >= 40) {
        iterator.remove();
      }
    }
    return searchStudentResult;
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    String searchCourse = "Java";
    List<StudentsCourses> searchCourseResult = repository.searchStudentsCourses();
    Iterator<StudentsCourses> iterator = searchCourseResult.iterator();
    while (iterator.hasNext()) {
      StudentsCourses studentsCourses = iterator.next();
      if (searchCourse.equals(studentsCourses.getCourse())) {
      } else {
        iterator.remove();
      }
    }
    return searchCourseResult;

  }
}
