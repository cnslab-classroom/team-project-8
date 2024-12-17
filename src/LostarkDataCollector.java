package src;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;

public class LostarkDataCollector {
    private static final String API_URL = "https://developer-lostark.game.onstove.com/characters/";
    private static final String API_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyIsImtpZCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyJ9.eyJpc3MiOiJodHRwczovL2x1ZHkuZ2FtZS5vbnN0b3ZlLmNvbSIsImF1ZCI6Imh0dHBzOi8vbHVkeS5nYW1lLm9uc3RvdmUuY29tL3Jlc291cmNlcyIsImNsaWVudF9pZCI6IjEwMDAwMDAwMDA1Njg2MjcifQ.d7faOvJxpujOTlT433qMY_9KDaTEkpPytiV3f9SMopmRgVcBCxPgruTtLW9Q694jwSkAd6lkcDdoSzj_7alagIErzu5h2WnafvXk26DJvXm3oVLy0uX_88Xv5huG905oVKC8-V1k_4aD35Rnt6z7R-AjPxiQ_NWAR_UCMfyUnMa0SBIcnrZymWud2x6MySIHrDaqm9cdQBijGkpcKWzCBHxpvt3w15kbLyFb9rbn9o9Ai5vz80s3Hkrjj6RnhZBnCe9v1fcl2pE4incjnFeytNEOk6kk82MTL_UYbDSflpweXWaLRMFVcEYXnTDeO4fHKGKzlcyQ8VPEyu2j4xd1yQ"; // 발급받은 API 키를 여기에 입력하세요.
    private static final String keyFilePath = "src/secureKey.txt"; // 암호화 키 파일 경로
    private static AESFileEncryptor encryptor;

    public static void Collector() {
        System.out.println("조회하고 싶은 캐릭터 이름을 입력해주세요.");
        System.out.print(">> ");

        Scanner sc = new Scanner(System.in, "EUC-KR");
        String characterName = sc.next();
        sc.nextLine(); // 버퍼 삭제

        try {
            // AESFileEncryptor 객체 생성 (키 파일 검증)
            encryptor = new AESFileEncryptor(keyFilePath);

            // 캐릭터 정보 조회
            String characterInfo = getCharacterInfo(characterName);

            DatabaseManager DB = DatabaseManager.getInstance();

            List<CharaData> charaData = DB.PlayerDataParser(characterInfo);

            String serverName = extractServerName(characterInfo); // 서버 이름 추출
            
            String info = "";
            for (int i = 0; i < charaData.size(); i++) {
                info += charaData.get(i).toString();
                info += "\n";

            }

            // 암호화된 캐릭터 정보 저장
            saveEncryptedCharacterInfoToServerFolder(serverName, characterName, info); // 서버별로 저장
            
            // 암호화된 파일을 복호화하여 확인
            // String decryptedInfo = decryptCharacterInfoFromServerFolder(serverName, characterName);
            // System.out.println("복호화된 캐릭터 정보: " + decryptedInfo);

        } catch (IOException e) {
            System.err.println("API 요청 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("암호화/복호화 처리 중 오류 발생: " + e.getMessage());
        }
    }

    public static String getCharacterInfo(String characterName) throws IOException {
        // API URL 생성
        String characterNameEncoded = URLEncoder.encode(characterName, "UTF-8");
        URL url = new URL(API_URL + characterNameEncoded + "/siblings");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        // 요청 헤더 설정
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) { // 200은 성공 코드
            throw new IOException("HTTP 요청 실패: 응답 코드 " + responseCode);
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer stringBuffer = new StringBuffer();
        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null)  {
            stringBuffer.append(inputLine);
        }
        bufferedReader.close();

        String response = stringBuffer.toString();

        return response;
    }

    // 캐릭터 정보를 파일에 저장하는 메서드
    public static String extractServerName(String characterInfo) {
        try {
            // JSON 형식의 데이터를 파싱하여 서버 이름 추출
            int startIndex = characterInfo.indexOf("\"ServerName\":") + 14; // "ServerName": 뒤의 값
            int endIndex = characterInfo.indexOf("\"", startIndex);
            return characterInfo.substring(startIndex, endIndex);
        } catch (Exception e) {
            System.err.println("서버 이름 추출 중 오류 발생: " + e.getMessage());
            return "unknown";
        }
    }

    // 암호화된 캐릭터 정보를 서버별 디렉토리에 저장하는 메서드
    public static void saveEncryptedCharacterInfoToServerFolder(String serverName, String characterName, String characterInfo) {
        String directoryPath = "servers/" + serverName; // 서버별 디렉토리 경로
        String filePath = directoryPath + "/" + characterName + "_info.txt";

        // 디렉토리 생성
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일에 암호화된 데이터 저장
        try {
            encryptor.encryptStringToFile(characterInfo, filePath);
            System.out.println("암호화된 캐릭터 정보가 파일에 저장되었습니다: " + filePath);
        } catch (Exception e) {
            System.err.println("암호화된 파일 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 암호화된 캐릭터 정보를 복호화하여 반환하는 메서드
    public static String decryptCharacterInfoFromServerFolder(String serverName, String characterName) throws Exception {
        String directoryPath = "servers/" + serverName; // 서버별 디렉토리 경로
        String filePath = directoryPath + "/" + characterName + "_info.txt";
        
        // 파일 복호화
        return encryptor.decryptFileToString(filePath);
    }
}
