public class Main {
    public static void main(String[] args) {
        try {
            // 암호화 키를 저장할 키 파일 경로
            String keyFilePath = "aes_key.txt";

            // AESFileEncryptor 객체 생성 (키 파일 로드 또는 생성)
            AESFileEncryptor encryptor = new AESFileEncryptor(keyFilePath);

            // 암호화 대상 파일 경로
            String filePath = "ItemList.txt";

            // 파일 암호화
            //encryptor.encryptFile(filePath);

            // 파일 복호화
            encryptor.decryptFile(filePath);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
