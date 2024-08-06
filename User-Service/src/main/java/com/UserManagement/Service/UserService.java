package com.UserManagement.Service;



import java.util.List;

import com.UserManagement.model.User;

public interface UserService {
    User save(User user);

    User findByUsername(String username);

    List<String> findUsers(List<Long> idList);
}
