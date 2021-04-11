package hm.moe.pokkedoll.psec.commands;

import hm.moe.pokkedoll.psec.PokkeSecurity;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class JailAllCommand implements TabExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    PokkeSecurity plugin = PokkeSecurity.getInstance();
    plugin.players.forEach((k, v) -> {
      Player target = Bukkit.getPlayer(UUID.fromString(k));
      if (target != null) {
        plugin.applyJailPlayer(target);
      }
    });
    sender.sendMessage("強制的にプレイヤーを収容しました");
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    return null;
  }
}
