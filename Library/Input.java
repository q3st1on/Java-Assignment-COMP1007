package Assignment;
import java.io.*;
import java.util.*;

public class Input
{
    /********************************************************************
     * Class variables for the Assignment class:                        *
     * br (BufferedReader)  : BufferedReader object for user input      *
     ********************************************************************/
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /*****************************************************
     * Name   : getInput()                               *
     * Date   : 08/10/2024                               *
     * Import : None                                     * 
     * Export : value (String)                           *
     * Purpose: To get a string entered by the user      *
     *          even if it contains spaces               * 
     *****************************************************/
    public static String getInput() {
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
    private static boolean isNumeric(String inputString)
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
    public static int getInt()
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
    public static int getBoundedInt(int lower, int upper)
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

    public static void closeReader()
    {
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
