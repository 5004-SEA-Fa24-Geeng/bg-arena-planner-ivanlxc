package student;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The GameList implements the IGameList interface.
 * Manages a collection of board game names (unique strings) and allows adding or removing names.
 */
public class GameList implements IGameList {
    /**
     * A Set to store the names of board games.
     * The names in the set are unique (insertion order is not guaranteed by HashSet).
     */
    private Set<String> gamesName;

    /** Constructor for GameList. Initializes an empty set of game names. */
    public GameList() {
        gamesName = new HashSet<>();
    }

    /**
     * Gets the names of all games in the list, sorted in ascending order ignoring case.
     * @return a List of game names sorted case-insensitively in ascending order.
     */
    @Override
    public List<String> getGameNames() {
        // Stream the set, sort ignoring case, and collect into a List
        return gamesName.stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    /** Clears all game names from the list. */
    @Override
    public void clear() {
        gamesName.clear();
    }

    /**
     * Returns the number of games in the list.
     * @return total count of game names stored.
     */
    @Override
    public int count() {
        return gamesName.size();
    }

    /**
     * Saves the current list of game names to a file, one name per line, in ascending order (case-insensitive).
     * @param filename the name of the file to write to.
     */
    @Override
    public void saveGame(String filename) {
        List<String> sorted = getGameNames();
        try (FileWriter fw = new FileWriter(filename, false)) {
            for (String name : sorted) {
                fw.write(name + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds games to the list based on the given input string and a filtered stream of BoardGame objects.
     * The input can be:
     * - "all" to add all games from the filtered stream,
     * - a range "X-Y" to add games from index X to Y (inclusive, 1-indexed) from the filtered list,
     * - a single number "N" to add the Nth game from the filtered list,
     * - or a game name (case-insensitive) to add that specific game.
     *
     * @param str      the selection string (name, index, range, or "all").
     * @param filtered the filtered Stream of BoardGame objects from which to pick names.
     * @throws IllegalArgumentException if the input is null/empty, an invalid range/index, or no matching game is found.
     */
    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty string for addToList");
        }
        String input = str.trim().toLowerCase();  // prepare input for case-insensitive comparison

        // Get a sorted list of game names from the filtered stream (for consistent ordering and indexing)
        List<String> filteredNames = filtered
                .map(BoardGame::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());

        if (IGameList.ADD_ALL.equalsIgnoreCase(input)) {
            // "all" -> add all filtered names
            gamesName.addAll(filteredNames);
            return;
        }
        // Check for a range input like "2-5"
        if (input.matches("\\d+-\\d+")) {
            String[] rangeParts = input.split("-");
            int start = Integer.parseInt(rangeParts[0]);
            int end = Integer.parseInt(rangeParts[1]);
            if (start <= 0 || end < start || end > filteredNames.size()) {
                throw new IllegalArgumentException("Invalid range: " + str);
            }
            for (int i = start; i <= end; i++) {
                gamesName.add(filteredNames.get(i - 1));
            }
            return;
        }
        // Check for a single index input like "3"
        if (input.matches("\\d+")) {
            int index = Integer.parseInt(input);
            if (index <= 0 || index > filteredNames.size()) {
                throw new IllegalArgumentException("Invalid number: " + str);
            }
            gamesName.add(filteredNames.get(index - 1));
            return;
        }
        // Otherwise, treat the input as a game name to add (case-insensitive match)
        boolean found = false;
        for (String gameName : filteredNames) {
            if (gameName.equalsIgnoreCase(input)) {
                gamesName.add(gameName);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("No matching game found for: " + str);
        }
    }

    /**
     * Removes games from the list based on the input string.
     * The input can be:
     * - "all" to remove all games from the list,
     * - a range "X-Y" to remove games from index X to Y (inclusive, 1-indexed) in the current list,
     * - a single number "N" to remove the Nth game in the current sorted list,
     * - or a game name (case-insensitive) to remove that specific game.
     *
     * @param str the selection string specifying which game(s) to remove.
     * @throws IllegalArgumentException if the input is null/empty, an invalid range/index, or the game is not found.
     */
    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty string for removeFromList");
        }
        String input = str.trim().toLowerCase();

        if (IGameList.ADD_ALL.equalsIgnoreCase(input)) {
            // "all" -> clear the entire list
            clear();
            return;
        }
        // Work with a sorted copy of current names for index-based removal
        List<String> currentList = getGameNames();
        if (input.matches("\\d+-\\d+")) {
            // Range removal
            String[] parts = input.split("-");
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            if (start <= 0 || end < start || end > currentList.size()) {
                throw new IllegalArgumentException("Invalid remove range: " + str);
            }
            for (int i = start; i <= end; i++) {
                gamesName.remove(currentList.get(i - 1));
            }
            return;
        }
        if (input.matches("\\d+")) {
            // Single index removal
            int index = Integer.parseInt(input);
            if (index <= 0 || index > currentList.size()) {
                throw new IllegalArgumentException("Invalid remove number: " + str);
            }
            gamesName.remove(currentList.get(index - 1));
            return;
        }
        // Name removal (case-insensitive match)
        boolean removed = false;
        for (String gameName : currentList) {
            if (gameName.equalsIgnoreCase(input)) {
                gamesName.remove(gameName);
                removed = true;
                break;
            }
        }
        if (!removed) {
            throw new IllegalArgumentException("Game name not found in list: " + str);
        }
    }
}
