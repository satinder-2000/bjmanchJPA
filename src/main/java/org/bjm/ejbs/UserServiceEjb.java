package org.bjm.ejbs;

import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Logger;
import org.bjm.dtos.UserDto;
import org.bjm.entities.Access;
import org.bjm.entities.User;

/**
 *
 * @author singh
 */
@Stateful
public class UserServiceEjb implements UserServiceEjbLocal {
    
    private static final Logger LOGGER=Logger.getLogger(UserServiceEjb.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    @Inject
    private EmailServiceEjbLocal emailServiceEjbLocal;
    
    @Override
    public Access registerUser(UserDto userDto) {
        User user= new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setGender(userDto.getGender());
        user.setDob(Date.valueOf(userDto.getDob()));
        user.setMobile(userDto.getMobile());
        user.setStateName(userDto.getStateName());
        user.setLokSabha(userDto.getLokSabha());
        
        Access access= new Access();
        access.setEmail(userDto.getEmail());
        access.setCreatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Kolkata"))));
        access.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Kolkata"))));
        
        em.persist(user);
        em.persist(access);
        em.flush();
        LOGGER.info("User regisered with ID "+user.getId()+" along with Access with ID "+access.getId());
        emailServiceEjbLocal.sendUserRegisteredEmail(access);
        
        
        return access;
    }

    @Override
    public boolean isUserRegistered(String email) {
        Query query=em.createNamedQuery("User.findByEmail", User.class);
        query.setParameter(1, email);
        if(query.getFirstResult()==0){
            return false;
        }else{
            return true;
        }
    }

    
}
