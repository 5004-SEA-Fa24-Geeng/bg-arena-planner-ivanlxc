package student;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The GameList implements the IGameList Interface.
 * This class manages a collection of board game names (as unique Strings),
 * and provides functionality to add or remove game names.
 */
public class GameList implements IGameList {

    /**
     * A Set to store the names of board games.
     * The names in the set are unique and are in insertion order.
     */
    private Set<String> gamesName;


    /**
     * Constructor for the GameList.
     */
    public GameList() {
        // Use a HashSet to store unique game names.
        gamesName = new HashSet<>();
    }

    /**
     * Gets the names of all games in the list, sorted in ascending order ignoring case.
     *
     * @return a List of game names, sorted case-insensitively in ascending order.
     */
    @Override
    public List<String> getGameNames() {
        // Convert the set to a stream, sort ignoring case, then collect into a List.
        return gamesName.stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    /**
     * Clear all names of game from the game list.
     */
    @Override
    public void clear() {
        gamesName.clear();
    }

    /**
     * Counts for the number of games in the list
     * @return the total number of game names in the list.
     */
    @Override
    public int count() {
        return gamesName.size();
    }

    /**
     * Saves the current list of game names to a file, one name per line,
     * in ascending order (case-insensitive).
     *
     * @param filename the name of the file to write to.
     */
    @Override
    public void saveGame(String filename) {
        // Obtain the sorted list of game names
        List<String> sorted = getGameNames();
        // Use a FileWriter to overwrite (false) the existing content, if any
        try (FileWriter fw = new FileWriter(filename, false)) {
            for (String name : sorted) {
                fw.write(name + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds games to the list from a given filtered stream of BoardGame objects.
     * @param str       The game name, index, or range in string format specifying what to add.
     * @param filtered the filtered Stream of BoardGame objects from which to pick names.
     * @throws IllegalArgumentException if the string is null, empty, invalid range/number,
     *                                  or no matching game is found.
     */
    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty string for addToList");
        }

        // Prepare the input string in lowercase for easier matching
        String input = str.trim().toLowerCase();

        // Convert the filtered stream to a List of names, sorted case-insensitively
        List<String> filteredNames = filtered
                .map(BoardGame::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());

        // If "all", simply add all names
        if (IGameList.ADD_ALL.equalsIgnoreCase(input)) {
            gamesName.addAll(filteredNames);
            return;
        }

        // Check if it's a range (e.g., "2-5")
        if (input.matches("\\d+-\\d+")) {
            String[] rangeParts = input.split("-");
            int start = Integer.parseInt(rangeParts[0]);
            int end = Integer.parseInt(rangeParts[1]);

            // Validate the range
            if (start <= 0 || end < start || end > filteredNames.size()) {
                throw new IllegalArgumentException("Invalid range: " + str);
            }

            // Add each game in the specified range
            for (int i = start; i <= end; i++) {
                gamesName.add(filteredNames.get(i - 1));
            }
            return;
        }

        // Check if it's a single number
        if (input.matches("\\d+")) {
            int index = Integer.parseInt(input);
            // Validate the index
            if (index <= 0 || index > filteredNames.size()) {
                throw new IllegalArgumentException("Invalid number: " + str);
            }
            gamesName.add(filteredNames.get(index - 1));
            return;
        }

        // Otherwise, treat it as a name (case-insensitive)
        boolean found = false;
        for (String gameName : filteredNames) {
            if (gameName.equalsIgnoreCase(str)) {
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
     * If the string is invalid, out of range, or does not match an existing game,
     * an IllegalArgumentException is thrown.
     * @param str The string to parse and remove games from the list.
     * @throws IllegalArgumentException if the string is null, empty, has an invalid
     *                                  range/number, or no matching game is found.
     */
    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty string for removeFromList");
        }

        // Convert to lowercase for comparisons
        String input = str.trim().toLowerCase();

        // If "all", clear the entire list
        if (IGameList.ADD_ALL.equalsIgnoreCase(input)) {
            clear();
            return;
        }

        // Get the current sorted list of names
        List<String> current = getGameNames();

        // Check if it's a range, e.g., "2-5"
        if (input.matches("\\d+-\\d+")) {
            String[] parts = input.split("-");
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            // Validate the range
            if (start <= 0 || end < start || end > current.size()) {
                throw new IllegalArgumentException("Invalid remove range: " + str);
            }
            // Remove each index-based item
            for (int i = start; i <= end; i++) {
                gamesName.remove(current.get(i - 1));
            }
            return;
        }

        // Check if it's a single number
        if (input.matches("\\d+")) {
            int index = Integer.parseInt(input);
            // Validate the index
            if (index <= 0 || index > current.size()) {
                throw new IllegalArgumentException("Invalid remove number: " + str);
            }
            gamesName.remove(current.get(index - 1));
            return;
        }

        // Otherwise, treat it as a name
        boolean removed = false;
        for (String g : current) {
            if (g.equalsIgnoreCase(str)) {
                gamesName.remove(g);
                removed = true;
                break;
            }
        }
        if (!removed) {
            throw new IllegalArgumentException("Game name not found in list: " + str);
        }
    }

}
