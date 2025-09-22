package org.bjm.ejbs;

import jakarta.ejb.Local;
import java.util.List;
import org.bjm.entities.Activity;

/**
 *
 * @author singh
 */
@Local
public interface ActivityServiceEjbLocal {
    
    public List<Activity> getRecentActivities(int size);
    public Activity logNewActivity(Activity activity);
    
}
