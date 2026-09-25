package cz.trixi.parsexml.api.dto;

import cz.trixi.parsexml.persistence.entity.Municipality;
import cz.trixi.parsexml.persistence.entity.MunicipalityPart;

public record MunicipalityPartDto(
        Long id,
        String name,
        String code,
        String municipalityCode
) {

    public static MunicipalityPartDto from(MunicipalityPart m) {
        return new MunicipalityPartDto(m.getId(), m.getName(), m.getCode(), m.getMunicipality().getCode());
    }
}
