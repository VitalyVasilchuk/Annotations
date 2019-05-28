package basilisk.apps.services;

import basilisk.apps.Init;
import basilisk.apps.Service;

@Service(name = "ASimpleService")
public class SimpleService {
    @Init
    public void simpleInit(int value) {
        System.out.format("simpleInit, initValue = %s\n", value);
    }

    public void testSimple1() {
        System.out.println("testSimple1");
    }

    public void testSimple2() {
        System.out.println("testSimple2");
    }

    public void testSimple3() {
        System.out.println("testSimple3");
    }
}
