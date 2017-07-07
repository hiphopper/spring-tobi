package com.study.kks.section3.chapter3_5_2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Administrator on 2017-07-07.
 */
public class Calculator {
    public Integer calcSum(String filepath) throws IOException{
        return lineReadTemplate(filepath, new LineCallback() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return Integer.parseInt(line) + value;
            }
        }, 0);
    }

    public Integer calcMultiply(String filepath) throws IOException{
        return lineReadTemplate(filepath, new LineCallback() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return Integer.parseInt(line) * value;
            }
        }, 1);
    }

    public Integer lineReadTemplate(final String filepath, LineCallback callback, int initVal) throws IOException{
        BufferedReader bufferedReader = null;

        try{
            bufferedReader = new BufferedReader(new FileReader(filepath));
            int result = initVal;
            String line;
            while ((line = bufferedReader.readLine()) != null){
                result = callback.doSomethingWithLine(line, result);
            }
            return result;
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw e;
        }finally {
            if(bufferedReader != null)
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
        }
    }
}
