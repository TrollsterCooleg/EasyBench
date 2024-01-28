package me.cooleg.easybench;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class EasyBench extends JavaPlugin {

    private static Map<String, BenchmarkProfile> profiles;
    private static File dataFolder;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        dataFolder = getDataFolder();
        profiles = new HashMap<>();
        PluginCommand command = getCommand("runbenchmark");
        BenchmarkCommand executor = new BenchmarkCommand(this);
        command.setExecutor(executor);
        command.setTabCompleter(executor);

        ConfigurationSerialization.registerClass(BenchmarkProfile.class);

        getConfig().getList("benchmarks");
    }

    public void reloadBenchmarks() {
        profiles.clear();
        reloadConfig();

        getConfig().getList("benchmarks");
    }

    public static Map<String, BenchmarkProfile> getProfiles() {
        return profiles;
    }

    public static File getFolder() {
        return dataFolder;
    }
}
