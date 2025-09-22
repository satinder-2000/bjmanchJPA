package org.bjm.ejbs;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Logger;
import org.bjm.entities.Activity;

/**
 *
 * @author singh
 */
@Stateless
public class ActivityServiceEjb implements ActivityServiceEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(ActivityServiceEjb.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    @Resource(name = "zoneId")
    private String zoneId;

    @Override
    public List<Activity> getRecentActivities(int size) {
        Query query=em.createNamedQuery("Activity.findLastN", Activity.class);
        query.setParameter(1, size);
        List<Activity> toReturn=query.getResultList();
        return toReturn;
    }

    @Override
    public Activity logNewActivity(Activity activity) {
        activity.setCreatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(zoneId))));
        em.persist(activity);
        em.flush();
        LOGGER.info(String.format("New Activity persisted with ID: ", activity.getId()));
        return activity;
    }

    
}
