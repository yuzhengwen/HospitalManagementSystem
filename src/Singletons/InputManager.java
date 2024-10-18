package Singletons;

import Controller.Controller;

import java.util.Scanner;

public class InputManager {
    private static InputManager instance;
    private Scanner scanner = new Scanner(System.in);
    private InputManager(){}
    public static synchronized InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    public int getInt(){
        return scanner.nextInt();
    }
    public String getString(){
        return scanner.nextLine();
    }
    public int getInt(String message){
        System.out.println(message);
        return scanner.nextInt();
    }
    public String getString(String message){
        System.out.println(message);
        return scanner.nextLine();
    }
    public void goBackPrompt(){
        getString("Press enter to go back");
        Controller.getInstance().navigateBack();
    }
}
