package org.mattrr78.sparsefetchdemo;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.ExceptionMethod;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.jar.asm.Opcodes;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import static net.bytebuddy.matcher.ElementMatchers.named;

@Component
public class SparseInstanceRepository {
    private final Map<String, Constructor> constructorsMap = new HashMap<>();

    public <T> void addConstructor(Class<T> clazz, String name, String... parameterNames) throws NoSuchMethodException  {
        List<Map<String, Method>> methodsMapList = traverseGetterMethods(clazz.getMethods(),
                new LinkedHashSet<>(Arrays.asList(parameterNames)));
        Map<String, Method> sparseGetterMethodsMap = methodsMapList.get(0);
        Map<String, Method> exceptionGetterMethodsMap = methodsMapList.get(1);

        Class<?>[] constructorParameterTypes = createConstructorParameterTypes(parameterNames, sparseGetterMethodsMap);

        DynamicType.Builder<T> dynamicTypeBuilder = initializeBuilder(clazz, parameterNames, constructorParameterTypes);

        for (Map.Entry<String, Method> entry : sparseGetterMethodsMap.entrySet())  {
            String fieldName = entry.getKey();
            Method method = entry.getValue();
            dynamicTypeBuilder = dynamicTypeBuilder
                    .defineField(fieldName, method.getReturnType(), Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL)
                    .method(named(method.getName())).intercept(FieldAccessor.ofField(fieldName));
        }

        for (Map.Entry<String, Method> entry : exceptionGetterMethodsMap.entrySet())  {
            String fieldName = entry.getKey();
            Method method = entry.getValue();
            dynamicTypeBuilder = dynamicTypeBuilder.method(named(method.getName()))
                    .intercept(ExceptionMethod.throwing(UnsupportedOperationException.class,
                            "Field '" + fieldName + "' was not sparse fetched."));
        }

        Class<? extends T> sparseClass = dynamicTypeBuilder.make().load(getClass().getClassLoader()).getLoaded();
        constructorsMap.put(name, sparseClass.getConstructor(constructorParameterTypes));
    }

    private List<Map<String, Method>> traverseGetterMethods(Method[] methods, Set<String> parameterNames)  {
        Map<String, Method> sparseGetterMethodsMap = new HashMap<>();
        Map<String, Method> exceptionGetterMethodsMap = new HashMap<>();
        for (Method method : methods)  {
            String fieldName = calculateFieldName(method);
            if (fieldName == null)  {
                continue;
            }

            if (parameterNames.contains(fieldName))  {
                sparseGetterMethodsMap.put(fieldName, method);
            } else  {
                exceptionGetterMethodsMap.put(fieldName, method);
            }
        }
        return Arrays.asList(sparseGetterMethodsMap, exceptionGetterMethodsMap);
    }

    private String calculateFieldName(Method method)  {
        String methodName = method.getName();
        String fieldName = null;
        if (methodName.startsWith("get"))  {
            fieldName = methodName.substring(3);
        } else if (methodName.startsWith("is"))  {
            fieldName = methodName.substring(2);
        }
        if (fieldName != null)  {
            fieldName = Character.toLowerCase(fieldName.charAt(0)) +  fieldName.substring(1);
        }
        return fieldName;
    }

    private Class[] createConstructorParameterTypes(String[] parameterNames,
                                                        Map<String, Method> sparseGetterMethodsMap)  {
        return Arrays.stream(parameterNames).map(parameterName ->
                sparseGetterMethodsMap.get(parameterName).getReturnType())
                .toArray(Class[]::new);
    }

    private <T> DynamicType.Builder<T> initializeBuilder(Class<T> clazz, String[] parameterNames,
                                                         Class[] constructorParameterTypes) throws NoSuchMethodException {
        return new ByteBuddy()
                .subclass(clazz)
                .defineConstructor(Opcodes.ACC_PUBLIC)
                .withParameters(constructorParameterTypes)
                .intercept(createMethodCall(parameterNames));
    }

    private Implementation createMethodCall(String[] parameterNames) throws NoSuchMethodException {
        Implementation.Composable methodCall = MethodCall.invoke(Object.class.getConstructor());
        for (int i = 0; i < parameterNames.length; i++)  {
            methodCall = methodCall.andThen(FieldAccessor.ofField(parameterNames[i]).setsArgumentAt(i));
        }
        return methodCall;
    }

    public <T> T createInstance(String name, Map<String, Object> map) throws IllegalArgumentException {
        Constructor<T> constructor = constructorsMap.get(name);
        Object[] constructorArgs = map.values().toArray();
        try {
            return constructor.newInstance(constructorArgs);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

}
