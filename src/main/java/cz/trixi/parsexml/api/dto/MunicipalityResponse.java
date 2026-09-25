package cz.trixi.parsexml.api.dto;

import cz.trixi.parsexml.persistence.entity.Municipality;

import java.util.List;
import java.util.stream.Collectors;

public record MunicipalityResponse(
        Long id,
        String name,
        String code,
        List<MunicipalityPartDto> parts
) {

    public static MunicipalityResponse from(Municipality m) {
        return new MunicipalityResponse(
                m.getId(),
                m.getName(),
                m.getCode(),
                m.getParts().stream().map(MunicipalityPartDto::from).collect(Collectors.toList())
        );
    }
}
