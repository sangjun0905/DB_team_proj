package team1.domain.review;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team1.domain.common.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_picture")
public class ReviewPicture extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name = "`Key`", nullable = false, length = 255)
    private String keyName;

    @Column(name = "review_picture", length = 255)
    private String pictureUrl;

    public ReviewPicture(Review review, String keyName, String pictureUrl) {
        this.review = review;
        this.keyName = keyName;
        this.pictureUrl = pictureUrl;
    }
}
