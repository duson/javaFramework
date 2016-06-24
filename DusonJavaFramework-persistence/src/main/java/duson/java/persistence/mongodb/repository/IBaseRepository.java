package duson.java.persistence.mongodb.repository;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IBaseRepository<T, ID extends Serializable> extends MongoRepository<T, ID> {

    /*public List save(Iterable iterable);

    public List findAll();

    public List findAll(Sort sort);

    public Object insert(Object obj);

    public List insert(Iterable iterable);
   
    public Page findAll(Pageable pageable);
    
    public Object save(Object obj);

    public Object findOne(Serializable serializable);

    public boolean exists(Serializable serializable);

    public Iterable findAll(Iterable iterable);

    public long count();

    public void delete(Serializable serializable);

    public void delete(Object obj);

    public void delete(Iterable iterable);

    public void deleteAll();*/
	
}
