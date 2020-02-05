package sergeiyarema;


public class Item {
    private static int maxId = 0;
    private final int id;
    private final int price;

    public Item() {
        id = maxId;
        price = 1;
        maxId++;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }
}
