package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
@AllArgsConstructor
public class Student {

  @Schema(description = "受講生ID", type = "int", example = "1", required = true)
  @Min(1)
  @Max(999)
  private int id;

  @Schema(description = "氏名", type = "String", example = "山田太郎", required = true)
  @Size(min = 2, max = 50)
  private String name;

  @Schema(description = "ふりがな", type = "String", example = "やまだたろう")
  @Size(max = 50)
  private String furigana;

  @Schema(description = "ニックネーム", type = "String", example = "たろう")
  @Size(max = 20)
  private String nickname;

  @Schema(description = "メールアドレス", type = "String", example = "abc@gmail.com", required = true)
  @Email
  @Size(min = 5, max = 254)
  private String email;

  @Schema(description = "居住地", type = "String", example = "東京都千代田区")
  @Size(max = 50)
  private String area;

  @Schema(description = "年齢", type = "int", example = "30")
  @Max(150)
  private int age;

  @Schema(description = "性別", type = "String", example = "男")
  @Size(max = 10)
  private String gender;

  @Schema(description = "備考", type = "String", example = "休会中")
  @Size(max = 500)
  private String remark;

  @Schema(description = "情報削除フラグ", type = "boolean")
  private boolean deleted;

}
