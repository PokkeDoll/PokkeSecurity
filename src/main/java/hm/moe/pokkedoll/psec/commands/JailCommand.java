package hm.moe.pokkedoll.psec.commands;

import hm.moe.pokkedoll.psec.Crime;
import hm.moe.pokkedoll.psec.PokkeSecurity;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JailCommand implements TabExecutor {

  private final PokkeSecurity plugin;

  public JailCommand(PokkeSecurity plugin) {
    super();
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if(sender instanceof Player && sender.hasPermission("jail.jail")) {
      Player player = (Player) sender;
      if(args.length != 0 && args[0].equalsIgnoreCase("all")) {
        plugin.players.forEach((k, v) -> {
          Player p = Bukkit.getPlayer(k);
          if (p != null) {
            plugin.applyJailPlayer(p);
          }
          player.sendMessage("強制的にプレイヤーを収容しました");
        });
      } else if(args.length < 5) {
        sender.sendMessage("引数が足りない！");
      } else {
        OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
        String jail = args[1];
        String reason = args[2];
        long term = PokkeSecurity.getTerm(args[3]);
        boolean mute = Boolean.getBoolean(args[4]);
        if(!op.hasPlayedBefore()) {
          player.sendMessage("プレイヤーが存在しません！！！１");
        } else if (!plugin.jails.containsKey(jail)) {
          player.sendMessage("監獄が見つかりません！");
        } else {
          String t = term == -1 ? "無期限" : args[3] + "まで";
          String m = mute ? "ミュート" : "ミュートなし";
          player.sendMessage(
                  "テスト: " + op.getName() + "(" + op.getUniqueId() + ") を " + jail + "に" + t + "の間" + reason + "により" + m + "で収監します！");
          plugin.players.put(op.getUniqueId().toString(), new Crime(reason, jail, term, mute));
          if(op.isOnline() && op.getPlayer() != null) {
            plugin.applyJailPlayer(op.getPlayer());
          }
          try {
            plugin.updatePlayers();
          } catch (IOException e) {
            player.sendMessage("保存中にエラーが発生しました！");
          }
        }
      }
    }
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    if(args.length == 0) {
      return null;
    } else if (args.length == 1) {
      List<String> players = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
      players.add("all");
      return players;
    } else if (args.length == 2) {
      return new ArrayList<>(plugin.jails.keySet());
    } else if (args.length == 3) {
      return Arrays.asList("ハッククライアント", "スパム", "【収監理由】");
    } else if (args.length == 4) {
      return Arrays.asList("-1", "【刑期(有期(yyyy-MM-dd;HH:mm),-1は無期】");
    } else if (args.length == 5) {
      return Arrays.asList("true", "false", "【ミュートするかしないか】");
    }
    return null;
  }
}
