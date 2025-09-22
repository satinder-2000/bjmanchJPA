package org.bjm.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.logging.Logger;
import org.bjm.entities.SurveyFromForum;
import org.bjm.entities.SurveyVote;

/**
 *
 * @author singh
 */
@Stateless
public class SurveyFromForumServiceEjb implements SurveyFromForumServiceEjbLocal {
    
    private static final Logger LOGGER = Logger.getLogger(SurveyFromForumServiceEjb.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;

    @Override
    public SurveyFromForum findById(int surveyFromForumId) {
        Query query=em.createNamedQuery("SurveyFromForum.findById", SurveyFromForum.class);
        return (SurveyFromForum) query.getSingleResult();
    }

    @Override
    public SurveyVote postSurveyFromForumVote(SurveyVote surveyVote) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SurveyVote> getAllVotesOnSurveyFromForum(int surveyFromForumId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
