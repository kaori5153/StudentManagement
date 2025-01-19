package raisetech.student.management.data;


import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private int crs_id;
  private int st_id;
  private String course;
  private LocalDate start_date;
  private LocalDate end_date;

}

