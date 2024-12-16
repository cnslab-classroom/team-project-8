package src;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESFileEncryptor {
    private final String keyFilePath;   //암호화 키를 저장할 파일 경로
    private final SecretKey secretKey;  //고정된 AES 암호화 키

    //생성자: 암호화 키 파일 경로를 전달받음
    public AESFileEncryptor(String keyFilePath) throws Exception {
        this.keyFilePath = keyFilePath;
        this.secretKey = validateAndSetKey();   //키 파일 검증 및 설정
    }

    //AES 키 검증 및 설정
    private SecretKey validateAndSetKey() throws Exception {
        //환경변수에서 고정된 키 가져오기
        String fixedKey = System.getenv("ENCRYPTION_KEY");      //환경변수에 고정된 키값 설정해줘야함.
        if (fixedKey == null || fixedKey.length() != 32) {
            throw new IllegalArgumentException("Invalid or missing encryption key in environment variable.");
        }
        byte[] fixedKeyBytes = fixedKey.getBytes();
        SecretKey fixedSecretKey = new SecretKeySpec(fixedKeyBytes, "AES");

        //키 파일 로드
        File keyFile = new File(keyFilePath);
        if (!keyFile.exists()) {
            throw new FileNotFoundException("The key file " + keyFilePath + " does not exist.");
        }
        String encodedKey = new String(Files.readAllBytes(keyFile.toPath()));

        String fixedKeyEncoded = Base64.getEncoder().encodeToString(fixedKeyBytes);

        if (!fixedKeyEncoded.equals(encodedKey)) {
            throw new SecurityException("The provided key file does not match the fixed encryption key.");
        }

        System.out.println("Encryption key validated and set successfully.");
        return fixedSecretKey;  //검증된 고정 키를 secretKey로 설정
    }

    //파일 암호화
    public void encryptFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("The file does not exist: " + filePath);
        }

        byte[] fileContent = Files.readAllBytes(file.toPath());
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedContent = cipher.doFinal(fileContent);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(encryptedContent);
        }
        System.out.println("File encrypted successfully: " + filePath);
    }

    //파일 복호화
    public void decryptFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("The file does not exist: " + filePath);
        }

        byte[] fileContent = Files.readAllBytes(file.toPath());
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedContent = cipher.doFinal(fileContent);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(decryptedContent);
        }
        System.out.println("File decrypted successfully: " + filePath);
    }

    //문자열 암호화 후 파일로 저장
    public void encryptStringToFile(String data, String filePath) throws Exception {
        byte[] dataBytes = data.getBytes();
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(dataBytes);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(encryptedData);
        }
        System.out.println("Encrypted string data saved to: " + filePath);
    }

    //암호화된 파일을 복호화하여 문자열로 반환
    public String decryptFileToString(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("The file does not exist: " + filePath);
        }

        byte[] fileContent = Files.readAllBytes(file.toPath());
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedContent = cipher.doFinal(fileContent);

        return new String(decryptedContent);
    }
}
