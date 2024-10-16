package Assignment;
import java.io.*;
import java.util.*;

/*****************************************************
 * Author : Orlando Morris-Johnson (22222598)        *
 * Purpose: To store and manage information about    *
 *          authors.                                 * 
 * Date   : 08/10/2024                               *
 *****************************************************/
public class Author
{
    private String familyName;
    private String firstName;
    private String nationality;
    private String birthYear;
    private Book[] books = new Book[1];

    //Getter functions to expose internal variables from author object
    public String getFirstName() { return firstName; }
    public String getLastName() { return familyName; }
    public String getNationality() { return nationality; }
    public String getBirthYear() { return birthYear; }
    public Book[] getBooks() { return books; }

    //Setter functions to expose internal variables from author object
    public void setFirstName(String newFirstName) { firstName = newFirstName; }
    public void setLastName(String newLastName) { familyName = newLastName; }
    public void setNationality(String newNationality) { nationality = newNationality; }
    public void setBirthYear(String newBirthYear) { birthYear = newBirthYear; }

    /*****************************************************
     * Name   : addBook                                  *
     * Date   : 08/10/2024                               *
     * Import : newBook (Book)                           * 
     * Export : None                                     *
     * Purpose: To manage addition of books to an author *
     *          object's book array                      * 
     *****************************************************/
    public void addBook(Book newBook)
    {
        if (books[0] == null)
        {
            // If no books in book array, set first entry in book array from null to the new book
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

    /*****************************************************
     * Name   : toString                                 *
     * Date   : 08/10/2024                               *
     * Import : None                                     * 
     * Export : value (String)                           *
     * Purpose: Creates toString function for testing    * 
     *****************************************************/
    @Override
    public String toString()
    {
        // Create a string for the authors full name.
        String fullName =  String.format("%s %s", firstName, familyName);

        // Return string with authors name year of birth and nationality
        return String.format("Name: %-20s | Born: %-6s | Nationality: %-10s", fullName, birthYear, nationality);

    }

    /*****************************************************
     * Name   : hashCode                                 *
     * Date   : 08/10/2024                               *
     * Import : None                                     * 
     * Export : hashCode (int)                           *
     * Purpose: Creates hashCode function for comparing  *
     *          Authors ignoring their book array        *  
     *****************************************************/
    @Override
    public int hashCode()
    {
        // Set hashcode to multiple of hashcodes for names, nationality and birthyear
        int hashCode = familyName.hashCode() * firstName.hashCode() * nationality.hashCode() * birthYear.hashCode();
        return hashCode;
    }
}