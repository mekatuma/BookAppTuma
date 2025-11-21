import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AudioBook extends Book {
    private int lengthMinutes;

    private static final List<AudioBook> allAudioBooks = new ArrayList<>();
    private static final List<AudioBook> lastThreeAudio = new ArrayList<>();

    public AudioBook(String title, String author, String genre, int lengthMinutes) {
        super(title, author, genre, 0.0);
        this.lengthMinutes = lengthMinutes;
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
        return lengthMinutes * 5.0;
    }

    public int getLengthMinutes() {
        return lengthMinutes;
    }

    public void register() {
        allAudioBooks.add(this);
        BookApp.addToAllBooks(this);

        lastThreeAudio.add(this);
        if (lastThreeAudio.size() > 3) lastThreeAudio.remove(0);
    }

    public static double averageLength() {
        if (allAudioBooks.isEmpty()) return 0.0;
        int total = 0;
        for (AudioBook a : allAudioBooks) total += a.lengthMinutes;
        return (double) total / allAudioBooks.size();
    }

    public static List<AudioBook> getAllAudioBooks() {
        return Collections.unmodifiableList(allAudioBooks);
    }

    public static List<AudioBook> getLastThreeAudio() {
        return Collections.unmodifiableList(lastThreeAudio);
    }

    public static void displayLastThreeAudio() {
        if (lastThreeAudio.isEmpty()) {
            System.out.println("No audiobooks recorded yet.");
            return;
        }
        System.out.println("Last three audiobooks:");
        for (AudioBook a : lastThreeAudio) {
            System.out.println(a + " | length (min): " + a.lengthMinutes);
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | lengthMinutes: %d", lengthMinutes);
    }
}
