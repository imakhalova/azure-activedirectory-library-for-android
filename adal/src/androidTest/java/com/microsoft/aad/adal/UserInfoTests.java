// Copyright (c) Microsoft Corporation.
// All rights reserved.
//
// This code is licensed under the MIT License.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package com.microsoft.aad.adal;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

// TODO: Likely Unit Test

import android.test.suitebuilder.annotation.SmallTest;
import junit.framework.TestCase;

public class UserInfoTests extends TestCase {

    @SmallTest
    public void testUserInfo() {
        UserInfo user = new UserInfo("userid", "givenName", "familyName", "identity", "userid");
        assertEquals("same userid", "userid", user.getUserId());
        assertEquals("same name", "givenName", user.getGivenName());
        assertEquals("same family name", "familyName", user.getFamilyName());
        assertEquals("same idenity name", "identity", user.getIdentityProvider());
        assertEquals("same flag", "userid", user.getDisplayableId());
    }

    @SmallTest
    public void testTestIdTokenParam_upn() {
        IdToken idToken = setTestIdTokenFields("objectid", "upnid", "email", "subj");
        UserInfo info = new UserInfo(idToken);
        assertEquals("same userid", "objectid", info.getUserId());
        assertEquals("same name", "givenName", info.getGivenName());
        assertEquals("same family name", "familyName", info.getFamilyName());
        assertEquals("same idenity name", "provider", info.getIdentityProvider());
        assertEquals("check displayable", "upnid", info.getDisplayableId());

        idToken = setTestIdTokenFields("", "upnid", "email", "subj");
        info = new UserInfo(idToken);
        assertEquals("same userid", "subj", info.getUserId());
        assertEquals("same name", "givenName", info.getGivenName());
        assertEquals("same family name", "familyName", info.getFamilyName());
        assertEquals("same idenity name", "provider", info.getIdentityProvider());
        assertEquals("check displayable", "upnid", info.getDisplayableId());

        idToken = setTestIdTokenFields("", "upnid", "email", "");
        info = new UserInfo(idToken);
        assertNull("null userid", info.getUserId());
        assertEquals("same name", "givenName", info.getGivenName());
        assertEquals("same family name", "familyName", info.getFamilyName());
        assertEquals("same idenity name", "provider", info.getIdentityProvider());
        assertEquals("check displayable", "upnid", info.getDisplayableId());

        idToken = setTestIdTokenFields("", "", "email", "");
        info = new UserInfo(idToken);
        assertNull("null userid", info.getUserId());
        assertEquals("same name", "givenName", info.getGivenName());
        assertEquals("same family name", "familyName", info.getFamilyName());
        assertEquals("same idenity name", "provider", info.getIdentityProvider());
        assertEquals("check displayable", "email", info.getDisplayableId());

        idToken = setTestIdTokenFields("", "", "", "");
        info = new UserInfo(idToken);
        assertNull("null userid", info.getUserId());
        assertNull("check displayable", info.getDisplayableId());
        assertEquals("same name", "givenName", info.getGivenName());
        assertEquals("same family name", "familyName", info.getFamilyName());
        assertEquals("same idenity name", "provider", info.getIdentityProvider());
    }

    @SmallTest
    public void testTestIdTokenParam_password() {
        IdToken idToken = setTestIdTokenFields("objectid", "upnid", "email", "subj");
        Calendar calendar = new GregorianCalendar();
        int seconds = 1000;
        idToken.mPasswordExpiration = seconds;
        idToken.mPasswordChangeUrl = "https://github.com/MSOpenTech/azure-activedirectory-library";
        UserInfo info = new UserInfo(idToken);
        calendar.add(Calendar.SECOND, seconds);
        Date passwordExpiresOn = calendar.getTime();

        assertEquals("same userid", "objectid", info.getUserId());
        assertEquals("same name", "givenName", info.getGivenName());
        assertEquals("same family name", "familyName", info.getFamilyName());
        assertEquals("same idenity name", "provider", info.getIdentityProvider());
        assertEquals("check displayable", "upnid", info.getDisplayableId());
        assertEquals("check expireson", passwordExpiresOn.getTime() / 1000, info
                .getPasswordExpiresOn().getTime() / 1000);
        assertEquals("check uri", "https://github.com/MSOpenTech/azure-activedirectory-library",
                info.getPasswordChangeUrl().toString());
    }

    private IdToken setTestIdTokenFields(String objId, String upn, String email, String subject) {
        IdToken idToken = new IdToken();
        idToken.mObjectId = objId;
        idToken.mSubject = subject;
        idToken.mTenantId = "tenantid";
        idToken.mUpn = upn;
        idToken.mGivenName = "givenName";
        idToken.mFamilyName = "familyName";
        idToken.mEmail = email;
        idToken.mIdentityProvider = "provider";
        return idToken;
    }
}
