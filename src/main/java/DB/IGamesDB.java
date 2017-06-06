package DB;

import Games.DeckManager;
import Games.Game;
import Games.IGame;
import Games.Policy;

/**
 * Created by tamir on 05/06/2017.
 */
public interface IGamesDB {
    IGame getGame(int id);

    void save(Policy game);
}
