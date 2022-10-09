package com.example.alcdiary.presentation;

import com.example.alcdiary.application.HelloUseCase;
import com.example.alcdiary.presentation.advice.ApiResponseModel;
import com.example.alcdiary.presentation.response.HelloResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HelloController {
    private final HelloUseCase helloUseCase;

    public HelloController(HelloUseCase helloUseCase) {
        this.helloUseCase = helloUseCase;
    }

    @GetMapping("/hello")
    public ApiResponseModel<HelloResponse> hello() {
        return new ApiResponseModel<>(false, "message", helloUseCase.getHello());
    }
}
