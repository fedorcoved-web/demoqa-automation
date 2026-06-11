package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class JsonDataProvider {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Object[][] readTextBoxData() {
        try (InputStream is = ClassLoader.getSystemResourceAsStream("testdata/users.json")) {
            List<Map<String, String>> users =
                    mapper.readValue(is, new TypeReference<List<Map<String, String>>>() {});
            Object[][] data = new Object[users.size()][4];
            for (int i = 0; i < users.size(); i++) {
                Map<String, String> u = users.get(i);
                data[i][0] = u.get("name");
                data[i][1] = u.get("email");
                data[i][2] = u.get("currentAddress");
                data[i][3] = u.get("permanentAddress");
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read testdata/users.json", e);
        }
    }

    public static Object[][] readApiUserData() {
        try (InputStream is = ClassLoader.getSystemResourceAsStream("testdata/api_users.json")) {
            List<Map<String, String>> users =
                    mapper.readValue(is, new TypeReference<List<Map<String, String>>>() {});
            Object[][] data = new Object[users.size()][2];
            for (int i = 0; i < users.size(); i++) {
                Map<String, String> u = users.get(i);
                data[i][0] = u.get("name");
                data[i][1] = u.get("job");
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read testdata/api_users.json", e);
        }
    }
}
