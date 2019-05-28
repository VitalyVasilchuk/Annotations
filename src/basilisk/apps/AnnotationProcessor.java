package basilisk.apps;

import basilisk.apps.services.LazyService;
import basilisk.apps.services.SimpleService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationProcessor {
    private static Map<String, Object> servicesMap = new HashMap<>();
    private static int initValue = 0;

    public static void main(String[] args) {
        System.out.println("==============================");
        inspectService(SimpleService.class);
        inspectService(LazyService.class);
        inspectService(String.class);
        System.out.println("==============================");

        loadService("basilisk.apps.services.LazyService");
        loadService("basilisk.apps.services.SimpleService");
        loadService("java.lang.String");

        for (Map.Entry<String, Object> service : servicesMap.entrySet()) {
            System.out.println(service.getKey());
            invokeInitMethod(service.getValue());
        }
    }

    private static void inspectService(Class<?> service) {
        if (service.isAnnotationPresent(Service.class)) {
            Service ann = service.getAnnotation(Service.class);
            System.out.printf("%s, [annotation name = '%s', lazyLoad = %s]\n", service.getName(), ann.name(), ann.lazyLoad());
            System.out.println("------------------------------");
            Method[] methods = service.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Init.class)) {
                    Init initMethod = method.getAnnotation(Init.class);
                    System.out.printf("annotation @Init found, [method name = %s(), suppressException = %s]\n", method.getName(), initMethod.suppressException());
                } else {
                    System.out.format("annotation @Init not found, [method name = %s()\n", method.getName());
                }
            }
            System.out.println("------------------------------");
        } else
            System.out.printf("%s, annotation @Service not found\n", service.getName());
    }

    private static void loadService(String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (clazz != null && clazz.isAnnotationPresent(Service.class)) {
            try {
                Object obj = clazz.newInstance();
                servicesMap.put(clazz.getName(), obj);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void invokeInitMethod(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Init.class)) {
                try {
                    Object[] args = new Object[]{initValue++} ;
                    method.setAccessible(true);
                    method.invoke(object, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Init annotation = method.getAnnotation(Init.class);
                    if (annotation.suppressException()) {
                        System.err.println(e.getMessage());
                    } else {
                        throw new RuntimeException();
                    }
                }
            }
        }
    }
}
