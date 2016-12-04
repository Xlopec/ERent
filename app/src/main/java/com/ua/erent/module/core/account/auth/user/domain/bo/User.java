package com.ua.erent.module.core.account.auth.user.domain.bo;

import com.ua.erent.module.core.account.auth.user.domain.vo.ContactInfo;
import com.ua.erent.module.core.account.auth.user.domain.vo.FullName;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;
import com.ua.erent.module.core.presentation.mvp.view.util.MyURL;
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
    private final MyURL avatar;
    private final ContactInfo contactInfo;

    public static class Builder implements IBuilder<User> {

        private UserID id;
        private FullName fullName;
        private ContactInfo contactInfo;
        private MyURL avatar;
        private String phone;
        private String skype;

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

        public MyURL getAvatar() {
            return avatar;
        }

        public Builder setAvatar(MyURL avatar) {
            this.avatar = avatar;
            return this;
        }

        public String getPhone() {
            return phone;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public String getSkype() {
            return skype;
        }

        public Builder setSkype(String skype) {
            this.skype = skype;
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
        this.avatar = builder.getAvatar();
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

    public MyURL getAvatar() {
        return avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!fullName.equals(user.fullName)) return false;
        if (!avatar.equals(user.avatar)) return false;
        return contactInfo.equals(user.contactInfo);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + avatar.hashCode();
        result = 31 * result + contactInfo.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName=" + fullName +
                ", avatar=" + avatar +
                ", contactInfo=" + contactInfo +
                '}';
    }
}
