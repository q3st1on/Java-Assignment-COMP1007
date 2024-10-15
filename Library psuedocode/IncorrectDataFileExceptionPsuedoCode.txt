package Library;
import java.io.*;
import java.util.*;

/*****************************************************
 * Author : Orlando Morris-Johnson (22222598)        *
 * Purpose: To create a throwable exception for an   * 
 *          incorrect data file                      * 
 * Date   : 08/10/2024                               *
 *****************************************************/
public class IncorrectDataFileException extends Exception { 
    public IncorrectDataFileException(String errorMessage) {
        super(errorMessage);
    }
}