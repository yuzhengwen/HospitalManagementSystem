package View;

import Singletons.InputManager;

/**
 * Abstract class for all views
 */
public abstract class ViewObject implements IView{
    public ActionsList actions = new ActionsList();

    /**
     * {@inheritDoc}
     */
    public abstract void display();

    /**
     * Get user input and handle it (Calls @see handleInput)
     * @return 0 to end the display loop, 1 to show the menu again
     */
    protected int getInput() { // get user input and handle it
        System.out.println("Enter your choice:");
        int choice = InputManager.getInstance().getInt();
        return handleInput(choice);
    }

    /**
     * Handle user input<br/>
     * Runs the function associated with the choice
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

    /**
     * Print all actions in the actions list
     * @see ActionsList#printActions()
     */
    protected void printActions() {
        actions.printActions();
    }
}

