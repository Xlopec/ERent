package com.ua.erent.module.core.account.auth.user.domain.storage;

import com.ua.erent.module.core.account.auth.user.domain.bo.User;
import com.ua.erent.module.core.account.auth.domain.vo.ContactInfo;
import com.ua.erent.module.core.account.auth.domain.vo.FullName;
import com.ua.erent.module.core.account.auth.domain.vo.UserID;

import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/5/2016.
 */

public final class UserMapper {

    private UserMapper() {
        throw new RuntimeException();
    }

    public static User toUser(@NotNull UserPO userPO) {
        Preconditions.checkNotNull(userPO);
        return new User.Builder().setId(new UserID(userPO.getId()))
                .setFullName(new FullName(userPO.getUsername()))
                .setContactInfo(new ContactInfo(userPO.getEmail()))
                .build();
    }

    public static UserPO toPersistenceObject(@NotNull User user) {
        Preconditions.checkNotNull(user);
        return new UserPO()
                .setId(user.getId().getId())
                .setEmail(user.getContactInfo().getEmail())
                .setUsername(user.getFullName().getUsername());
    }
}
