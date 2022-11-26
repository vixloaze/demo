package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j // 로그 어노테이션
@Service // 서비스 레이어임을 알리는 어노테이션
public class TodoService { // todo 프로젝트 로직 구현 클래스
    @Autowired
    private TodoRepository repository;
    public String testService() {
        // TodoEntity 생성
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
        // TodoEntity 저장
        repository.save(entity);
        // TodoEntity 검색
        TodoEntity saveEntity = repository.findById(entity.getId()).get();
        return saveEntity.getTitle();
    }
}
