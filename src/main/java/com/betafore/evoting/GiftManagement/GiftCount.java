package com.betafore.evoting.GiftManagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gift_count")
public class GiftCount {

    @Id
    @SequenceGenerator(
        name = "gift_count_sequence",
        sequenceName = "gift_count_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "gift_count_sequence"
    )
    private Long id;
    private Long userId;
    private Long giftId;
    private Integer eligibility;
}
