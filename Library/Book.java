package Library;
import java.io.*;
import java.util.*;

/*****************************************************
 * Author : Orlando Morris-Johnson (22222598)        *
 * Purpose: To store and manage information about    *
 *          books.                                   * 
 * Date   : 08/10/2024                               *
 *****************************************************/
public class Book
{
    private Author[] authors = new Author[3];
    private int authorCount = 0;
    private boolean ebook;
    private String title;
    private String isbn;
    private String year;
    private int edition;

    //Getter functions to expose internal variables from book object
    public Author[] getAuthors() { return authors; }
    public boolean isEbook() { return ebook; }
    public String getTitle() { return title; }
    public String getISBN() { return isbn; }
    public String getYear() { return year; }
    public int getEdition() { return edition; }
    public int getAuthorCount() { return authorCount; }
    
    //Setter functions to expose internal variables from book object
    public void setEbook(boolean isEbook) { ebook = isEbook; }
    public void setTitle(String newTitle) { title = newTitle; }
    public void setISBN(String newISBN) { isbn = newISBN; }
    public void setEdition(int newEdition) { edition = newEdition; }
    public void setYear(String newYear) { year = newYear; }

    /*****************************************************
     * Name   : addAuthor                                *
     * Date   : 08/10/2024                               *
     * Import : newAuthor (Author)                       * 
     * Export : None                                     *
     * Purpose: To manage addition of authors to a book  *
     *          and ensure no more than three are added  * 
     *****************************************************/
    public void addAuthor(Author newAuthor) {
        // Check there is space for aditional author
        if (authorCount < 3)
        {
            // Boolean to represent if Author is unique
            boolean unique = true;

            // Loop through all current authors of book
            for (int i = 0; i < authorCount; i++)
            {
                // Check if new author is the same as existing author
                if (newAuthor.hashCode() == authors[i].hashCode())
                {
                    unique = false;
                }
            }

            if (unique)
            {
                // If author is unique, add them to the authors array
                authors[authorCount] = newAuthor;

                // Increment authorCount
                authorCount ++;
            }
            else
            {
                // Print error message that the chosen author is already added to the book
                System.err.println("Error: Author already added to book!");
            }
        }
        else
        {
            System.err.println("Error: Cannot add more than 3 Authors to a book");
        }
    }

    /*****************************************************
     * Name   : overwriteAuthor                          *
     * Date   : 08/10/2024                               *
     * Import : index (int), newAuthor (Author)          * 
     * Export : None                                     *
     * Purpose: To overwrite specific author instance in *
     *          the books authors array                  * 
     *****************************************************/
    public void overwriteAuthor(int index, Author newAuthor)
    {
        authors[index] = newAuthor;
    }

    /*****************************************************
     * Name   : removeAuthor                             *
     * Date   : 08/10/2024                               *
     * Import : index (int)                              * 
     * Export : None                                     *
     * Purpose: To remove specific author from authors   *
     *          array and move other authors if needed   * 
     *****************************************************/
    public void removeAuthor(int index)
    {        
        int i;

        // Overwrite author at chosen position with null
        authors[index] = null;

        for (i = index; i < authorCount-1; i++)
        {
            // For each author from the chosen position to the final author in the book
            // overwrite the author with the next author in the array
            overwriteAuthor(i, authors[i+1]);
        }

        System.out.println(i);
        authors[i] = null;
        authorCount -= 1;
    }
    
    /*****************************************************
     * Name   : pruneAuthors                             *
     * Date   : 08/10/2024                               *
     * Import : authorIndex (int)                        * 
     * Export : None                                     *
     * Purpose: To check if suspected duplicate author   * 
     *          is in the authors array twice and if so  *
     *          to remove the duplicate entry            *
     *****************************************************/
    public void pruneAuthors(int authorIndex)
    {
        boolean duplicate = false;

        // Loop through authors of book
        for (int i = 0; i < authorCount-1; i++)
        {
            // Ensure we are not checking the suspected entry against itself
            if (i != authorIndex)
            {
                // Check if authors are equal
                if (authors[i].hashCode() == authors[authorIndex].hashCode())
                {
                    // Mark duplicate as true if they are equal
                    duplicate = true;
                }
            }
        }

        // If the author is a duplicate, remove them from the array
        if (duplicate)
        {
            removeAuthor(authorIndex);
        }
    }

    /*****************************************************
     * Name   : toString                                 *
     * Date   : 08/10/2024                               *
     * Import : None                                     * 
     * Export : None                                     *
     * Purpose: Creates toString function for testing    * 
     *****************************************************/
    @Override
    public String toString()
    {
        return String.format("Title: %-30s | Year: %-6s | Author: %-20s | ISBN: %-14s | Edition: %-3d | Ebook: %b ", title, year, authors[0], isbn, edition, ebook);
    }
}