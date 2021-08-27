package pl.travel.travelapp.builders;

import pl.travel.travelapp.entites.FriendsRequest;
import pl.travel.travelapp.entites.PersonalData;

public class FriendsRequestBuilder {
    private long id;
    private PersonalData sender;
    private long receiver;

    public FriendsRequestBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public FriendsRequestBuilder setSender(PersonalData sender) {
        this.sender = sender;
        return this;
    }

    public FriendsRequestBuilder setReceiver(long receiver) {
        this.receiver = receiver;
        return this;
    }

    public FriendsRequest createFriendsRequest() {
        return new FriendsRequest(sender, receiver);
    }
}