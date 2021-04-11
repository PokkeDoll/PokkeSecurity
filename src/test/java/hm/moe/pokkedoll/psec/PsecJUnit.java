package hm.moe.pokkedoll.psec;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

public class PsecJUnit {
  @Test
  public void これはゼロです() {
    assertThat(0, is(0));
  }

  @Test
  public void 時間() {
    long a = PokkeSecurity.getTerm("2021-04-15;23:59");
    System.out.println(a);
  }
}
