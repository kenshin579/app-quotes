import React from 'react';
import PageTemplate from "../components/common/PageTemplate";
import ProfileContainer from "../containers/user/profile/ProfileContainer";

const ProfilePage = () => {
    return (
        <PageTemplate>
            <ProfileContainer/>
        </PageTemplate>
    );
};

export default ProfilePage;