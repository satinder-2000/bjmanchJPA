package org.bjm.ejbs;

import jakarta.ejb.Local;
import java.util.List;
import org.bjm.entities.ForumCategory;
import org.bjm.entities.LokSabha;
import org.bjm.entities.State;
import org.bjm.entities.SurveyCategory;
import org.bjm.entities.VidhanSabha;

/**
 *
 * @author singh
 */
@Local
public interface ReferenceDataServiceEjbLocal {
    
    public List<State> getAllStates();
    public List<LokSabha> getLokSabhasForState(String code);
    public List<VidhanSabha> getVidhanSabhasForState(String code);
    public List<ForumCategory> getForumCategories();
    public List<String> getForumCategoriesAsStrings();
    public List<String> getForumSubCategories(String category);
    public List<SurveyCategory> getSurveyCategories();
    public List<String> getSurveyCategoriesAsStrings();
    public List<String> getSurveySubCategories(String category);
    
    
}
