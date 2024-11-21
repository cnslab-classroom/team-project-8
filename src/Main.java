package src;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {

        LostarkDataCollector.Collector();

        /*
         
        try {
            // 암호화 키를 저장할 파일 경로
            String keyFilePath = "src/secureKey.txt";

            // AESFileEncryptor 객체 생성 (키 파일 검증)
            AESFileEncryptor encryptor = new AESFileEncryptor(keyFilePath);

            // 암호화 대상 파일 경로
            String filePath = "src/test.txt";

            // 파일 암호화
            // encryptor.encryptFile(filePath);

            // 파일 복호화
            // encryptor.decryptFile(filePath);

        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please ensure the key file exists before running the program.");
        } catch (SecurityException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("The key file does not match the fixed encryption key.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        //파일 읽기
        DatabaseManager DB = DatabaseManager.getInstance();
        DB.ReadCSVFile(".\\src\\test.txt");
         */
    }
}
