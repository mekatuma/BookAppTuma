import java.util.List;
import java.util.Map;

public interface BookInterface {

    default void displayLastSixBooks(List<Book> allBooks) {
        if (allBooks == null || allBooks.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        System.out.println("Last up to 6 books:");
        int start = Math.max(0, allBooks.size() - 6);
        for (int i = allBooks.size() - 1; i >= start; i--) {
            System.out.println(allBooks.get(i));
        }
        System.out.println();
    }

    Map<String, Integer> getBooksPerGenre();

    double getTotalCost();
}
