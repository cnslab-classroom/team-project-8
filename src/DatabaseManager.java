package src;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    
    private List<Data> dataList = new ArrayList<>();

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
            String[] split_text = text.split("\n");

            for (int i = 0; i < split_text.length; i++) {
                String[] realData = split_text[i].split(",");
                Data data = new Data(Integer.parseInt(realData[0]), realData[1]);
                dataList.add(data);
            }

            sortDataByLevel();
            printDataList();

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public static DatabaseManager getInstance() {
        // 싱글톤
        // !!! 현재 코드는 threadSafe하지 않음.

        if (instance == null)
            instance = new DatabaseManager();
        
        return instance;
    }
    
    public void sortDataByLevel() {
        DataSort.sortByLevel(dataList);
    }

    public void printDataList(){
        int rank = 1;
        for(Data data: dataList){
            System.out.println("Ranking" + rank + ": " +  "Name : " + data.getName() + " " + "Level : " + data.getLevel());
            rank++;
        }
    }

}
