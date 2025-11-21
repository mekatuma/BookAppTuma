import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrintedBook extends Book {
    private int pages;

    private static final List<PrintedBook> allPrintedBooks = new ArrayList<>();
    private static final List<PrintedBook> lastThreePrinted = new ArrayList<>();

    public PrintedBook(String title, String author, String genre, int pages) {
        super(title, author, genre, 0.0);
        this.pages = pages;
        this.cost = getCost();
    }

    @Override
    public void storeBook(String title, String author, String genre, double cost) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.cost = cost;
    }

    @Override
    public double getCost() {
        return pages * 10.0;
    }

    public int getPages() {
        return pages;
    }

    // add to central lists
    public void register() {
        allPrintedBooks.add(this);
        BookApp.addToAllBooks(this);

        lastThreePrinted.add(this);
        if (lastThreePrinted.size() > 3) {
            lastThreePrinted.remove(0);
        }
    }

    public static double averagePages() {
        if (allPrintedBooks.isEmpty()) return 0.0;
        int total = 0;
        for (PrintedBook p : allPrintedBooks) total += p.pages;
        return (double) total / allPrintedBooks.size();
    }

    public static List<PrintedBook> getAllPrintedBooks() {
        return Collections.unmodifiableList(allPrintedBooks);
    }

    public static List<PrintedBook> getLastThreePrinted() {
        return Collections.unmodifiableList(lastThreePrinted);
    }

    public static void displayLastThreePrinted() {
        if (lastThreePrinted.isEmpty()) {
            System.out.println("No printed books recorded yet.");
            return;
        }
        System.out.println("Last three printed books:");
        for (PrintedBook p : lastThreePrinted) {
            System.out.println(p + " | pages: " + p.pages);
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | pages: %d", pages);
    }
}
