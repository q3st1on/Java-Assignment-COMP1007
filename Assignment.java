import java.io.*;
import java.util.*;

class Author
{
    private String familyName;
    private String firstName;
    private String nationality;
    private String birthYear;
    private Book[] books = new Book[1];

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
        if (books[0] == null)
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

    @Override
    public String toString()
    {
        String fullName =  String.format("%s %s", firstName, familyName);
        return String.format("Name: %-20s | Born: %-6s | Nationality: %-10s", fullName, birthYear, nationality);

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
    public void setAuthorCount(int newAuthorCount) { authorCount = newAuthorCount; }
    public void addAuthor(Author newAuthor) {
        if (authorCount < 3)
        {
            boolean unique = true;
            for (int i = 0; i < authorCount; i++)
            {
                if (newAuthor.hashCode() == authors[i].hashCode())
                {
                    unique = false;
                }
            }
            if (unique)
            {
                authors[authorCount] = newAuthor;
                authorCount ++;
            }
            else
            {
                System.out.println("Error: Author already added to book!");
            }
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

    public void removeAuthor(int i)
    {
        authors[i] = null;
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
    static String filePath = "StartingDataFile.csv";
    static String[] csvHeader;
    
    static void clearTerm() { // Clear whole terminal
        System.out.print("\033[2J"); // ANSI code to erase whole terminal
        System.out.print("\033[1;1H"); // ANSI code to move cursor to top left of terminal
    }

    private static String getInput() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
  
        System.out.flush();
  
        try
        {
            return input.readLine();
        } catch (Exception e)
        {
          return "Error: " + e.getMessage();
        }
    }

    static int getInt() { // Integer Input Func
        try {
            return sc.nextInt(); // Try to read int from Scanner and return that
        }
        catch (InputMismatchException e) { // Catch Input Mismatch
            System.out.print("\nError: invalid input. Please enter an integer value\n> "); // Prompt user to enter an actual int
            sc.next(); // Clear invalid input to avoid looping triggering of the catch block
            return getInt(); // Recursively call getInt till valid input is found and return that
        }
    }

    static int getBoundedInt(int lower, int upper)
    {
        int result = getInt();
        if (lower < result && result < upper)
        {
            return result;
        }
        else
        {
            System.out.printf("Please enter an integer between %d and %d exclusive.\n", lower, upper);
            return getBoundedInt(lower, upper);
        }
    }
    
    static String[][] csvReader()
    {
        File file = null;
        FileInputStream fs = null;
        InputStreamReader isr;
        BufferedReader br;
        String line;
        boolean empty = true;
        String[][] Data = new String[][] {{""}};
        
        try
        {
            file = new File(filePath);
            fs = new FileInputStream(file);
            isr = new InputStreamReader(fs);
            br = new BufferedReader(isr);
            line = br.readLine();
            while(line != null)
            {
                filePath = file.getAbsolutePath();
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
        if (authors[0] == null)
        {
            authors[0] = newAuthor;
        }
        else
        {
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

    static void writeCSVLine(String[] data, FileOutputStream file) throws IOException
    {
        int i;
        for (i = 0; i < (data.length - 1); i++)
        {
            file.write(data[i].getBytes());
            file.write(",".getBytes());
        }
        file.write(data[data.length-1].getBytes());
        file.write("\n".getBytes());
    }

    static void writeCSV() 
    {
        FileOutputStream fileStream = null;
        File tempFile = null;
        File outFile = null;
        try 
        {
            tempFile = new File("libraryDataFile.tmp");
            outFile = new File(filePath);
            fileStream = new FileOutputStream(tempFile);
            if (tempFile.exists())
            {
                writeCSVLine(csvHeader, fileStream);
                
                for (Book book : books)
                {
                    String[] array = new String[csvHeader.length];
                    array[0] = book.getTitle();
                    
                    for (int i = 0; i < 3; i++)
                    {
                        if (i < book.getAuthorCount())
                        {
                            array[1+(i*4)] = book.getAuthors()[i].getLastName();
                            array[2+(i*4)] = book.getAuthors()[i].getFirstName();
                            array[3+(i*4)] = book.getAuthors()[i].getNationality();
                            array[4+(i*4)] = book.getAuthors()[i].getBirthYear();
                        }
                        else
                        {
                            array[1+(i*4)] = "";
                            array[2+(i*4)] = "";
                            array[3+(i*4)] = "";
                            array[4+(i*4)] = "";
                        }
                    }
                    array[13] = book.getYear();
                    array[14] = book.getISBN();
                    if (book.isEbook())
                    {
                        array[15] = "TRUE";
                    }
                    else
                    {
                        array[15] = "FALSE";
                    }
                    array[16] = String.valueOf(book.getEdition());

                    writeCSVLine(array, fileStream);
                }
                fileStream.close();

                tempFile.renameTo(outFile);
            }
            else
            {
                throw new IOException("Temp file could not be created at: " + tempFile.getAbsolutePath());
            }

        }
        catch (IOException e)
        {
            if (fileStream != null)
            {
                try
                {
                    fileStream.close();
                }
                catch (IOException e2)
                {
                    System.err.println("Error while trying to close file Stream: "+ e2.getMessage());
                }
            }
            e.printStackTrace();
        }



    }
    
    static void genBooks(String[][]CSVData)
    {
        csvHeader = CSVData[0];

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
                if (CSVData[i][1+(j*4)] != "")
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
            "Delete Book",
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
        
        return getBoundedInt(0, menuOptions.length + 1);
    }

    static void bookPrinter(Book[] bookArray)
    {
        if (bookArray[0] == null)
        {
            System.out.println();
            System.out.println("No Books Found");
        }
        else
        {
            for (Book book : bookArray)
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
                    System.out.printf("  Name: %s %s\n", book.getAuthors()[i].getFirstName(), book.getAuthors()[i].getLastName());
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
        System.out.println();
        System.out.println("List of Authors:");

        for (int i = 0; i < authors.length; i++)
        {
            System.out.printf(" %d > %s\n", i+1, authors[i]);
        }
        System.out.println();
        System.out.print("Select an Author: ");

        bookPrinter(authors[getInt()-1].getBooks());

        System.out.println("************************************");
        System.out.println();
    }

    static void eBooks()
    {
        System.out.println("************************************");
        System.out.println("               eBooks               ");
        System.out.println("************************************");

        Book[] foundBooks = new Book[1];
        
        for (Book book : books)
        {
            if (book.isEbook())
            {
                if (foundBooks[0] == null)
                {
                    foundBooks[0] = book;
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
        System.out.println();
    }

    static void noneBooks()
    {
        System.out.println("************************************");
        System.out.println("             Non-eBooks             ");
        System.out.println("************************************");

        Book[] foundBooks = new Book[1];
        
        for (Book book : books)
        {
            if (!book.isEbook())
            {
                if (foundBooks[0] == null)
                {
                    foundBooks[0] = book;
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
        System.out.println();
    }

    static void addBook()
    {
        System.out.println("************************************");
        System.out.println("              Add Book              ");
        System.out.println("************************************"); 
        System.out.println();

        Book newBook = new Book();

        System.out.print("Enter Book Title: ");
        newBook.setTitle(getInput());
        System.out.print("Enter Book Year: ");
        newBook.setYear(String.valueOf(getInt()));
        System.out.print("Enter Book ISBN: ");
        newBook.setISBN(getInput());
        System.out.print("Enter Book Edition: ");
        newBook.setEdition(getInt());
        System.out.print("Is Book an eBook (y/n)? ");
        String reply = getInput().toLowerCase();
        if (reply.equals("y") || reply.equals("yes") || reply.equals("t") || reply.equals("true"))
        {
            newBook.setEbook(true);
        }
        else
        {
            newBook.setEbook(false);
        }
        System.out.print("How many Authors are there?");
        int authorCount = getBoundedInt(0, 4);
        for (int i = 0; i < authorCount; i++)
        {
            addAuthorPrompt(newBook);
        }

        if (books[0] == null)
        {
            books[0] = newBook;
        }
        else
        {
            Book[] newBooks = Arrays.copyOf(books, books.length + 1);
            books = newBooks;
            books[books.length-1] = newBook;
        }

        System.out.println("************************************");
        System.out.println();
    }

    static void addAuthorPrompt(Book book)
    {
        System.out.println("************************************");
        System.out.println("             Add Author             ");
        System.out.println("************************************");
        System.out.println();
        Author newAuthor = new Author();
        System.out.print("Enter Author First Name: ");
        newAuthor.setFirstName(getInput());
        System.out.print("Enter Author Last Name: ");
        newAuthor.setLastName(getInput());
        System.out.print("Enter Author Nationality: ");
        newAuthor.setNationality(getInput());
        System.out.print("Enter Author Birth Year: ");
        newAuthor.setBirthYear(String.valueOf(getInt()));
        newAuthor.addBook(book);
        book.addAuthor(newAuthor);
        addAuthors(newAuthor);
        System.out.println("************************************");

    }

    static void editAuthor(int bookIndex)
    {
        System.out.println("************************************");
        System.out.println("            Edit Author             ");
        System.out.println("************************************");
        System.out.println();

        int i, authorIndex;
        boolean loop = true;

        System.out.println("List of Authors:");
        System.out.println();

        for (i = 0; i < books[bookIndex].getAuthorCount(); i++)
        {
            System.out.printf("Author %d:\n", i+1);
            System.out.printf("%d > Name: %s %s\n", i+1, books[bookIndex].getAuthors()[i].getFirstName(), books[bookIndex].getAuthors()[i].getLastName());
            System.out.printf("%d > Nationality: %s\n", i+1, books[bookIndex].getAuthors()[i].getNationality());
            System.out.printf("%d > Born: %s\n", i+1, books[bookIndex].getAuthors()[i].getBirthYear());
            System.out.println();
        }
        System.out.print("Select an Author: ");

        authorIndex = getBoundedInt(0, books[bookIndex].getAuthorCount() + 1) - 1;

        while (loop)
        {
            switch (printAuthorEditMenu()) {
                case 1:
                    System.out.print("Enter new first name: ");
                    books[bookIndex].getAuthors()[authorIndex].setFirstName(getInput());
                    break;
    
                case 2:
                    System.out.print("Enter new last name: ");
                    books[bookIndex].getAuthors()[authorIndex].setLastName(getInput());
                    break;
    
                case 3:
                    System.out.print("Enter new birth year: ");
                    books[bookIndex].getAuthors()[authorIndex].setBirthYear(String.valueOf(getInt()));
                    break;
    
                case 4:
                    System.out.print("Enter new nationality: ");
                    books[bookIndex].getAuthors()[authorIndex].setNationality(getInput());
                    break;
    
                case 5:
                    loop = false;
                    break;

                default:
                    System.out.println("Please choose a valid menu option (enter 1-5)");
            }
        }
    }

    static int printAuthorEditMenu()
    {
        System.out.println("************************************");
        System.out.println("            Edit Author             ");
        System.out.println("************************************"); 
        String[] menuOptions = {
            "Change First Name",
            "Change Last Name",
            "Change Birth Year",
            "Change Nationality",
            "Exit"
        };        
        for (int i = 0; i < menuOptions.length; i++)
        {
            System.out.printf(" %d > %s\n", i+1, menuOptions[i]);
        }
        System.out.println("************************************");

        System.out.print("Your choice: ");
        
        return getBoundedInt(0, menuOptions.length + 1);
    }
    
    static int printBookEditMenu()
    {
        System.out.println("************************************");
        System.out.println("             Edit Book              ");
        System.out.println("************************************"); 
        String[] menuOptions = {
            "Add Author",
            "Remove Author",
            "Edit Author",
            "Change Title",
            "Change Year",
            "Change Edition",
            "Change ISBN",
            "Toggle eBook",
            "Exit"
        };

        System.out.println();
        System.out.println("What would you like to do?");
        
        for (int i = 0; i < menuOptions.length; i++)
        {
            System.out.printf(" %d > %s\n", i+1, menuOptions[i]);
        }
        System.out.print("Your choice: ");
        
        return getBoundedInt(0, menuOptions.length + 1);

    }

    static void removeAuthorPrompt(int bookIndex)
    {
        int i, chosen;
        for (i = 0; i < books[bookIndex].getAuthorCount(); i++)
        {
            System.out.print("Author:\n");
            System.out.printf(" Name: %s\n", books[bookIndex].getAuthors()[i].toString());
            System.out.printf(" Nationality: %s\n", books[bookIndex].getAuthors()[i].getNationality());
            System.out.printf(" Born: %s\n", books[bookIndex].getAuthors()[i].getBirthYear());
        }

        System.out.println();
        System.out.print("Select an author to remove: ");

        chosen = getBoundedInt(0, books[bookIndex].getAuthorCount()+1) - 1;
        books[bookIndex].removeAuthor(chosen);

        for (i = chosen; i < books[bookIndex].getAuthorCount()-1; i++)
        {
            books[bookIndex].overwriteAuthor(i, books[bookIndex].getAuthors()[i+1]);
        }

        books[bookIndex].removeAuthor(i);
        books[bookIndex].setAuthorCount(books[bookIndex].getAuthorCount()-1);
    }

    static void editBook()
    {
        int i, j, bookIndex;
        boolean loop = true;

        System.out.println("************************************");
        System.out.println("            Select Book             ");
        System.out.println("************************************"); 
        System.out.println();
        System.out.println("List of Books:");
        System.out.println();

        for (i = 0; i < books.length; i++)
        {
            System.out.printf("Book %d:\n", i+1);
            System.out.printf(" %d > Title: %s\n", i+1, books[i].getTitle());
            System.out.printf(" %d > Published: %s\n", i+1, books[i].getYear());
            System.out.printf(" %d > ISBN: %s\n", i+1, books[i].getISBN());
            System.out.printf(" %d > eBook: %b\n", i+1, books[i].isEbook());
            System.out.printf(" %d > Edition: %d\n", i+1, books[i].getEdition());
            System.out.printf(" %d > Authors: %d\n", i+1, books[i].getAuthorCount());

            for (j = 0; j < books[i].getAuthorCount(); j++)
            {
                System.out.printf(" %d > Author:\n", i+1);
                System.out.printf(" %d >  Name: %s %s\n", i+1, books[i].getAuthors()[j].getFirstName(), books[i].getAuthors()[j].getLastName());
                System.out.printf(" %d >  Nationality: %s\n", i+1, books[i].getAuthors()[j].getNationality());
                System.out.printf(" %d >  Born: %s\n", i+1, books[i].getAuthors()[j].getBirthYear());
            }
            System.out.println();
        }
        System.out.print("Select a book: ");

        bookIndex = getBoundedInt(0, books.length + 1) - 1;

        System.out.println();
        System.out.println("************************************"); 

        while (loop)
        {
            clearTerm();
            switch (printBookEditMenu()) {
                case 1:
                    if (books[bookIndex].getAuthorCount() >= 3)
                    {
                        System.out.println("Error: Cannot add more than 3 authors!");
                        System.out.println("       Please delete an author first.");
                        pauseExecution();
                    }
                    else
                    {
                        addAuthorPrompt(books[bookIndex]);
                    }
                    break;
    
                case 2:
                    removeAuthorPrompt(bookIndex);
                    break;
    
                case 3:
                    editAuthor(bookIndex);
                    break;
    
                case 4:
                    System.out.print("Enter new title: ");
                    books[bookIndex].setTitle(getInput());
                    break;
    
                case 5:
                    System.out.print("Enter new year of publication: ");
                    books[bookIndex].setYear(String.valueOf(getInt()));
                    break;
    
                case 6:
                    System.out.print("Enter new Edition: ");
                    books[bookIndex].setEdition(getInt());
                    break;
    
                case 7:
                    System.out.print("Enter new ISBN: ");
                    books[bookIndex].setISBN(getInput());
                    break;
                    
                case 8:
                    books[bookIndex].setEbook(!books[bookIndex].isEbook());
                    break;

                case 9:
                    loop = false;
                    break;

                default:
                    System.out.println("Please choose a valid menu option (enter 1-9)");
            }
        }
        
        System.out.println("************************************");
        System.out.println();
    }

    static void deleteBook()
    {
        int i, j, bookIndex;

        System.out.println("************************************");
        System.out.println("            Delete Book             ");
        System.out.println("************************************"); 
        System.out.println();
        System.out.println("List of Books:");

        for (i = 0; i < books.length; i++)
        {
            System.out.printf("Book %d:\n", i+1);
            System.out.printf(" %d > Title: %s\n", i+1, books[i].getTitle());
            System.out.printf(" %d > Published: %s\n", i+1, books[i].getYear());
            System.out.printf(" %d > ISBN: %s\n", i+1, books[i].getISBN());
            System.out.printf(" %d > eBook: %b\n", i+1, books[i].isEbook());
            System.out.printf(" %d > Edition: %d\n", i+1, books[i].getEdition());
            System.out.printf(" %d > Authors: %d\n", i+1, books[i].getAuthorCount());

            for (j = 0; j < books[i].getAuthorCount(); j++)
            {
                System.out.printf(" %d > Author:\n", i+1);
                System.out.printf(" %d >  Name: %s %s\n", i+1, books[i].getAuthors()[j].getFirstName(), books[i].getAuthors()[j].getLastName());
                System.out.printf(" %d >  Nationality: %s\n", i+1, books[i].getAuthors()[j].getNationality());
                System.out.printf(" %d >  Born: %s\n", i+1, books[i].getAuthors()[j].getBirthYear());
            }
        }
        System.out.println();
        System.out.print("Select a book to delete: ");

        bookIndex = getBoundedInt(0, books.length + 1) - 1;

        books[bookIndex] = null;
        
        for (i = bookIndex; i < books.length-1; i++)
        {
            books[i] = books[i+1];
        }
        
        books[i] = null;

        Book[] newBooks = Arrays.copyOf(books, books.length-1);
        books = newBooks;

        System.out.println("************************************");
        System.out.println();
    }

    static void printAllBooks()
    {
        System.out.println("************************************");
        System.out.println("             All Books              ");
        System.out.println("************************************"); 
        System.out.println();
        System.out.println("List of Books:");
        bookPrinter(books);
        System.out.println("************************************");
        System.out.println();
    }
    
    static void pauseExecution() { // Ask user to press a key to continue execution
        System.out.println("\nPress any key to continue...");
        sc.nextLine(); // hacky fix to make function work
        sc.nextLine();
    }

    public static void main(String[] args)
    {
        try
        {
            String[][] CSVData = csvReader();
            genBooks(CSVData);

            boolean loop = true;
            while (loop)
            {
                clearTerm();
                switch (printMenu()) {
                    case 1:
                        clearTerm();
                        printAllBooks();
                        pauseExecution();
                        break;
        
                    case 2:
                        clearTerm();
                        eBooks();
                        pauseExecution();
                        break;
        
                    case 3:
                        clearTerm();
                        noneBooks();
                        pauseExecution();
                        break;
        
                    case 4:
                        clearTerm();
                        booksByAuthor();
                        pauseExecution();
                        break;
        
                    case 5:
                        clearTerm();
                        addBook();
                        writeCSV();
                        pauseExecution();
                        break;
        
                    case 6:
                        clearTerm();
                        editBook();
                        writeCSV();
                        pauseExecution();
                        break;
        
                    case 7:
                        clearTerm();
                        deleteBook();
                        writeCSV();
                        pauseExecution();
                        break;

                    case 8:
                        loop = false;
                        break;
                        
                    default:
                        System.out.println("Please choose a valid menu option (enter 1-7)");
                }
            }
            writeCSV();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
