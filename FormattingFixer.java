/*
OBJECTIVE: Take in an annoyingly formatted .java file, and fix it to my personal style of formatting.
GOOD FORMATTING RULES:
    DONE 1: Semicolons should touch the end of a statement.  (Good ex. int x = 1;) (Bad ex. int x = 1 ;)
    2: Beginning Curly Braces should touch the end of the previous statement.  (Good ex. while(x > 1){ ) (Bad ex. while(x > 1) \n { )
    3: Beginning paranthesis should touch the end of the previous statement.  (Good ex. if(x > 1) ) (Bad ex. if (x > 1) )
    4: The first character inside paranthesis should be touching the first parenthesis.  (Good ex. (x > 1) ) (Bad ex. ( x > 1 ) )
    5: The last character inside paranthesis should be touching the last parenthesis.  (Good ex. (x > 1) ) (Bad ex. ( x > 1 ) )
    6: Every line within a set of Curly braces should be 1 tab ahead, with the closing brace being one tab behind and on it's own seperate line 1 below.  (no example needed)
    7: Only one space before and after '=' (no example needed)
    8: If curly braces are used to define array elements, then don't put the closing brace on a newline
    9: A proper tab is 4 spaces long.
*/

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class FormattingFixer{
    public static void main(String[] args){
        FileFormatter formatter = new FileFormatter("BadFormattingExample.java");
        
        formatter.formatFull(); //preform a full format

        System.out.println(formatter.getCurrentContent());
        //formatter.printWhitespace();
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
        formatCurlyBraces();
    }

    //fix semicolon formatting
    //made public to give the option of only formatting semicolons
    public void formatSemicolons(){
        cursor = 0; //reset cursor
        while(cursor < content.length()){ //run through whole file
            if(content.charAt(cursor) == ';'){ //if semicolon found...
                while(Character.isWhitespace(content.charAt(cursor-1))){ //use character class to see if the character behind the semicolon is any kind of whitespace
                    removeBehindCursor(); //remove space and patch up
                    cursor--; //go back a spot to get rid of more spaces if more are left
                }
            }
            cursor++; //move to next character
        }
    }

    //*FIXME*
    //fix curly brace formatting
    //made public to give the option of only formatting curly braces
    public void formatCurlyBraces(){
        byte tabCount = 0; //keep track of how many tabs are needed for everything before the closing braces

        //First, fix opening braces
        cursor = 0; //reset cursor
        while(cursor < content.length()){ //run through whole file
            if(content.charAt(cursor) == '{'){ //if opening brace found...
                tabCount++; //increase amount of tabs needed for later indenting the first found closing brace
                while(Character.isWhitespace(content.charAt(cursor-1))){
                    removeBehindCursor(); //remove space and patch up
                    cursor--; //go back a spot to get rid of more spaces if more are left
                }
            }
            cursor++; //move to next character
        }

        //Next, look for array assignments and fix accordingly
        cursor = 0; //reset cursor
        while(cursor < content.length()){
            if(content.charAt(cursor) == '{' && (content.charAt(cursor - 1) == '=' || 
                                                 content.charAt(cursor - 1) == '{' || 
                                                 content.charAt(cursor - 1) == ',')){
                insert(cursor, ' ');
                tabCount--; //disqualify this set of braces as tab-worthy
            }
            cursor++; //onward
        }

        //Next, fix closing braces, ensuring to not seperate braces used to declare arrays on newlines
        //First, put all closing braces on newlines EXCEPT ones used for array value declaration
        cursor = 0; //reset cursor
        while(cursor < content.length()){
            if(content.charAt(cursor) == '}'){
                //if there is a number behind the closing brace or a semicolon after, this is an array declaration and needs to be ignored
                if(Character.isDigit(content.charAt(cursor - 1)) || content.charAt(cursor + 1) == ';'){
                    //do nothing
                }
                else{
                    insert(cursor, '\n');
                    cursor++; //foward cursor by 1 to prevent infinite loops
                }
            }
            cursor++; //onward
        }

        //Next, remove all tabs/spaces in the beginning of the "lines" of the string and redo them based off the amount of } on their own lines.
        Scanner scanner = new Scanner(content);
        String tempContent = "";
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            line = line.trim();
            tempContent += line + "\n";        
        }
        scanner.close();
        content = tempContent;

        //Add tabs accordingly from the bottom-up
        String reverseContent = new StringBuilder(content).reverse().toString();
        Scanner revScanner = new Scanner(reverseContent);
        byte tabsToAdd = 0;
        tempContent = "";
        while(revScanner.hasNextLine()){
            String line = revScanner.nextLine();
            line = line.trim();
            int tabsAdded = 0;
            
            
            while(tabsAdded < tabsToAdd){
                line = line + "    ";
                tabsAdded++;
            }
            
            if(line.contains("}") && tabsToAdd < tabCount){
                tabsToAdd++;
                //line = line.trim();
            }

            tempContent += "\n" + line;
        }
        revScanner.close();
        content = new StringBuilder(tempContent).reverse().toString(); //reverse, reverse! *dj rewind noise*

        //Finally, fix first half of the code's indention.
        Scanner scanner2 = new Scanner(content);
        tempContent = "";
        byte tabsToKeep = 0;
        while(scanner2.hasNextLine()){
            String line = scanner2.nextLine();
            //ensure the lines with closing braces are untouched!
            if(line.contains("}") && !(line.matches(".*[a-z].*"))){
                tabsToKeep--;
            }
            else{
                line = line.trim();
                short spacesToKeep = (short)(tabsToKeep * 4);
                final int indentedLength = line.length() + spacesToKeep; //prevents infinite loop by referencing length pre-tab insertion.
                while(line.length() < indentedLength){
                    line = "    " + line;
                }

                if(!(new StringBuilder(line).isEmpty())){
                    if(line.charAt(line.length() - 1) == '{'){
                        tabsToKeep++;
                    }
                }
            }
                
            tempContent += line + "\n";
        }
        scanner2.close();
        content = tempContent;
    }
    
    //remove one chracter to the left of the cursor/current positon
    private void removeBehindCursor() { content = content.substring(0, cursor - 1) + content.substring(cursor); } 

    //insert a specified character into the file content at a specifed position
    private void insert(int index, char character) { content = content.substring(0, index) + character + content.substring(index); }

    //debug
    public void printWhitespace(){
        cursor = 0;
        while(cursor < content.length()){ //run through whole file
            if(Character.isWhitespace(content.charAt(cursor))){
                System.out.println("Location: " + cursor + " Value: " + (int)content.charAt(cursor));
            }
            cursor++; //move to next character
        }
    }
}