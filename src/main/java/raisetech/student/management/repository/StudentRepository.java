package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
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
      @Result(property = "crs_id", column = "crs_id"),
      @Result(property = "st_id", column = "st_id"),
      @Result(property = "course", column = "course"),
      @Result(property = "start_date", column = "start_date"),
      @Result(property = "end_date", column = "end_date"),
      @Result(property = "remark", column = "remark"),
      @Result(property = "isDeleted", column = "isDeleted")
  })
  List<StudentsCourses> searchStudentsCourses();
}
