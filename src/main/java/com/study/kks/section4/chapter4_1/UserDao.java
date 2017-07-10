package com.study.kks.section4.chapter4_1;

import com.mysql.jdbc.MysqlErrorNumbers;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by home on 2017-07-05.
 */
public class UserDao {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> userRowMapper =  new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));

            return user;
        }
    };

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void deleteAll() throws SQLException{
        /*this.jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("delete from users");
            }
        }); 아래 구현은 내부에서 이런 형태로 존재한다.  */

        this.jdbcTemplate.update("delete from users");
    }

    public void add(final User user) throws DuplicationUserIdException{
        try{
            this.jdbcTemplate.update("insert into users(id, name, password) values(?, ?, ?)", user.getId(), user.getName(), user.getPassword());
        }catch (DuplicateKeyException e){
            throw new DuplicationUserIdException(e);
        }catch (DataAccessException e){
            throw new RuntimeException(e);
        }
    }

    public User get(String id) throws SQLException{
        return this.jdbcTemplate.queryForObject("select id, name, password from users where id = ?"
                , new Object[]{id}, this.userRowMapper);
    }

    public int getCount() throws SQLException{
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

        return this.jdbcTemplate.queryForObject("select count(*) from users", null, Integer.class);
    }

    public List<User> getAll() throws SQLException{
        return this.jdbcTemplate.query("select id, name, password from users order by id", this.userRowMapper);
    }
}
