package com.atstudio.eyfofalafel.backend.controller.auth

import com.atstudio.eyfofalafel.backend.entities.security.User
import org.junit.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import static com.atstudio.eyfofalafel.backend.controller.MapperHelper.sampleRestUser
import static com.atstudio.eyfofalafel.backend.controller.MapperHelper.sampleUser
import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class UserRestMapperTest {

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder)

    private UserRestMapper underTest = new UserRestMapper(encoder)

    @Test
    void toEntity() {
        when(encoder.encode(anyString())).thenReturn("encoded")
        UserRestDto rest = sampleRestUser()
        User transformed = underTest.toEntity(rest)
        assert transformed.getUsername() == rest.getUserName() &&
                transformed.getFirstName() == rest.getFirstName() &&
                transformed.getLastName() == rest.getLastName() &&
                // passwords must be hashed, we don't care about client provided id
                transformed.getPassword() == 'encoded' &&
                transformed.getId() == null
    }

    @Test
    void toRestObject() {
        User sample = sampleUser()
        UserRestDto rest = underTest.toRest(sample)
        // direct props vs getters -> declares contract with clients
        assert rest['userName'] == sample.getUsername() &&
                rest['id'] == sample.getId() &&
                rest['firstName'] == sample.getFirstName() &&
                rest['lastName'] == sample.getLastName() &&
                // no password for clients
                rest['password'] == null
    }




}
