package com.ua.erent.module.core.account.auth.user.domain.storage;

import com.ua.erent.BuildConfig;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;
import com.ua.erent.module.core.account.auth.user.domain.vo.ContactInfo;
import com.ua.erent.module.core.account.auth.user.domain.vo.FullName;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;
import com.ua.erent.module.core.presentation.mvp.view.util.MyURL;

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
                .setContactInfo(new ContactInfo(userPO.getEmail(), userPO.getPhone(), userPO.getSkype()))
                .setSkype(userPO.getSkype())
                .setAvatar(userPO.getUrl() == null ? null : new MyURL(BuildConfig.API_BASE_URL.concat(userPO.getUrl())))
                .build();
    }

    public static UserPO toPersistenceObject(@NotNull User user) {
        Preconditions.checkNotNull(user);
        return new UserPO()
                .setId(user.getId().getId())
                .setEmail(user.getContactInfo().getEmail())
                .setUsername(user.getFullName().getUsername())
                .setPhone(user.getContactInfo().getPhone())
                .setUrl(user.getAvatar() != null ? user.getAvatar().toExternalForm() : null)
                .setSkype(user.getContactInfo().getSkype());
    }
}
