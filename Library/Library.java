package Library;
import java.io.*;
import java.util.*;

/*****************************************************
 * Author : Orlando Morris-Johnson (22222598)        *
 * Purpose: To store and manage information in the   *
 *          library system.                          *
 * Date   : 08/10/2024                               *
 *****************************************************/
public class Library
{
    
    /********************************************************************
     * Class variabled for the Assignment class:                        *
     * br (BufferedReader)  : BufferedReader object for user input      *
     * books (Book[])       : Array for books inn the library           *
     * authors (Author[])   : Array for authors of books in the libaray *
     * filePath (String)    : Location of the library data file         *
     * csvHeader (String[]) : Known good headers for the data file      *
     ********************************************************************/
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static Book[] books = new Book[1];
    static Author[] authors = new Author[1];

    /*****************************************************
     * Name   : getInput()                                *
     * Date   : 08/10/2024                               *
     * Import : None                                     * 
     * Export : value (String)                           *
     * Purpose: To get a string entered by the user      *
     *          even if it contains spaces               * 
     *****************************************************/
    private static String getInput() {
        // Flush output buffer
        System.out.flush();
  
        try
        {
            // Read line from BufferedReader
            return br.readLine();
        }
        catch (Exception e)
        {
            // Print error messages to console
            System.out.println("Error: " + e.getStackTrace());  
          
            // Recursively call getInput function
            return getInput();
        }
    }

    /*****************************************************
     * Name   : isNumeric()                              *
     * Date   : 08/10/2024                               *
     * Import : None                                     * 
     * Export : isNumeric (boolean)                      *
     * Purpose: check if string is purely numeric        * 
     *****************************************************/
    static boolean isNumeric(String inputString)
    {
        // Create char array of numbers
        char[] numeric = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        boolean isNumeric = true;

        // Loop through chars in string
        for (int i = 0; i < inputString.length(); i++)
        {
            boolean matchFound = false;

            // Check current char in string against each number
            for (char c : numeric)
            {
                if (inputString.charAt(i) == c)
                {
                    matchFound = true;
                }
            }

            isNumeric = matchFound;
        }

        return isNumeric;
    }

    /*****************************************************
     * Name   : getInt                                   *
     * Date   : 08/10/2024                               *
     * Import : None                                     * 
     * Export : value (int)                              *
     * Purpose: To get an integer entered by the user    *
     *****************************************************/
    static int getInt()
    { 

        // Try to read int from Scanner and return that
        String input = getInput();

        if (isNumeric(input))
        {
            return Integer.parseInt(input);
        }
        else
        { 
            // Prompt user to enter an actual int
            System.out.print("\nError: invalid input. Please enter an integer value\n> "); 

            // Recursively call getInt
            return getInt();
        } 
    }

    /*****************************************************
     * Name   : getBoundedInt                            *
     * Date   : 08/10/2024                               *
     * Import : lower (int), upper (int)                 * 
     * Export : value (int)                              *
     * Purpose: To get an integer entered by the user    * 
     *          that falls between lower and upper       * 
     *****************************************************/
    static int getBoundedInt(int lower, int upper)
    {
        // Get int from user
        int result = getInt();

        if (lower < result && result < upper)
        {
            // If int is in range, return it
            return result;
        }
        else
        {
            // If int not in range, prompt user for new int in the set range
            System.err.printf("Please enter an integer between %d and %d exclusive.\n", lower, upper);

            // Recursively call getBoundedInt
            return getBoundedInt(lower, upper);
        }
    }

    /*****************************************************
     * Name   : addAuthors                               *
     * Date   : 08/10/2024                               *
     * Import : newAuthor (Author)                       * 
     * Export : None                                     *
     * Purpose: To take a new author, if unique to add   *
     *          them to the array of authors and if not  *
     *          unique to add the books they have made   *
     *          to the existing entry for that author    * 
     *****************************************************/
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

    /*****************************************************
     * Name   : pruneAuthors                             *
     * Date   : 08/10/2024                               *
     * Import : checkAuthor (Author)                     * 
     * Export : None                                     *
     * Purpose: If an action has been taken that has the *
     *          possibility of having duplicated an      *
     *          author, this function is called to find  *
     *          and remove any created duplicates        * 
     *****************************************************/
    static void pruneAuthors(Author checkAuthor)
    {
        int checkAuthorHash, testAuthorIndex;
        checkAuthorHash = checkAuthor.hashCode();

        for (testAuthorIndex = 0; testAuthorIndex < authors.length; testAuthorIndex++)
        {
            if (authors[testAuthorIndex].hashCode() == checkAuthorHash)
            {
                if (!authors[testAuthorIndex].equals(checkAuthor))
                {
                    if (checkAuthor.getBooks() == authors[testAuthorIndex].getBooks())
                    {
                        deleteAuthor(testAuthorIndex);
                    }
                    else
                    {
                        int checkAuthorIndex, checkAuthorBooks, testAuthorBooks;

                        checkAuthorIndex = getAuthorIndex(checkAuthor);
                        checkAuthorBooks = checkAuthor.getBooks().length;
                        testAuthorBooks = authors[testAuthorIndex].getBooks().length;
                        
                        if (testAuthorBooks > checkAuthorBooks)
                        {
                            deleteAuthor(checkAuthorIndex);
                        }
                        else
                        {
                            deleteAuthor(testAuthorIndex);
                        }
                    }
                }
            }
        }
    }


    /*****************************************************
     * Name   : getAuthorIndex                           *
     * Date   : 08/10/2024                               *
     * Import : checkAuthor (Author)                     *
     * Export : None                                     *
     * Purpose: To find the index of a given author in   *
     *          authors array.                           *
     *****************************************************/
    static int getAuthorIndex(Author checkAuthor)
    {
        int index = 0;
        
        for (int i = 0; i < authors.length; i++) {
            if (authors[i].equals(checkAuthor))
            {
                index = i;
                i = authors.length;
            }
        }

        return index;
    }

    /*****************************************************
     * Name   : deleteAuthor                             *
     * Date   : 08/10/2024                               *
     * Import : authorIndex (int)                        *
     * Export : None                                     *
     * Purpose: To delete a given author from the        *
     *          authors array and move other authors so  *
     *          we can reduce the array size             *
     *****************************************************/
    static void deleteAuthor(int authorIndex)
    {
        int i;
        // Overwrite author at chosen position with null
        authors[authorIndex] = null;

        for (i = authorIndex; i < authors.length-1; i++)
        {
            // For each author from the chosen position to the final author
            // overwrite the author with the next author in the array
            authors[i] = authors[i+1];
        }

        authors[i] = null;

        Author[] authors2 = Arrays.copyOf(authors, authors.length-1);
        authors = authors2;

    }
    
    /*****************************************************
     * Name   : genBooks                                 *
     * Date   : 08/10/2024                               *
     * Import : CSVData (String[])                       *
     * Export : None                                     *
     * Purpose: To take a 2d array of a ibrary data file *
     *          and create the relevant Author and Book  *
     *          object instanced                         *
     *****************************************************/
    static void genBooks(String[][]csvData) throws IncorrectDataFileException
    {
        // If csv header is incorrect throw exception to be handle in main method.
        if (csvData[0].equals(csvHeader)) 
        {
            throw new IncorrectDataFileException("Incorrect Data File: CSV header is incorrect.");
        }

        // Loop through each array in CSV except the first
        for (int i = 1; i < csvData.length; i++) 
        {
            // Create new book instance 
            Book newBook = new Book();

            // Sets the information of the new book
            newBook.setTitle(csvData[i][0]);
            newBook.setYear(csvData[i][13]);
            newBook.setISBN(csvData[i][14]);
            newBook.setEbook(csvData[i][15].equals("TRUE"));
            newBook.setEdition(Integer.parseInt(csvData[i][16]));

            // Loop through each author field
            for (int j = 0; j < 3; j++)
            {
                // Check that author name field is not empty (if it is, don't create an author for that)
                if (csvData[i][1+(j*4)] != "")
                {
                    // Create new author instance
                    Author newAuthor = new Author();

                    // Set the information of the new author
                    newAuthor.setLastName(csvData[i][1 + (j*4)]);
                    newAuthor.setFirstName(csvData[i][2 + (j*4)]);
                    newAuthor.setNationality(csvData[i][3 + (j*4)]);
                    newAuthor.setBirthYear(csvData[i][4 + (j*4)]);
                    newAuthor.addBook(newBook);

                    // Add author to book
                    newBook.addAuthor(newAuthor);

                    // Call addAuthors to handle duplication of authors across books
                    addAuthors(newAuthor);
                }
            }

            if (i == 1)
            {
                // If first book, change first entry of books array from null to the new book
                books[0] = newBook;
            } 
            else
            {
                // If not first book, create a new copy of books array with 1 extra book at the end
                Book[] books1 = Arrays.copyOf(books, books.length + 1);

                // Overwrite books array with the new longer version
                books = books1;

                // Set book at the end to the new book we have made
                books[books.length-1] = newBook;
            }
        }
    }

    /*****************************************************
     * Name   : printMenu                                *
     * Date   : 08/10/2024                               *
     * Import : None                                     *
     * Export : value (int)                              *
     * Purpose: To print out the main library menu and   *
     *          get the users selected menu option       *
     *****************************************************/
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
            System.out.printf("%2d > %s\n", i+1, menuOptions[i]);
        }

        System.out.println("************************************");
        System.out.print("Your choice: ");
        
        return getBoundedInt(-1, menuOptions.length + 1);
    }

    /*****************************************************
     * Name   : bookPrinter                              *
     * Date   : 08/10/2024                               *
     * Import : bookArray (Book[])                       *
     * Export : None                                     *
     * Purpose: To print out the data of each book in an *
     *          array of books                           *
     *****************************************************/
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

    /*****************************************************
     * Name   : booksByAuthor                            *
     * Date   : 08/10/2024                               *
     * Import : None                                     *
     * Export : None                                     *
     * Purpose: To display the books by an author the    *
     *          user selects                             *
     *****************************************************/
    static void booksByAuthor()
    {
        System.out.println("************************************");
        System.out.println("          Books by Author           ");
        System.out.println("************************************");
        System.out.println();
        System.out.println("List of Authors:");

        for (int i = 0; i < authors.length; i++)
        {
            System.out.printf("%2d > %s\n", i+1, authors[i]);
        }
        System.out.println();
        System.out.print("Select an Author: ");

        bookPrinter(authors[getInt()-1].getBooks());

        System.out.println("************************************");
        System.out.println();
    }

    /*****************************************************
     * Name   : eBooks                                   *
     * Date   : 08/10/2024                               *
     * Import : None                                     *
     * Export : None                                     *
     * Purpose: To display all books which are ebooks    *
     *****************************************************/
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

    /*****************************************************
     * Name   : noneBooks                                *
     * Date   : 08/10/2024                               *
     * Import : None                                     *
     * Export : None                                     *
     * Purpose: To display all books which aren't ebooks *
     *****************************************************/
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

    /*****************************************************
     * Name   : addBook                                  *
     * Date   : 08/10/2024                               *
     * Import : None                                     *
     * Export : None                                     *
     * Purpose: To allow the user to enter information   *
     *          for a new book to be in the system       *
     *****************************************************/
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

    /*****************************************************
     * Name   : addAuthorPrompt                          *
     * Date   : 08/10/2024                               *
     * Import : Book book                                *
     * Export : None                                     *
     * Purpose: To allow the user to enter information   *
     *          for a new author to be in the system     *
     *****************************************************/
    static void addAuthorPrompt(Book book)
    {
        if (book.getAuthorCount() >= 3)
        {
            System.out.println("Error: Cannot add more than 3 authors!");
            System.out.println("       Please delete an author first.");
            return;
        }

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

    /*****************************************************
     * Name   : editAuthor                               *
     * Date   : 08/10/2024                               *
     * Import : int bookIndex                            *
     * Export : None                                     *
     * Purpose: To allow the user to select a speficic   *
     *          author of a book to edit                 *
     *****************************************************/
    static void editAuthor(int bookIndex)
    {
        System.out.println("************************************");
        System.out.println("            Edit Author             ");
        System.out.println("************************************");
        System.out.println();

        int authorIndex;
        boolean loop = true;

        System.out.println("List of Authors:");
        System.out.println();

        for (int i = 0; i < books[bookIndex].getAuthorCount(); i++)
        {
            System.out.printf("Author %d:\n", i+1);
            System.out.printf("%2d > Name: %s %s\n", i+1, books[bookIndex].getAuthors()[i].getFirstName(), books[bookIndex].getAuthors()[i].getLastName());
            System.out.printf("%2d > Nationality: %s\n", i+1, books[bookIndex].getAuthors()[i].getNationality());
            System.out.printf("%2d > Born: %s\n", i+1, books[bookIndex].getAuthors()[i].getBirthYear());
            System.out.println();
        }
        System.out.print("Select an Author: ");

        authorIndex = getBoundedInt(0, books[bookIndex].getAuthorCount() + 1) - 1;

        while (loop)
        {
            switch (printAuthorEditMenu(books[bookIndex].getAuthors()[authorIndex])) {
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
                    System.err.println("Please choose a valid menu option (enter 1-5)");
                    
            }
        }

        books[bookIndex].pruneAuthors(authorIndex);

        for(int i = 0; i < books[bookIndex].getAuthorCount(); i++)
        {
            pruneAuthors(books[bookIndex].getAuthors()[i]);
        }

    }

    /*****************************************************
     * Name   : printAuthorEditMenu                      *
     * Date   : 08/10/2024                               *
     * Import : author (Author)                          *
     * Export : None                                     *
     * Purpose: To allow the user to edit information    *
     *          for a specific author of a specific book *
     *****************************************************/
    static int printAuthorEditMenu(Author author)
    {
        System.out.println("************************************");
        System.out.println("            Edit Author             ");
        System.out.println("************************************"); 
        String[] menuOptions = {
            "Change First Name",
            "Change Last Name",
            "Change Birth Year",
            "Change Nationality",
            "Exit menu"
        };

        System.out.println();
        
        System.out.printf("Author:\n");
        System.out.printf(" Name: %s %s\n",author.getFirstName(), author.getLastName());
        System.out.printf(" Nationality: %s\n", author.getNationality());
        System.out.printf(" Born: %s\n", author.getBirthYear());

        System.out.println();
        System.out.println("What would you like to do?");

        for (int i = 0; i < menuOptions.length; i++)
        {
            System.out.printf("%2d > %s\n", i+1, menuOptions[i]);
        }
        System.out.println("************************************");

        System.out.print("Your choice: ");
        
        return getBoundedInt(0, menuOptions.length + 1);
    }
    
    /*****************************************************
     * Name   : printBookEditMenu                        *
     * Date   : 08/10/2024                               *
     * Import : Book book                                *
     * Export : None                                     *
     * Purpose: To allow the user to select a speficic   *
     *          book to edit                             *
     *****************************************************/
    static int printBookEditMenu(Book book)
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
            "Delete Book (and exit menu)",
            "Exit menu"
        };

        System.out.println();

        System.out.printf("Title: %s\n", book.getTitle());
        System.out.printf("Published: %s\n", book.getYear());
        System.out.printf("ISBN: %s\n", book.getISBN());
        System.out.printf("eBook: %b\n", book.isEbook());
        System.out.printf("Edition: %d\n", book.getEdition());

        for (int i = 0; i < book.getAuthorCount(); i++)
        {
            System.out.printf("Author:\n");
            System.out.printf(" Name: %s %s\n", book.getAuthors()[i].getFirstName(), book.getAuthors()[i].getLastName());
            System.out.printf(" Nationality: %s\n", book.getAuthors()[i].getNationality());
            System.out.printf(" Born: %s\n", book.getAuthors()[i].getBirthYear());
        }

        System.out.println();
        System.out.println("What would you like to do?");
        
        for (int i = 0; i < menuOptions.length; i++)
        {
            System.out.printf("%2d > %s\n", i+1, menuOptions[i]);
        }
        System.out.print("Your choice: ");
        
        return getBoundedInt(0, menuOptions.length + 1);
    }

    /*****************************************************
     * Name   : printBookEditMenu                        *
     * Date   : 08/10/2024                               *
     * Import : bookIndex (int)                          *
     * Export : None                                     *
     * Purpose: To allow the user to select a speficic   *
     *          author of a book to remove               *
     *****************************************************/
    static void removeAuthorPrompt(int bookIndex)
    {
        int chosen;
        for (int i = 0; i < books[bookIndex].getAuthorCount(); i++)
        {
            System.out.printf("Author %d:\n", i+1);
            System.out.printf("%2d > Name: %s\n", i+1, books[bookIndex].getAuthors()[i].toString());
            System.out.printf("%2d > Nationality: %s\n", i+1, books[bookIndex].getAuthors()[i].getNationality());
            System.out.printf("%2d > Born: %s\n", i+1, books[bookIndex].getAuthors()[i].getBirthYear());
        }

        System.out.println();
        System.out.print("Select an author to remove: ");

        chosen = getBoundedInt(0, books[bookIndex].getAuthorCount()+1) - 1;
        books[bookIndex].removeAuthor(chosen);
    }

    /*****************************************************
     * Name   : editBook                                 *
     * Date   : 08/10/2024                               *
     * Import : None                                     *
     * Export : None                                     *
     * Purpose: To allow the user to select a speficic   *
     *          book and edit it                         *
     *****************************************************/
    static void editBook()
    {
        int bookIndex;
        boolean loop = true;

        System.out.println("************************************");
        System.out.println("            Select Book             ");
        System.out.println("************************************"); 
        System.out.println();
        System.out.println("List of Books:");
        System.out.println();

        for (int i = 0; i < books.length; i++)
        {
            System.out.printf("Book %d:\n", i+1);
            System.out.printf("%2d > Title: %s\n", i+1, books[i].getTitle());
            System.out.printf("%2d > Published: %s\n", i+1, books[i].getYear());
            System.out.printf("%2d > ISBN: %s\n", i+1, books[i].getISBN());
            System.out.printf("%2d > eBook: %b\n", i+1, books[i].isEbook());
            System.out.printf("%2d > Edition: %d\n", i+1, books[i].getEdition());

            for (int j = 0; j < books[i].getAuthorCount(); j++)
            {
                System.out.printf("%2d > Author:\n", i+1);
                System.out.printf("%2d >  Name: %s %s\n", i+1, books[i].getAuthors()[j].getFirstName(), books[i].getAuthors()[j].getLastName());
                System.out.printf("%2d >  Nationality: %s\n", i+1, books[i].getAuthors()[j].getNationality());
                System.out.printf("%2d >  Born: %s\n", i+1, books[i].getAuthors()[j].getBirthYear());
            }
            System.out.println();
        }
        System.out.print("Select a book: ");

        bookIndex = getBoundedInt(0, books.length + 1) - 1;

        System.out.println();
        System.out.println("************************************"); 

        while (loop)
        {
            
            switch (printBookEditMenu(books[bookIndex])) {
                case 1:
                    addAuthorPrompt(books[bookIndex]);
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
                    deleteBook(bookIndex);
                    loop = false;
                    break;

                case 10:
                    loop = false;
                    break;

                default:
                    System.err.println("Please choose a valid menu option (enter 1-9)");
                    
            }
        }
        
        System.out.println("************************************");
        System.out.println();
    }

    /*****************************************************
     * Name   : deleteBook                               *
     * Date   : 08/10/2024                               *
     * Import : bookIndex (int)                          *
     * Export : None                                     *
     * Purpose: To allow the user to delete a speficic   *
     *          book                                     *
     *****************************************************/
    static void deleteBook(int bookIndex)
    {
        int i;
        books[bookIndex] = null;
        
        for (i = bookIndex; i < books.length-1; i++)
        {
            books[i] = books[i+1];
        }
        
        books[i] = null;

        Book[] newBooks = Arrays.copyOf(books, books.length-1);
        books = newBooks;
    }

    /*****************************************************
     * Name   : clearScreen                              *
     * Date   : 08/10/2024                               *
     * Import : None                                     *
     * Export : None                                     *
     * Purpose: implements clear screen easter egg       *
     *****************************************************/
    static void clearScreen()
    {
        System.out.print("\033[H\033[2J"); 
        System.out.flush();
    }

    /*****************************************************
     * Name   : printAllBooks                            *
     * Date   : 08/10/2024                               *
     * Import : None                                     *
     * Export : None                                     *
     * Purpose: prints all books                         *
     *****************************************************/
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

    /*****************************************************
     * Name   : printHelpPrompt                          *
     * Date   : 08/10/2024                               *
     * Import : None                                     *
     * Export : None                                     *
     * Purpose: prints usage help prompt                 *
     *****************************************************/
    static void printHelpPrompt()
    {
        System.out.println("Library system for COMP1007 (PDI) Assignment.");
        System.out.println("Written by Orlando Morris-Johnson (22222598).");
        System.out.println("");
        System.out.println("Usage:");
        System.out.println("\t-h\t--help\tDisplay this prompt.");
        System.out.println("\t-f\t--file\tSpecify library data file location (default: .\\StartingDataFile.csv)");
    }

    /*****************************************************
     * Name   : handleArgs                               *
     * Date   : 08/10/2024                               *
     * Import : args (String[])                          *
     * Export : value (boolean)                          *
     * Purpose: Handle user arguments (if supplied).     *
     *          Allows users to specify alternate data   *
     *          file locations.                          *
     *****************************************************/
    static boolean handleArgs(String[] args)
    {
        if (args.length == 0)
        {
            return true;
        }

        if ((args[0] == "-h") || (args[0] == "--help"))
        {
            printHelpPrompt();
            return false;
        }

        if ((args[0] == "-f") || (args[0] == "--file"))
        {
            if (args.length == 1) 
            {
                System.out.println("Error: please specify data file location when using file flag.");
                return false;
            }

            System.out.printf("Reading from data file %s\n", args[1]);
            try {
                File dataFile = new File(args[1]);
            
                if (dataFile.exists())
                {
                    filePath = args[1];
                    return true;
                }
                else
                {
                    System.out.printf("Error: Could not open file located at %s\n", args[1]);
                    return false;
                }
            }
            catch (Exception e)
            {
                System.out.printf("Error opening file %s:\n", args[1]);
                System.out.println(e.getMessage());
                return false;
            }
        }

        System.out.println("Error: unknown flags provided.");
        System.out.println("Printing usage information...");
        printHelpPrompt();
        return false;
    }

    /*****************************************************
     * Name   : main                                     *
     * Date   : 08/10/2024                               *
     * Import : None                                     *
     * Export : None                                     *
     * Purpose: read in data file, generate library book *
     *          arrays then loop through menu prompt     *
     *          until user exits.                        *
     *****************************************************/
    public static void main(String[] args)
    {
        if (handleArgs(args))
        {
            try
            {
                String[][] CSVData = csvReader();
                genBooks(CSVData);
    
                boolean loop = true;
                while (loop)
                {
                    switch (printMenu()) {
                        case 0:
                            clearScreen();
                            break;
    
                        case 1:
                            printAllBooks();
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
                            addBook();
                            writeCSV();
                            break;
            
                        case 6:
                            editBook();
                            writeCSV();
                            break;
    
                        case 7:
                            loop = false;
                            break;
                            
                        default:
                            System.err.println("Please choose a valid menu option (enter 1-7)");
                            
                    }
                }
                writeCSV();
                System.out.println("Thank you for using the system!"); 
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            br.close();
        }
        catch (Exception e)
        {
            System.err.println("Error closing BufferedReader");
            e.printStackTrace();
        }
    }
}