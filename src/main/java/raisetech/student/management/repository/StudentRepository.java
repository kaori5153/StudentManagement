package raisetech.student.management.repository;

import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;

/**
 * 受講生情報を扱うリポジトリ。
 * <p>
 * 全体検索や単一条件での検索、コース情報の検索が行えるクラスです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 生徒情報を全件検索します。
   *
   * @return 全件検索した受講生情報の一覧
   */
  @Select("SELECT * FROM students")
  List<Student> searchStudent();

  /**
   * 受講生のコース情報を全件検索します。
   *
   * @return 全件検索した受講コース情報の一覧
   */
  @Select("SELECT * FROM students_courses")
  @Results({
      @Result(property = "courseId", column = "course_id"),
      @Result(property = "studentId", column = "student_id"),
      @Result(property = "course", column = "course"),
      @Result(property = "startDate", column = "start_date"),
      @Result(property = "endDate", column = "end_date"),
      @Result(property = "remark", column = "remark"),
      @Result(property = "isDeleted", column = "isDeleted")
  })
  List<StudentsCourses> searchStudentsCourses();

  /**
   * 新規登録した生徒idを検索します。
   *
   * @return 新規登録した生徒id
   */
  @Select("SELECT max(id) FROM students")
  int newStudentId();

  /**
   * 新規受講生の情報を登録します。
   *
   * @param newStudentId：新規受講生のID
   * @param newStudentName：新規受講生の名前
   * @param newStudentFurigana：新規受講生の名前のフリガナ
   * @param newStudentNickName：新規受講生のニックネーム
   * @param newStudentEmail：新規受講生のメールアドレス
   * @param newStudentArea：新規受講生の住んでいる地域
   * @param newStudentAge：新規受講生の年齢
   * @param newStudentGender：新規受講生の性別
   */
  @Insert("INSERT INTO students (id,"
      + " name, "
      + "furigana, "
      + "nickname, "
      + "email, "
      + "area, "
      + "age, "
      + "gender) "
      + "VALUES "
      + "(#{newStudentId}, "
      + "#{newStudentName}, "
      + "#{newStudentFurigana}, "
      + "#{newStudentNickName}, "
      + "#{newStudentEmail}, "
      + "#{newStudentArea}, "
      + "#{newStudentAge}, "
      + "#{newStudentGender})")
  void registerNewStudent(int newStudentId, String newStudentName, String newStudentFurigana,
      String newStudentNickName, String newStudentEmail, String newStudentArea, int newStudentAge,
      String newStudentGender);

  @Insert("INSERT INTO students_courses (course_id,"
      + "student_id, "
      + "course) "
      + "VALUES "
      + "(#{newStudentCourseId}, "
      + "#{newStudentId}, "
      + "#{newStudentCourse})")
  void registerNewCourse(int newStudentCourseId, int newStudentId, String newStudentCourse);

  @Select("SELECT * FROM students WHERE id = #{searchId}")
  List<Student> searchIdStudent(int searchId);

  @Select("SELECT * FROM students_courses WHERE student_id = #{searchId}")
  List<StudentsCourses> searchIdStudentCourses(int searchId);

  @Select("SELECT * FROM students_courses WHERE course_id = #{searchId}")
  StudentsCourses searchCourse(int searchId);

  @Update("UPDATE students SET name = #{updateStudentName},"
      + " furigana = #{updateStudentFurigana},"
      + " nickname = #{updateStudentNickName},"
      + " email = #{updateStudentEmail},"
      + " area = #{updateStudentArea},"
      + " age = #{updateStudentAge},"
      + " gender = #{updateStudentGender},"
      + " remark = #{updateStudentRemark}"
      + " WHERE id = #{searchStudentId}")
  void updateStudentInfo(int searchStudentId, String updateStudentName,
      String updateStudentFurigana,
      String updateStudentNickName, String updateStudentEmail, String updateStudentArea,
      int updateStudentAge,
      String updateStudentGender, String updateStudentRemark);

  @Update("UPDATE students_courses SET start_date = #{updateStartDate},"
      + " end_date = #{updateEndDate}"
      + " WHERE course_id = #{searchCourseId}")
  void updateStudentCourseInfo(int searchCourseId, LocalDate updateStartDate,
      LocalDate updateEndDate);
}
