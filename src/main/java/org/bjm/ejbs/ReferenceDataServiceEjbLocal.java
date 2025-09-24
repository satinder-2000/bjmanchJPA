package org.bjm.ejbs;

import jakarta.ejb.Local;
import java.util.List;
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
@Local
public interface ReferenceDataServiceEjbLocal {
    
    public List<State> getAllStates();
    public State findStateByName(String name);
    public List<LokSabha> getLokSabhasForState(String code);
    public List<LokSabhaNominate> getLokSabhaNominationsForConstituency(String stateCode, String constituency);
    public LokSabhaNominate findByLSNominatedCandidate(String stateCode, String candidateName);
    public List<VidhanSabha> getVidhanSabhasForState(String code);
    public List<VidhanSabhaNominate> getVidhanSabhaNominationsForConstituency(String stateCode, String constituency);
    public VidhanSabhaNominate findByVSNominatedCandidate(String stateCode, String candidateName);
    public List<ForumCategory> getForumCategories();
    public List<String> getForumCategoriesAsStrings();
    public List<String> getForumSubCategories(String category);
    public List<SurveyCategory> getSurveyCategories();
    public List<String> getSurveyCategoriesAsStrings();
    public List<String> getSurveySubCategories(String category);
    
    
}
