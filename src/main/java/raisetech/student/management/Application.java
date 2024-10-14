package raisetech.student.management;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

  private String name;
  private Map<String, String> student = new HashMap<>();

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  //	生徒の情報を登録する
  @PostMapping("/studentInfo")
  public void setStudentInfo(String name, String age) {
    student.put(name, age);
  }

  //	全ての生徒情報を表示する
  @GetMapping("/studentAllInfo")
  public String getStudentInfo() {
    return student.toString();
  }

  //	表示したい生徒の名前を入力する
  @PostMapping("/studentName")
  public void setStudentName(String name) {
    this.name = name;
  }

  //	入力した生徒名の年齢を表示する
  @GetMapping("/studentAge")
  public String getStudentAge() {
    return this.name + " : " + student.get(this.name) + "歳";
  }

  //	生徒情報を削除する
  @PostMapping("/removeInfo")
  public void removeStudentInfo(String name) {
    student.remove(name);
  }

}