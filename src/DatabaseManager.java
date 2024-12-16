package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    private static DatabaseManager instance;
    
    private List<CharaData> dataList = new ArrayList<>();

    public static DatabaseManager getInstance() {
        // �̱���
        // !!! ���� �ڵ�� threadSafe���� ����.

        if (instance == null)
            instance = new DatabaseManager();
        
        return instance;
    }

    public void ReadPlayerFile(String _fileName) {
        // _fileName�� player������ �о� dataList�� ����
        
        try{
            BufferedReader br = new BufferedReader(new FileReader(_fileName));

            String text = "";
            String line;
            while ((line = br.readLine()) != null) { 
                text += line + "\n";
            }

            br.close();
            
            System.out.print(text);

            sortDataByLevel();
            // printDataList();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<CharaData> PlayerDataParser(String jsonString) {
        // Remove leading and trailing brackets
        jsonString = jsonString.trim();
        if (jsonString.startsWith("[")) {
            jsonString = jsonString.substring(1);
        }
        if (jsonString.endsWith("]")) {
            jsonString = jsonString.substring(0, jsonString.length() - 1);
        }

        // Split into individual JSON objects
        String[] objects = jsonString.split("}");
        List<CharaData> result = new ArrayList<>();

        for (String object : objects) {
            // Clean up each object and split into key-value pairs
            object = object.trim();
            if (object.startsWith("{")) {
                object = object.substring(1);
            }
            if (object.endsWith("}")) {
                object = object.substring(0, object.length() - 1);
            }

            // Parse key-value pairs
            String[] pairs = object.split(",");
            Map<String, String> map = new HashMap<>();
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replaceAll("\\\"", "");
                    String value = keyValue[1].trim().replaceAll("\\\"", "");
                    map.put(key, value);
                }
            }

            // Map to CharaData
            String serverName = map.getOrDefault("ServerName", "");
            String characterName = map.getOrDefault("CharacterName", "");
            int characterLevel = Integer.parseInt(map.getOrDefault("CharacterLevel", "0"));
            String characterClassName = map.getOrDefault("CharacterClassName", "");
            String itemAvgLevel = map.getOrDefault("ItemAvgLevel", "");
            String itemMaxLevel = map.getOrDefault("ItemMaxLevel", "");

            result.add(new CharaData(serverName, characterName, characterLevel, characterClassName, itemAvgLevel, itemMaxLevel));
        }

        return result;

    }
    
    public void sortDataByLevel() {
        DataSort.sortByLevel(dataList);
    }

    public boolean isExistPlayer(String _name) {
        String serverName = "";
        
        for (int i = 0; i < 8; i++) {
            switch (i) {
                case 0:
                    serverName = "�����";
                    break;
                case 1:
                serverName = "�Ǹ���";
                    break;
                case 2:
                serverName = "�Ƹ�";
                    break;
                case 3:
                serverName = "ī����";
                    break;
                case 4:
                serverName = "ī���ν�";
                    break;
                case 5:
                serverName = "�ƺ근����";
                    break;
                case 6:
                serverName = "ī��";
                    break;
                case 7:
                serverName = "�ϳ���";
                    break;
                default:
                    System.out.println("error.");
                    break;
            }

            String filePath = "servers/" + serverName + "/" + _name + "_info.txt";
            
            File file = new File(filePath);
            if (file.exists())
            return true;

        }
        return false;
    }
}
