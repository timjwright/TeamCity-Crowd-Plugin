package teamcity.crowd.plugin.loginmodule;

import jetbrains.buildServer.groups.UserGroupManager;

import java.util.Formatter;

public class GroupNameToGroupKey {
    private final UserGroupManager userGroupManager;

    public GroupNameToGroupKey(UserGroupManager userGroupManager) {
        this.userGroupManager = userGroupManager;
    }

    //private final UserGroupManager groupManger;

    public String transform(String groupName) {
        String sanitizedGroupName = groupName.toUpperCase().replaceAll(" ", "_").replaceAll("-", "_");
        if (sanitizedGroupName.length() > 16) {
            return sanitizedGroupName.substring(0, 16);
        }
        if ( userGroupManager.findUserGroupByKey(sanitizedGroupName) == null ) {
            return sanitizedGroupName;
        } else {
            return findAlternative(sanitizedGroupName);
        }
    }

    private String findAlternative(String sanitizedGroupName) {
        String prefix = sanitizedGroupName.substring(0,13);
        for ( int i = 0 ; i < 1000 ; i++ ) {
            String newName = prefix + String.format("%03d", i);
            if (userGroupManager.findUserGroupByKey(newName) == null) {
                return newName;
            }
        }
        // What should we do now....
        return sanitizedGroupName;
    }
}
