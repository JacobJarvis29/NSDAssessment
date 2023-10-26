package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;


public class write{



    public static <T> void write(T rewrite, String fileName, boolean append){
        try {

            FileWriter file = new FileWriter(fileName,append);                                     //Opening our filewriter.
            BufferedWriter output = new BufferedWriter(file);
            if(append) {
                output.write((rewrite + ","));                        //write that to the file
                output.newLine();
            }else{
                if(rewrite instanceof List<?>){
                    List<?> list = (List<?>)rewrite;
                    int size= list.size();
                    System.out.println(list);
                    for(int i=0;i<size;i++){

                        User obj = (User) list.get(i);

                        output.write(obj.getUsername() +","+obj.getPassword()+",");
                        for(String str:obj.getSubscribed()){
                            output.write(str+",");
                        }
                        output.newLine();
                    }
                }
            }



            //When a record is finished, we start a new line, for the next record.
            output.close();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }






}