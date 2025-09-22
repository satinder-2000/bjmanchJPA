package org.bjm.mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.bjm.ejbs.ActivityServiceEjbLocal;
import org.bjm.entities.Activity;

/**
 *
 * @author singh
 */
@Named(value = "activityMBean")
@ApplicationScoped
public class ActivityMBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(ActivityMBean.class.getName());
    private List<Activity> activityList;
    private int activityListSize;
    
    @Inject
    private ActivityServiceEjbLocal activityServiceEjbLocal;
    
    @PostConstruct
    public void init(){
        activityList = new ArrayList<>();
        ServletContext servletContext=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        activityListSize=Integer.getInteger(servletContext.getInitParameter("activityListSize"));
        activityList=activityServiceEjbLocal.getRecentActivities(activityListSize);
    }
    
    public void addActivity(Activity activity){
        
        Activity activityResult=activityServiceEjbLocal.logNewActivity(activity);
        LOGGER.info(String.format("Activity created with ID: %s", activityResult.getId()));
        if (activityList.size()==activityListSize){//accomodate new one at the expense of the oldest record - at the bottom of the List
            activityList.remove(0);
        }
        activityList.add(activity);
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }
    
    
    
    
}
