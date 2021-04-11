package hm.moe.pokkedoll.psec;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Listeners implements Listener {

  private PokkeSecurity plugin;

  /*
    @EventHandler
    public void onChat(AsyncChatEvent e) {
      Player player = e.getPlayer();
      Crime crime = plugin.players.get(player.getUniqueId().toString());
      if (crime != null && crime.getMute()) {
        e.setCancelled(true);
        plugin.getLogger().info(player.getName() + "tried to say " + e.message() + ". but he is muted!");
      }
    }
  */

  public Listeners(PokkeSecurity plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onLogin(PlayerLoginEvent e) {
    Player player = e.getPlayer();
    Crime crime = plugin.players.get(player.getUniqueId().toString());
    if(crime != null) {
      new BukkitRunnable() {
        @Override
        public void run() {
          plugin.applyJailPlayer(player);
        }
      }.runTaskLater(plugin, 1L);
    }
  }

  @EventHandler
  public void onRespawn(PlayerRespawnEvent e) {
    Player player = e.getPlayer();
    assert plugin.players != null;
    assert player.getUniqueId().toString() != null;
    Crime crime = plugin.players.get(player.getUniqueId().toString());
    if (crime != null && plugin.jails.containsKey(crime.getLocation())) {
      e.setRespawnLocation(plugin.jails.get(crime.getLocation()));
      plugin.wearPrisonerUniform(player);
    }
  }
}
