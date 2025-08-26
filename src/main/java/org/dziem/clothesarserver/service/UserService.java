package org.dziem.clothesarserver.service;

import java.util.UUID;

public interface UserService {

    boolean userExists(String userId);

    boolean userExists(UUID userId);

}
