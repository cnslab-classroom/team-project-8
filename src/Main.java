package src;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("하고 싶은 행동을 선택해주세요. (숫자 입력, 예시 --- 1)\n");
        System.out.println("1. 플레이어 검색 2. 랭킹 보기 3. 직업 선호도 보기 4. 종료\n");
        
        DatabaseManager DB = DatabaseManager.getInstance();

        Scanner sc = new Scanner(System.in, "EUC-KR");
        String input = sc.nextLine().trim();

        ShowTop show = new ShowTop();

        switch (input) {
            case "1":
            LostarkDataCollector.Collector();
                break;
            case "2":
            input = GetServerInput();
            show.ShowTop100Level(input);
                break;
            case "3":
            input = GetServerInput();
            show.ShowJobRatio(input);
                break;
            case "4":
            default:
                System.out.println("유효하지 않은 명령입니다.");
                return;
        }
        
        /*
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
            */
        
    }

    private static String GetServerInput() {
        Scanner sc = new Scanner(System.in, "EUC-KR");

        System.out.println("로스트 아크 서버를 선택하세요: (서버 이름, 예시 --- 아만)");
        System.out.println("1. 아만 2. 카마인 3. 루페온 4. 실리안 5. 카제로스 6. 아브렐슈드 7. 니나브 8.카단");
        String input = sc.nextLine().trim();
        
        if (input.equals("아만") || input.equals("카마인") || input.equals("루페온") || input.equals("실리안") || 
        input.equals("카제로스") || input.equals("아브렐슈드") || input.equals("니나브") || input.equals("카단"))
            return input;
        else
            return GetServerInput();
    }
}
