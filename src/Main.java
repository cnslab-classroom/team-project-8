package src;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        LostarkDataCollector.Collector();

        
         //암호화 및 복호화 코드
        try {
            //암호화 키를 저장할 파일 경로함함
            String keyFilePath = "src/secureKey.txt";

            //AESFileEncryptor 객체 생성 (키 파일 검증)
            AESFileEncryptor encryptor = new AESFileEncryptor(keyFilePath);

            //암호화 대상 파일 경로
            String filePath = "servers\\루페온\\박요한_info.txt";

            Scanner scanner = new Scanner(System.in);

            System.out.println("명령을 입력하세요 (encrypt, decrypt, exit): ");
            String input = scanner.nextLine().trim(); // 사용자 입력 받기 및 공백 제거
    
            switch (input) {
                case "encrypt":
                    System.out.println("암호화를 수행합니다.");
                    encryptor.encryptFile(filePath);
                    break;
                case "decrypt":
                    System.out.println("복호화를 수행합니다.");
                    // 복호화 로직 호출
                    encryptor.decryptFile(filePath);
                    break;
                case "exit":
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("유효하지 않은 명령입니다.");
                    break;
            }
            
        } catch (SecurityException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("The key file does not match the fixed encryption key.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        //파일 읽기
        //DatabaseManager DB = DatabaseManager.getInstance();
        //DB.ReadCSVFile(".\\src\\test.txt");
        
    }
}
