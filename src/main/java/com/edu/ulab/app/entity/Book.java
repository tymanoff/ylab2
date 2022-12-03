package com.edu.ulab.app.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book extends DistributedEntity{
    private Long userId;
    private String title;
    private String author;
    private long pageCount;
}
