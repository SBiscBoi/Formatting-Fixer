/*
OBJECTIVE: Take in an annoyingly formatted .java file, and fix it to my personal style of formatting.
GOOD FORMATTING RULES:
    1: Semicolons should touch the end of a statement.  (Good ex. int x = 1;) (Bad ex. int x = 1 ;)
    2: Beginning Curly Braces should touch the end of the previous statement.  (Good ex. while(x > 1){ ) (Bad ex. while(x > 1) \n { )
    3: Beginning paranthesis should touch the end of the previous statement.  (Good ex. if(x > 1) ) (Bad ex. if (x > 1) )
    4: The first character inside paranthesis should be touching the first parenthesis.  (Good ex. (x > 1) ) (Bad ex. ( x > 1 ) )
    5: The last character inside paranthesis should be touching the last parenthesis.  (Good ex. (x > 1) ) (Bad ex. ( x > 1 ) )
    6: Every line within a set of Curly braces should be 1 tab ahead, with the closing brace being one tab behind and on it's own seperate line 1 below.  (no example needed)
*/

import java.io.FileReader;
import java.io.IOException;

public class FormattingFixer{
    public static void main(String[] args){
        String fileContent = ""; //content of the file
        try{
            FileReader reader = new FileReader("BadFormattingExample.java"); //INPUT FILE HERE
         
            //append each character to the content string
            int character;
            while((character = reader.read()) != -1){
                fileContent += (char)character;
        }
        reader.close();
        } catch(IOException e){
            e.printStackTrace();
        }
      
      
        System.out.println(fileContent);
   }
}
