package com.thelegendofbald.model.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import static org.yaml.snakeyaml.nodes.Tag.MAP;
import org.yaml.snakeyaml.representer.Representer;

import com.thelegendofbald.model.common.Timer.TimeData;
import com.thelegendofbald.utils.LoggerUtils;

/**
 * DataManager is responsible for loading and saving game runs to a YAML file.
 * It uses SnakeYAML for serialization and deserialization of GameRun objects.
 */
public final class DataManager {

    private static final String SAVE_FILE_DIRECTORY = "game_data" + File.separator + "runs";
    private static final String SAVE_FILE_PATH = SAVE_FILE_DIRECTORY + File.separator + "users_data.yml";

    private final Yaml yaml;

    /**
     * Constructs a DataManager instance with configured YAML options.
     * Initializes the YAML parser with custom settings for serialization and
     * deserialization.
     */
    public DataManager() {
        final DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setPrettyFlow(true);

        final LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setAllowDuplicateKeys(false);

        final Constructor constructor = new Constructor(List.class, loaderOptions);

        final TypeDescription gameRunDescription = new TypeDescription(GameRun.class);
        constructor.addTypeDescription(gameRunDescription);

        final Representer representer = new Representer(dumperOptions);
        representer.setPropertyUtils(new PropertyUtils());

        representer.addClassTag(GameRun.class, MAP);
        representer.addClassTag(TimeData.class, MAP);

        yaml = new Yaml(constructor, representer, dumperOptions, loaderOptions);
        yaml.setBeanAccess(BeanAccess.FIELD);
    }

    /**
     * Loads game runs from the YAML file.
     * If the file does not exist or is empty, returns an empty list.
     *
     * @return a list of GameRun objects loaded from the YAML file.
     */
    public List<GameRun> loadGameRuns() {
        final List<?> rawList;
        try (InputStream input = new FileInputStream(SAVE_FILE_PATH)) {
            rawList = yaml.load(input);
        } catch (final IOException e) {
            return new ArrayList<>();
        }
        final List<GameRun> runs = new ArrayList<>();

        Optional.ofNullable(rawList).ifPresent(list -> {
            list.stream()
                    .filter(obj -> obj instanceof GameRun || obj instanceof Map)
                    .forEach(obj -> {
                        switch (obj) {
                            case GameRun gr -> runs.add(gr);
                            case Map<?, ?> map -> {
                                final String name = (String) map.get("name");
                                final Object timedataObj = map.get("timedata");
                                if (timedataObj instanceof Map tdMap) {
                                    final int hours = (int) tdMap.get("hours");
                                    final int minutes = (int) tdMap.get("minutes");
                                    final int seconds = (int) tdMap.get("seconds");
                                    final GameRun gr = new GameRun(name, new TimeData(hours, minutes, seconds));
                                    runs.add(gr);
                                }
                            }
                            default -> {
                            }
                        }
                    });
        });

        return runs;
    }

    /**
     * Saves a GameRun object to the YAML file.
     * If the file does not exist, it will be created.
     *
     * @param gameRun the GameRun object to save.
     * @throws IOException if an error occurs while writing to the file.
     */
    public void saveGameRun(final GameRun gameRun) throws IOException {
        final List<GameRun> gameRuns = loadGameRuns();
        gameRuns.add(gameRun);

        final File saveFile = new File(SAVE_FILE_PATH);
        final File parentDir = saveFile.getParentFile();

        if (Optional.ofNullable(parentDir).isPresent() && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
        }

        try (FileWriter writer = new FileWriter(saveFile, StandardCharsets.UTF_8)) {
            yaml.dump(gameRuns, writer);
        } catch (final IOException e) {
            LoggerUtils.error("Failed to save game run: " + e.getMessage());
        }

    }

}
