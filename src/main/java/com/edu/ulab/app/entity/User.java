package com.edu.ulab.app.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends DistributedEntity{
    private String fullName;
    private String title;
    private int age;
}
