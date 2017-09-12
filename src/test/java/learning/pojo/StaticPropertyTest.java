package learning.pojo;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by home on 2017-07-04.
 */
public class StaticPropertyTest {

    @Before
    public void setUp(){
        Exam exam = new Exam();
        exam.msg = "setUp";
    }

    @Test
    public void test(){
        Exam exam = new Exam();
        assertThat(exam.msg, is("setUp"));
    }

    static class Exam{
        static String msg;
    }
}
