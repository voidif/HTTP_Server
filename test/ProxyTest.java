import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;


public class ProxyTest {
    public static void main(String[] args) throws Exception {
        Vendor vendor = new Vendor();
        DynamicProxy inter = new DynamicProxy(vendor);

        Class[] classArray = new Class[]{Sell.class};
        Sell proxy = (Sell)(Proxy.newProxyInstance(vendor.getClass().getClassLoader(),
                classArray, inter));
        System.out.println(proxy.getClass());
        proxy.sell();

        ClassLoader classLoader = ProxyTest.class.getClassLoader();
        System.out.println(classLoader.getClass());

    }
}

interface Sell {
    void sell();
    void ad();
}

class Vendor implements Sell {
    public void sell() {
        System.out.println("vendor sell!");
    }

    public void ad() {
        System.out.println("vendor ad!");
    }
}

class StaticProxy implements Sell {
    private final Vendor vendor;
    public StaticProxy(Vendor vendor) {
        this.vendor = vendor;
    }
    @Override
    public void sell() {
        vendor.sell();
    }

    @Override
    public void ad() {
        vendor.ad();
    }
}

//interface  InvocationHandler {
//    Object invoke(Object proxy, Method method, Object[] args);
//}

class DynamicProxy implements InvocationHandler {
    private final Object obj;

    public DynamicProxy(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            Object result = method.invoke(obj, args);
            return result;
        } catch (Exception e) {
            System.out.println("Dynamic Proxy Error!!!");
        }
        return null;
    }

}