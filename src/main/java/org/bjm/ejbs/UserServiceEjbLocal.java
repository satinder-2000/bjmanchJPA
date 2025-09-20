package org.bjm.ejbs;

import jakarta.ejb.Local;
import org.bjm.dtos.UserDto;
import org.bjm.entities.Access;

/**
 *
 * @author singh
 */
@Local
public interface UserServiceEjbLocal {
    
    public Access registerUser(UserDto userDto);
    public boolean isUserRegistered(String email);
    
    
    
}
