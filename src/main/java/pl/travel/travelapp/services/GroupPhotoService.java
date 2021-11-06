package pl.travel.travelapp.services;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.interfaces.GroupNotificationInterface;
import pl.travel.travelapp.services.delete.interfaces.IGroupDeleteService;
import pl.travel.travelapp.services.query.interfaces.IGroupPhotoQueryService;
import pl.travel.travelapp.services.query.interfaces.IGroupQueryService;
import pl.travel.travelapp.services.query.interfaces.IPersonalQueryService;
import pl.travel.travelapp.services.save.interfaces.IGroupPhotoSaveService;

@Service
public class GroupPhotoService {

    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${bucket-name}")
    private String bucket;
    @Value("${url-gcp-addr}")
    private String url;

    private final IGroupPhotoQueryService groupPhotoQueryService;
    private final IGroupPhotoSaveService groupPhotoSaveService;
    private final IGroupDeleteService groupDeleteService;
    private final GroupNotificationInterface groupNotificationInterface;
    private final IGroupQueryService groupQueryService;
    private final IPersonalQueryService personalQueryService;

    @Autowired
    public GroupPhotoService(IGroupPhotoQueryService groupPhotoQueryService , IGroupPhotoSaveService groupPhotoSaveService , IGroupDeleteService groupDeleteService , GroupNotificationInterface groupNotificationInterface , IGroupQueryService groupQueryService , IPersonalQueryService personalQueryService) {
        this.groupPhotoQueryService = groupPhotoQueryService;
        this.groupPhotoSaveService = groupPhotoSaveService;
        this.groupDeleteService = groupDeleteService;
        this.groupNotificationInterface = groupNotificationInterface;
        this.groupQueryService = groupQueryService;
        this.personalQueryService = personalQueryService;
    }




}
