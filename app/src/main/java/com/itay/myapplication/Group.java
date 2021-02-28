package com.itay.myapplication;

public class Group {
    private String GroupID;
    private String GroupName;
    private String GroupAdmin;

    public Group(String groupID, String groupName, String groupAdmin){
        GroupID = groupID;
        GroupName=groupName;
        GroupAdmin = groupAdmin;
    }

    public Group(){

    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupAdmin() {
        return GroupAdmin;
    }

    public void setGroupAdmin(String groupAdmin) {
        GroupAdmin = groupAdmin;
    }
}
