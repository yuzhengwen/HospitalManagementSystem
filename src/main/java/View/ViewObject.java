package View;

import Singletons.InputManager;

/**
 * Abstract class for all views
 */
public abstract class ViewObject implements IView{
    public ActionsList actions = new ActionsList();

    public abstract void display();

    protected int getInput() { // get user input and handle it
        System.out.println("Enter your choice:");
        int choice = InputManager.getInstance().getInt();
        return handleInput(choice);
    }

    /**
     * Handle user input
     * @param choice user input
     * @return 0 to end the display loop, 1 to show the menu again
     */
    public int handleInput(int choice) {
        for (Action action : actions) {
            if (action.choice == choice) { // if choice matches, run the function
                return action.function.get();
            }
        }
        // if no choice is matched, show the menu again
        System.out.println("Invalid choice");
        System.out.println();
        return 1;
    }

    protected void printActions() {
        actions.printActions();
    }
}

