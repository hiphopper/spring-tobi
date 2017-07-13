package com.study.kks.section5.chapter5_1_1;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2017-07-13.
 */
public class UserTest {
    private User user;

    @Before
    public void setUp(){
        user = new User();
    }

    @Test
    public void upgaradeLevel(){
        Level[] levels = Level.values();
        for(Level level : levels){
            if(level.nextValue() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();

            assertThat(user.getLevel(), is(level.nextValue()));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void cannotUpgradeLevel(){
        Level[] levels = Level.values();
        for(Level level : levels){
            if(level.nextValue() != null) continue;
            user.setLevel(level);
            user.upgradeLevel();
        }
    }
}
