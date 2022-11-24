package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TestRequestBodyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test") //리소스 -> api 주소가 /test가 됨 (경로 설정)
public class TestController {
    @GetMapping
    public String testController() {
        return "Hello World"; // Get 메소드로 요청했을 때 실행되는 컨트롤러
    }

    @GetMapping("/testGetMapping") // @GetMapping 어노테이션 사용 시 경로를 같이 설정 가능함
    public String testControllerWithPath() {
        return "Hello World testGetMapping";
    }

    @GetMapping("/{id}") // 경로로 들어오는 임의의 숫자 또는 변수 Mapping
    public String testControllerWithPathVariables(@PathVariable(required = false) int id) {
        return "Hello World ID " + id;
    }

    @GetMapping("/testRequestParam") // 경로에 요청 매개변수로 넘어오는 값 Mapping
    public String testControllerRequestParam(@RequestParam(required = false) int id){
        return "Hello World ID " + id;
    }

    @GetMapping("/testRequestBody") // json을 요청 바디를 통해 넘겨줄 때
    public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
        return "Hello World! ID " + testRequestBodyDTO.getId() + " Message : " + testRequestBodyDTO.getMessage();
    }

    @GetMapping("/testResponseBody") // 오브젝트를 리턴해주는 경로
    public ResponseDTO<String> testControllerResponseBody() {
        List<String> list = new ArrayList<>();
        list.add("Hello World! I'm ResponseDTO");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return response;
    }

    @GetMapping("/testResponseEntity")
    public ResponseEntity<?> testControllerResponseEntity() {
        List<String> list = new ArrayList<>();
        list.add("Hello World! I'm ResponseEntity. And you got 400!");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        // http status = 400 으로 설정
        return ResponseEntity.badRequest().body(response);
    }
}
