package com.study.kks.section1.chapter1_4;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by cp91716 on 2017-06-27.
 */
public interface ConnectionMaker {
    Connection getConnection() throws ClassNotFoundException, SQLException;
}
