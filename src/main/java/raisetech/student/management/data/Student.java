package raisetech.student.management.data;

import lombok.Getter;
import lombok.Setter;

//getとsetの関数自動生成してくれる。レコードオブジェクト。
@Getter
@Setter
public class Student {

  private String id;
  private String name;
  private String furigana;
  private String nickname;
  private String email;
  private String area;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;

}
