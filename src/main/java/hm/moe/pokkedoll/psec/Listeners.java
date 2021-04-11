package hm.moe.pokkedoll.psec;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Listeners implements Listener {

  private final PokkeSecurity plugin;

  public Listeners(PokkeSecurity plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onLogin(PlayerLoginEvent e) {
    Player player = e.getPlayer();
    Crime crime = plugin.getCrime(player);
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
    Crime crime = plugin.getCrime(player);
    if (crime != null && plugin.jails.containsKey(crime.getLocation())) {
      e.setRespawnLocation(plugin.jails.get(crime.getLocation()));
      plugin.wearPrisonerUniform(player);
    }
  }
}
