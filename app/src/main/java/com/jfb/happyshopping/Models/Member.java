package com.jfb.happyshopping.Models;

/**
 * Created by Shanyl Jimenez on 9/3/2016.
 */
public class Member {

    private String MemberID;
    private String FirstName;
    private String LastName;

    public Member() {
    }

    public Member(String memberID, String firstName, String lastName) {
        MemberID = memberID;
        FirstName = firstName;
        LastName = lastName;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String memberID) {
        MemberID = memberID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
