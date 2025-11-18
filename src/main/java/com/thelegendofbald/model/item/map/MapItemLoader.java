package com.thelegendofbald.model.item.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.thelegendofbald.utils.LoggerUtils;

/**
 * Loader class for reading item spawn data files from resources.
 * Each line in the file should contain: ID row col
 */
public class MapItemLoader {

    /**
     * Read file item spawn data from resources.
     *
     * @param fileName Filename to read
     * @return List of ItemSpawnData read from file 
     * @throws IOException if file cannot be read
     */
    public List<ItemSpawnData> load(final String fileName) throws IOException {
        final List<ItemSpawnData> data = new ArrayList<>();

        final String resourcePath = "/item_map/" + fileName;
        final InputStream stream = MapItemLoader.class.getResourceAsStream(resourcePath);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            while (line != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                final String[] tokens = line.split("\\s+");
                if (tokens.length != 3) {
                    LoggerUtils.error("Invalid line in item file: " + line);
                    continue;
                }

                try {
                    final int id = Integer.parseInt(tokens[0]);
                    final int row = Integer.parseInt(tokens[1]);
                    final int col = Integer.parseInt(tokens[2]);
                    data.add(new ItemSpawnData(id, row, col));
                } catch (final NumberFormatException e) {
                    LoggerUtils.error("Invalid number format in line: " + line);
                }
                line = reader.readLine();
            }
        }
        return data;
    }
}
