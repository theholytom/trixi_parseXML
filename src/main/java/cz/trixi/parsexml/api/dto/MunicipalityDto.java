package cz.trixi.parsexml.api.dto;

import cz.trixi.parsexml.persistence.entity.Municipality;

public record MunicipalityDto(
        Long id,
        String name,
        String code
) {
    public static MunicipalityDto from(Municipality municipality) {
        return new MunicipalityDto(municipality.getId(), municipality.getName(), municipality.getCode());
    }

}
