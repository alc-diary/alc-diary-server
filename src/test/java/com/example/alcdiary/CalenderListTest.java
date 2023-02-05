package com.example.alcdiary;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CalenderListTest {

    @Test
    public void test() {
        List<TestModel> tests = List.of(new TestModel(), new TestModel(), new TestModel());
        tests.forEach(test ->  {
            test.tests.addAll(List.of("test","test","test"));
            System.out.println(test.tests);
        });
    }


    public static class TestModel {
        List<String> tests =  new ArrayList<>();
    }
}
