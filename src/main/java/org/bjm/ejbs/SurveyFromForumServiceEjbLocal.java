package org.bjm.ejbs;

import jakarta.ejb.Local;
import java.util.List;
import org.bjm.entities.SurveyFromForum;
import org.bjm.entities.SurveyVote;

/**
 *
 * @author singh
 */
@Local
public interface SurveyFromForumServiceEjbLocal {
    public SurveyFromForum findById(int surveyFromForumId);
    public SurveyVote postSurveyFromForumVote(SurveyVote surveyVote);
    public List<SurveyVote> getAllVotesOnSurveyFromForum(int surveyFromForumId);
}
