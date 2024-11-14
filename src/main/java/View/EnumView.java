package View;

/**
 * A view object that displays a menu of enum values and allows the user to select one.
 *
 * @param <E> The enum type to display
 */
public class EnumView<E extends Enum<E>> extends ViewObject {
    private E selected;

    public EnumView(Class<E> enumType) {
        E[] values = enumType.getEnumConstants(); // Retrieve all possible enum values

        for (int i = 0; i < values.length; i++) {  // Iterate through the enum constants
            int finalI = i;
            actions.add(new Action(
                    values[i].toString(), () -> {  // Lambda to handle user selection
                selected = values[finalI];  // Set the selected enum value
                return 0;  // Return 0 to exit the menu
            }
            ));
        }
    }

    @Override
    public void display() {
        System.out.println("Choose Type: ");
        printActions();
        getInput();
    }

    public E getSelected() {
        return selected;
    }
}
