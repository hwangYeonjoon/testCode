package com.example.pushtest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {

    public static void main(String[] args) {

        List<Integer> numbers = Arrays.asList(1,2,3,4,5,6);

        List<Integer> eventNumbers = numbers.stream()
                .filter(n -> n%2 ==0)
                .map(n ->n *2)
                .collect(Collectors.toList());

        System.out.println("tt :" + eventNumbers);

    }
}
