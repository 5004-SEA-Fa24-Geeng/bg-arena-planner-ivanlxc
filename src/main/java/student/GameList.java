package student;

import java.io.FileWriter;
import java.io.IOException;
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
        return gamesName.stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }


    @Override
    public void clear() {
        gamesName.clear();
    }


    @Override
    public int count() {
        return gamesName.size();
    }



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




    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty string for addToList");
        }

        String input = str.trim().toLowerCase();
        List<String> filteredNames = filtered
                .map(BoardGame::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());

        if (IGameList.ADD_ALL.equalsIgnoreCase(input)) {
            gamesName.addAll(filteredNames);
            return;
        }

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


        if (input.matches("\\d+")) {
            int index = Integer.parseInt(input);
            if (index <= 0 || index > filteredNames.size()) {
                throw new IllegalArgumentException("Invalid number: " + str);
            }
            gamesName.add(filteredNames.get(index - 1));
            return;
        }


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




    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty string for removeFromList");
        }
        String input = str.trim().toLowerCase();

        if (IGameList.ADD_ALL.equalsIgnoreCase(input)) {
            clear();
            return;
        }

        List<String> current = getGameNames();

        if (input.matches("\\d+-\\d+")) {
            String[] parts = input.split("-");
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            if (start <= 0 || end < start || end > current.size()) {
                throw new IllegalArgumentException("Invalid remove range: " + str);
            }
            for (int i = start; i <= end; i++) {
                gamesName.remove(current.get(i - 1));
            }
            return;
        }

        if (input.matches("\\d+")) {
            int index = Integer.parseInt(input);
            if (index <= 0 || index > current.size()) {
                throw new IllegalArgumentException("Invalid remove number: " + str);
            }
            gamesName.remove(current.get(index - 1));
            return;
        }

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
