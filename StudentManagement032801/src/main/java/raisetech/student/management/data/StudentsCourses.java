package raisetech.student.management.data;


import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private int courseId;
  private int studentId;
  private String course;
  private LocalDate startDate;
  private LocalDate endDate;

}

