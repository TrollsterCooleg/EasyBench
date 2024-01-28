package me.cooleg.easybench;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class BenchmarkProfile implements ConfigurationSerializable {

    private String profileName;
    private String executionCommand;
    // If null, assume the script exists locally already.
    private URL downloadLink;
    private String downloadName;
    private boolean local = false;

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


    public BenchmarkProfile(Map<String, Object> properties) {
        Object nameObject = properties.get("profile-name");
        if (nameObject instanceof String) {
            profileName = (String) nameObject;
        } else {
            throw new RuntimeException("No \"profile-name\" set for a benchmark profile in the config :(");
        }

        Object executionObject = properties.get("execution-command");
        if (executionObject instanceof String) {
            executionCommand = (String) executionObject;
        } else {
            throw new RuntimeException("No \"execution-command\" set for benchmark profile \"" + profileName + "\"");
        }

        Object download = properties.get("download-link");
        if (download instanceof String) {
            String linkString = (String) download;
            try {
                downloadLink = new URL(linkString);
            } catch (MalformedURLException ex) {
                throw new RuntimeException("Invalid url for benchmark profile \"" + profileName + "\"");
            }

            Object downloadNameObject = properties.get("download-name");
            if (downloadNameObject instanceof String) {
                downloadName = (String) downloadNameObject;
            } else {
                throw new RuntimeException("No \"download-name\" set for benchmark profile \"" + profileName + "\"");
            }
        } else {
            local = true;
            Object localObject = properties.get("local");
            if (!(localObject instanceof Boolean && ((Boolean) localObject))) {
                Bukkit.getLogger().info("Benchmark profile " + profileName + " has no download link specified." +
                        "If this is intended, ignore this message, or add \"local: true\" to the profile to disable it.");
            }
        }

        EasyBench.getProfiles().put(profileName, this);
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
