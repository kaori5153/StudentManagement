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
  private int targetAge;


  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList(String age) {
    switch (age) {
      case "10s" -> targetAge = 10;
      case "20s" -> targetAge = 20;
      case "30s" -> targetAge = 30;
      case "40s" -> targetAge = 40;
      case "50s" -> targetAge = 50;
      case "60s" -> targetAge = 60;
      default -> targetAge = 70;
    }
    List<Student> searchStudentResult = repository.searchStudent();
    Iterator<Student> iterator = searchStudentResult.iterator();
    while (iterator.hasNext()) {
      Student student = iterator.next();
      if (targetAge == 70) {
        if (student.getAge() < targetAge) {
          iterator.remove();
        }
      } else if (student.getAge() < targetAge || student.getAge() >= (targetAge + 10)) {
        iterator.remove();
      }
    }
    return searchStudentResult;
  }

  public List<StudentsCourses> searchStudentsCourseList(String searchCourse) {
    List<StudentsCourses> searchCourseResult = repository.searchStudentsCourses();
    Iterator<StudentsCourses> iterator = searchCourseResult.iterator();
    while (iterator.hasNext()) {
      StudentsCourses studentsCourses = iterator.next();
      if (!searchCourse.equals(studentsCourses.getCourse())) {
        iterator.remove();
      }
    }
    return searchCourseResult;

  }
}
