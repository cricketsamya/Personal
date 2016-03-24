
package de.fhhof.datebook.event.dao;

import de.fhhof.datebook.common.dao.GenericDaoImpl;
import de.fhhof.datebook.user.domain.Eventdetails;
import de.fhhof.datebook.user.domain.Userdetails;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sumedh
 */
@Repository
public class EventDetailsDaoImpl extends GenericDaoImpl<Eventdetails, Integer> implements EventDetailsDao {

    public void Addevent(Eventdetails oEventdetails) {
        this.persist(oEventdetails);
    }

    public List<Eventdetails> fetchEvents(Eventdetails oEventdetails) {

        List<Eventdetails> tmpList = entityManager.createNamedQuery("Eventdetails.findByIntUserIDandIntEventtypeID").setParameter("intUserID", oEventdetails.getIntUserID().getIntUserID()).setParameter("intEventtypeID", oEventdetails.getIntEventTypeID().getIntEventtypeID()).getResultList();
        return tmpList;
    }

    public void deleteEvent(Eventdetails oEventdetails){

        List<Eventdetails> tmpList = entityManager.createNamedQuery("Eventdetails.findByIntEventID").setParameter("intEventID", oEventdetails.getIntEventID()).getResultList();
        Eventdetails tmpEventdetails = new Eventdetails();
        if(!tmpList.isEmpty()){
            tmpEventdetails = (Eventdetails)tmpList.get(0);
            this.delete(tmpEventdetails);
        }
        
    }
	@Override
	public List<Eventdetails> getUserEventsByMonth(Integer userId,Date start, Date end) {
            String clause = " t.dtEventdate >=  ?1 and t.dtEventdate <=  ?2 and t.intUserID.intUserID=?3 order by t.dtEventdate ASC ";
            
            String hsql = "Select t from " + Eventdetails.class.getName()+ " t where " + clause;
            log.debug(hsql);
            Query query = entityManager.createQuery(hsql);
            /** bind parameters */

            query.setParameter(1, start,TemporalType.TIMESTAMP);
            query.setParameter(2, end,TemporalType.TIMESTAMP);
            query.setParameter(3, userId);

            return query.getResultList();

	}

	public List<Eventdetails> getUserAllEvents(Integer userId,Integer type,Date start, Date end) {
		

            String clause = " t.dtEventdate >=  ?1 and t.dtEventdate <=  ?2 and t.intUserID.intUserID=?3 and t.intEventTypeID.intEventtypeID=?4 order by t.dtEventdate ASC ";
            
            String hsql = "Select t from " + Eventdetails.class.getName()+ " t where " + clause;
            
            Query query = entityManager.createQuery(hsql);
            /** bind parameters */
            
            query.setParameter(1, start,TemporalType.TIMESTAMP);
            query.setParameter(2, end,TemporalType.TIMESTAMP);
            query.setParameter(3, userId);
            query.setParameter(4, type);

            return query.getResultList();

	}

    public List<Eventdetails> getEventDetailsByDate(final Userdetails objUserdetails, Date dtStartDate, Date dtEndDate) {

        List tmpList = entityManager.createNamedQuery("Eventdetails.findByDates").setParameter("intUserID", objUserdetails.getIntUserID())
                    .setParameter("dtEventdateStart", dtStartDate)
                    .setParameter("dtEventdateEnd", dtEndDate)
                    .getResultList();
        return tmpList;
    }

    public void updateEvent(Eventdetails oEventdetails){
         this.update(oEventdetails);
     }

      public List<Eventdetails> getEventDetailsByID(Eventdetails oEventdetails){

          List<Eventdetails> lsteventdetails= entityManager.createNamedQuery("Eventdetails.findByIntEventID").setParameter("intEventID", oEventdetails.getIntEventID()).getResultList();
          return lsteventdetails;
      }

	@Override
	public Eventdetails getEvent(Integer eventId) {
		return this.loadById(eventId);
	}

	@Override
	public List<Eventdetails> getUserEventsByType(Integer userId, Integer type) {
        List<Eventdetails> tmpList = entityManager.createNamedQuery("Eventdetails.findByEventFreq").setParameter("intUserID", userId).setParameter("intFrequencyID", type).getResultList();
        return tmpList;
	}
}
