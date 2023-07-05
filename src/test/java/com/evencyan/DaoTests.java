package com.evencyan;

import com.evencyan.dao.PermissionDAO;
import com.evencyan.dao.RoleDAO;
import com.evencyan.dao.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class DaoTests {

    @Autowired
    private PermissionDAO permissionDao;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserDAO userDAO;

    @Test
    void permissionDaoTest() {
        System.out.println(permissionDao.selectPermissionsByUid(1));
    }

    @Test
    void roleDaoTest() {
        log.info(roleDAO.selectRolesByUid(1).toString());
    }

    @Test
    void userDaoTest() {
        System.out.println(userDAO.selectById(1));
    }
}
