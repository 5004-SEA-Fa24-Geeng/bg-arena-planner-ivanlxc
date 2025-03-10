package student;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameList implements IGameList {


    private Set<String> gamesName;
    /**
     * Constructor for the GameList.
     */
    public GameList() {
        gamesName = new HashSet<>();
    }


    @Override
    public List<String> getGameNames() {
        List<String> gamesList =  List.copyOf(gamesName);

        return gamesList.stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }


    @Override
    public void clear() {
        gamesName.clear();
    }




    @Override
    public int count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void saveGame(String filename) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveGame'");
    }




    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToList'");
    }




    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFromList'");
    }


}
