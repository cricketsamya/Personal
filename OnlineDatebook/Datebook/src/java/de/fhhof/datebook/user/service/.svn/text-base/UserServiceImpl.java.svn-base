/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.datebook.user.service;

import de.fhhof.datebook.event.dao.EventDetailsDao;
import de.fhhof.datebook.user.dao.UserDao;
import de.fhhof.datebook.user.domain.Userdetails;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Shamim
 */

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private EventDetailsDao eventDao;
    
//    public List<User> getAllUser() {
//       return this.userDao.loadAll();
//    }

    /**
     * @param userDao the userDao to set
     */
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    @Autowired
    public void setEventDao(EventDetailsDao eventDao){
        this.eventDao=eventDao;
    }

    public void registerUser(Userdetails userdetails) {
        userDao.registerUser(userdetails);
    }

   

    public Userdetails login(Userdetails userdetails){
        List oUserdetails = userDao.login(userdetails);
        Userdetails tmpUserdetails = null;
        if(oUserdetails!=null){
            if(!oUserdetails.isEmpty()) {
                tmpUserdetails = (Userdetails)oUserdetails.get(0);
                return tmpUserdetails;
            }
        }
        return tmpUserdetails;

    }

    public void updateUserProfile(Userdetails userdetails) {
        userDao.update(userdetails);
    }
}
