package me.cooleg.easybench.profiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public abstract class AbstractBenchmarkProfile {

    protected String profileName;
    protected String executionCommand;
    protected URL downloadLink;
    protected String downloadName;
    protected boolean local = false;

    public AbstractBenchmarkProfile(Map<String, Object> properties) {
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
                System.out.println("Benchmark profile " + profileName + " has no download link specified." +
                        "If this is intended, ignore this message, or add \"local: true\" to the profile to disable it.");
            }
        }
    }

    public abstract void runBenchmark();

}
