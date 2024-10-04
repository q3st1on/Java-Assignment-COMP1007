import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import javax.xml.crypto.Data;

class Author
{
    private String familyName;
    private String firstName;
    private String nationality;
    private String birthYear;
    private Book[] books = new Book[1];
    private boolean f1 = true;

    public String getFirstName() { return firstName; }
    public String getLastName() { return familyName; }
    public String getNationality() { return nationality; }
    public String getBirthYear() { return birthYear; }
    public Book[] getBooks() { return books; }

    
    public void setFirstName(String newFirstName) { firstName = newFirstName; }
    public void setLastName(String newLastName) { familyName = newLastName; }
    public void setNationality(String newNationality) { nationality = newNationality; }
    public void setBirthYear(String newBirthYear) { birthYear = newBirthYear; }

    public void addBook(Book newBook)
    {
        if (f1)
        {
            books[0] = newBook;
            f1 = false;
        }
        else
        {
            Book[] books1 = Arrays.copyOf(books, books.length + 1);
            books = books1;
            books[books.length-1] = newBook;
        }
    }

    @Override
    public String toString()
    {
        return String.format("%s %s", firstName, familyName);
    }

    @Override
    public int hashCode()
    {
        int hashCode = 1;
        try {
            hashCode *= familyName.hashCode() * firstName.hashCode() * nationality.hashCode() * birthYear.hashCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashCode;
    }

}

class Book
{
    private Author[] authors = new Author[3];
    private int authorCount = 0;
    private boolean ebook;
    private String title;
    private String isbn;
    private String year;
    private int edition;

    public Author[] getAuthors() { return authors; }
    public boolean isEbook() { return ebook; }
    public String getTitle() { return title; }
    public String getISBN() { return isbn; }
    public String getYear() { return year; }
    public int getEdition() { return edition; }
    public int getAuthorCount() { return authorCount; }

    public void setEbook(boolean isEbook) { ebook = isEbook; }
    public void setTitle(String newTitle) { title = newTitle; }
    public void setISBN(String newISBN) { isbn = newISBN; }
    public void setEdition(int newEdition) { edition = newEdition; }
    public void setYear(String newYear) { year = newYear; }
    public void addAuthor(Author newAuthor) {
        if (authorCount < 3)
        {
            authors[authorCount] = newAuthor;
            authorCount += 1;
        }
        else
        {
            System.err.println("Error: Cannot add more than 3 Authors to a book");
        }
    }

    public void overwriteAuthor(int i, Author newAuthor)
    {
        authors[i] = newAuthor;
    }
    
    @Override
    public String toString()
    {
        return String.format("Title: %-30s | Year: %-6s | Author: %-20s | ISBN: %-14s | Edition: %-3d | Ebook: %b ", title, year, authors[0], isbn, edition, ebook);
    }

}

public class Assignment
{
    static Scanner sc = new Scanner(System.in);

    static Book[] books = new Book[1];
    static Author[] authors = new Author[1];
    
    static int getInt()
    {
        boolean loop = true;
        int result;
        while (loop)
        {
            try
            {
                result = sc.nextInt();
                loop = false;
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid Input! Please enter an integer :)");
                sc.next();
            }
        }

        return result;
    }
    
    static String[][] csvReader(String csvPath)
    {
        FileInputStream fs = null;
        InputStreamReader isr;
        BufferedReader br;
        int i;
        String line;
        boolean empty = true;
        String[][] Data = new String[][] {{""}};
        
        try
        {
            fs = new FileInputStream(csvPath);
            isr = new InputStreamReader(fs);
            br = new BufferedReader(isr);
            i = 0;
            line = br.readLine();
            while(line != null)
            {
                i++;
                if (empty)
                {
                    Data[0] = line.split(",");
                    empty = false;
                } else
                {
                    String[][] Data1 = Arrays.copyOf(Data, Data.length + 1);
                    Data = Data1;
                    Data[Data.length-1] = line.split(",");
                }
                line = br.readLine();
            }
            fs.close();
        }
        catch (IOException e)
        {
            if (fs != null)
            {
                try
                {
                    fs.close();
                }
                catch (IOException e2)
                {
                    System.err.println("Error while trying to close file Stream: "+ e2.getMessage());
                }
            }
            System.out.println("Error in file reading: " + e.getMessage());
        }
        return Data;
    }

    static void addAuthors(Author newAuthor)
    {
        if (authors.length() == 1 && authors[0] == null)
        {
            authors[0] = newAuthor;
        }
        else
        {
            Author[] newAuthors = new Author[1];
            boolean unique = true;
            int newAuthorHash = newAuthor.hashCode();
            
            for (Author author : authors)
            {
                if (author.hashCode() == newAuthorHash)
                {
                    unique = false;
                    for (Book book : newAuthor.getBooks())
                    {
                        author.addBook(book);
                        for (int i = 0; i < book.getAuthorCount(); i++)
                        {
                            if (book.getAuthors()[i] == newAuthor)
                            {
                                book.overwriteAuthor(i, author);
                            }
                        }
                    }
                }
            }
    
            if (unique)
            {
                Author[] authors1 = Arrays.copyOf(authors, authors.length + 1);
                authors = authors1;
                authors[authors.length-1] = newAuthor;
            }
        }
    }
    
    static void genBooks(String[][]CSVData)
    {
        for (int i = 1; i < CSVData.length; i++)
        {
            Book newBook = new Book();

            newBook.setTitle(CSVData[i][0]);
            newBook.setYear(CSVData[i][13]);
            newBook.setISBN(CSVData[i][14]);
            newBook.setEbook(CSVData[i][15].equals("TRUE"));
            newBook.setEdition(Integer.parseInt(CSVData[i][16]));

            for (int j = 0; j < 3; j++)
            {
                if (CSVData[i][1+(j*4)] != null)
                {
                    Author newAuthor = new Author();

                    newAuthor.setLastName(CSVData[i][1 + (j*4)]);
                    newAuthor.setFirstName(CSVData[i][2 + (j*4)]);
                    newAuthor.setNationality(CSVData[i][3 + (j*4)]);
                    newAuthor.setBirthYear(CSVData[i][4 + (j*4)]);
                    newAuthor.addBook(newBook);

                    newBook.addAuthor(newAuthor);
                    addAuthors(newAuthor);
                }

            }

            if (i == 1)
            {
                books[0] = newBook;
            } 
            else
            {
                Book[] books1 = Arrays.copyOf(books, books.length + 1);
                books = books1;
                books[books.length-1] = newBook;
            }
        }
    }

    static int printMenu()
    {
        String[] menuOptions = {
            "View all Books",
            "View eBooks",
            "View non-eBooks",
            "View an author\'s Books",
            "Add Book",
            "Edit Book",
            "Exit"
        };

        System.out.println("************************************");
        System.out.println("       Welcome to the Library       ");
        System.out.println("************************************");
        
        for (int i = 0; i < menuOptions.length; i++)
        {
            System.out.printf(" %d > %s\n", i+1, menuOptions[i]);
        }

        System.out.println("************************************");
        System.out.print("Your choice: ");
        
        return getInt();
    }

    static void bookPrinter(Book[] books)
    {
        if (books.length == 1)
        {
            if (books[0] == null)
            {
                System.out.println();
                System.out.println("No Books Found");
            }
        }
        else
        {
            for (Book book : books)
            {
                System.out.println();
                System.out.println("Book:");
                System.out.printf(" Title: %s\n", book.getTitle());
                System.out.printf(" Published: %s\n", book.getYear());
                System.out.printf(" ISBN: %s\n", book.getISBN());
                System.out.printf(" eBook: %b\n", book.isEbook());
                System.out.printf(" Edition: %d\n", book.getEdition());
                System.out.printf(" Authors: %d\n", book.getAuthorCount());
    
                for (int i = 0; i < book.getAuthorCount(); i++)
                {
                    System.out.println(" Author:");
                    System.out.printf("  Name: %s\n", book.getAuthors()[i].toString());
                    System.out.printf("  Nationality: %s\n", book.getAuthors()[i].getNationality());
                    System.out.printf("  Born: %s\n", book.getAuthors()[i].getBirthYear());
                }
            }
        }
    }

    static void booksByAuthor()
    {
        System.out.println("************************************");
        System.out.println("          Books by Author           ");
        System.out.println("************************************");
        System.out.print(" Enter Author Name: ");
        String name = sc.next();

        Book[] foundBooks = new Book[1];
        boolean first = true;

        Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
        
        for (Book book : books)
        {
            boolean match = false;
            for (Author author : book.getAuthors())
            {
                if (pattern.matcher(author.toString()).find())
                {
                    match = true;
                }
            }

            if (match)
            {
                if (first)
                {
                    foundBooks[0] = book;
                    first = false;
                }
                else
                {
                    Book[] fb = Arrays.copyOf(foundBooks, foundBooks.length + 1);
                    foundBooks = fb;
                    foundBooks[foundBooks.length-1] = book;
                }
            }
        }

        bookPrinter(foundBooks);

        System.out.println("************************************");

    }

    static void eBooks()
    {
        System.out.println("************************************");
        System.out.println("               eBooks               ");
        System.out.println("************************************");

        Book[] foundBooks = new Book[1];
        boolean first = true;

        
        for (Book book : books)
        {
            if (book.isEbook())
            {
                if (first)
                {
                    foundBooks[0] = book;
                    first = false;
                }
                else
                {
                    Book[] fb = Arrays.copyOf(foundBooks, foundBooks.length + 1);
                    foundBooks = fb;
                    foundBooks[foundBooks.length-1] = book;
                }
            }
        }
    
        bookPrinter(foundBooks);

        System.out.println("************************************");

    }

    static void noneBooks()
    {
        System.out.println("************************************");
        System.out.println("             Non-eBooks             ");
        System.out.println("************************************");

        Book[] foundBooks = new Book[1];
        boolean first = true;

        
        for (Book book : books)
        {
            if (!book.isEbook())
            {
                if (first)
                {
                    foundBooks[0] = book;
                    first = false;
                }
                else
                {
                    Book[] fb = Arrays.copyOf(foundBooks, foundBooks.length + 1);
                    foundBooks = fb;
                    foundBooks[foundBooks.length-1] = book;
                }
            }
        }
    
        bookPrinter(foundBooks);

        System.out.println("************************************");

    }

    static void addBook()
    {
        System.out.println("************************************");
        System.out.println("              Add Book              ");
        System.out.println("************************************"); 
        System.out.println();

        Book newBook = new Book();

        System.out.print("Enter Book Title: ");
        newBook.setTitle(sc.next());
        System.out.print("Enter Book Year: ");
        newBook.setYear(sc.next());
        System.out.print("Enter Book ISBN: ");
        newBook.setISBN(sc.next());
        System.out.print("Enter Book Edition: ");
        newBook.setEdition(getInt());
        System.out.print("Is Book an eBook? ");
        String reply = sc.next().toLowerCase();
        if (reply.equals("y") || reply.equals("yes") || reply.equals("t") || reply.equals("true"))
        {
            newBook.setEbook(true);
        }
        else
        {
            newBook.setEbook(false);
        }
        System.out.print("How many Authors are there?");
        int authorCount = getInt();
        for (int i = 0; i < authorCount; i++)
        {
            Author newAuthor = new Author();
            System.out.print("Enter Author First Name: ");
            newAuthor.setFirstName(sc.next());
            System.out.print("Enter Author Last Name: ");
            newAuthor.setLastName(sc.next());
            System.out.print("Enter Author Nationality: ");
            newAuthor.setNationality(sc.next());
            System.out.print("Enter Author Birth Year: ");
            newAuthor.setBirthYear(sc.next());
            newAuthor.addBook(newBook);
            newBook.addAuthor(newAuthor);
            addAuthors(newAuthor);
        }

    }
    public static void main(String[] args)
    {
        try
        {
            String[][] CSVData =  csvReader("StartingDataFile.csv");
            genBooks(CSVData);
            boolean loop = true;
            while (loop)
            {
                switch (printMenu()) {
                    case 1:
                        bookPrinter(books);
                        break;
        
                    case 2:
                        eBooks();
                        break;
        
                    case 3:
                        noneBooks();
                        break;
        
                    case 4:
                        booksByAuthor();
                        break;
        
                    case 5:
                        break;
        
                    case 6:
                        break;
        
                    case 7:
                        loop = false;
                        break;
                        
                    default:
                        System.out.println("Please choose a valid menu option (enter 1-7)");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
