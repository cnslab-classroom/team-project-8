package src;

public class Data{
    int level;
    String name;

    Data(int _level, String _name) {
        level = _level;
        name = _name;
    }

    public int getLevel(){
        return level;
    }
    
    public String getName(){
        return name;
    }
}
