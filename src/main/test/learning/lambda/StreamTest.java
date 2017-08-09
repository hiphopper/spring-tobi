package learning.lambda;

import org.junit.Test;

import java.util.stream.IntStream;

/**
 * Created by Administrator on 2017-08-03.
 */
public class StreamTest {
    @Test
    public void create(){
        IntStream.range(1, 10).filter(n->n%2==0).map(n->n*n).forEach(n-> System.out.println(n));
    }
}
