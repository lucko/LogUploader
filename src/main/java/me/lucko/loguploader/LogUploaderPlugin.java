package me.lucko.loguploader;

import com.google.common.collect.Maps;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class LogUploaderPlugin extends JavaPlugin {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.RED + "Starting upload - please wait...");
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            Path logFile = Paths.get("logs/latest.log");
            if (!Files.exists(logFile)) {
                sender.sendMessage("Log file doesn't exist!");
                return;
            }

            try {
                String content = new String(Files.readAllBytes(logFile), StandardCharsets.UTF_8);
                String url = PasteUtils.paste("latest.log", Collections.singletonList(Maps.immutableEntry("latest.log", content)));
                sender.sendMessage(ChatColor.RED + "Upload complete. URL: " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
