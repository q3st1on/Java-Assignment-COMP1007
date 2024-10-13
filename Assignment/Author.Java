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