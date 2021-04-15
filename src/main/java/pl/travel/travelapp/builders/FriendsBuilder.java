package pl.travel.travelapp.builders;

import pl.travel.travelapp.models.FriendMessages;
import pl.travel.travelapp.models.Friends;
import pl.travel.travelapp.models.LinkFriends;

import java.util.List;

public class FriendsBuilder {
    private long id;
    private List <LinkFriends> friends;
    private FriendMessages messages;
    private long groupLeader;

    public FriendsBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public FriendsBuilder setFriends(List <LinkFriends> friends) {
        this.friends = friends;
        return this;
    }

    public FriendsBuilder setMessages(FriendMessages messages) {
        this.messages = messages;
        return this;
    }

    public FriendsBuilder setGroupLeader(long groupLeader) {
        this.groupLeader = groupLeader;
        return this;
    }

    public Friends createFriends() {
        return new Friends(id , friends , messages , groupLeader);
    }
}