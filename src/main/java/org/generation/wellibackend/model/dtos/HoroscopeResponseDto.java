package org.generation.wellibackend.model.dtos;

import lombok.Getter; import lombok.Setter;

@Getter @Setter
public class HoroscopeResponseDto {
    private String sign;
    private String scope;
    private String periodKey;
    private String work;
    private String health;
    private String love;
}
