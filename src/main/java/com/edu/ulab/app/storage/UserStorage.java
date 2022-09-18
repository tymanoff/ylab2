package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserStorage extends Storage<User> {

    @Override
    public User save(User entity) {
        return super.save(entity);
    }

    @Override
    public User update(User entity) {
        return super.update(entity);
    }

    @Override
    public User findById(Long id) {
        return super.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
