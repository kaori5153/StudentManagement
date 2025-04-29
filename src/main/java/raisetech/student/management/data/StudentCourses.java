package raisetech.student.management.data;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourses {

  @Min(1)
  @Max(999)
  private int courseId;

  @Min(1)
  @Max(999)
  private int studentId;

  @Size(min = 1, max = 20)
  private String course;

  private LocalDate startDate;
  private LocalDate endDate;

}

