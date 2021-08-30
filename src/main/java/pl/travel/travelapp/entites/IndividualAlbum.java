package pl.travel.travelapp.entites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.builders.IndividualAlbumDTOBuilder;
import pl.travel.travelapp.builders.IndividualAlbumFullInformationBuilder;
import pl.travel.travelapp.builders.PersonalInformationDTOBuilder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IndividualAlbum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 50)
    private String name;
    @Size(max = 800)
    private String description;
    private String mainPhoto;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Coordinates coordinate;
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalData owner;
    private boolean isPublic = false;
    @OneToMany(mappedBy = "individualAlbum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    private List <SharedAlbum> sharedAlbum = new ArrayList <>();
    @OneToMany(mappedBy = "individualAlbum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List <AlbumPhotos> photos = new ArrayList <>();

    public void addNewUserToAlbumShare(SharedAlbum sharedAlbum) {
        boolean isExist = this.sharedAlbum.stream().anyMatch(shared -> sharedAlbum.getUserId() == shared.getUserId());
        if ( isExist ) return;
        this.sharedAlbum.add(sharedAlbum);
        sharedAlbum.setIndividualAlbum(this);
    }

    public void deleteUserFromAlbumShare(SharedAlbum sharedAlbum) {
        this.sharedAlbum.remove(sharedAlbum);
    }

    public void addNewPhoto(AlbumPhotos albumPhotos) {
        this.photos.add(albumPhotos);
    }

    public void deletePhoto(AlbumPhotos albumPhotos) {
        this.photos.remove(albumPhotos);
    }

    public IndividualAlbumDTO buildIndividualAlbumDTO() {
        return new IndividualAlbumDTOBuilder()
                .setCoordinate(this.getCoordinate())
                .setDescription(this.getDescription())
                .setName(this.getName())
                .setMainPhoto(this.getMainPhoto())
                .setPersonalInformationDTO(new PersonalInformationDTOBuilder()
                        .setId(this.getOwner().getId())
                        .setName(this.getOwner().getFirstName())
                        .setSurName(this.getOwner().getSurName())
                        .setPhoto(this.getOwner().getProfilePicture())
                        .createPersonalInformationDTO())
                .setPublic(this.isPublic())
                .setId(this.getId())
                .setSharedAlbum(this.getSharedAlbum())
                .createIndividualAlbumDTO();
    }

    public IndividualAlbumFullInformationBuilder buildIndividualAlbumFullInformation(){
        return IndividualAlbumFullInformationBuilder.builder()
                .name(this.getName())
                .coordinate(this.getCoordinate())
                .id(this.getId())
                .description(this.getDescription())
                .personalInformationDTO(new PersonalInformationDTO(this.getOwner().getId() , this.getOwner().getFirstName() , this.getOwner().getSurName() , this.getOwner().getProfilePicture()))
                .mainPhoto(this.getMainPhoto())
                .sharedAlbumList(this.getSharedAlbum())
                .isPublic(this.isPublic())
                .photos(this.getPhotos())
                .build();
    }

}
