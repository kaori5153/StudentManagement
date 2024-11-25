package raisetech.student.management.data;


import java.time.LocalDate;
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

