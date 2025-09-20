package org.bjm.ejbs;

import jakarta.ejb.Local;
import java.util.List;
import org.bjm.entities.LokSabha;
import org.bjm.entities.State;
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
    
}
