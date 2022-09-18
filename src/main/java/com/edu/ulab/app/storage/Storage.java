package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.DistributedEntity;
import com.edu.ulab.app.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class Storage<T extends DistributedEntity> implements CrudMemoryRepository<T> {
    //todo создать хранилище в котором будут содержаться данные
    // сделать абстракции через которые можно будет производить операции с хранилищем
    // продумать логику поиска и сохранения
    // продумать возможные ошибки
    // учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
    // продумать что у узера может быть много книг и нужно создать эту связь
    // так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции

    private final Map<Long, T> data = new ConcurrentHashMap<>();

    private long count;

    @Override
    public T save(T entity) {
        Long uuid = nextLongIdentifier();
        entity.setId(uuid);
        data.put(uuid, entity);
        return data.get(uuid);
    }

    @Override
    public T update(T entity) {
        data.put(entity.getId(), entity);
        return data.get(entity.getId());
    }

    @Override
    public T findById(Long id) {
        return data.get(id);
    }

    @Override
    public List<T> findAll() {
        return List.copyOf(data.values());
    }

    @Override
    public void deleteById(Long id) {
        data.remove(id);
    }

    public Long nextLongIdentifier() {
        long value;
        synchronized (this) {
            value = count++;
            if (count == java.lang.Long.MAX_VALUE) {
                throw new NotFoundException
                        ("The maximum number of identifiers has been reached");
            }
        }
        return value;
    }
}
