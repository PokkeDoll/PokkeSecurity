package hm.moe.pokkedoll.psec;

import org.bukkit.plugin.java.JavaPlugin;

public class PokkeSecurity extends JavaPlugin {
  private static PokkeSecurity instance;

  public static PokkeSecurity getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;
  }
}
