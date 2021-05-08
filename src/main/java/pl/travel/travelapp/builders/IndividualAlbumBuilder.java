package pl.travel.travelapp.builders;

import pl.travel.travelapp.models.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class IndividualAlbumBuilder {
    private long id;
    private String name;
    private String description;
    private String mainPhoto;
    private Coordinates coordinate;
    private PersonalData owner;
    private boolean isPublic = false;
    private List <SharedAlbum> sharedAlbum = new ArrayList <>();
    private List<AlbumPhotos> photos = new ArrayList<>();

    public IndividualAlbum createIndividualAlbum(){
        return new IndividualAlbum(id,name,description,mainPhoto,coordinate,owner,isPublic,sharedAlbum,photos);
    }


    public IndividualAlbumBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public IndividualAlbumBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public IndividualAlbumBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public IndividualAlbumBuilder setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
        return this;
    }

    public IndividualAlbumBuilder setCoordinate(Coordinates coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public IndividualAlbumBuilder setOwner(PersonalData owner) {
        this.owner = owner;
        return this;
    }

    public IndividualAlbumBuilder setPublic(boolean aPublic) {
        isPublic = aPublic;
        return this;
    }

    public IndividualAlbumBuilder setSharedAlbum(List <SharedAlbum> sharedAlbum) {
        this.sharedAlbum = sharedAlbum;
        return this;
    }

    public IndividualAlbumBuilder setPhotos(List <AlbumPhotos> photos) {
        this.photos = photos;
        return this;
    }
}
