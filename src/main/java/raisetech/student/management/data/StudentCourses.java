package raisetech.student.management.data;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourses {

  @Schema(description = "コースID", type = "int", example = "1", required = true)
  @Min(1)
  @Max(999)
  private int courseId;

  @Schema(description = "受講生ID", type = "int", example = "1", required = true)
  @Min(1)
  @Max(999)
  private int studentId;

  @Schema(description = "コース", type = "String", example = "Java", required = true)
  @Size(min = 1, max = 20)
  private String course;

  @Schema(description = "学習開始日", type = "LocalDate", example = "2023-04-01")
  private LocalDate startDate;

  @Schema(description = "終了日時", type = "LocalDate", example = "2023-12-31")
  private LocalDate endDate;

}

