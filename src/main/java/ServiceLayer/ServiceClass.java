package ServiceLayer;


import Games.IGame;
import Games.Preferences;
import Users.*;
import java.util.ArrayList;

public class ServiceClass implements IServiceClass {
    @Override
    public ServiceUser register(String username, String password, String email, int wallet) throws EmailNotValid, NegativeValue, UsernameNotValid, UserAlreadyExists, PasswordNotValid {
        IAccountManager.getInstance().register(username, password, email, wallet);
        int league = IAccountManager.getInstance().getDefaultLeague();
        return new ServiceUser(username, password, email, new ServiceWallet(wallet), league);
    }

    @Override
    public ServiceUser login(String username, String password) throws UsernameAndPasswordNotMatch, AlreadyLoggedIn, UsernameNotValid, PasswordNotValid, UserNotExists {
        IUserManager u = IAccountManager.getInstance().login(username, password);
        int league = u.getUser().getLeague();
        ServiceWallet wallet = new ServiceWallet(u.getUser().getWallet().getAmountOfMoney());
        String email = u.getUser().getEmail();
        return new ServiceUser(username, password, email, wallet, league);
    }

    @Override
    public void logout(String username) throws UserNotExists, AlreadyLoggedOut {
        IAccountManager.getInstance().logout(username);
    }

    @Override
    public ArrayList<Integer> findSpectatableGames(String username) throws UserNotLoggedIn, UserNotExists {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        ArrayList<IGame> games = u.findActiveGamesBySpectatingPolicy(true);
        ArrayList<Integer> ret = new ArrayList<>();
        for (IGame i : games) ret.add(i.getId());
        return ret;
    }

    @Override
    public int createGame(String username, String gameType, int BuyInPolicy, int ChipPolicy,
                          int minimumBet, int minimalAmountOfPlayers,
                          int maximalAmountOfPlayers, boolean spectatingMode) throws UserNotLoggedIn, UserNotExists {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        Preferences p = new Preferences();
        p.setBuyInPolicy(BuyInPolicy);
        p.setChipPolicy(ChipPolicy);
        p.setMinBetPolicy(minimumBet);
        p.setMinAmountPolicy(minimalAmountOfPlayers);
        p.setMaxAmountPolicy(maximalAmountOfPlayers);
        p.setSpectatePolicy(spectatingMode);
        u.CreateGame(gameType, p);
        return 0;
    }

    @Override
    public void joinGame(int gamenum, String username) throws UserNotLoggedIn, UserNotExists {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        u.JoinGame(gamenum);
    }

    @Override
    public void spectateGame(int gamenum, String username) throws UserNotLoggedIn, UserNotExists {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        u.spectateGame(gamenum);
    }

    @Override
    public void viewReplay(int gamenum, String username) throws UserNotLoggedIn, UserNotExists {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        u.viewReplay(gamenum);
    }

    @Override
    public void saveFavoriteTurn(int gamenum, String username, String turn) throws UserNotLoggedIn, UserNotExists {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        u.addFavoriteTurn(turn);
    }

    @Override
    public ArrayList<Integer> findActiveGamesByPotSize(int potSize, String username) throws UserNotLoggedIn, UserNotExists {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        ArrayList<IGame> games = u.findActiveGamesByPotSize(potSize);
        ArrayList<Integer> ret = new ArrayList<>();
        for (IGame i : games) ret.add(i.getId());
        return ret;
    }

    @Override
    public void setDefaultLeague(String username, int defaultLeague) throws UserNotLoggedIn, UserNotExists, NegativeValue {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        u.setDefaultLeague(defaultLeague);
    }

    @Override
    public void setCriteria(String username, int criteria) throws UserNotLoggedIn, UserNotExists, NegativeValue {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        u.setCriteria();
    }

    @Override
    public void moveToLeague(String username, String userToMove, int league) throws UserNotLoggedIn, UserNotExists, LeagueNotExists, NegativeValue, UserAlreadyInLeague, UserNotInLeague {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        u.moveUserToLeague(userToMove, league);
    }

    @Override
    public void check(String username, int gameID) throws UserNotLoggedIn, UserNotExists {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        u.check(gameID);
    }

    @Override
    public void bet(String username, int gameID, int amount) throws UserNotLoggedIn, UserNotExists {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        u.bet(gameID, amount);
    }

    @Override
    public void raise(String username, int gameID, int amount) throws UserNotLoggedIn, UserNotExists {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        u.raise(gameID, amount);
    }

    @Override
    public void allIn(String username, int gameID) throws UserNotLoggedIn, UserNotExists {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        u.allIn(gameID);
    }

    @Override
    public void fold(String username, int gameID) throws UserNotLoggedIn, UserNotExists {
        IUserManager u = new UserManager(IAccountManager.getInstance().getLoggedInUser(username));
        u.fold(gameID);
    }



}