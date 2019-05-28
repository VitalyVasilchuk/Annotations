package basilisk.apps.services;

import basilisk.apps.Init;
import basilisk.apps.Service;

@Service(name = "ALazyService", lazyLoad = true)
public class LazyService {
    @Init(suppressException = true)
    private void lazyInit(int value) throws Exception {
        System.out.format("simpleInit, initValue = %s\n", value);
    }

    public void testLazy1() {
        System.out.println("testLazy1");
    }

    public void testLazy2() {
        System.out.println("testLazy2");
    }

    public void testLazy3() {
        System.out.println("testLazy3");
    }
}
