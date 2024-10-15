package Library;

public class Printers {
    /*****************************************************
     * Name   : printHelpPrompt                          *
     * Date   : 08/10/2024                               *
     * Import : None                                     *
     * Export : None                                     *
     * Purpose: prints usage help prompt                 *
     *****************************************************/
    public static void printHelpPrompt()
    {
        System.out.println("Library system for COMP1007 (PDI) Assignment.");
        System.out.println("Written by Orlando Morris-Johnson (22222598).");
        System.out.println("");
        System.out.println("Usage:");
        System.out.println("\t-h\t--help\tDisplay this prompt.");
        System.out.println("\t-f\t--file\tSpecify library data file location (default: .\\StartingDataFile.csv)");
    }

    /*****************************************************
     * Name   : printAuthorEditMenu                      *
     * Date   : 08/10/2024                               *
     * Import : author (Author)                          *
     * Export : None                                     *
     * Purpose: To allow the user to edit information    *
     *          for a specific author of a specific book *
     *****************************************************/
    public static int printAuthorEditMenu(Author author)
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
    public static int printBookEditMenu(Book book)
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
}
