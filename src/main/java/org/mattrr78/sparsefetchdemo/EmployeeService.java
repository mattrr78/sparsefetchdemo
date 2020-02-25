package org.mattrr78.sparsefetchdemo;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.data.convert.Jsr310Converters.DateToLocalDateConverter;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;

    private final SparseInstanceRepository sparseRepository;

    public EmployeeService(EmployeeRepository repo, SparseInstanceRepository sparseRepository)  {
        this.repo = repo;
        this.sparseRepository = sparseRepository;
    }

    @PostConstruct
    public void initializeSparseClass() throws NoSuchMethodException {
        sparseRepository.addConstructor(Employee.class, "sparseFetch",
                "id", "firstName", "lastName", "joinDate", "salary");
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
                Employee employee = sparseRepository.createInstance("sparseFetch", map);
                employees.add(employee);
            }
        } catch(Exception e)  {
            throw new IllegalStateException(e);
        }
        return employees;
    }

}
