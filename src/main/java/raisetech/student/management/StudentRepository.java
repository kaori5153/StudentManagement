package raisetech.student.management;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchStudent();

  @Select("SELECT * FROM students_courses")
  @Results({
      @Result(property = "crs_id", column = "crs_id"),
      @Result(property = "st_id", column = "st_id"),
      @Result(property = "course", column = "course"),
      @Result(property = "start_date", column = "start_date"),
      @Result(property = "end_date", column = "end_date")
  })
  List<StudentsCourses> searchStudentsCourses();
}
