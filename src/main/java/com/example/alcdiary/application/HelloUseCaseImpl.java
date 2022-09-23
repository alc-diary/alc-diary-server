package com.example.alcdiary.application;

import com.example.alcdiary.presentation.response.HelloResponse;
import org.springframework.stereotype.Service;

@Service
public class HelloUseCaseImpl implements HelloUseCase {
    @Override
    public HelloResponse getHello() {
        return new HelloResponse("hello....alcohol....");
    }
}
