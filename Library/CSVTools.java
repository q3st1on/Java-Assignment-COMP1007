package Library;
import java.io.*;
import java.util.*;


public class CSVTools {
    private static String filePath = "StartingDataFile.csv";
    private static String[] csvHeader = {
        "title",
        "familyNameOne",  "firstNameOne",  "nationalityOne",  "birthYearOne",
        "familyNameTwo",  "firstNameTwo",  "nationalityTwo",  "birthYearTwo",
        "familyNameThree","firstNameThree","nationalityThree","birthYearThree",
        "year",
        "isbn",
        "ebook",
        "edition"
    };
    
    /*****************************************************
     * Name   : csvReader                                *
     * Date   : 08/10/2024                               *
     * Import : None                                     * 
     * Export : data (String[][])                        *
     * Purpose: To read in the contents of a csv file    * 
     *          and return them as a 2d string array     * 
     *****************************************************/
    public static String[][] csvReader()
    {
        File file = null;
        FileInputStream fs = null;
        InputStreamReader isr;
        BufferedReader br;
        String line;
        boolean empty = true;
        String[][] data = new String[][] {{""}};
        
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
                    data[0] = line.split(",");
                    empty = false;
                } else
                {
                    String[][] data1 = Arrays.copyOf(data, data.length + 1);
                    data = data1;
                    data[data.length-1] = line.split(",");
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
            System.err.println("Error in file reading: " + e.getMessage());
        }
        return data;
    }

    /*****************************************************
     * Name   : writeCSVLine                             *
     * Date   : 08/10/2024                               *
     * Import : data (String[]), file (FileOutputStream) *
     * Export : None                                     *
     * Purpose: To take an array of strings and write it *
     *          to a line of a CSV file                  *
     *****************************************************/
    private static void writeCSVLine(String[] data, FileOutputStream file) throws IOException
    {
        for (int i = 0; i < (data.length - 1); i++)
        {
            file.write(data[i].getBytes());
            file.write(",".getBytes());
        }
        file.write(data[data.length-1].getBytes());
        file.write("\n".getBytes());
    }

    /*****************************************************
     * Name   : writeCSV                                 *
     * Date   : 08/10/2024                               *
     * Import : None                                     *
     * Export : None                                     *
     * Purpose: To write out the library data to the CSV *
     *          data file                                *
     *****************************************************/
    public static void writeCSV(Book[] books) 
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
                fileStream.flush();
                fileStream.close();

                tempFile.renameTo(outFile);
            }
            else
            {
                fileStream.flush();
                fileStream.close();
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

}
