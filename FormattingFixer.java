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
        FileFormatter formatter = new FileFormatter("BadFormattingExample.java");
        
        formatter.formatFull(); //preform a full format

        System.out.println(formatter.getCurrentContent());
   }
}

class FileFormatter{
    //PIV's
    int cursor = 0; //position in the file/string, used to evaluate the current character
    String content = ""; //content of the file, stored as 1 string.

    //Constructors
    public FileFormatter(String filePath){ //takes in the path of a java file to format
        try{
            FileReader reader = new FileReader(filePath);
         
            //append each character to the content string
            int character;
            while((character = reader.read()) != -1){
                content += (char)character;
        }
        reader.close();
        } catch(IOException e){
            e.printStackTrace();
            System.exit(-1); //end execution, file could not be read.
        }
    }

    //Methods
    public String getCurrentContent() { return content; } //Return the current content of the file being formatted

    //preform a full format of the file, calling all formatting method
    public void formatFull(){
        formatSemicolons();
    }

    //fix semicolon formatting
    //made public to give the option of only formatting semicolons
    public void formatSemicolons(){
        cursor = 0; //reset cursor
        while(cursor < content.length()){ //run through whole file
            if(content.charAt(cursor) == ';'){ //if semicolon found...
                while(content.charAt(cursor-1) == ' '){
                    content = content.substring(0, cursor - 1) + content.substring(cursor); //split string to before the space, then append everything after the space to essentially take the space out.
                    cursor--; //go back a spot to get rid of more spaces if more are left
                }
            }
            cursor++; //move to next character
        }
    }
}