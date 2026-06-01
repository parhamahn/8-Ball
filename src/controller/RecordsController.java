package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RecordsController {

    public void saveIntoFile(List<model.Record> records){
        try(ObjectOutputStream RecordsOOS = new ObjectOutputStream(new FileOutputStream("Records"))){
            for (model.Record record: records){
                RecordsOOS.writeObject(record);
            }
            RecordsOOS.flush();
        }catch (IOException e){
            System.out.println("***ERROR WHILE SAVING RECORDS***\n" + e);
        }
    }

    public List<model.Record> loadFromFile(){
        ArrayList<model.Record> list = new ArrayList<>();
        File file = new File("Records");
        if (!file.exists()) return list;
        try (ObjectInputStream RecordsOIS = new ObjectInputStream(new FileInputStream("Records"))) {
            while (true) {
                try {
                    Object object = RecordsOIS.readObject();
                    if (object instanceof model.Record) {
                        list.add((model.Record) object);
                    }
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    System.out.println("***UNKNOWN CLASS IN RECORDS***\n" + e);
                }
            }
        } catch (IOException e) {
            System.out.println("***ERROR WHILE LOADING RECORDS***\n" + e);
        }
        return list;
    }

}
