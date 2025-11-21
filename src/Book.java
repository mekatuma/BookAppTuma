import java.util.List;
import java.util.Map;
import java.util.HashMap;

public abstract class Book implements BookInterface {
    protected String title;
    protected String author;
    protected String genre;
    protected double cost;

    public Book(String title, String author, String genre, double cost) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.cost = cost;
    }

    public abstract void storeBook(String title, String author, String genre, double cost);

    @Override
    public double getTotalCost() {
        // Aggregate costs from BookApp lists
        double total = 0.0;
        for (PrintedBook p : BookApp.getPrintedBooks()) total += p.getCost();
        for (AudioBook a : BookApp.getAudioBooks()) total += a.getCost();
        return total;
    }

    public abstract double getCost();

    @Override
    public Map<String, Integer> getBooksPerGenre() {
        Map<String, Integer> map = new HashMap<>();
        for (Book b : BookApp.getAllBooks()) {
            map.put(b.genre, map.getOrDefault(b.genre, 0) + 1);
        }
        return map;
    }

    @Override
    public String toString() {
        return String.format("%s by %s | genre: %s | cost: $%.2f",
                title, author, genre, cost);
    }
}
