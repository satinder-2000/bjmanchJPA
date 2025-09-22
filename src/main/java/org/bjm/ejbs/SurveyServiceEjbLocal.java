package org.bjm.ejbs;

import jakarta.ejb.Local;
import java.util.List;
import org.bjm.entities.Survey;
import org.bjm.entities.SurveyVote;

/**
 *
 * @author singh
 */
@Local
public interface SurveyServiceEjbLocal {
    
    public Survey findById(int surveyId);
    public SurveyVote postSurveyVote(SurveyVote surveyVote);
    public List<SurveyVote> getAllVotesOnSurvey(int surveyId);
    
}
