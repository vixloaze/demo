package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    // create Todo 서비스 구현
    public List<TodoEntity> create(final TodoEntity entity) {
        // Validations 검증 단계 = 엔티티가 유효한지 검사
        validate(entity);

        repository.save(entity); // 엔티티 DB에 저장

        // 로그 남기기
        log.info("Entity name: {} is saved",entity.getId());

        // 저장된 엔티티를 포함하는 새 리스트 리턴
        return repository.findByUserId(entity.getUserId());
    }

    // retrieve Todo 서비스 구현
    public List<TodoEntity> retrieve(final String userId){
        log.info("UserId: {} is find",userId);
        return repository.findByUserId(userId);
    }

    // update Todo 서비스 구현
    public List<TodoEntity> update(final TodoEntity entity) {
        // 저장할 엔티티 유효한지 검사
        validate(entity);

        // 넘겨받은 엔티티 ID를 이용해 TodoEntity 가져온다. 
        // 존재하지 않는 엔티티는 가져올 수 없기 때문에
        final Optional<TodoEntity> original = repository.findById(entity.getId());

        original.ifPresent(todo -> {
            // 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어씌움
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            // DB에 새 값 저장
            repository.save(todo);
        });
        // retrieve Todo 에서 만든 메서드를 이용해 유저의 모든 Todo 리스트 리턴
        return retrieve(entity.getUserId());
    }
    //검증 단계 = 엔티티가 유효한지 검사
    private void validate(final TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }
        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
}
