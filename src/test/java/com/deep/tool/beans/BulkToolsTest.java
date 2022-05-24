package com.deep.tool.beans;

import com.deep.tool.model.Student;
import com.deep.tool.model.User;
import junit.framework.TestCase;
import net.sf.cglib.beans.BulkBean;

import java.util.Arrays;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/23 13:25
 */
public class BulkToolsTest extends TestCase {

    public static void main(String[] args) {

        String[] getter = new String[]{"getName", "getNumber"};
        String[] setter = new String[]{"setName", "setNumber"};
        Class<?>[] typeClass = new Class[]{String.class, Integer.class};

        BulkBean bulkBean = BulkBean.create(Student.class, getter, setter, typeClass);
        Student student = new Student(111, "liuwenhao");
        Object[] getObject = new Object[2];
        bulkBean.getPropertyValues(student, getObject);
        System.out.println(Arrays.toString(getObject));
        Object[] setObject = new Object[]{"pizihao", 456};
        bulkBean.setPropertyValues(student, setObject);
        System.out.println(Arrays.toString(setObject));

        Object user = new User(123,"jkl",10);
        System.out.println(user.getClass());

    }

}