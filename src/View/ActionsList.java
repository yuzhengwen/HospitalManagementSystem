package View;

import java.util.ArrayList;

public class ActionsList extends ArrayList<Action> {
    @Override
    public boolean add(Action action) {
        action.setChoice(this.size() + 1);
        return super.add(action);
    }

    @Override
    public Action remove(int index) {
        for (int i = index; i < this.size(); i++) {
            this.get(i).setChoice(this.get(i).choice - 1);
        }
        return super.remove(index);
    }

    public void printActions() {
        this.sort((a1, a2) -> a1.choice - a2.choice);
        for (Action action : this) {
            System.out.println(action.getString());
        }
    }
}
