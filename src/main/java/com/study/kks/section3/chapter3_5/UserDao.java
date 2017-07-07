package com.study.kks.section3.chapter3_5;

import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by home on 2017-07-05.
 */
public class UserDao {
    private DataSource dataSource;
    private JdbcContext jdbcContext;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void deleteAll() throws SQLException{
        this.jdbcContext.executeSql("delete from users");
    }

    public void add(final User user) throws SQLException{
        this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");

                preparedStatement.setString(1, user.getId());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getPassword());

                return preparedStatement;
            }
        });
    }

    public User get(String id) throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = this.dataSource.getConnection();
            preparedStatement = connection.prepareStatement("select id, name, password from users where id = ?");
            preparedStatement.setString(1, id);

            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getString("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));

                return user;
            }else{
                throw new EmptyResultDataAccessException(1);
            }
        }catch (SQLException e){
            throw e;
        }finally {
            if(resultSet != null){
                try{
                    resultSet.close();
                }catch (SQLException e){}
            }

            if(preparedStatement != null){
                try{
                    preparedStatement.close();
                }catch (SQLException e){}
            }

            if(connection != null){
                try{
                    connection.close();
                }catch (SQLException e){}
            }
        }
    }

    public int getCount() throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.dataSource.getConnection();
            preparedStatement = connection.prepareStatement("select count(*) from users");
            resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return resultSet.getInt(1);
        }catch (SQLException e){
            throw e;
        }finally {
            if(resultSet != null){
                try{
                    resultSet.close();
                }catch (SQLException e){}
            }

            if(preparedStatement != null){
                try{
                    preparedStatement.close();
                }catch (SQLException e){}
            }

            if(connection != null){
                try{
                    connection.close();
                }catch (SQLException e){}
            }
        }
    }
}
