package com.study.kks.section1.chapter1_7;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by cp91716 on 2017-06-28.
 */
public class CountingConnectionMaker implements ConnectionMaker {
    private int count;
    private ConnectionMaker connectionMaker;

    public CountingConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        count++;
        return connectionMaker.getConnection();
    }

    public int getCount(){
        return this.count;
    }
}
