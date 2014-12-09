package jp.terasoluna.fw.collector.db;

import java.io.Serializable;

public class UserBean implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1307205338355441359L;

    private String userId = null;

    private String familyName = null;

    private String firstName = null;

    private String userAge = null;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

}
