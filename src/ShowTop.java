package src;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShowTop {

    public void ShowTop100Level(String _serverName) {
        DatabaseManager DB = DatabaseManager.getInstance();

        if (DB.readServerFile(_serverName)) {
            // 레벨 기준 오름차순 정렬
            List<CharaData> top100Players = DB.playerDataList.stream()
                    .sorted(Comparator.comparingInt(CharaData::getCharacterLevel).reversed())
                    .limit(100)
                    .collect(Collectors.toList());

            // 상위 100명 출력
            top100Players.forEach(player -> 
    System.out.println("Name: " + player.getCharacterName() + 
                       ", Level: " + player.getCharacterLevel() + 
                       ", Class: " + player.getCharacterClassName()));
        }
    }

    
    public void ShowJobRatio(String _serverName) {
        // 서버 파일 데이터 읽기
        DatabaseManager DB = DatabaseManager.getInstance();

        if (DB.readServerFile(_serverName)) {

            // 직업 비율 계산
            Map<String, Integer> jobCountMap = new HashMap<>();
            for (CharaData player : DB.playerDataList) {
                String job = player.getCharacterClassName();
                jobCountMap.put(job, jobCountMap.getOrDefault(job, 0) + 1);
            }
    
            // 총 플레이어 수 계산
            int totalPlayers = jobCountMap.values().stream().mapToInt(Integer::intValue).sum();
    
            // 직업 비율 계산 및 정렬
            List<Map.Entry<String, Double>> sortedJobRatios = jobCountMap.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), (entry.getValue() * 100.0) / totalPlayers))
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .collect(Collectors.toList());
    
            // 결과 출력
            System.out.println("인기 직업:");
            sortedJobRatios.forEach(entry -> 
                System.out.printf("%s: %.2f%%\n", entry.getKey(), entry.getValue())
            );
        }
    }
    
}
