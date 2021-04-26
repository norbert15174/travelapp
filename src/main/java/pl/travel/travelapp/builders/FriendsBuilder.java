package pl.travel.travelapp.builders;

import pl.travel.travelapp.models.FriendMessages;
import pl.travel.travelapp.models.Friends;
import pl.travel.travelapp.models.PersonalData;

import java.util.List;

public class FriendsBuilder {
    private long id;
    private PersonalData firstUser;
    private PersonalData secondUser;
    private List <FriendMessages> messages;

    public FriendsBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public FriendsBuilder setFirstUser(PersonalData firstUser) {
        this.firstUser = firstUser;
        return this;
    }

    public FriendsBuilder setSecondUser(PersonalData secondUser) {
        this.secondUser = secondUser;
        return this;
    }

    public FriendsBuilder setMessages(List <FriendMessages> messages) {
        this.messages = messages;
        return this;
    }

    public Friends createFriends() {
        return new Friends(id , firstUser , secondUser , messages);
    }
}