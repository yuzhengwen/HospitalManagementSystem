package View;

import java.util.Scanner;

/**
 * Abstract class for all views
 */
public abstract class ViewObject implements IView{
    public ActionsList actions = new ActionsList();

    public abstract void display();

    protected void getInput() {
        System.out.println("Enter your choice:");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        handleInput(choice);
    }

    public void handleInput(int choice) {
        for (Action action : actions) {
            if (action.choice == choice) {
                action.function.run();
                return;
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

