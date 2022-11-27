package com.example.demo.controller;


import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {
    @Autowired
    private TodoService service;
    @GetMapping("/test")
    public ResponseEntity<?> testTodo() { //TodoService.java 를 수행하기 위한 클래스
        String str = service.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user"; // temporary user id.

            // TodoEntity로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);
            // id를 null로 초기화 생성 당시에는 id가 없어야 하기 때문
            entity.setId(null);
            // 임시 유저 아이디 설정. 이 부분은 4장 인증과 인가에서 수행할 예정
            // 한 유저만 로그인 없이 사용 가능한 애플리케이션인 셈.
            entity.setUserId(temporaryUserId);
            // 서비스를 이용해 Todo 엔티티 생성
            List<TodoEntity> entities = service.create(entity);
            // 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            // 변환된 TodoDto 리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            // ResponseDTO 리턴
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            // 에러가 났을 시 dto 대신 error 에 메시지를 넣어 리턴함
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        String temporaryUserId = "temporary-user"; // temporary user id.
        // 서비스 메서드의 retrieve 메서드를 이용해 Todo 리스트를 가져옴
        List<TodoEntity> entities = service.retrieve(temporaryUserId);
        // 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO :: new).collect(Collectors.toList());
        // 변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        // ResponseDTO 리턴
        return ResponseEntity.ok().body(response);
    }
}
