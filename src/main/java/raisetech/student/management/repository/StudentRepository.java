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
      @Result(property = "deleted", column = "deleted")
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
   */
  @Insert("INSERT INTO students ("
      + " name, "
      + "furigana, "
      + "nickname, "
      + "email, "
      + "area, "
      + "age, "
      + "gender) "
      + "VALUES "
      + "(#{name}, "
      + "#{furigana}, "
      + "#{nickname}, "
      + "#{email}, "
      + "#{area}, "
      + "#{age}, "
      + "#{gender})")
  void registerNewStudent(Student newStudent);

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

  @Update("UPDATE students SET name = #{name},"
      + " furigana = #{furigana},"
      + " nickname = #{nickname},"
      + " email = #{email},"
      + " area = #{area},"
      + " age = #{age},"
      + " gender = #{gender},"
      + " remark = #{remark},"
      + " deleted = #{deleted}"
      + " WHERE id = #{id}")
  void updateStudentInfo(Student updateStudent);

  @Update("UPDATE students_courses SET start_date = #{startDate},"
      + " end_date = #{endDate}"
      + " WHERE course_id = #{courseId}")
  void updateStudentCourseInfo(StudentsCourses updatedCourses);
}
