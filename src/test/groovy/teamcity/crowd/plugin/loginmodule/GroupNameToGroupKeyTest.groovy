package teamcity.crowd.plugin.loginmodule

import jetbrains.buildServer.groups.SUserGroup
import jetbrains.buildServer.groups.UserGroupManager
import org.junit.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class GroupNameToGroupKeyTest {
    private def UserGroupManager userGroupManager = mock(UserGroupManager)
    private def groupToKey = new GroupNameToGroupKey(userGroupManager)

    @Test
    void 'should trim key to be 16 characters'(){
        assert groupToKey.transform('longer-name than it should') == 'LONGER_NAME_THAN'
    }

    @Test
    void 'should replace - wih _'(){
        assert groupToKey.transform('some-name') == 'SOME_NAME'
    }

    @Test
    void 'should replace spaces in group names into _'(){
        assert groupToKey.transform('some name') == 'SOME_NAME'
    }

    @Test
    void 'should upcase the group name into key'(){
         assert groupToKey.transform('foobar') == 'FOOBAR'
    }

    @Test
    void 'should return 16 char name'(){
        assert groupToKey.transform('1234567890ABCDEF') == '1234567890ABCDEF'
    }

    @Test
    void 'should return a unique key if default exists'(){
        when(userGroupManager.findUserGroupByKey('1234567890ABCDEF')).thenReturn(mock(SUserGroup))
        assert groupToKey.transform('1234567890ABCDEF') == '1234567890ABC000'
    }

    @Test
    void 'should return a unique key if first munged key exists'(){
        when(userGroupManager.findUserGroupByKey('1234567890ABCDEF')).thenReturn(mock(SUserGroup))
        when(userGroupManager.findUserGroupByKey('1234567890ABC000')).thenReturn(mock(SUserGroup))

        assert groupToKey.transform('1234567890ABC000') == '1234567890ABC001'
        assert groupToKey.transform('1234567890ABCDEF') == '1234567890ABC001'
    }
}
