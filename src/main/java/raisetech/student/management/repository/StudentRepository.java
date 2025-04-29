package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;

/**
 * 受講生情報を扱うリポジトリ。
 * <p>
 * 全体検索や単一条件での検索、コース情報の検索が行えるクラスです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生情報を全件検索します。
   *
   * @return 全件検索した受講生情報の一覧
   */
  List<Student> searchStudent();

  /**
   * 受講生のコース情報を全件検索します。
   *
   * @return 全件検索した受講コース情報の一覧
   */
  List<StudentCourses> searchStudentsCourses();

  /**
   * 新規登録した受講生idを検索します。
   *
   * @return 新規登録した受講生id
   */
  int newStudentId();

  /**
   * 新規受講生の情報を登録します。受講生IDは自動採番する。
   */
  void registerNewStudent(Student newStudent);

  /**
   * 新規コース情報を登録する。コースIDは自動採番する。
   *
   * @param newStudentId     登録するコースと紐づく受講生ID
   * @param newStudentCourse 登録するコース情報
   */
  void registerNewCourse(int newStudentId, String newStudentCourse);

  /**
   * 指定した受講生IDに紐づく受講生情報を検索する。
   *
   * @param searchId 受講生ID
   * @return 受講生情報
   */
  Student searchIdStudent(int searchId);

  /**
   * 受講生IDに紐づくコース情報を検索する
   *
   * @param searchId 受講生ID
   * @return コース情報
   */
  List<StudentCourses> searchIdStudentCourses(int searchId);

  /**
   * コースIDに紐づくコース情報を検索する
   *
   * @param searchId コースID
   * @return コース情報
   */
  StudentCourses searchIdCourse(int searchId);

  /**
   * 受講生情報を更新する。
   *
   * @param updateStudent 更新する受講生情報
   */
  void updateStudentInfo(Student updateStudent);

  /**
   * コース情報のうち受講開始日と終了日を更新する。
   *
   * @param updatedCourse 更新するコース情報
   */
  void updateStudentCourseInfo(StudentCourses updatedCourse);
}
