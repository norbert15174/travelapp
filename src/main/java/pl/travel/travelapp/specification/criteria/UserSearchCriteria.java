package pl.travel.travelapp.specification.criteria;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserSearchCriteria {

    private String name;
    private String from;

    public boolean isFromExist() {
        return !Strings.isNullOrEmpty(this.from);
    }

}
