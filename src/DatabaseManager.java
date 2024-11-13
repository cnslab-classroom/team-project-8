package src;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    
    private List<Data> dataList;

    public void ReadCSVFile(String _fileName) {
        // _fileName의 csv파일을 읽어 dataList에 저장
        
        try{
            BufferedReader br = new BufferedReader(new FileReader(_fileName));

            String text = "";
            String line;
            while ((line = br.readLine()) != null) { 
                text += line + "\n";
            }

            br.close();
            
            System.out.print(text);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public DatabaseManager getInstance() {
        // 싱글톤

        if (instance == null)
            instance = this;
        
        return instance;
    }
}
