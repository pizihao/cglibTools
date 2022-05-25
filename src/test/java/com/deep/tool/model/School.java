package com.deep.tool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/25 17:01
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class School {

    private String name;

    private List<Student> students;

    private List<User> users;

}