package org.bjm.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.logging.Logger;
import org.bjm.entities.Survey;
import org.bjm.entities.SurveyVote;

/**
 *
 * @author singh
 */
@Stateless
public class SurveyServiceEjb implements SurveyServiceEjbLocal {
    
    private static final Logger LOGGER = Logger.getLogger(SurveyServiceEjb.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;

    @Override
    public Survey findById(int surveyId) {
        Query query=em.createNamedQuery("Survey.findById", Survey.class);
        query.setParameter(1, surveyId);
        return (Survey)query.getSingleResult();
    }

    @Override
    public SurveyVote postSurveyVote(SurveyVote surveyVote) {
        em.persist(surveyVote);
        em.flush();
        LOGGER.info(String.format("SurveyVote created with ID %d", surveyVote.getId()));
        return surveyVote;
    }

    @Override
    public List<SurveyVote> getAllVotesOnSurvey(int surveyId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
