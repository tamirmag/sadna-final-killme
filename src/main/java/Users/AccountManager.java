package Users;

import DB.IUsersDB;
import Games.Player;
import Loggers.ActionLogger;
import Loggers.IActionLogger;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class AccountManager implements IAccountManager {

    final int UNKNOWN_RANK = 0;
    AtomicInteger usersCounter = new AtomicInteger(0);
    private final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private static final AccountManager instance = new AccountManager();

    private AccountManager() {
    }

    public static AccountManager getInstance() {
        return instance;
    }

    @Override
    public boolean isUserExists(String username) {
        return IUsersDB.getInstance().isExistUser(username);
    }

    @Override
    public UserManager register(String username, String password, String email, int wallet) throws UserAlreadyExists, PasswordNotValid, EmailNotValid, NegativeValue, UsernameNotValid {
        if (username == null || username.equals("") || username.contains(" "))
            throw new UsernameNotValid(username);
        else if (isUserExists(username)) throw new UserAlreadyExists(username);
        else if (password == null || password.equals("") || password.contains(" "))
            throw new PasswordNotValid(password == null ? "null" : password);
        else if (!isValidEmail(email))
            throw new EmailNotValid(email == null ? "null" : email);
        else if (wallet < 0)
            throw new NegativeValue(wallet);
        else {
            User u = new User(usersCounter.getAndIncrement(), username, password, UNKNOWN_RANK, email, new Wallet(wallet));
            IUsersDB.getInstance().register(u);
            IActionLogger.getInstance().writeToFile("user " + username + " successfully registered.");
            return new UserManager(u);
        }
    }

    @Override
    public boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    @Override
    public void logout(User u) throws AlreadyLoggedOut, UserNotExists {

    }

    public boolean isLoggedIn(String username) {
        return IUsersDB.getInstance().isLoggedIn(username);
    }

    @Override
    public void logout(String username) throws AlreadyLoggedOut, UserNotExists {
        if (username == null) throw new UserNotExists("null");
        else if (!isUserExists(username)) throw new UserNotExists(username);
        else if (!isLoggedIn(username)) throw new AlreadyLoggedOut(username);
        else {
            IUsersDB.getInstance().logout(username);
            ActionLogger.getInstance().writeToFile(username + " successfully logged out.");
        }
    }

    public boolean isUsernameAndPasswordMatch(String username, String password) {
        return IUsersDB.getInstance().isUsernameAndPasswordMatch(username, password);
    }

    @Override
    public UserManager login(String username, String password) throws UsernameNotValid, PasswordNotValid, UsernameAndPasswordNotMatch, AlreadyLoggedIn, UserNotExists {
        if (username == null || username.equals("") || username.contains(" "))
            throw new UsernameNotValid(username);
        else if (!isUserExists(username)) throw new UserNotExists(username);
        else if (password == null || password.equals("") || password.contains(" "))
            throw new PasswordNotValid(password == null ? "null" : password);
        else if (!isUsernameAndPasswordMatch(username, password))
            throw new UsernameAndPasswordNotMatch(username, password);
        else if (isLoggedIn(username))
            throw new AlreadyLoggedIn(username);
        else {
            User u = IUsersDB.getInstance().login(username);
            ActionLogger.getInstance().writeToFile(u.getUsername() + " successfully logged in.");
            return new UserManager(u);
        }
    }

    @Override
    public void setUsername(User u, String username) throws UsernameNotValid, UserAlreadyExists {
        if (username == null || username.equals("") || username.contains(" "))
            throw new UsernameNotValid(username);
        else if (isUserExists(username)) throw new UserAlreadyExists(username);
        else {
            IUsersDB.getInstance().setUsername(u.getUsername(), username);
        }
    }

    @Override
    public void setEmail(User u, String email) throws EmailNotValid {
        if (!isValidEmail(email))
            throw new EmailNotValid(email == null ? "null" : email);
        else {
            IUsersDB.getInstance().setEmail(u.getUsername(), email);
        }
    }

    @Override
    public void setPassword(User u, String password) throws PasswordNotValid {
        if (password == null || password.equals("") || password.contains(" "))
            throw new PasswordNotValid(password == null ? "null" : password);
        else {
            IUsersDB.getInstance().setPassword(u.getUsername(), password);
        }
    }

    @Override
    public int getUnknownLeague() {
        return UNKNOWN_RANK;
    }

    public User getUser(String username) {
        return IUsersDB.getInstance().getUser(username);
    }

    public void moveUserToLeague(String username, int newLeague) {
        IUsersDB.getInstance().moveUserToLeague(username, newLeague);
    }

    @Override
    public void updateNumOfGames(ArrayList<Player> players) {
        for (Player p : players) {
            User u = getUser(p.getName());
            int numOfGames = u.getNumOfGames();
            IUsersDB.getInstance().setNumOfGames(u.getUsername() ,numOfGames+1);
            if ((numOfGames + 1) % 10 == 0) {
                int formerLeague = u.getLeague();
                moveUserToLeague(u.getUsername(), formerLeague + 1);
            }
        }
    }

    @Override
    public void clearUsers() {
        IUsersDB.getInstance().deleteAllUsers();
    }

    @Override
    public User getLoggedInUser(String username) throws UserNotExists, UserNotLoggedIn {
        User u = IUsersDB.getInstance().getUser(username);
        if(u==null) throw new UserNotExists("null");
        else if (!u.isLoggedIn()) throw new UserNotLoggedIn(u.getUsername());
        return u;
    }
    /*********redundant functions**********/


    @Override
    public void clearLoggedInUsers() {

    }

    @Override
    public void clearLeagues() {

    }

    @Override
    public void addUser(User u) throws UserAlreadyExists, UsernameNotValid {

    }

    @Override
    public void addLoggedInUser(User u) throws AlreadyLoggedIn {

    }

}