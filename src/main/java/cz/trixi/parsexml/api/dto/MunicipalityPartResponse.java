package cz.trixi.parsexml.api.dto;

import cz.trixi.parsexml.persistence.entity.MunicipalityPart;


public record MunicipalityPartResponse(
        Long id,
        String name,
        String code,
        MunicipalityDto municipality
) {
    public static MunicipalityPartResponse from(MunicipalityPart m) {
        return new MunicipalityPartResponse(
                m.getId(),
                m.getName(),
                m.getCode(),
                MunicipalityDto.from(m.getMunicipality())
        );
    }
}
