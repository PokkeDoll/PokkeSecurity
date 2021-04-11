package hm.moe.pokkedoll.psec;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Crime implements ConfigurationSerializable {
  public static final String REASON = "reason";
  public static final String LOCATION = "location";
  public static final String TERM = "term";
  public static final String MUTE = "mute";

  private String reason;
  private String location;
  private Long term;
  private Boolean mute;

  /**
   * 有罪です。
   * @param reason 理由
   * @param term 刑期が終わる時間。-1なら無期
   */
  public Crime(String reason, String location, Long term, Boolean mute) {
    this.reason = reason;
    this.location = location;
    this.term = term;
    this.mute = mute;
  }

  public Crime(Map<String, Object> args) {
    this.reason = args.getOrDefault(REASON, "").toString();
    this.location = args.getOrDefault(LOCATION, "").toString();
    this.term = Long.getLong(args.getOrDefault(TERM, "0").toString());
    this.mute = Boolean.getBoolean(args.getOrDefault(MUTE, false).toString());
  }

  public String getReason() {
    return this.reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getLocation() {
    return this.location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Long getTerm() {
    return this.term;
  }

  public void setTerm(Long term) {
    this.term = term;
  }

  public Boolean getMute() {
    return this.mute;
  }

  public void setMute(Boolean mute) {
    this.mute = mute;
  }

  /**
   * Creates a Map representation of this class.
   * <p>
   * This class must provide a method to restore this class, as defined in
   * the {@link ConfigurationSerializable} interface javadocs.
   *
   * @return Map containing the current state of this class
   */
  @Override
  public @NotNull Map<String, Object> serialize() {
    Map<String, Object> map = new HashMap<>();
    map.put(REASON, this.reason);
    map.put(LOCATION, this.location);
    map.put(TERM, this.term);
    map.put(MUTE, this.mute);
    return map;
  }

  @Override
  public String toString() {
    return "Crime{" +
            "reason='" + reason + '\'' +
            ", location='" + location + '\'' +
            ", term=" + term +
            ", mute=" + mute +
            '}';
  }

  public static @Nullable Crime of(Object obj) {
    return obj instanceof Crime ? (Crime) obj : null;
  }
}
