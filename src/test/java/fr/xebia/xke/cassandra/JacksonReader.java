package fr.xebia.xke.cassandra;

import java.io.File;
import org.codehaus.jackson.map.ObjectMapper;

public class JacksonReader {

    public static <T extends Object> T readJsonFile(Class<T> clazz, File file) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(file, clazz);
        } catch(Exception e) {
            System.out.println("Couldn't read file " + file.getName() + " : " + e.getMessage());
        }
        return null;
    }
}
