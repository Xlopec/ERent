package com.ua.erent.module.core.account.auth.user.domain.bo;

import com.ua.erent.module.core.account.auth.user.domain.vo.ContactInfo;
import com.ua.erent.module.core.account.auth.user.domain.vo.FullName;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;
import com.ua.erent.module.core.util.IBuilder;

import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * <p>
 *     Represents user aggregate
 * </p>
 * Created by Максим on 11/4/2016.
 */

public final class User {

    private final UserID id;
    private final FullName fullName;
    private final ContactInfo contactInfo;

    public static class Builder implements IBuilder<User> {

        private UserID id;
        private FullName fullName;
        private ContactInfo contactInfo;

        public Builder() {
        }

        public Builder(@NotNull User user) {
            setId(user.getId())
                    .setFullName(user.getFullName())
                    .setContactInfo(user.getContactInfo());
        }

        public final UserID getId() {
            return id;
        }

        public final Builder setId(UserID id) {
            this.id = id;
            return this;
        }

        public final FullName getFullName() {
            return fullName;
        }

        public final Builder setFullName(FullName fullName) {
            this.fullName = fullName;
            return this;
        }

        public final ContactInfo getContactInfo() {
            return contactInfo;
        }

        public final Builder setContactInfo(ContactInfo contactInfo) {
            this.contactInfo = contactInfo;
            return this;
        }

        @Override
        public final User build() {
            return new User(this);
        }
    }

    private User(@NotNull Builder builder) {
        Preconditions.checkNotNull(builder, "builder == null");
        this.id = Preconditions.checkNotNull(builder.getId());
        this.fullName = Preconditions.checkNotNull(builder.getFullName());
        this.contactInfo = Preconditions.checkNotNull(builder.getContactInfo());
    }

    public UserID getId() {
        return id;
    }

    public FullName getFullName() {
        return fullName;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }
}
