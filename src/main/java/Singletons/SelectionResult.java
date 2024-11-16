package Singletons;

public class SelectionResult<T> {
    private final T selected;
    private final boolean back;

    public SelectionResult(T selected, boolean back) {
        this.selected = selected;
        this.back = back;
    }

    public SelectionResult(T selected) {
        this(selected, false);
    }

    public T getSelected() {
        return selected;
    }

    public boolean isBack() {
        return back;
    }
}
