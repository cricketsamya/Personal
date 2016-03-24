/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.datebook.user.dao;

import de.fhhof.datebook.common.dao.GenericDaoImpl;
import de.fhhof.datebook.user.domain.Userdetails;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sameer
 */
@Repository
public class UserDaoImpl extends GenericDaoImpl<Userdetails, Long> implements UserDao {

    public List login(Userdetails oUserdetails) {

        List tmpList = entityManager.createNamedQuery("Userdetails.findByStrEmailIDAndStrPassword").setParameter("strEmailID", oUserdetails.getStrEmailID()).setParameter("strPassword", oUserdetails.getStrPassword()).getResultList();
        return tmpList;
    }

    public void registerUser(Userdetails oUserdetails) {
        this.persist(oUserdetails);
    }


}
