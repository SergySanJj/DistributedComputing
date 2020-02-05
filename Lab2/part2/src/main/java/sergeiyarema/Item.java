package sergeiyarema;

public class Item {
    private static int maxId = 0;
    private int id;

    public Item() {
        id = maxId;
        maxId++;
    }

    public int getId() {
        return id;
    }
}
