package raisetech.student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
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

  /**
   * 受講生情報とコース情報を検索して紐づけを行う。
   * @return 受講生詳細
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> searchStudentResult = repository.searchStudent();
    List<StudentCourses> searchCourseResult = repository.searchStudentsCourses();
    return converter.convertStudentDetails(searchStudentResult, searchCourseResult);
  }

  /**
   * コース情報を検索する。
   * @return コース情報リスト
   */
  public List<StudentCourses> searchStudentsCourseList() {
    List<StudentCourses> searchCourseResult = repository.searchStudentsCourses();
    return searchCourseResult;
  }

  /**
   * 受講生IDに対して受講生情報の検索を行う
   * @param studentId 受講生ID
   * @return 受講生情報
   */
  public StudentDetail searchIdStudentInfo(int studentId) {
    Student searchIdStudent = repository.searchIdStudent(studentId);
    List<StudentCourses> searchIdStudentCourses = repository.searchIdStudentCourses(studentId);
    return new StudentDetail(searchIdStudent, searchIdStudentCourses);
  }

  /**
   * コースIDに対してコース情報の検索を行う
   * @param courseId コースID
   * @return コース情報
   */
  public StudentCourses searchCourses(int courseId) {
    StudentCourses searchStudentCourses = repository.searchIdCourse(courseId);
    return searchStudentCourses;
  }

  /**
   * 受講生情報を登録する。
   * @param newStudent 新規登録する受講生の情報
   */
  @Transactional
  public void registerStudent(Student newStudent) {
    repository.registerNewStudent(newStudent);
  }

  /**
   * コース情報を登録する。
   * @param newStudentCourse 新規登録するコースの情報
   */
  @Transactional
  public void registerCourse(List<StudentCourses> newStudentCourse) {
    repository.registerNewCourse( repository.newStudentId(),
        newStudentCourse.getLast().getCourse());
  }

  /**
   * 受講生の情報を更新する。
   * @param updateStudent 更新する受講生情報
   */
  @Transactional
  public void updateStudent(Student updateStudent) {
    repository.updateStudentInfo(updateStudent);
  }

  /**
   * コース情報を更新する。
   * @param updatedCourse 更新するコース情報
   */
  @Transactional
  public void updateStudentCourse(StudentCourses updatedCourse) {
    repository.updateStudentCourseInfo(updatedCourse);
  }
}
