package common.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtils {

    /**
     * Json 문자열을 List<Map<String,Object>> 객체로 변환
     * @param jsonString
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static List<Map<String,Object>> jsonToList(String jsonString) throws JsonParseException, JsonMappingException, IOException {

        List<Map<String, Object>> list = null;
        if(!CommUtils.isEmpty(jsonString)) {
            ObjectMapper mapper = new ObjectMapper();
            TypeFactory typeFactory = mapper.getTypeFactory();
            list = mapper.readValue(jsonString, typeFactory.constructCollectionType(List.class,  Map.class));
        }
        return list;
    }

    /**
     * json 문자열을 Map 객체로 변환
     * 이때 입력된 순서를 유지하기 위해 LinkedHashMap을 이용
     * @param jsonString
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static Map<String, Object> jsonToMap(String jsonString)  throws JsonParseException, JsonMappingException, IOException {
        Map<String, Object> map = null;
        if(!CommUtils.isEmpty(jsonString)) {
            ObjectMapper mapper = new ObjectMapper();
            map = new LinkedHashMap<String, Object>();
            // convert JSON string to map
            map = mapper.readValue(jsonString, new TypeReference<Map<String,String>>(){});

        }
        return map;
    }

    /**
     * map 객체를 문자열로 변환
     * @param map
     * @return
     * @throws JsonProcessingException
     */
    public static String mapToJson(Map<String, Object> map) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json         = "";

        // convert map to JSON string
//        json = mapper.writeValueAsString(map);
        json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

        return json;
    }
}
