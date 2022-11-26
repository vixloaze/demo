package com.example.demo.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity // 자바 클래스를 엔티티로 지정
@Table (name = "Todo") // 테이블 이름 지정 (이 엔티티는 DB의 Todo 테이블에 Mapping)
public class TodoEntity {
    @Id // 기본 키
    @GeneratedValue(generator = "system-uuid") // generator 로 어떻게 ID를 생성할 지 지정이 가능
    @GenericGenerator(name = "system-uuid", strategy = "uuid") // 기본 generator가 아닌 나만의 generator를 사용하고 싶을 때 사용
    private String id; // 이 오브젝트의 Id
    private String userId; // 이 오브젝트를 생성한 유저의 아이디
    private String title; // Todo 타이틀 예) 운동 하기
    private boolean done; // true - todo를 완료한 경우 (checked)
}
