package com.study.kks.section7.chapter7_5;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
    private final BaseSqlService sqlService = new BaseSqlService();

    public void setSqlRegistry(SqlRegistry sqlRegistry){ this.sqlRegistry = sqlRegistry; }
    public void setSqlmap(Resource sqlmap) {
        this.sqlReader.setSqlmap(sqlmap);
    }
    public void setUnmarshaller(Unmarshaller unmarshaller){ this.sqlReader.unmarshaller = unmarshaller; }

    @PostConstruct
    public void loadSql(){
        this.sqlService.setSqlReader(this.sqlReader);
        this.sqlService.setSqlRegistry(this.sqlRegistry);

        this.sqlService.loadSql();
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return this.sqlService.getSql(key);
    }

    private static class OxmSqlReader implements SqlReader{
        private Unmarshaller unmarshaller;
        private Resource sqlmap = new ClassPathResource("sqlmap.xml", UserDao.class);

        public void setSqlmap(Resource sqlmap) {
            this.sqlmap = sqlmap;
        }
        public void setUnmarshaller(Unmarshaller unmarshaller){ this.unmarshaller = unmarshaller; }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                Source source = new StreamSource(this.sqlmap.getInputStream());
                Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(source);

                for(SqlType sql : sqlmap.getSql()){
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(this.sqlmap.getFilename()+"을 가져올 수 없습니다.");
            }
        }
    }
}
