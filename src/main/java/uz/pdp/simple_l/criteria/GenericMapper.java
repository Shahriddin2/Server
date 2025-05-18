package uz.pdp.simple_l.criteria;

import java.util.List;

public interface GenericMapper<E, D, CD> {
    E toEntity(CD dto);

    D toDto(E entity);

    List<D> toDtoList(List<E> entity);
}
