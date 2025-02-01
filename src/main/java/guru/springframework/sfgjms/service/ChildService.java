package guru.springframework.sfgjms.service;

import guru.springframework.sfgjms.entity.Child;
import guru.springframework.sfgjms.repository.ChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChildService {
    public static final String CHILD_ID_HEADER = "child_id";
    private final ChildRepository repository;

    @Autowired
    public ChildService(ChildRepository repository) {
        this.repository = repository;
    }

public Child findById(long id){
    return repository.getOne(id);
}
    public void save(Child child) {
        repository.save(child);
    }
}
