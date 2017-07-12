package com.study.kks.section5.chapter5_1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2017-07-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext_5_1.xml")
public class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired private DataSource dataSource;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp(){
        user1 = new User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0);
        user2 = new User("leegw700", "이길원", "springno2", Level.SILVER, 55, 10);
        user3 = new User("bumjin", "박범진", "springno3", Level.GOLD, 100, 40);
    }

    @Test
    public void addAndGet() throws SQLException {
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));

        checkSameUser(userDao.get(user1.getId()), user1);

        checkSameUser(userDao.get(user2.getId()), user2);
    }
    @Test
    public void getCount() throws SQLException{
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        assertThat(userDao.getCount(), is(1));
        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));
        userDao.add(user3);
        assertThat(userDao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException{
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.get("unknown_id");
    }

    @Test
    public void getAll() throws SQLException{
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        List<User> userList = userDao.getAll();
        assertThat(userList.size(), is(0));

        userDao.add(user1);
        List<User> userList1 = userDao.getAll();
        assertThat(userList1.size(), is(1));
        checkSameUser(userList1.get(0), user1);

        userDao.add(user2);
        List<User> userList2 = userDao.getAll();
        assertThat(userList2.size(), is(2));
        checkSameUser(userList2.get(0), user1);
        checkSameUser(userList2.get(1), user2);

        userDao.add(user3);
        List<User> userList3 = userDao.getAll();
        assertThat(userList3.size(), is(3));
        checkSameUser(userList3.get(0), user3);
        checkSameUser(userList3.get(1), user1);
        checkSameUser(userList3.get(2), user2);
    }

    private void checkSameUser(User user1, User user2){
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }

    @Test(expected = DuplicateKeyException.class)
    public void duplicateKey(){
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        userDao.add(user1);
    }

    @Test
    public void sqlExceptionTranslate(){
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        try{
            userDao.add(user1);
            userDao.add(user1);
        }catch (DataAccessException e){
            SQLException sqlE = (SQLException) e.getRootCause();
            SQLErrorCodeSQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);

            assertThat(translator.translate(null, null, sqlE), is(instanceOf(DuplicateKeyException.class)));
        }
    }

    @Test
    public void update(){
        userDao.deleteAll();
        userDao.add(user1);
        userDao.add(user2);

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        userDao.update(user1);

        checkSameUser(userDao.get(user1.getId()), user1);
        checkSameUser(userDao.get(user2.getId()), user2);
    }
}
