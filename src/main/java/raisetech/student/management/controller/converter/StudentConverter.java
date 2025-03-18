package raisetech.student.management.controller.converter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;

@Component
public class StudentConverter {

  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourses> studentsCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    for (Student student : students) {
      if(!student.isDeleted()) {
        StudentDetail studentDetail = new StudentDetail();
        studentDetail.setStudent(student);

        List<StudentsCourses> convertStudentCourses = new ArrayList<>();
        for (StudentsCourses StudentCourse : studentsCourses) {
          if (student.getId() == StudentCourse.getStudentId()) {
            convertStudentCourses.add(StudentCourse);
          }
        }
        studentDetail.setStudentsCourses(convertStudentCourses);
        studentDetails.add(studentDetail);
      }
    }
    return studentDetails;
  }
}
