package com.nn;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.util.*;

@SpringBootTest
public class NnApplicationTests {

    @Test
    public void contextLoads() {
        Integer[] integer1 = new Integer[]{new Integer(1),new Integer(2),new Integer(3)};
        Integer[] clone = integer1.clone();
        clone[1] = 12;
        System.out.println(integer1);
        System.out.println(clone);
    }

}
