package me.cooleg.easybench;

import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkCommand implements CommandExecutor, TabCompleter {

    private final EasyBench main;
    private static final String RELOAD_MESSAGE = ChatColor.GREEN + "Reloading benchmark options";
    private static final String RUNNING_BENCHMARK = ChatColor.GREEN + "Attempting to run benchmark!";
    private static final String UNKNOWN_BENCHMARK = ChatColor.RED + "No benchmark with this name could be found :(";

    public BenchmarkCommand(EasyBench main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1) {commandSender.sendMessage(RELOAD_MESSAGE); main.reloadBenchmarks(); return true;}
        String benchName = strings[0];
        BenchmarkProfile profile = EasyBench.getProfiles().get(benchName);

        if (profile == null) {commandSender.sendMessage(UNKNOWN_BENCHMARK); return true;}
        profile.runBenchmark();
        commandSender.sendMessage(RUNNING_BENCHMARK);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length != 1) {return ImmutableList.of();}
        return StringUtil.copyPartialMatches(strings[0], EasyBench.getProfiles().keySet(), new ArrayList<>());
    }
}
