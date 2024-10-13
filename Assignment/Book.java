package Assignment;

import java.io.*;
import java.util.*;

/*****************************************************
 * Author : Orlando Morris-Johnson (22222598)        *
 * Purpose: To store and manage information about    *
 *          books.                                   * 
 * Date   : 08/10/2024                               *
 *****************************************************/
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
                System.err.println("Error: Author already added to book!");
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