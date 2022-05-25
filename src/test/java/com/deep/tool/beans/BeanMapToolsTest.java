package com.deep.tool.beans;

import com.deep.tool.model.School;
import com.deep.tool.model.Student;
import com.deep.tool.model.User;
import junit.framework.TestCase;
import net.sf.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/25 15:23
 */
public class BeanMapToolsTest extends TestCase {

    public static void main(String[] args) {
        User user1 = new User(123, "测试1", 111);
        User user2 = new User(234, "测试2", 111);
        BeanMap beanMap = BeanMap.create(user1);
        System.out.println(beanMap);
        Student student1 = new Student(1, "学生1");
        Student student2 = new Student(2, "学生2");
        Student student3 = new Student(3, "学生3");
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        School school = new School("学校", students, users);
        BeanMap beanMap1 = BeanMap.create(school);
//        beanMap1.setBean(user1);
        BeanMap beanMap2 = beanMap.newInstance(user2);
        System.out.println(beanMap);
        System.out.println(beanMap2);

        System.out.println(beanMap1.getPropertyType("students"));
    }

}