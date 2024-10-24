package View;

import Controller.Controller;

import java.util.ArrayList;

/**
 * A view that displays a list of items and allows the user to select one.
 *
 * @param <T> The type of the items in the list.
 */
public class SelectionView<T> extends ViewObject {

    private T selected;

    public SelectionView(ArrayList<T> list) {
        actions.add(new Action("Back", 0, () -> {
            Controller.getInstance().navigateBack();
        }));
        for (int i = 0; i < list.size(); i++) {
            int currentIndex = i;
            actions.add(new Action(list.get(i).toString(), i + 1, () -> {
                selected = list.get(currentIndex);
            }));
        }
    }

    @Override
    public void showMenu() {
        printActions();
        getInput();
    }

    public T getSelected() {
        return selected;
    }
}
