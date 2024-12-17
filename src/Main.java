package src;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while(true) {
            try {
                System.out.println("메뉴를 선택하세요요.(입력 예시 >> 1)");
                System.out.println("[1].랭킹 보기 // [2].직업 선호도 보기 // [3].플레이어 검색 및 추가 // [4].종료");
                System.out.print(">> ");
                
                DatabaseManager DB = DatabaseManager.getInstance();

                Scanner sc = new Scanner(System.in, "EUC-KR");
                String input = sc.nextLine().trim();

                ShowTop show = new ShowTop();

                System.out.println("");
                switch (input) {
                    case "1":
                        input = GetServerInput();
                        show.ShowTop100Level(input);
                        System.out.println("");
                        break;
                    case "2":
                        input = GetServerInput();
                        show.ShowJobRatio(input);
                        System.out.println("");
                        break;
                    case "3":
                        LostarkDataCollector.Collector();
                        System.out.println("");
                        break;
                    case "4":
                        System.out.println("프로그램을 종료합니다...");
                        System.out.println("");
                        return;
                    default:
                        System.out.println("유효하지 않은 입력입니다...");
                        System.out.println("");
                        break;
                }
            } catch (SecurityException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("The key file does not match the fixed encryption key.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
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
            */  
    }

    private static String GetServerInput() {
        Scanner sc = new Scanner(System.in, "EUC-KR");

        System.out.println("로스트 아크 서버를 선택하세요.(입력 예시 >> 아만)");
        System.out.println("[1].아만 [2].카마인 [3].루페온 [4].실리안 [5].카제로스 [6].아브렐슈드 [7].니나브 [8].카단");
        System.out.print(">> ");
        String input = sc.nextLine().trim();
        
        if (input.equals("아만") || input.equals("카마인") || input.equals("루페온") || input.equals("실리안") || 
        input.equals("카제로스") || input.equals("아브렐슈드") || input.equals("니나브") || input.equals("카단"))
            return input;
        else {
            System.out.println("Error: 서버 이름을 다시 입력해주세요.");
            System.out.println("");
            return GetServerInput();
        }
    }
}
