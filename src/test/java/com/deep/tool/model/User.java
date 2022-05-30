package com.deep.tool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/24 9:21
 */
@Data
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer number;

    private String name;

    private Integer age;

    public void set(Integer number, String name) {
        this.number = number;
        this.name = name;
    }
}