package me.cooleg.easybench;

import me.cooleg.easybench.profiles.AbstractBenchmarkProfile;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class BenchmarkProfile extends AbstractBenchmarkProfile implements ConfigurationSerializable {

    public BenchmarkProfile(Map<String, Object> properties) {
        super(properties);

        EasyBench.getProfiles().put(profileName, this);
    }

    public void runBenchmark() {
        File folder = EasyBench.getFolder();

        if (!local) {
            try {
                InputStream stream = downloadLink.openStream();
                File output = Paths.get(folder.getAbsolutePath(), downloadName).toFile();
                Files.copy(stream, output.toPath(), StandardCopyOption.REPLACE_EXISTING);
                output.setExecutable(true);
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
        }

        try {
            ProcessBuilder builder = new ProcessBuilder(executionCommand.replace("%path%", folder.getAbsolutePath() + "/").split("\\s"));
            builder.directory(folder);
            builder.inheritIO();
            builder.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("profile-name", profileName);
        map.put("execution-command", executionCommand);
        if (local) {map.put("local", true);}
        else {
            map.put("download-link", downloadLink.toString());
            map.put("download-name", downloadName);
        }

        return map;
    }

}
