package View;

import java.util.function.Supplier;

/**
 * This class is used to create an object that represents an action that can be taken by the user.
 * It is used in the Menu class to create a list of actions that the user can choose from.
 */
public class Action{
    public String description;
    public int choice;
    public Supplier<Integer> function;

    public Action(String description, Supplier<Integer> function){
        this.description = description;
        this.function = function;
    }
    public Action(String description, int choice){
        this.description = description;
        this.choice = choice;
    }
    public String getString(){
        return choice + ". " + description;
    }
    public void setChoice(int choice){
        this.choice = choice;
    }
}
