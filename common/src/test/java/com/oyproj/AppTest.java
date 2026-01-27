package com.oyproj;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.oyproj.common.utils.SnowFlake;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {


    @Test
    void test(){
        String str = "hello";
        String str2 = "hello";
        System.out.println(str==str2);
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        String id = SnowFlake.getIdStr();
        System.out.println(id);
    }
}
