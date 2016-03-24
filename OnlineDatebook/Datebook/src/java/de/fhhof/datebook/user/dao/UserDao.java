/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.datebook.user.dao;

import de.fhhof.datebook.common.dao.GenericDao;
import de.fhhof.datebook.user.domain.Userdetails;
import java.util.List;

/**
 *
 * @author Shamim
 */
public interface UserDao extends GenericDao<Userdetails,Long>{

    List login(Userdetails oUserdetails);

    public void registerUser(Userdetails oUserdetails);
}
