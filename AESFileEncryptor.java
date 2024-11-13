import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Scanner;

public class AESFileEncryptor {
    private SecretKey secretKey;

    public AESFileEncryptor(SecretKey key) {
        this.secretKey = key;
    }

    public AESFileEncryptor() throws Exception {
        this.secretKey = generateKey();
    }

    // 새로운 AES 키 생성
    public SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // Use 256-bit AES encryption
        return keyGen.generateKey();
    }

    // 파일에 키 저장
    public void saveKey(String keyFile) throws Exception {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        try (FileWriter writer = new FileWriter(keyFile)) {
            writer.write(encodedKey);
        }
    }

    // 파일로부터 키 불러오기
    public static SecretKey loadKey(String keyFile) throws Exception {
        String encodedKey = new String(Files.readAllBytes(new File(keyFile).toPath()));
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    // 텍스트 파일 내용 암호화
    public void encryptText(String txtFilePath) throws Exception {
        File txtFile = new File(txtFilePath);
        if (!txtFile.exists()) {
            throw new FileNotFoundException("The TXT file does not exist: " + txtFilePath);
        }

        byte[] fileContent = Files.readAllBytes(txtFile.toPath());
        System.out.println("Original file size: " + fileContent.length);

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedContent = cipher.doFinal(fileContent);

        System.out.println("Encrypted file size: " + encryptedContent.length);

        try (FileOutputStream fos = new FileOutputStream(txtFilePath)) {
            fos.write(encryptedContent);
        }
    }

    // 텍스트 파일 내용 복호화
    public void decryptText(String txtFilePath) throws Exception {
        File txtFile = new File(txtFilePath);
        if (!txtFile.exists()) {
            throw new FileNotFoundException("The TXT file does not exist: " + txtFilePath);
        }

        byte[] fileContent = Files.readAllBytes(txtFile.toPath());
        System.out.println("Encrypted file size: " + fileContent.length);

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedContent = cipher.doFinal(fileContent);

        System.out.println("Decrypted file size: " + decryptedContent.length);

        try (FileOutputStream fos = new FileOutputStream(txtFilePath)) {
            fos.write(decryptedContent);
        }
    }

    // CSV 파일 내용 암호화
    public void encryptCSV(String csvFilePath) throws Exception {
        File csvFile = new File(csvFilePath);
        if (!csvFile.exists()) {
            throw new FileNotFoundException("The CSV file does not exist: " + csvFilePath);
        }
    
        // csv 파일 내용 읽기
        byte[] fileContent = Files.readAllBytes(csvFile.toPath());
        System.out.println("Original file size: " + fileContent.length);
    
        // 내용 암호화
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedContent = cipher.doFinal(fileContent);
        System.out.println("Encrypted file size: " + encryptedContent.length);
    
        // 파일에 암호화된 내용 다시 쓰기
        try (FileOutputStream fos = new FileOutputStream(csvFilePath)) {
            fos.write(encryptedContent);
        }
    }

        // CSV 파일 내용 복호화
    public void decryptCSV(String csvFilePath) throws Exception {
        File csvFile = new File(csvFilePath);
        if (!csvFile.exists()) {
            throw new FileNotFoundException("The CSV file does not exist: " + csvFilePath);
        }
    
        // 암호화된 내용 읽기
        byte[] fileContent = Files.readAllBytes(csvFile.toPath());
        System.out.println("Encrypted file size: " + fileContent.length);
    
        // 내용 복호화
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedContent = cipher.doFinal(fileContent);
        System.out.println("Decrypted file size: " + decryptedContent.length);
    
        // 파일에 복호화된 내용 다시 쓰기
        try (FileOutputStream fos = new FileOutputStream(csvFilePath)) {
            fos.write(decryptedContent);
        }
    }
    

    // Main method for testing
    public static void main(String[] args) {
        String keyFilePath = "aes_key.txt"; // AES 키 저장할 파일 경로
        String txtPath = "ItemList.txt"; // 대상 텍스트 파일
        String csvPath = "ItemList.csv"; // 대상 csv 파일

        try {
            // Step 1: AES 키 로드하거나 생성
            SecretKey secretKey;
            File keyFile = new File(keyFilePath);

            if (keyFile.exists()) {
                secretKey = AESFileEncryptor.loadKey(keyFilePath);
                System.out.println("Existing AES Key loaded from " + keyFilePath);
            } else {
                AESFileEncryptor encryptor = new AESFileEncryptor();
                secretKey = encryptor.generateKey();
                encryptor.saveKey(keyFilePath);
                System.out.println("New AES Key generated and saved to " + keyFilePath);
            }

            // Step 2: 암호화 복호화 테스트
            AESFileEncryptor encryptor = new AESFileEncryptor(secretKey);

            System.out.println("1. 암호화");
            System.out.println("2. 복호화");
            System.out.print(">> ");
            Scanner sc = new Scanner(System.in);
            int menu = sc.nextInt();
            sc.close();
            
            if(menu==1) {
                // txt 암호화
                encryptor.encryptText(txtPath);
                System.out.println("Text File encrypted successfully: " + txtPath);
                // csv 암호화
                encryptor.encryptCSV(csvPath);
                System.out.println("CSV File encrypted successfully: " + csvPath);
            }

            if(menu==2) {
                // txt 복호화
                encryptor.decryptText(txtPath);
                System.out.println("Text File decrypted successfully: " + txtPath);
                // csv 복호화
                encryptor.decryptCSV(csvPath);
                System.out.println("CSV File decrypted successfully: " + csvPath);
            }

        } catch (Exception e) {
            System.out.println("Error Msg: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
