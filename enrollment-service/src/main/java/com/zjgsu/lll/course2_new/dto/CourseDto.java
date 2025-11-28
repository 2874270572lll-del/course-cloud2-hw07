package com.zjgsu.lll.course2_new.dto;
import lombok.Data;

@Data
public class CourseDto {
    private String id;
    private String courseCode;
    private String name;
    private int capacity;
    private int enrolled;
}
