package hm.moe.pokkedoll.psec.commands;

import hm.moe.pokkedoll.psec.PokkeSecurity;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class JailReleaseCommand implements TabExecutor {

  private PokkeSecurity plugin;

  public JailReleaseCommand(PokkeSecurity plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if(args.length != 0) {
      OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
      if(op.hasPlayedBefore()) {
        plugin.players.remove(op.getUniqueId().toString());
        try {
          plugin.updatePlayers();
          if(op.isOnline() && op.getPlayer() != null) {
            plugin.releaseJailPlayer(op.getPlayer());
          }
          sender.sendMessage("削除！");
        } catch (IOException e) {
          e.printStackTrace();
        }


      } else {
        sender.sendMessage(args[0] + "は存在しません");
      }
    }
    return true;
  }


  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    return null;
  }
}
