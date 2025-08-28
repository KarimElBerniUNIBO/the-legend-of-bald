package com.thelegendofbald.model.item.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loader class for reading item spawn data files from resources.
 * Each line in the file should contain: ID row col
 */
public class MapItemLoader {

    /**
     * Legge un file item e restituisce la lista di item da spawnare.
     *
     * @param fileName Nome del file dentro resources/item_map/
     * @return Lista di ItemSpawnData
     * @throws IOException Se il file non può essere letto
     */
    public List<ItemSpawnData> load(final String fileName) throws IOException {
        final List<ItemSpawnData> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/item_map/" + fileName)))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                final String[] tokens = line.split("\\s+");
                if (tokens.length != 3) {
                    System.err.println("Formato riga non valido in " + fileName + ": " + line);
                    continue;
                }

                try {
                    final int id = Integer.parseInt(tokens[0]);
                    final int row = Integer.parseInt(tokens[1]);
                    final int col = Integer.parseInt(tokens[2]);
                    data.add(new ItemSpawnData(id, row, col));
                } catch (final NumberFormatException e) {
                    System.err.println("Errore di formato numerico: " + line);
                }
            }
        }

        return data;
    }
}
