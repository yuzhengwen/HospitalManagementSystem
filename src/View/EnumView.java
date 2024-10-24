package View;

/**
 * A view object that displays a menu of enum values and allows the user to select one.
 * @param <E> The enum type to display
 */
public class EnumView<E extends Enum<E>> extends ViewObject {
    private E selected;
    private Class<E> enumType;

    public EnumView(Class<E> enumType) {
        this.enumType = enumType;
        E[] values = enumType.getEnumConstants(); // Retrieve all possible enum values

        for (int i = 0; i < values.length; i++) {  // Iterate through the enum constants
            int finalI = i;
            actions.add(new Action(
                    values[i].toString(), i + 1, () -> {  // Lambda to handle user selection
                        selected = values[finalI];  // Set the selected enum value
                    }
            ));
        }
    }

    @Override
    public void showMenu() {
        System.out.println("Choose Type: ");
        printActions();
        getInput();
    }

    public E getSelected() {
        return selected;
    }
}
