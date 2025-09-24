package org.bjm.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.bjm.entities.ForumCategory;
import org.bjm.entities.LokSabha;
import org.bjm.entities.LokSabhaNominate;
import org.bjm.entities.State;
import org.bjm.entities.SurveyCategory;
import org.bjm.entities.VidhanSabha;
import org.bjm.entities.VidhanSabhaNominate;

/**
 *
 * @author singh
 */
@Singleton
public class ReferenceDataServiceEjb implements ReferenceDataServiceEjbLocal {
    
    private static final Logger LOGGER = Logger.getLogger(ReferenceDataServiceEjb.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    private List<State> allStates;
    private Map<String, List<LokSabha>> lokSabhaMap;
    private Map<String, List<VidhanSabha>> vidhanSabhaMap;
    
    
    @PostConstruct
    public void init(){
        Query query= em.createNamedQuery("State.findAll", State.class);
        allStates=query.getResultList();
        LOGGER.info("Count of States loaded "+allStates.size());
        lokSabhaMap=new HashMap<>();
        Set<String> stateCodes=lokSabhaMap.keySet();
        for(String stateCode: stateCodes){
            query= em.createNamedQuery("LokSabha.findByStateCode", LokSabha.class);
            query.setParameter(1, stateCode);
            lokSabhaMap.put(stateCode, query.getResultList());
            String logStr="Count of LokSabhas for State %s is %d";
            String result=String.format(logStr, stateCode, query.getResultList().size());
            LOGGER.info(result);
        }
        vidhanSabhaMap=new HashMap<>();
        for(String stateCode: stateCodes){
            query= em.createNamedQuery("VidhanSabha.findByStateCode", VidhanSabha.class);
            query.setParameter(1, stateCode);
            vidhanSabhaMap.put(stateCode, query.getResultList());
            String logStr="Count of VidhanSabhas for State %s is %d";
            String result=String.format(logStr, stateCode, query.getResultList().size());
            LOGGER.info(result);
        }
        
        
    }
    @Override
    public List<ForumCategory> getForumCategories() {
        Query query=em.createNamedQuery("ForumCategory.findAll", ForumCategory.class);
        List<ForumCategory> toReturn=query.getResultList();
        LOGGER.info(String.format("Count of ForumCategory is %d", toReturn.size()));
        return toReturn;
    }

    
    
    @Override
    public List<String> getForumCategoriesAsStrings() {
        Query query=em.createNamedQuery("ForumCategory.findAll", ForumCategory.class);
        List<ForumCategory> forumCategories = query.getResultList();
        Set<String> forumCategoriesSet=new HashSet<>();
        for (ForumCategory fc: forumCategories){
            forumCategoriesSet.add(fc.getType());
        }
        List<String> toReturn=new ArrayList<>();
        toReturn.addAll(forumCategoriesSet);
        LOGGER.info(String.format("Count of ForumCategory as String is %d", toReturn.size()));
        return toReturn;
    }

    @Override
    public List<String> getForumSubCategories(String category) {
        Query query=em.createNamedQuery("ForumCategory.findByType", ForumCategory.class);
        query.setParameter(1, category);
        List<ForumCategory> forumCategories = query.getResultList();
        Set<String> forumSubCategoriesSet=new HashSet<>();
        for (ForumCategory fc: forumCategories){
            forumSubCategoriesSet.add(fc.getSubType());
        }
        List<String> toReturn=new ArrayList<>();
        toReturn.addAll(forumSubCategoriesSet);
        LOGGER.info(String.format("Count of ForumSubCategory for category %s is %d",category, toReturn.size()));
        return toReturn;
    }
    
    @Override
    public List<SurveyCategory> getSurveyCategories() {
        Query query=em.createNamedQuery("SurveyCategory.findAll", SurveyCategory.class);
        List<SurveyCategory> toReturn=query.getResultList();
        LOGGER.info(String.format("Count of SurveyCategory is %d", toReturn.size()));
        return toReturn;
    }

    @Override
    public List<String> getSurveyCategoriesAsStrings() {
        Query query=em.createNamedQuery("SurveyCategory.findAll", SurveyCategory.class);
        List<SurveyCategory> surveyCategories = query.getResultList();
        Set<String> surveyCategoriesSet=new HashSet<>();
        for (SurveyCategory sc: surveyCategories){
            surveyCategoriesSet.add(sc.getType());
        }
        List<String> toReturn=new ArrayList<>();
        toReturn.addAll(surveyCategoriesSet);
        LOGGER.info(String.format("Count of SurveyCategory as String is %d", toReturn.size()));
        return toReturn;
    }

    @Override
    public List<String> getSurveySubCategories(String category) {
        Query query=em.createNamedQuery("SurveyCategory.findByType", SurveyCategory.class);
        query.setParameter(1, category);
        List<SurveyCategory> surveyCategories = query.getResultList();
        Set<String> surveySubCategoriesSet=new HashSet<>();
        for (SurveyCategory sc: surveyCategories){
            surveySubCategoriesSet.add(sc.getSubType());
        }
        List<String> toReturn=new ArrayList<>();
        toReturn.addAll(surveySubCategoriesSet);
        LOGGER.info(String.format("Count of SurveySubCategory for category %s is %d",category, toReturn.size()));
        return toReturn;
    }

    public List<State> getAllStates() {
        return allStates;
    }

    public void setAllStates(List<State> allStates) {
        this.allStates = allStates;
    }

    @Override
    public List<LokSabha> getLokSabhasForState(String code) {
        return lokSabhaMap.get(code);
    }

    @Override
    public List<VidhanSabha> getVidhanSabhasForState(String code) {
        return vidhanSabhaMap.get(code);
    }

    @Override
    public State findStateByName(String name) {
        Query query = em.createNamedQuery("State.findByName", State.class);
        query.setParameter(1, name);
        return (State) query.getSingleResult();
    }

    @Override
    public List<LokSabhaNominate> getLokSabhaNominationsForConstituency(String stateCode, String constituency) {
        Query query = em.createNamedQuery("LokSabhaNominate.findForStateCodeAndConstituency", LokSabhaNominate.class);
        query.setParameter(1, stateCode);
        query.setParameter(2, constituency);
        List<LokSabhaNominate> toReturn = query.getResultList();
        LOGGER.info(String.format("Found %d Nominations for StateCode %s and LokSabha Constituency %s",toReturn.size(), stateCode, constituency));
        return toReturn;
                
    }

    @Override
    public LokSabhaNominate findByLSNominatedCandidate(String stateCode, String candidateName) {
        Query query = em.createNamedQuery("LokSabhaNominate.findByCandidateName", LokSabhaNominate.class);
        query.setParameter(1, stateCode);
        query.setParameter(2, candidateName);
        return (LokSabhaNominate)query.getSingleResult();
    }

    @Override
    public List<VidhanSabhaNominate> getVidhanSabhaNominationsForConstituency(String stateCode, String constituency) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public VidhanSabhaNominate findByVSNominatedCandidate(String stateCode, String candidateName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    

    

    
}
