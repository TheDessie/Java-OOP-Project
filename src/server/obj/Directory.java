package server.obj;

import java.util.List;

public class Directory {

    private List<User> users;

    public Directory() {
    }

    public Directory(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    // Връща обект User по дадено име
    public User getUserByName(String name) {
        int countOfUsers = users.size();

        for (int i = 0; i < countOfUsers; i++) {
            if (users.get(i).getName().equals(name)) {
                return users.get(i);
            }
        }
        return null; // This user does not exist!
    }

    // Добавя обект User към списъка от обекти users (добавя го само ако не съществува в списъка)
    public boolean addUser(String name, String password, boolean canRegisterToken, boolean canGetCardNumber) {

        User user = getUserByName(name);

        if (user == null) {
            user = new User(name, password, canRegisterToken, canGetCardNumber);
            users.add(user);
            return true;
        }
        else {
            return false;
        }
    }
}
