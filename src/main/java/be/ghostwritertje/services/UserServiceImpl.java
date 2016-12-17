package be.ghostwritertje.services;

import org.springframework.stereotype.Service;

/**
 * Created by Ghostwritertje
 * Date: 29-Sep-16.
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public String getLoggedInUser() {
        return "Example User";
    }
}
