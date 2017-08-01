package com.study.kks.section7.chapter7_3;

import org.springframework.oxm.Unmarshaller;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

/**
 * Created by Administrator on 2017-08-01.
 */
public class OxmSqlService implements SqlService {
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();
    private final OxmSqlReader sqlReader = new OxmSqlReader();

    public void setSqlRegistry(SqlRegistry sqlRegistry){ this.sqlRegistry = sqlRegistry; }
    public void setSqlmapFile(String sqlmapFile){ this.sqlReader.setSqlmapFile(sqlmapFile); }
    public void setUnmarshaller(Unmarshaller unmarshaller){ this.sqlReader.unmarshaller = unmarshaller; }

    @PostConstruct
    public void loadSql(){
        this.sqlReader.read(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return this.sqlRegistry.findSql(key);
    }

    private static class OxmSqlReader implements SqlReader{
        private Unmarshaller unmarshaller;
        private static final String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
        private String sqlmapFile = DEFAULT_SQLMAP_FILE;

        public void setSqlmapFile(String sqlmapFile) {
            this.sqlmapFile = sqlmapFile;
        }
        public void setUnmarshaller(Unmarshaller unmarshaller){ this.unmarshaller = unmarshaller; }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                Source source = new StreamSource(UserDao.class.getResourceAsStream(this.sqlmapFile));
                Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(source);

                for(SqlType sql : sqlmap.getSql()){
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
