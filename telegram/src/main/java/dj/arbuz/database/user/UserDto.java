package dj.arbuz.database.user;

import dj.arbuz.database.group.GroupDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"subscribedGroups", "adminGroup"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "user_data")
@Table(schema = "public", catalog = "users_database")
public class UserDto {
    @Id
    @Column(name = "telegram_id", nullable = false)
    private String telegramId;

    @Builder.Default
    @Column(name = "admin", nullable = false)
    private Boolean admin = false;

    @Column(name = "user_id", nullable = false)
    private Long vkId;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @ManyToMany(mappedBy = "admins")
    private List<GroupDto> adminGroup;

    @ManyToMany(mappedBy = "subscribedUsers")
    private List<GroupDto> subscribedGroups;

    public void addSubscribedGroup(GroupDto subscribedGroup) {
        subscribedGroups.add(subscribedGroup);
    }
}
