package raisetech.student.management;


import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private String crs_id;
  private String st_id;
  private String course;
  private LocalDate start_date;
  private LocalDate end_date;

}

