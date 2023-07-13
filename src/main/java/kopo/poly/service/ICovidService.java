package kopo.poly.service;

import kopo.poly.dto.CovidDTO;

import java.util.List;

public interface ICovidService {
    List<CovidDTO> getCovidRes() throws Exception;
}
