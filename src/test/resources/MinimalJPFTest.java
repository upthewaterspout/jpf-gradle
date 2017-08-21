import org.junit.Test;

import gov.nasa.jpf.util.test.TestJPF;
import gov.nasa.jpf.vm.Verify;

public class MinimalJPFTest extends TestJPF {

  @Test
  public void test() throws InterruptedException {
    if(verifyNoPropertyViolation()) {
      Verify.incrementCounter(0);
    }
    assertEquals(1, Verify.getCounter(0));
  }
}