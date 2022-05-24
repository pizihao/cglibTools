package com.deep.tool.beans;

import com.deep.tool.model.Student;
import com.deep.tool.model.User;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/24 9:20
 */
public class CopierToolsTest extends TestCase {

    public void testCreate() {
        Class<?> sourceClass = User.class;
        Class<?> targetClass = Student.class;
        Object copier = CopierTools.create(sourceClass, targetClass);
        assertEquals(copier, CopierTools.create(sourceClass, targetClass, false));
        Assert.assertNotEquals(copier, CopierTools.create(sourceClass, targetClass, true));
    }

    public void testCopy() {
        User user = new User(123, "测试", 111);
        Student student = new Student();
        CopierTools.copy(user, student);
        assertEquals(user.getNumber(), student.getNumber());
        assertEquals(user.getName(), student.getName());
    }

    public void testNewInstanceCopy() {
        User user = new User(123, "测试", 111);
        Student student = CopierTools.newInstanceCopy(user, Student.class);
        assertEquals(user.getNumber(), student.getNumber());
        assertEquals(user.getName(), student.getName());
    }

    public void testCopyAcquire() {
        User user = new User(123, "测试", 111);
        Student student = new Student();
        Student student1 = CopierTools.copyAcquire(user, student);
        assertEquals(user.getNumber(), student.getNumber());
        assertEquals(user.getName(), student.getName());
        assertEquals(user.getNumber(), student1.getNumber());
        assertEquals(user.getName(), student1.getName());
        student1.setName("修改后");
        assertEquals(student.getName(), "修改后");
    }

    public void testCopyCollectionAcquire() {
        List<User> userList = new ArrayList<>();
        User user1 = new User(123, "测试", 111);
        User user2 = new User(456, "新增", 222);
        Collection<Student> students = CopierTools.copyCollectionAcquire(userList, Student.class);
        List<Student> studentList = new ArrayList<>(students);
        for (int i = 0; i < studentList.size(); i++) {
            User user = userList.get(i);
            Student student = studentList.get(i);
            assertEquals(user.getNumber(), student.getNumber());
            assertEquals(user.getName(), student.getName());
        }
    }

}