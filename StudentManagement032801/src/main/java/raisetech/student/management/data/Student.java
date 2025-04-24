package raisetech.student.management.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

//getとsetの関数自動生成してくれる。レコードオブジェクト。
@Getter
@Setter
public class Student {

  @Min(1)
  @Max(999)
  private int id;

  @Size(min=2, max=50)
  private String name;

  @Size(min=2, max=50)
  private String furigana;

  @Size(max=20)
  private String nickname;

  @Email
  @Size(min=5, max=254)
  private String email;

  @Size(max=50)
  private String area;

  @Max(150)
  private int age;

  @Size(max=10)
  private String gender;

  @Size(max=500)
  private String remark;

  private boolean deleted;

}
