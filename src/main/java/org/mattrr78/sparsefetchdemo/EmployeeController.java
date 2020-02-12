package org.mattrr78.sparsefetchdemo;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/employee")
@Transactional(readOnly = true)
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service)  {
        this.service = service;
    }

    @PostConstruct
    public void warmup()  {
        for (int i = 0; i < 4; i++)  {
            service.findJpaMultiArray();
            service.findAll();
            service.findSparseJpa();
            service.findSparseNative();
            service.findSparseInstances();
        }
    }

    @GetMapping(path = "/findAll")
    public ResponseEntity<PerformanceResult> findAll()  {
        StopWatch stopWatch = new StopWatch();
        PerformanceResult result = start(stopWatch);

        List<EmployeeEntity> employees = service.findAll();

        return ok(stop(stopWatch, result));
    }

    @GetMapping(path = "/findJpaMap")
    public ResponseEntity<PerformanceResult> findJpaMap()  {
        StopWatch stopWatch = new StopWatch();
        PerformanceResult result = start(stopWatch);

        List<Map<String, Object>> employees = service.findJpaMap();

        return ok(stop(stopWatch, result));
    }

    @GetMapping(path = "/findNativeMap")
    public ResponseEntity<PerformanceResult> findNativeMap()  {
        StopWatch stopWatch = new StopWatch();
        PerformanceResult result = start(stopWatch);

        List<Map<String, Object>> employees = service.findNativeMap();

        return ok(stop(stopWatch, result));
    }

    @GetMapping(path = "/findNativeMultiArray")
    public ResponseEntity<PerformanceResult> findNativeMultiArray()  {
        StopWatch stopWatch = new StopWatch();
        PerformanceResult result = start(stopWatch);

        Object[][] employees = service.findNativeMultiArray();

        return ok(stop(stopWatch, result));
    }

    @GetMapping(path = "/findJpaMultiArray")
    public ResponseEntity<PerformanceResult> findJpaMultiArray()  {
        StopWatch stopWatch = new StopWatch();
        PerformanceResult result = start(stopWatch);

        Object[][] employees = service.findJpaMultiArray();

        return ok(stop(stopWatch, result));
    }

    @GetMapping(path = "/findSparseJpa")
    public ResponseEntity<PerformanceResult> findSparseJpa()  {
        StopWatch stopWatch = new StopWatch();
        PerformanceResult result = start(stopWatch);

        List<Map<String, Object>> mapsList = service.findSparseJpa();

        return ok(stop(stopWatch, result));
    }

    @GetMapping(path = "/findSparseNative")
    public ResponseEntity<PerformanceResult> findSparseNative()  {
        StopWatch stopWatch = new StopWatch();
        PerformanceResult result = start(stopWatch);

        List<Map<String, Object>> mapsList = service.findSparseNative();

        return ok(stop(stopWatch, result));
    }

    @GetMapping(path = "/findSparseInstances")
    public ResponseEntity<PerformanceResult> findSparseInstances()  {
        StopWatch stopWatch = new StopWatch();
        PerformanceResult result = start(stopWatch);

        List<Employee> employees = service.findSparseInstances();

        return ok(stop(stopWatch, result));
    }

    @GetMapping(path = "/findAndAccessAll")
    public ResponseEntity<PerformanceResult> findAndAccessAll()  {
        StopWatch stopWatch = new StopWatch();
        PerformanceResult result = start(stopWatch);

        objectGet(service.findAll());

        return ok(stop(stopWatch, result));
    }

    @GetMapping(path = "/findAndAccessSparseJpa")
    public ResponseEntity<PerformanceResult> findAndAccessSparseJpa()  {
        StopWatch stopWatch = new StopWatch();
        PerformanceResult result = start(stopWatch);

        sparseGet(service.findSparseJpa());

        return ok(stop(stopWatch, result));
    }

    @GetMapping(path = "/findAndAccessSparseNative")
    public ResponseEntity<PerformanceResult> findAndAccessSparseNative()  {
        StopWatch stopWatch = new StopWatch();
        PerformanceResult result = start(stopWatch);

        sparseGet(service.findSparseNative());

        return ok(stop(stopWatch, result));
    }

    @GetMapping(path = "/findAndAccessSparseInstances")
    public ResponseEntity<PerformanceResult> findAndAccessSparseInstances()  {
        StopWatch stopWatch = new StopWatch();
        PerformanceResult result = start(stopWatch);

        objectGet(service.findSparseInstances());

        return ok(stop(stopWatch, result));
    }

    @GetMapping(path = "/findSparseJpaError")
    public ResponseEntity<Integer> findSparseJpaError()  {
        return ok((int)service.findSparseJpa().get(0).get("age"));
    }

    @GetMapping(path = "/findSparseInstanceError")
    public ResponseEntity<Integer> findSparseInstanceError()  {
        return ok(service.findSparseInstances().get(0).getAge());
    }

    private PerformanceResult start(StopWatch stopWatch)  {
        PerformanceResult result = new PerformanceResult();
        Runtime runtime = Runtime.getRuntime();
        result.setMemoryBefore(runtime.totalMemory() - runtime.freeMemory());
        stopWatch.start();
        return result;
    }

    private PerformanceResult stop(StopWatch stopWatch, PerformanceResult result)  {
        stopWatch.stop();
        Runtime runtime = Runtime.getRuntime();
        result.setMemoryAfter(runtime.totalMemory() - runtime.freeMemory());
        result.setTestTime(stopWatch.getTotalTimeMillis());
        return result;
    }

    private void objectGet(List<? extends Employee> employees)  {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            for (Employee employee : employees) {
                builder.setLength(0);
                int id = employee.getId();
                String firstName = employee.getFirstName();
                String lastName = employee.getLastName();
                LocalDate joinDate = employee.getJoinDate();
                int salary = employee.getSalary();

                builder.append(id).append(firstName).append(lastName).append(joinDate.toString()).append(salary);
            }
        }
    }

    private void sparseGet(List<Map<String, Object>> mapsList)  {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 5; i++)  {
            for(Map<String, Object> map : mapsList)  {
                builder.setLength(0);
                int id = (int) map.get("id");
                String firstName = (String) map.get("firstName");
                String lastName = (String) map.get("lastName");
                LocalDate joinDate = (LocalDate) map.get("joinDate");
                int salary = (int) map.get("salary");

                builder.append(id).append(firstName).append(lastName).append(joinDate.toString()).append(salary);
            }
        }
    }

}