package View;

import Singletons.InputManager;

import java.util.Scanner;

/**
 * Abstract class for all views
 */
public abstract class ViewObject implements IView{
    public ActionsList actions = new ActionsList();

    public abstract void display();

    protected void getInput() { // get user input and handle it
        System.out.println("Enter your choice:");
        Scanner scanner = new Scanner(System.in);
        int choice = InputManager.getInstance().getInt();
        handleInput(choice);
    }

    public void handleInput(int choice) {
        for (Action action : actions) {
            if (action.choice == choice) { // if choice matches, run the function
                action.function.run();
                return; // exit method and get user input again
            }
        }
        // if no choice is matched, show the menu again
        System.out.println("Invalid choice");
        System.out.println();
        display();
    }

    protected void printActions() {
        actions.printActions();
    }
}

