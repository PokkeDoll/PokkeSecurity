package hm.moe.pokkedoll.psec;

import hm.moe.pokkedoll.psec.commands.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PokkeSecurity extends JavaPlugin {
  private static PokkeSecurity instance;

  public static PokkeSecurity getInstance() {
    return instance;
  }

  public Map<String, Crime> players;
  public Map<String, Location> jails;

  private File configFile;

  @Override
  public void onEnable() {
    ConfigurationSerialization.registerClass(Crime.class);
    instance = this;
    players = new HashMap<>();
    jails = new HashMap<>();
    saveDefaultConfig();

    configFile = new File(getDataFolder(), "config.yml");

    loadJails();
    loadPlayers();

    Map<String, TabExecutor> commands = new LinkedHashMap<>() {
      {
        put("jail", new JailCommand(instance));
        put("jail-all", new JailAllCommand());
        put("jail-release", new JailReleaseCommand(instance));
        put("jail-info", new JailInfoCommand(instance));
      }
    };

    commands.forEach((k, v) -> {
      Objects.requireNonNull(getCommand(k)).setExecutor(v);
      Objects.requireNonNull(getCommand(k)).setTabCompleter(v);
    });

    Bukkit.getPluginManager().registerEvents(new Listeners(this), this);

    getLogger().info("players: " + players.size());
    getLogger().info("jails: " + jails.size());
  }

  private void loadPlayers() {
    Objects.requireNonNull(getConfig().getConfigurationSection("players"))
            .getKeys(false)
            .forEach(key -> players.put(key, Crime.of(getConfig().get("players." + key))));
  }

  private void loadJails() {
    Objects.requireNonNull(getConfig().getConfigurationSection("jails"))
            .getKeys(false)
            .forEach(key -> jails.put(key, getConfig().getLocation("jails." + key)));
  }

  public @Nullable String getUUID(String name) {
    if(name.length() > 16) {
      return name;
    } else {
      OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
      if(offlinePlayer.hasPlayedBefore()) {
        return offlinePlayer.getUniqueId().toString();
      } else {
        return null;
      }
    }
  }

  public static long getTerm(String term) {
    if (term.equalsIgnoreCase("-1")) {
      return -1;
    } else {
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd;HH:mm");
      try {
        cal.setTime(sdf.parse(term));
      } catch (ParseException e) {
        e.printStackTrace();
        return -1;
      }
      return cal.getTimeInMillis();
    }
  }

  public void updatePlayers() throws IOException {
    getConfig().set("players", null);
    players.forEach((k, v) -> getConfig().set("players." + k, v));
    getConfig().save(configFile);
  }

  public void updateJails() throws IOException {
    getConfig().set("jails", null);
    jails.forEach((k, v) -> getConfig().set("jails."+k, v));
    getConfig().save(configFile);
  }

  /**
   * プレイヤーを収監する！
   * @param player
   */
  public void applyJailPlayer(Player player) {
    Crime crime = players.get(player.getUniqueId().toString());
    if(crime != null && jails.containsKey(crime.getLocation())) {
      player.teleport(jails.get(crime.getLocation()));
      wearPrisonerUniform(player);
    }
  }

  public void releaseJailPlayer(Player player) {
    player.sendMessage("解放された！");
    player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
    EntityEquipment eq = player.getEquipment();
    if(eq != null) {
      eq.setChestplate(new ItemStack(Material.AIR));
      eq.setLeggings(new ItemStack(Material.AIR));
      eq.setBoots(new ItemStack(Material.AIR));
    }
  }

  public void wearPrisonerUniform(Player player) {
    EntityEquipment es = player.getEquipment();
    assert es != null;
    es.setChestplate(getPrisonerUniform(Material.LEATHER_CHESTPLATE));
    es.setLeggings(getPrisonerUniform(Material.LEATHER_LEGGINGS));
    es.setBoots(getPrisonerUniform(Material.LEATHER_BOOTS));
  }

  private ItemStack getPrisonerUniform(Material material) {
    ItemStack i = new ItemStack(material);
    ItemMeta m = i.getItemMeta();
    if(m instanceof LeatherArmorMeta) {
      i.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE);
      m.setDisplayName(ChatColor.GOLD + "囚人服");
      m.setLore(Collections.singletonList(ChatColor.GOLD + "おはよう、朝だよ。"));
      ((LeatherArmorMeta)m).setColor(Color.ORANGE);
      i.setItemMeta(m);
      return i;
    } else {
      return new ItemStack(Material.AIR);
    }
  }
}
