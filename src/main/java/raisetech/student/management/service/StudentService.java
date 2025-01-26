package raisetech.student.management.service;

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
    return searchStudentResult;
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    List<StudentsCourses> searchCourseResult = repository.searchStudentsCourses();
    return searchCourseResult;
  }

  public void registerStudent(Student newStudent) {
    repository.registerNewStudent(0, newStudent.getName(), newStudent.getFurigana(),
        newStudent.getNickname(), newStudent.getEmail(), newStudent.getArea(), newStudent.getAge(),
        newStudent.getGender());
  }

  public void registerCourse(List<StudentsCourses> newStudentCourse) {
    repository.registerNewCourse(0, repository.newStudentId(),
        newStudentCourse.getLast().getCourse());
  }
}
