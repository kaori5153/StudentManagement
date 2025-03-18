package raisetech.student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

  public List<Student> searchIdStudentInfo(int studentId) {
    List<Student> searchIdStudent = repository.searchIdStudent(studentId);
    return searchIdStudent;
  }

  public List<StudentsCourses> searchIdStudentCourses(int studentId) {
    List<StudentsCourses> searchIdStudentCourses = repository.searchIdStudentCourses(studentId);
    return searchIdStudentCourses;
  }

  public StudentsCourses searchCourses(int courseId) {
    StudentsCourses searchStudentCourses = repository.searchCourse(courseId);
    return searchStudentCourses;
  }

  @Transactional
  public void registerStudent(Student newStudent) {
    repository.registerNewStudent(0, newStudent.getName(), newStudent.getFurigana(),
        newStudent.getNickname(), newStudent.getEmail(), newStudent.getArea(), newStudent.getAge(),
        newStudent.getGender());
  }

  @Transactional
  public void registerCourse(List<StudentsCourses> newStudentCourse) {
    repository.registerNewCourse(0, repository.newStudentId(),
        newStudentCourse.getLast().getCourse());
  }

  @Transactional
  public void updateStudent(Student updateStudent) {
    repository.updateStudentInfo(updateStudent.getId(), updateStudent.getName(),
        updateStudent.getFurigana(), updateStudent.getNickname(), updateStudent.getEmail(),
        updateStudent.getArea(), updateStudent.getAge(), updateStudent.getGender(),
        updateStudent.getRemark(),updateStudent.isDeleted());
  }

  @Transactional
  public void updateStudentCourse(StudentsCourses updatedCourses) {
    repository.updateStudentCourseInfo(updatedCourses.getCourseId(), updatedCourses.getStartDate(),
        updatedCourses.getEndDate());
  }
}
