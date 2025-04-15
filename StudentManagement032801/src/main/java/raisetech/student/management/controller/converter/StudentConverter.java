package raisetech.student.management.controller.converter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;

/**
 * 受講生詳細を受講生情報とコース情報、もしくはその逆の変換を行うコンバーター
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づくコース情報をマッピングする。
   * コース情報は受講生に対して複数存在するのでループを回して受講生情報詳細情報を組み立てる。
   * 論理削除がtrueでなければ、受講生情報をセットする。
   * @param students 受講生一覧
   * @param studentsCourses コース情報のリスト
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentCourses> studentsCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    for (Student student : students) {
      if(!student.isDeleted()) {
        StudentDetail studentDetail = new StudentDetail();
        studentDetail.setStudent(student);

        List<StudentCourses> convertStudentCourses = new ArrayList<>();
        for (StudentCourses StudentCourse : studentsCourses) {
          if (student.getId() == StudentCourse.getStudentId()) {
            convertStudentCourses.add(StudentCourse);
          }
        }
        studentDetail.setStudentCourses(convertStudentCourses);
        studentDetails.add(studentDetail);
      }
    }
    return studentDetails;
  }
}
