package hm.moe.pokkedoll.psec.commands;

import hm.moe.pokkedoll.psec.PokkeSecurity;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class JailSetCommand implements TabExecutor {
  private PokkeSecurity plugin;

  public JailSetCommand(PokkeSecurity plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (args.length == 0) {
        player.sendMessage("は？");
      } else if (args[0].equalsIgnoreCase("list")) {
        StringBuilder sb = new StringBuilder("牢獄リスト");
        plugin.jails.forEach((k, v) -> {
          sb.append("\n")
                  .append(k)
                  .append(": ")
                  .append(v.toString());

        });
        player.sendMessage(sb.toString());
      } else if (args[0].equalsIgnoreCase("add") && args.length > 1) {
        String location = args[1];
        if (plugin.jails.containsKey(location)) {
          player.sendMessage(location + "はすでに存在します");
        } else {
          plugin.jails.put(location, player.getLocation());
          try {
            plugin.updateJails();
            player.sendMessage(location + "を登録しました！");
          } catch (IOException e) {
            player.sendMessage(location + "を登録しようとしましたが、保存できませんでした");
          }
        }
      } else if (args[0].equalsIgnoreCase("del") && args.length > 1) {
        String location = args[1];
        if (plugin.jails.containsKey(location)) {
          plugin.jails.remove(location);
          try {
            plugin.updateJails();
            player.sendMessage("削除しました");
          } catch (IOException e) {
            player.sendMessage("削除しようとしましたが、エラーが発生しました");
          }
        } else {
          player.sendMessage("存在しません！");
        }
      }
    }
    return false;
  }


  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    return null;
  }
}
