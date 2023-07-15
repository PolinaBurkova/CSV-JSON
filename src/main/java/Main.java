import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String filename = "data.csv";
        List<Employee> list = parseCSV(columnMapping, filename);
        list.forEach(System.out :: println);

        String json = listToJson(list);
        System.out.println(json);

        String jsonFileName = "data.json";
        writeString(json, jsonFileName);
    }
    public static List<Employee> parseCSV(String[] columnMapping, String filename) {
        List<Employee> list = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filename))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csvToBean.parse();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return list;
    }
    public static String listToJson(List<Employee> list){
        Type listType = new TypeToken<List<Employee>>(){}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        return gson.toJson(list, listType);
    }
    public static void writeString(String jsonText, String filename){

        try (FileWriter fileWriter = new FileWriter(filename)){
            fileWriter.write(jsonText);
            fileWriter.flush();
        }catch (IOException exception){
            System.out.println(exception.getMessage());
        }
    }
}
