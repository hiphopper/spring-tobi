package com.study.kks.section3.chapter3_5;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017-07-06.
 */
public class JdbcContext {
    private DataSource dataSource;
    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy statementStrategy) throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.dataSource.getConnection();
            preparedStatement = statementStrategy.makePreparedStatement(connection);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw e;
        }finally {
            if(preparedStatement != null){ try{ preparedStatement.close(); }catch (SQLException e){} }
            if(connection != null){ try{ connection.close(); }catch (SQLException e){} }
        }
    }

    public void executeSql(final String sql) throws SQLException{
        workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement(sql);
            }
        });
    }
}
