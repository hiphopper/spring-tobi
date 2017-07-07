package com.study.kks.section3.chapter3_5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by home on 2017-07-05.
 */
public interface StatementStrategy {
    PreparedStatement makePreparedStatement(Connection connection) throws SQLException;
}
