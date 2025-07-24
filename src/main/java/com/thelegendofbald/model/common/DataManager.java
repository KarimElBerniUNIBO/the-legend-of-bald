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

public class DataManager {

    private static final String SAVE_FILE_DIRECTORY = "game_data" + File.separator + "runs";
    private static final String SAVE_FILE_PATH = SAVE_FILE_DIRECTORY + File.separator + "users_data.yml";

    private final Yaml yaml;

    public DataManager() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setPrettyFlow(true);

        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setAllowDuplicateKeys(false);

        Constructor constructor = new Constructor(List.class, loaderOptions);

        TypeDescription gameRunDescription = new TypeDescription(GameRun.class);
        constructor.addTypeDescription(gameRunDescription);

        Representer representer = new Representer(dumperOptions);
        representer.setPropertyUtils(new PropertyUtils());

        representer.addClassTag(GameRun.class, MAP);
        representer.addClassTag(TimeData.class, MAP);

        yaml = new Yaml(constructor, representer, dumperOptions, loaderOptions);
        yaml.setBeanAccess(BeanAccess.FIELD);
    }

    public List<GameRun> loadGameRuns() {
        List<?> rawList;
        try (InputStream input = new FileInputStream(SAVE_FILE_PATH)) {
            rawList = yaml.load(input);
        } catch (IOException e) {
            return new ArrayList<>();
        }
        List<GameRun> runs = new ArrayList<>();

        Optional.ofNullable(rawList).ifPresent(list -> {
            list.stream()
                .filter(obj -> obj instanceof GameRun || obj instanceof Map)
                .forEach(obj -> {
                switch (obj) {
                    case GameRun gr -> runs.add(gr);
                    case Map<?, ?> map -> {
                        String name = (String) map.get("name");
                        Object timedataObj = map.get("timedata");
                        if (timedataObj instanceof Map tdMap) {
                            int hours = (int) tdMap.get("hours");
                            int minutes = (int) tdMap.get("minutes");
                            int seconds = (int) tdMap.get("seconds");
                            GameRun gr = new GameRun(name, new TimeData(hours, minutes, seconds));
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

    public void saveGameRun(GameRun gameRun) throws IOException {
        List<GameRun> gameRuns = loadGameRuns();
        gameRuns.add(gameRun);

        File saveFile = new File(SAVE_FILE_PATH);
        File parentDir = saveFile.getParentFile();

        if (Optional.ofNullable(parentDir).isPresent() && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }

        try (FileWriter writer = new FileWriter(saveFile, StandardCharsets.UTF_8)) {
            yaml.dump(gameRuns, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

}
