package src;
public class Main {
	public static void main(String[] args) {
        
        Main m = new Main();

        AESFileEncryptor.testAESFileEncryptor();

	}

    public void TestDB() {
        DatabaseManager DB = new DatabaseManager();
        DB.ReadCSVFile(".\\src\\ItemList.txt");
    }
}