package org.mattrr78.sparsefetchdemo;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.ExceptionMethod;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.jar.asm.Opcodes;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.springframework.data.convert.Jsr310Converters.DateToLocalDateConverter;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;

    private Constructor<? extends Employee> sparseConstructor;

    public EmployeeService(EmployeeRepository repo)  {
        this.repo = repo;
    }

    @PostConstruct
    public void initializeSparseClass() throws NoSuchMethodException {
        Set<String> queryFields = new LinkedHashSet<>();
        queryFields.add("id");
        queryFields.add("firstName");
        queryFields.add("lastName");
        queryFields.add("joinDate");
        queryFields.add("salary");

        Class[] constructorParameterTypes = {int.class, String.class, String.class, LocalDate.class, int.class};

        DynamicType.Builder<Employee> dynamicTypeBuilder = new ByteBuddy()
                .subclass(Employee.class)
                .defineConstructor(Opcodes.ACC_PUBLIC)
                .withParameters(constructorParameterTypes)
                .intercept(MethodCall.invoke(Object.class.getConstructor())
                        .andThen(FieldAccessor.ofField("id").setsArgumentAt(0))
                        .andThen(FieldAccessor.ofField("firstName").setsArgumentAt(1))
                        .andThen(FieldAccessor.ofField("lastName").setsArgumentAt(2))
                        .andThen(FieldAccessor.ofField("joinDate").setsArgumentAt(3))
                        .andThen(FieldAccessor.ofField("salary").setsArgumentAt(4))
                );

        for (Method method : Employee.class.getDeclaredMethods())  {
            String methodName = method.getName();
            String fieldName = null;
            if (methodName.startsWith("get"))  {
                fieldName = methodName.substring(3);
            } else if (methodName.startsWith("is"))  {
                fieldName = methodName.substring(2);
            } else  {
                continue;
            }
            fieldName = Character.toLowerCase(fieldName.charAt(0)) +  fieldName.substring(1);

            if (queryFields.contains(fieldName))  {
                dynamicTypeBuilder = dynamicTypeBuilder
                        .defineField(fieldName, method.getReturnType(), Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL)
                        .method(named(methodName)).intercept(FieldAccessor.ofField(fieldName));
            } else  {
                dynamicTypeBuilder = dynamicTypeBuilder.method(named(methodName))
                        .intercept(ExceptionMethod.throwing(UnsupportedOperationException.class,
                                "Field '" + fieldName + "' was not sparse fetched."));
            }
        }

        Class<? extends Employee> sparseClass = dynamicTypeBuilder.make().load(getClass().getClassLoader()).getLoaded();
        sparseConstructor = sparseClass.getConstructor(constructorParameterTypes);
    }

    List<EmployeeEntity> findAll()  {
        return repo.findAll();
    }

    List<Map<String, Object>> findJpaMap()  {
        return repo.findSparseJpa();
    }

    List<Map<String, Object>> findNativeMap()  {
        return repo.findSparseNative();
    }

    Object[][] findNativeMultiArray()  {
        return repo.findNativeMultiArray();
    }

    Object[][] findJpaMultiArray()  {
        return repo.findJpaMultiArray();
    }

    List<Map<String, Object>> findSparseJpa()  {
        List<Map<String, Object>> mapsList = new ArrayList<>();
        for (Map<String, Object> map : repo.findSparseJpa()) {
            SparseMap sparseMap = new SparseMap(map);
            mapsList.add(sparseMap);
        }
        return mapsList;
    }

    List<Map<String, Object>> findSparseNative()  {
        List<Map<String, Object>> mapsList = new ArrayList<>();
        for (Map<String, Object> map : repo.findSparseNative())  {
            Date joinDate = (Date)map.get("joinDate");
            LocalDate joinLocalDate = DateToLocalDateConverter.INSTANCE.convert(joinDate);
            SparseMap sparseMap = new SparseMap(map);
            sparseMap.put("joinDate", joinLocalDate);
            mapsList.add(sparseMap);
        }
        return mapsList;
    }

    List<Employee> findSparseInstances()  {
        List<Employee> employees = new ArrayList<>();
        try {
            for (Map<String, Object> map : repo.findSparseJpa()) {
                Employee employee = sparseConstructor.newInstance(
                        (int) map.get("id"),
                        (String) map.get("firstName"),
                        (String) map.get("lastName"),
                        (LocalDate) map.get("joinDate"),
                        (int) map.get("salary")
                );
                employees.add(employee);
            }
        } catch(Exception e)  {
            throw new IllegalStateException(e);
        }
        return employees;
    }

}
