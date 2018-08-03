package me.lucko.loguploader;

import com.google.common.io.ByteStreams;

import me.lucko.loguploader.http.Bytebin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

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
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                try (InputStream in = Files.newInputStream(logFile); GZIPOutputStream out = new GZIPOutputStream(byteOut)) {
                    ByteStreams.copy(in, out);
                }

                String url = Bytebin.postCompressedContent(byteOut.toByteArray());
                sender.sendMessage(ChatColor.RED + "Upload complete. URL: " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
