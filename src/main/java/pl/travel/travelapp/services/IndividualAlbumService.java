package pl.travel.travelapp.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.BasicIndividualAlbumDTO;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.builders.IndividualAlbumBuilder;
import pl.travel.travelapp.builders.IndividualAlbumFullInformationBuilder;
import pl.travel.travelapp.entites.*;
import pl.travel.travelapp.exceptions.AccessForbiddenException;
import pl.travel.travelapp.exceptions.ObjectNotFoundException;
import pl.travel.travelapp.interfaces.CoordinateInterface;
import pl.travel.travelapp.interfaces.IndividualAlbumInterface;
import pl.travel.travelapp.interfaces.SharedAlbumInterface;
import pl.travel.travelapp.mappers.IndividualAlbumToBasicIndividualAlbumDTOMapper;
import pl.travel.travelapp.mappers.PersonalDataAlbumsToAlbumsDTOMapperClass;
import pl.travel.travelapp.repositories.*;
import pl.travel.travelapp.services.query.interfaces.IIndividualAlbumQueryService;
import pl.travel.travelapp.services.query.interfaces.IPersonalQueryService;
import pl.travel.travelapp.specification.criteria.AlbumSearchCriteria;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndividualAlbumService implements IndividualAlbumInterface, CoordinateInterface, SharedAlbumInterface {
    private IndividualAlbumRepository individualAlbumRepository;
    private CommentsRepository commentsRepository;
    private CoordinatesRepository coordinatesRepository;
    private AlbumPhotosRepository albumPhotosRepository;
    private CountryRepository countryRepository;
    private SharedAlbumRepository sharedAlbumRepository;
    private IPersonalQueryService personalQueryService;
    private IIndividualAlbumQueryService individualAlbumQueryService;
    private PersonalDataRepository personalDataRepository;

    public IndividualAlbumService(IndividualAlbumRepository individualAlbumRepository , CommentsRepository commentsRepository , CoordinatesRepository coordinatesRepository , AlbumPhotosRepository albumPhotosRepository , CountryRepository countryRepository , SharedAlbumRepository sharedAlbumRepository , IPersonalQueryService personalQueryService , IIndividualAlbumQueryService individualAlbumQueryService , PersonalDataRepository personalDataRepository) {
        this.individualAlbumRepository = individualAlbumRepository;
        this.commentsRepository = commentsRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.albumPhotosRepository = albumPhotosRepository;
        this.countryRepository = countryRepository;
        this.sharedAlbumRepository = sharedAlbumRepository;
        this.personalQueryService = personalQueryService;
        this.individualAlbumQueryService = individualAlbumQueryService;
        this.personalDataRepository = personalDataRepository;
    }

    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${bucket-name}")
    private String bucket;
    @Value("${url-gcp-addr}")
    private String url;

    private boolean checkIfIndividualAlbumDTOisCorrect(IndividualAlbumDTO individualAlbumDTO) {
        //check Coordinates
        Coordinates coordinates = individualAlbumDTO.getCoordinate();
        if ( coordinates == null ) return false;
        if ( coordinates.getLang() > 180 || coordinates.getLang() < -180 ) return false;
        if ( coordinates.getLat() > 90 || coordinates.getLat() < -90 ) return false;
        if ( coordinates.getPlace().isBlank() ) return false;
        if ( individualAlbumDTO.getName().isBlank() ) return false;
        if ( individualAlbumDTO.getDescription().isBlank() ) return false;
        return true;
    }

    @Override
    @Transactional
    public ResponseEntity <IndividualAlbumDTO> addNewAlbum(Principal principal , IndividualAlbumDTO individualAlbumDTO) {
        PersonalData user = personalQueryService.getPersonalInformationWithAlbums(principal.getName());
        if ( !checkIfIndividualAlbumDTOisCorrect(individualAlbumDTO) )
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        Country country = countryRepository.findById(individualAlbumDTO.getCoordinate().getCountry().getId()).get();
        Coordinates coord = individualAlbumDTO.getCoordinate();
        coord.setCountry(country);
        Coordinates coordinates = coordinatesRepository.save(coord);
        IndividualAlbum individualAlbum = new IndividualAlbumBuilder()
                .setPublic(individualAlbumDTO.isPublic())
                .setCoordinate(coordinates)
                .setName(individualAlbumDTO.getName())
                .setDescription(individualAlbumDTO.getDescription())
                .createIndividualAlbum();
        if ( individualAlbumDTO.getSharedAlbumList() != null ) {
            individualAlbumDTO.getSharedAlbumList().forEach(sharedAlbum -> {
                Optional <PersonalData> personalData = personalQueryService.findById(sharedAlbum.getUserId());
                if ( personalData.isPresent() ) {
                    SharedAlbum shared = new SharedAlbum().build(personalData.get());
                    individualAlbum.addNewUserToAlbumShare(shared);
                }
            });
        }
        individualAlbum.setOwner(user);
        return new ResponseEntity <>(individualAlbumRepository.save(individualAlbum).buildIndividualAlbumDTO() , HttpStatus.OK);
    }


    @Transactional
    @Override
    public ResponseEntity deleteAlbum(Principal principal , long id) {
        if ( id < 0 ) return new ResponseEntity("invalid id" , HttpStatus.BAD_REQUEST);
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        if ( individualAlbumRepository.findIndividualAlbumById(id , user.getId()).isPresent() ) {
            IndividualAlbum album = individualAlbumRepository.findById(id).get();
            user.removeAlbum(album);
            individualAlbumRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity("No such album was found or it is not yours" , HttpStatus.FORBIDDEN);
        }


    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <IndividualAlbum> findAlbum(Principal principal , long id) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        Optional <IndividualAlbum> individualAlbum = individualAlbumRepository.findIndividualAlbumByIdAndReturnFullInformation(id , user.getId());
        if ( individualAlbum.isPresent() ) return new ResponseEntity(individualAlbum.get() , HttpStatus.OK);
        return new ResponseEntity("The album does not exist or you do not have permission" , HttpStatus.FORBIDDEN);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <IndividualAlbum> findIndividualAlbumByIdOnlyOwner(Principal principal , long id) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        IndividualAlbum individualAlbum = individualAlbumRepository.findIndividualAlbumByOwnerAndReturnFullInformation(id , user.getId()).get();
        return new ResponseEntity(individualAlbum , HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <List <IndividualAlbumDTO>> findAllUserAlbums(Principal principal) {
        PersonalData user = personalQueryService.getPersonalInformationWithAlbums(principal.getName());
        if ( user.getAlbums().isEmpty() ) return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity <>(PersonalDataAlbumsToAlbumsDTOMapperClass.mapPersonalDataToAlbumsDTO(user) , HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <List <BasicIndividualAlbumDTO>> findAlbumsByUser(long id) {
        List <IndividualAlbum> individualAlbum = individualAlbumRepository.findUserPublicAlbumsByUserId(id);
        return new ResponseEntity(IndividualAlbumToBasicIndividualAlbumDTOMapper.mapindividualAlbumToBasicIndividualAlbumDTO(individualAlbum) , HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <List <IndividualAlbumDTO>> findAlbumsByName(String name , int page) {
        List <IndividualAlbum> individualAlbums = individualAlbumRepository.findUserAlbumsByAlbumName(name , PageRequest.of(page , 5));
        if ( individualAlbums.isEmpty() ) return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity <>(PersonalDataAlbumsToAlbumsDTOMapperClass.mapPersonalDataToAlbumsDTO(individualAlbums) , HttpStatus.OK);
    }


    @Override
    @Transactional
    public ResponseEntity <List <IndividualAlbumDTO>> findAlbumsByUserId(long id) {
        List <IndividualAlbum> individualAlbums = individualAlbumRepository.findUserPublicAlbumsByUserId(id);
        if ( individualAlbums.isEmpty() ) return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity <>(PersonalDataAlbumsToAlbumsDTOMapperClass.mapPersonalDataToAlbumsDTO(individualAlbums) , HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity <List <IndividualAlbumFullInformationBuilder>> findFullAlbumsByUserId(long id , Principal principal) {
        PersonalData personalData = personalQueryService.getPersonalInformation(principal.getName());
        List <IndividualAlbum> individualAlbums = individualAlbumRepository.findFullUserPublicAlbumsByUserId(id);
        if ( individualAlbums.isEmpty() ) return new ResponseEntity(HttpStatus.NO_CONTENT);
        Set <IndividualAlbumFullInformationBuilder> albums = new HashSet <>();
        for (IndividualAlbum album : individualAlbums) {
            if ( personalData.getId() != album.getOwner().getId() ) {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
            albums.add(album.buildIndividualAlbumFullInformation());
        }
        return new ResponseEntity(albums , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity <BasicIndividualAlbumDTO> modifyAlbum(Principal principal , BasicIndividualAlbumDTO basicIndividualAlbumDTO , Long id) {
        PersonalData personalData = personalQueryService.getPersonalInformation(principal.getName());
        Optional <IndividualAlbum> individualAlbum = individualAlbumRepository.findIndividualAlbumByOwner(id , personalData.getId());
        if ( individualAlbum.isPresent() ) {
            IndividualAlbum album = individualAlbum.get();
            if ( basicIndividualAlbumDTO.getCoordinate() != null ) {
                if ( basicIndividualAlbumDTO.getCoordinate().getCountry() != null ) {
                    Optional <Country> country = countryRepository.findFirstByCountry(basicIndividualAlbumDTO.getCoordinate().getCountry().getCountry());
                    if ( country.isPresent() ) {
                        album.getCoordinate().setCountry(country.get());
                    }
                    album.setCoordinate(basicIndividualAlbumDTO.getCoordinate());
                } else {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            }
            if ( basicIndividualAlbumDTO.getDescription() != null )
                album.setDescription(basicIndividualAlbumDTO.getDescription());
            if ( basicIndividualAlbumDTO.getName() != null )
                album.setName(basicIndividualAlbumDTO.getName());
            if ( album.isPublic() != basicIndividualAlbumDTO.isPublic() )
                album.setPublic(basicIndividualAlbumDTO.isPublic());
            return new ResponseEntity(IndividualAlbumToBasicIndividualAlbumDTOMapper.mapindividualAlbumToBasicIndividualAlbumDTO(individualAlbumRepository.save(album)) , HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @Transactional(readOnly = true)
    @Override
    public IndividualAlbum findUserAlbum(Principal principal , long id) {
        Long userId = personalQueryService.getPersonalInformation(principal.getName()).getId();
        if ( individualAlbumRepository.findIndividualAlbumByOwner(id , userId).isPresent() )
            return individualAlbumRepository.findIndividualAlbumByOwner(id , userId).get();
        else
            return null;
    }

    @Transactional
    @Override
    public IndividualAlbum saveAlbum(IndividualAlbum individualAlbum) {
        return individualAlbumRepository.save(individualAlbum);
    }

    @Transactional
    @Override
    public ResponseEntity <IndividualAlbumDTO> setMainPhotoToIndividualAlbum(Principal principal , MultipartFile file , long id) {
        IndividualAlbum individualAlbum = findUserAlbum(principal , id);
        if ( individualAlbum != null ) {
            try {
                String path = "user/" + principal.getName() + "/album/" + id + "/picture/main/" + file.getOriginalFilename();
                BlobId blobId = BlobId.of(bucket , path);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
                storage.create(blobInfo , file.getBytes());
                individualAlbum.setMainPhoto(url + path);
                IndividualAlbum userAlbum = saveAlbum(individualAlbum);
                return new ResponseEntity <>(userAlbum.buildIndividualAlbumDTO() , HttpStatus.OK);
            } catch ( IOException e ) {
                e.printStackTrace();
                return new ResponseEntity <>(HttpStatus.NOT_MODIFIED);
            }
        }
        return new ResponseEntity <>(HttpStatus.FORBIDDEN);
    }

    @Transactional
    @Override
    public ResponseEntity <List <AlbumDTO>> getAvailableAlbums(Principal principal) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        List <SharedAlbum> sharedAlbum = sharedAlbumRepository.findAvailableAlbums(user.getId());
        return new ResponseEntity(sharedAlbum.stream().map(shared -> new AlbumDTO().build(shared.getIndividualAlbum())).sorted(Comparator.comparing(k -> k.getAlbum().getId())).collect(Collectors.toList()) , HttpStatus.OK);
    }

    public List <SharedAlbum> getSharedByUserId(Long userId) {
        return sharedAlbumRepository.findAvailableAlbumsPage(userId , PageRequest.of(0 , 10));
    }

    @Transactional
    @Override
    public ResponseEntity <AlbumDTO> getAlbumFullInformation(Principal principal , Long albumId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        try {
            AlbumDTO album = individualAlbumQueryService.getFullAlbumInformation(user , albumId);
            return new ResponseEntity(album , HttpStatus.OK);
        } catch ( ObjectNotFoundException e ) {
            return new ResponseEntity(e.getMessage() , HttpStatus.NOT_FOUND);
        } catch ( AccessForbiddenException e ) {
            return new ResponseEntity(e.getMessage() , HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @Override
    public ResponseEntity deleteShared(Principal principal , List <Long> sharedIds) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        deleteSharedUser(user , sharedIds);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity addShared(Principal principal , List <Long> sharedIds , Long albumId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        List <PersonalData> users = personalQueryService.findAllById(sharedIds);
        try {
            IndividualAlbum album = individualAlbumRepository.findById(albumId).get();
            if ( user.getId() != album.getOwner().getId() ) return new ResponseEntity(HttpStatus.FORBIDDEN);
            users.forEach(person -> {
                        SharedAlbum sharedAlbum = new SharedAlbum().build(person);
                        album.addNewUserToAlbumShare(sharedAlbum);
                        individualAlbumRepository.save(album);
                    }
            );
            return new ResponseEntity(HttpStatus.CREATED);
        } catch ( NoSuchElementException ex ) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <List <IndividualAlbumDTO>> getPublicAlbumsMainWeb() {
        return new ResponseEntity <>(individualAlbumQueryService.getPublicAlbums() , HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <AlbumDTO> getPublicAlbumById(Long id) {
        Optional <AlbumDTO> album = individualAlbumQueryService.getAlbumById(id);
        if ( !album.isPresent() ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(album.get() , HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <List <BasicIndividualAlbumDTO>> getAlbumsByCriteria(Principal principal , AlbumSearchCriteria criteria , Integer page) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        criteria.setUserId(user.getId());
        return new ResponseEntity (individualAlbumQueryService.getAlbumsByCriteria(criteria, page), HttpStatus.OK);
    }


    public Set <SharedAlbum> findAllUserSharedAlbumsByOwnerAndSharedUserId(long id , long ownerId) {
        return sharedAlbumRepository.findAllUserSharedAlbumsBySharedUserId(id , ownerId);
    }

    public void deleteSharedUserDuringFriendDelete(List <Long> sharedIds) {
        for (Long id : sharedIds) {
            Optional <SharedAlbum> sharedAlbums = sharedAlbumRepository.findById(id);
            if ( sharedAlbums.isPresent() ) {
                IndividualAlbum album = sharedAlbums.get().getIndividualAlbum();
                sharedAlbumRepository.deleteById(id);
            }
        }
    }

    private void deleteSharedUser(PersonalData user , List <Long> sharedIds) {
        for (Long id : sharedIds) {
            Optional <SharedAlbum> sharedAlbums = sharedAlbumRepository.findById(id);
            if ( sharedAlbums.isPresent() ) {
                IndividualAlbum album = sharedAlbums.get().getIndividualAlbum();
                if ( album.getOwner().getId() == user.getId() ) {
                    sharedAlbumRepository.deleteById(id);
                }
            }
        }
    }


}
