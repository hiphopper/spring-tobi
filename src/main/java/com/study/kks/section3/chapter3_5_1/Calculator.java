package com.study.kks.section3.chapter3_5_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Administrator on 2017-07-07.
 */
public class Calculator {
    public int calcSum(String file) throws IOException {
        return fileReadTemplate(file, new BufferedReaderCallback() {
            @Override
            public int doSomethingWithReader(BufferedReader bufferedReader) throws IOException {
                int sum = 0;
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    sum += Integer.parseInt(line);
                }
                return sum;
            }
        });
    }

    public int calcMultiply(String file) throws IOException {
        return fileReadTemplate(file, new BufferedReaderCallback() {
            @Override
            public int doSomethingWithReader(BufferedReader bufferedReader) throws IOException {
                int multiply = 1;
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    multiply *= Integer.parseInt(line);
                }
                return multiply;
            }
        });
    }

    public int fileReadTemplate(final String file, BufferedReaderCallback bufferedReaderCallback) throws IOException{
        BufferedReader bufferedReader = null;

        try{
            bufferedReader = new BufferedReader(new FileReader(file));
            return bufferedReaderCallback.doSomethingWithReader(bufferedReader);
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw e;
        }finally {
            if(bufferedReader != null){ try{ bufferedReader.close(); }catch (IOException e){
                System.out.println(e.getMessage());
            } }
        }
    }
}
