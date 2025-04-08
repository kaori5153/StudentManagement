package raisetech.student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  public List<StudentDetail> searchStudentList() {
    List<Student> searchStudentResult = repository.searchStudent();
    List<StudentsCourses> searchCourseResult = repository.searchStudentsCourses();
    return converter.convertStudentDetails(searchStudentResult, searchCourseResult);
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    List<StudentsCourses> searchCourseResult = repository.searchStudentsCourses();
    return searchCourseResult;
  }

  public StudentDetail searchIdStudentInfo(int studentId) {
    Student searchIdStudent = repository.searchIdStudent(studentId);
    List<StudentsCourses> searchIdStudentCourses = repository.searchIdStudentCourses(studentId);
    return new StudentDetail(searchIdStudent, searchIdStudentCourses);
  }

  public StudentsCourses searchCourses(int courseId) {
    StudentsCourses searchStudentCourses = repository.searchCourse(courseId);
    return searchStudentCourses;
  }

  @Transactional
  public void registerStudent(Student newStudent) {
    repository.registerNewStudent(newStudent);
  }

  @Transactional
  public void registerCourse(List<StudentsCourses> newStudentCourse) {
    repository.registerNewCourse(0, repository.newStudentId(),
        newStudentCourse.getLast().getCourse());
  }

  @Transactional
  public void updateStudent(Student updateStudent) {
    repository.updateStudentInfo(updateStudent);
  }

  @Transactional
  public void updateStudentCourse(StudentsCourses updatedCourses) {
    repository.updateStudentCourseInfo(updatedCourses);
  }
}
