import java.io.*;
import java.util.*;

public class BookApp {
    private static final List<Book> allBooks = new ArrayList<>();
    private static final List<PrintedBook> printedBooks = new ArrayList<>();
    private static final List<AudioBook> audioBooks = new ArrayList<>();

    // Accessors used by Book/other classes
    public static List<Book> getAllBooks() {
        return Collections.unmodifiableList(allBooks);
    }

    public static List<PrintedBook> getPrintedBooks() {
        return Collections.unmodifiableList(printedBooks);
    }

    public static List<AudioBook> getAudioBooks() {
        return Collections.unmodifiableList(audioBooks);
    }

    // helper used by PrintedBook/AudioBook register to also add into allBooks
    public static void addToAllBooks(Book b) {
        allBooks.add(b);
    }

    public void addPrintedBooks(PrintedBook p) {
        printedBooks.add(p);
        p.register(); // registers in allBooks and lastThreePrinted
    }

    public void addAudioBooks(AudioBook a) {
        audioBooks.add(a);
        a.register();
    }

    public Map<String, Integer> getBooksPerGenre() {
        Map<String, Integer> map = new HashMap<>();
        for (Book b : allBooks) {
            map.put(b.genre, map.getOrDefault(b.genre, 0) + 1);
        }
        return map;
    }

    public double getTotalCost() {
        double total = 0.0;
        for (PrintedBook p : printedBooks) total += p.getCost();
        for (AudioBook a : audioBooks) total += a.getCost();
        return total;
    }


    public void saveToFile(String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (Book b : allBooks) {
                if (b instanceof PrintedBook) {
                    PrintedBook p = (PrintedBook) b;
                    pw.printf("%s,%s,%s,PRINTED,%d%n",
                            escape(p.title), escape(p.author), escape(p.genre), p.getPages());
                } else if (b instanceof AudioBook) {
                    AudioBook a = (AudioBook) b;
                    pw.printf("%s,%s,%s,AUDIO,%d%n",
                            escape(a.title), escape(a.author), escape(a.genre), a.getLengthMinutes());
                }
            }
        }
    }

    public void loadFromFile(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                // simple CSV split (no complex quoting)
                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;
                String title = unescape(parts[0]);
                String author = unescape(parts[1]);
                String genre = unescape(parts[2]);
                String type = parts[3];
                int val = Integer.parseInt(parts[4]);
                if ("PRINTED".equalsIgnoreCase(type)) {
                    PrintedBook p = new PrintedBook(title, author, genre, val);
                    addPrintedBooks(p);
                } else if ("AUDIO".equalsIgnoreCase(type)) {
                    AudioBook a = new AudioBook(title, author, genre, val);
                    addAudioBooks(a);
                }
            }
        }
    }

    private String escape(String s) {
        return s.replace(",", ";"); // crude escaping for commas
    }

    private String unescape(String s) {
        return s.replace(";", ",");
    }

    // Demo Main
    public static void main(String[] args) {
        BookApp app = new BookApp();
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== BOOK APP MENU =====");
            System.out.println("1. Add Printed Book");
            System.out.println("2. Add Audio Book");
            System.out.println("3. Show Last 6 Books");
            System.out.println("4. Show Last 3 Printed Books");
            System.out.println("5. Show Last 3 Audio Books");
            System.out.println("6. Show Books Per Genre");
            System.out.println("7. Show Total Cost");
            System.out.println("8. Save to File");
            System.out.println("9. Load from File");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice = Integer.parseInt(in.nextLine().trim());
            System.out.println();

            switch (choice) {

                case 1: {
                    System.out.print("Title: ");
                    String title = in.nextLine();
                    System.out.print("Author: ");
                    String author = in.nextLine();
                    System.out.print("Genre: ");
                    String genre = in.nextLine();
                    System.out.print("Pages: ");
                    int pages = Integer.parseInt(in.nextLine());

                    PrintedBook p = new PrintedBook(title, author, genre, pages);
                    app.addPrintedBooks(p);
                    System.out.println("Printed book added.");
                    break;
                }

                case 2: {
                    System.out.print("Title: ");
                    String title = in.nextLine();
                    System.out.print("Author: ");
                    String author = in.nextLine();
                    System.out.print("Genre: ");
                    String genre = in.nextLine();
                    System.out.print("Length in minutes: ");
                    int length = Integer.parseInt(in.nextLine());

                    AudioBook a = new AudioBook(title, author, genre, length);
                    app.addAudioBooks(a);
                    System.out.println("Audio book added.");
                    break;
                }

                case 3: {
                    BookInterface bi = new BookInterface() {
                        @Override
                        public Map<String, Integer> getBooksPerGenre() {
                            return app.getBooksPerGenre();
                        }

                        @Override
                        public double getTotalCost() {
                            return app.getTotalCost();
                        }
                    };

                    bi.displayLastSixBooks(BookApp.getAllBooks());
                    break;
                }

                case 4:
                    PrintedBook.displayLastThreePrinted();
                    break;

                case 5:
                    AudioBook.displayLastThreeAudio();
                    break;

                case 6:
                    System.out.println("Books per genre:");
                    System.out.println(app.getBooksPerGenre());
                    break;

                case 7:
                    System.out.printf("Total cost of all books: $%.2f%n", app.getTotalCost());
                    break;

                case 8: {
                    System.out.print("Enter file name to save (e.g. books.csv): ");
                    String file = in.nextLine();
                    try {
                        app.saveToFile(file);
                        System.out.println("Saved.");
                    } catch (Exception ex) {
                        System.out.println("Error saving file.");
                    }
                    break;
                }

                case 9: {
                    System.out.print("Enter file name to load: ");
                    String file = in.nextLine();
                    try {
                        app.loadFromFile(file);
                        System.out.println("Loaded.");
                    } catch (Exception ex) {
                        System.out.println("Error loading file.");
                    }
                    break;
                }

                case 0:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

}
