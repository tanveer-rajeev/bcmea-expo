package com.betafore.evoting.LotteryManagement;

import com.betafore.evoting.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "lotteryPrizeMap")
public class LotteryPrizeMap extends BaseEntity {

    private Long lotteryId;
    private Long prizeId;
    private Integer position;
}
