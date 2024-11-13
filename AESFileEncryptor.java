import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

public class AESFileEncryptor {
    private final String keyFilePath; // 암호화 키를 저장할 파일 경로
    private SecretKey secretKey; // 암호화 키 (로드되거나 생성됨)

    // 생성자: 암호화 키 파일 경로를 전달받음
    public AESFileEncryptor(String keyFilePath) throws Exception {
        this.keyFilePath = keyFilePath;
        this.secretKey = loadORgenerateKey(); // 키 파일 로드 또는 생성
    }

    // AES 키 로드 또는 생성
    private SecretKey loadORgenerateKey() throws Exception {
        File keyFile = new File(keyFilePath);
        if (keyFile.exists()) {
            System.out.println("Loading existing AES key from " + keyFilePath);
            return loadKey();
        } else {
            System.out.println("Generating new AES key...");
            SecretKey newKey = generateKey();
            saveKey(newKey);
            System.out.println("New AES key saved to " + keyFilePath);
            return newKey;
        }
    }

    // AES 키 생성
    private SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // 256-bit AES encryption
        return keyGen.generateKey();
    }

    // 키 파일에 저장
    private void saveKey(SecretKey key) throws Exception {
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        try (FileWriter writer = new FileWriter(keyFilePath)) {
            writer.write(encodedKey);
        }
    }

    // 키 파일에서 로드
    private SecretKey loadKey() throws Exception {
        String encodedKey = new String(Files.readAllBytes(new File(keyFilePath).toPath()));
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        if (decodedKey.length != 32) { // AES 256-bit 키는 32바이트여야 함
            throw new IllegalArgumentException("Invalid AES key length in key file!");
        }
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    // 파일 암호화
    public void encryptFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("The file does not exist: " + filePath);
        }
    
        byte[] fileContent = Files.readAllBytes(file.toPath());
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedContent = cipher.doFinal(fileContent);
    
        // "암호화된 파일" 헤더 추가
        byte[] header = "암호화된 파일".getBytes();
        byte[] combinedContent = new byte[header.length + encryptedContent.length];
        System.arraycopy(header, 0, combinedContent, 0, header.length);
        System.arraycopy(encryptedContent, 0, combinedContent, header.length, encryptedContent.length);
    
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(combinedContent);
        }
        System.out.println("File encrypted successfully: " + filePath);
    }
    

    // 파일 복호화
    public void decryptFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("The file does not exist: " + filePath);
        }
    
        byte[] fileContent = Files.readAllBytes(file.toPath());
    
        // "암호화된 파일" 헤더가 있는지 확인
        byte[] header = "암호화된 파일".getBytes();
        for (int i = 0; i < header.length; i++) {
            if (fileContent[i] != header[i]) {
                throw new IllegalStateException("The file is not encrypted!");
            }
        }
    
        // Remove header before decryption
        byte[] encryptedContent = new byte[fileContent.length - header.length];
        System.arraycopy(fileContent, header.length, encryptedContent, 0, encryptedContent.length);
    
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedContent = cipher.doFinal(encryptedContent);
    
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(decryptedContent);
        }
        System.out.println("File decrypted successfully: " + filePath);
    }
    
}
