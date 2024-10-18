package Singletons;

import Model.User;

import java.util.ArrayList;

public class UserLoginManager {
    private ArrayList<User> users = new ArrayList<>();

    private static UserLoginManager instance;
    private UserLoginManager(){}
    public static synchronized UserLoginManager getInstance() {
        if (instance == null) {
            instance = new UserLoginManager();
        }
        return instance;
    }
    public User getUserById(String id){
        for(User user : users){
            if(user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

    public void addUser(User user){
        users.add(user);
    }
    public void removeUser(User user){
        users.remove(user);
    }
    public ArrayList<User> getUsers(){
        return users;
    }
}
