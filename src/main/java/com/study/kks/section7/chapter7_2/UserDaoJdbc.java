package com.study.kks.section7.chapter7_2;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by home on 2017-07-05.
 */
public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private SqlService sqlService;
    private RowMapper<User> userRowMapper =  new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            user.setEmail(rs.getString("email"));

            return user;
        }
    };

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public void setSqlService(SqlService sqlService) { this.sqlService = sqlService; }

    public void deleteAll() {
        /*this.jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("delete from users");
            }
        }); 아래 구현은 내부에서 이런 형태로 존재한다.  */

        this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
    }

    public void add(User user) {
        this.jdbcTemplate.update(this.sqlService.getSql("userAdd")
                , user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet")
                , new Object[]{id}, this.userRowMapper);
    }

    public int getCount() {
        /*this.jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("select count(*) from users");
            }
        }, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getInt(1);
            }
        });  아래 구현은 내부에서 이런 형태로 존재한다. */

        // this.jdbcTemplate.queryFor("select count(*) from users");
        // 3.2.2 버전 이후로는 Deprecated 되었으면 상위에는 없어졌음. 그래서 아래와 같이 사용해야함.
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount"), null, Integer.class);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"), this.userRowMapper);
    }

    public void update(User user){
        this.jdbcTemplate.update(this.sqlService.getSql("userUpdate")
                , user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
    }
}
