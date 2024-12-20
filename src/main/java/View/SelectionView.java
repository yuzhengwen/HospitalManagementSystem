package View;

import java.util.List;

/**
 * A view that displays a list of items and allows the user to select one.
 *
 * @param <T> The type of the items in the list.
 */
public class SelectionView<T> extends ViewObject {

    private T selected;
    private boolean back = false;

    public SelectionView(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            int currentIndex = i;
            actions.add(new Action(list.get(i).toString(), () -> {
                selected = list.get(currentIndex);
                return 0;
            }));
        }
    }
    public SelectionView<T> allowBack() {
        actions.addFirst(new Action("Back", this::back));
        return this;
    }
    private int back() {
        back = true;
        return 0;
    }
    public boolean backSelected() {
        return back;
    }

    @Override
    public void display() {
        printActions();
        getInput();
    }

    public T getSelected() {
        return selected;
    }
}
