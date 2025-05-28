package raisetech.student.management.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)  //StudentControllerインスタンス生成される
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;  //spring bootが用意してるMockの仕組み

  @MockBean
  private StudentService service;

  @Test
  void 受講生一覧検索が実行できて空のリストが返ってくること() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders.get("/students"))//検証する内容。getの検証
        .andExpect(status().isOk());

      verify(service, times(1)).searchStudentList();
  }

}