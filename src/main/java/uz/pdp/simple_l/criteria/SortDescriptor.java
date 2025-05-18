package uz.pdp.simple_l.criteria;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Sort;

public record SortDescriptor(String field, @JsonProperty("direction") String direction) {

    @JsonIgnore
    public Sort.Order getOrder() {
        return new Sort.Order(getDirection(), field);
    }

    @JsonIgnore
    private Sort.Direction getDirection() {
        return direction != null && direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    }
}
