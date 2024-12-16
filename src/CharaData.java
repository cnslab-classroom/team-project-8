package src;

public class CharaData {

    private String ServerName;
    private String CharacterName;
    private int CharacterLevel;
    private String CharacterClassName;
    private String ItemAvgLevel;
    private String ItemMaxLevel;

    public CharaData(String serverName, String characterName, int characterLevel, String characterClassName, String itemAvgLevel, String itemMaxLevel) {
        this.ServerName = serverName;
        this.CharacterName = characterName;
        this.CharacterLevel = characterLevel;
        this.CharacterClassName = characterClassName;
        this.ItemAvgLevel = itemAvgLevel;
        this.ItemMaxLevel = itemMaxLevel;
    }

    // Getters and Setters
    public String getServerName() {
        return ServerName;
    }

    public void setServerName(String serverName) {
        ServerName = serverName;
    }

    public String getCharacterName() {
        return CharacterName;
    }

    public void setCharacterName(String characterName) {
        CharacterName = characterName;
    }

    public int getCharacterLevel() {
        return CharacterLevel;
    }

    public void setCharacterLevel(int characterLevel) {
        CharacterLevel = characterLevel;
    }

    public String getCharacterClassName() {
        return CharacterClassName;
    }

    public void setCharacterClassName(String characterClassName) {
        CharacterClassName = characterClassName;
    }

    public String getItemAvgLevel() {
        return ItemAvgLevel;
    }

    public void setItemAvgLevel(String itemAvgLevel) {
        ItemAvgLevel = itemAvgLevel;
    }

    public String getItemMaxLevel() {
        return ItemMaxLevel;
    }

    public void setItemMaxLevel(String itemMaxLevel) {
        ItemMaxLevel = itemMaxLevel;
    }

    @Override
    public String toString() {
        return "Character{" +
                "ServerName='" + ServerName + '\'' +
                ", CharacterName='" + CharacterName + '\'' +
                ", CharacterLevel=" + CharacterLevel +
                ", CharacterClassName='" + CharacterClassName + '\'' +
                ", ItemAvgLevel='" + ItemAvgLevel + '\'' +
                ", ItemMaxLevel='" + ItemMaxLevel + '\'' +
                '}';
    }

}