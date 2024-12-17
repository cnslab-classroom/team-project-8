package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DatabaseManager {
    private static DatabaseManager instance;
    private static AESFileEncryptor encryptor;
    private static final String keyFilePath = "src/secureKey.txt"; // 암호화 키 파일 경로
    
    List<CharaData> playerDataList = new ArrayList<>(); // 플레이어 데이터를 저장할 리스트

    public static DatabaseManager getInstance() {
        // �̱���
        // !!! ���� �ڵ�� threadSafe���� ����.

        if (instance == null)
            instance = new DatabaseManager();
        
        return instance;
    }
    
    public void readSeverFile(String _serverName) {
        try {
            encryptor = new AESFileEncryptor(keyFilePath);
            
            // 폴더 내 모든 파일 읽기
            Files.list(Paths.get("servers/" + _serverName))
                    .filter(Files::isRegularFile) // 파일만 필터링
                    .forEach(file -> {
                        try {

                            // 암호화된 파일 복호화 후 문자열로 읽기
                            String decryptedData = encryptor.decryptFileToString(file.toString());

                            // 복호화된 데이터를 줄 단위로 처리
                            String[] lines = decryptedData.split("\n");
                            for (String line : lines) {
                                CharaData player = txtToCharaData(line.trim());
                                if (player != null) {
                                    playerDataList.add(player);
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Failed to decrypt or process file: " + file.getFileName());
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            System.err.println("Error reading folder: " + _serverName);
        } catch (Exception e) {
            System.err.println("Failed to Load encryptor");
            e.printStackTrace();
        }
    }

    // 한 줄의 데이터를 파싱하여 CharaData 객체로 변환
    private static CharaData txtToCharaData(String line) {
        try {
            // 데이터 형식 확인 및 추출
            if (!line.startsWith("Character{") || !line.endsWith("}")) {
                return null;
            }

            // 중괄호 내부의 데이터 추출
            String data = line.substring(line.indexOf('{') + 1, line.lastIndexOf('}'));
            String[] fields = data.split(", ");

            // 각 필드의 값을 추출
            Map<String, String> fieldMap = new HashMap<>();
            for (String field : fields) {
                String[] keyValue = field.split("=", 2);
                fieldMap.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
            }

            // 필드를 기반으로 CharaData 객체 생성
            return new CharaData(
                    fieldMap.getOrDefault("ServerName", ""), 
                    fieldMap.getOrDefault("CharacterName", ""), 
                    Integer.parseInt(fieldMap.getOrDefault("CharacterLevel", "0")), 
                    fieldMap.getOrDefault("CharacterClassName", ""), 
                    fieldMap.getOrDefault("ItemAvgLevel", "0"), 
                    fieldMap.getOrDefault("ItemMaxLevel", "0")
            );
        } catch (Exception e) {
            System.err.println("Failed to parse line: " + line);
            e.printStackTrace();
            return null;
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
        // DataSort.sortByLevel(dataList);
    }

    public boolean isExistPlayer(String _name) {
        String serverName = "";
        
        for (int i = 0; i < 8; i++) {
            switch (i) {
                case 0:
                    serverName = "아만";
                    break;
                case 1:
                serverName = "카마인";
                    break;
                case 2:
                serverName = "루페온";
                    break;
                case 3:
                serverName = "실리안";
                    break;
                case 4:
                serverName = "카제로스";
                    break;
                case 5:
                serverName = "아브렐슈드";
                    break;
                case 6:
                serverName = "니나브";
                    break;
                case 7:
                serverName = "카단";
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
