package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor

public class ResponseDTO<T> { // HTTP 응답으로 사용할 DTO
    private String error;
    private List<T> data;
}
