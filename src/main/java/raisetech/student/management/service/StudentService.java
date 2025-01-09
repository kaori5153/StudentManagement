package raisetech.student.management.service;

import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;
  private String newStudentId;


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

  public void resisterStudent(Student newStudent) {
//    次のidを探す
    int lastStudentId = 1;
    List<Student> searchStudentId = repository.searchStudent();
    Iterator<Student> iterator = searchStudentId.iterator();

    while (iterator.hasNext()) {
      Student students = iterator.next();
      lastStudentId++;
    }
    newStudentId = String.format("%03d", lastStudentId++);
    repository.resisterNewStudent(newStudentId, newStudent.getName(), newStudent.getFurigana(),
        newStudent.getNickname(), newStudent.getEmail(), newStudent.getArea(), newStudent.getAge(),
        newStudent.getGender());
  }

  public void resisterCourse(List<StudentsCourses> newStudentCourse) {
//    次のidを探す
    int lastStudentsCourseId = 1;
    List<StudentsCourses> searchStudentsCourseId = repository.searchStudentsCourses();
    Iterator<StudentsCourses> iterator = searchStudentsCourseId.iterator();

    while (iterator.hasNext()) {
      StudentsCourses studentsCourses = iterator.next();
      lastStudentsCourseId++;
    }

    repository.resisterNewCourse(String.format("%03d", lastStudentsCourseId++), newStudentId,
        newStudentCourse.getLast().getCourse());
  }
}
