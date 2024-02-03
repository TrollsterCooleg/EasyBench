package me.cooleg.easybench.profiles;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class StandaloneYabsProfile extends AbstractBenchmarkProfile {

    private static Map<String, Object> info = new HashMap<>();

    static {
        info.put("profile-name", "YABS");
        info.put("download-name", "yabs.sh");
        info.put("execution-command", "%path%yabs.sh -w output.json");
        info.put("download-link", "https://raw.githubusercontent.com/masonr/yet-another-bench-script/master/yabs.sh");
    }

    public StandaloneYabsProfile() {
        super(info);
    }

    public void runBenchmark() {
        File output = new File(downloadName);

        if (!local) {
            try {
                InputStream stream = downloadLink.openStream();
                Files.copy(stream, output.toPath(), StandardCopyOption.REPLACE_EXISTING);
                output.setExecutable(true);
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
        }

        try {
            ProcessBuilder builder = new ProcessBuilder(executionCommand.replace("%path%", output.getAbsoluteFile().getParentFile().getAbsolutePath() + "/").split("\\s"));
            builder.inheritIO();
            builder.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
