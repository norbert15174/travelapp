package pl.travel.travelapp.builders;

import pl.travel.travelapp.models.Friends;
import pl.travel.travelapp.models.LinkFriends;
import pl.travel.travelapp.models.PersonalData;

public class LinkFriendsBuilder {
    private PersonalData user;
    private Friends friend;

    public LinkFriendsBuilder setUser(PersonalData user) {
        this.user = user;
        return this;
    }

    public LinkFriendsBuilder setFriend(Friends friend) {
        this.friend = friend;
        return this;
    }

    public LinkFriends createLinkFriends() {
        return new LinkFriends(user , friend);
    }
}