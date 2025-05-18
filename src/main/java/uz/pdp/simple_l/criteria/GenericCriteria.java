package uz.pdp.simple_l.criteria;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ParameterObject
public abstract class GenericCriteria {

    @PositiveOrZero
    private int page = 0;
    @Positive
    private int size = 10;

    private String query;

    @JsonProperty("sorts")
    private List<SortDescriptor> sorts;

    @JsonIgnore
    public Sort getSort() {
        if (!CollectionUtils.isEmpty(sorts)) {
            return Sort.by(getOrders());
        }
        return defaultSort();
    }

    @JsonIgnore
    private List<Sort.Order> getOrders() {
        return sorts.stream().map(SortDescriptor::getOrder).toList();
    }

    public void setQuery(String query) {
        this.query = query != null ? StringUtils.trimAllWhitespace(query).toLowerCase() : null;
    }

    public abstract Sort defaultSort();
}
