package org.bjm.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.bjm.entities.LokSabha;
import org.bjm.entities.State;
import org.bjm.entities.VidhanSabha;

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

    
}
