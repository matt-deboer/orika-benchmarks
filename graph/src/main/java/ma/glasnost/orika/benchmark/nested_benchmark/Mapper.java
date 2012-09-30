
package ma.glasnost.orika.benchmark.nested_benchmark;

/**
 */
public interface Mapper {

    /**
     * Convert entity to DTO.
     *
     * @param entity entity object
     * @return dto
     */
    Object fromEntity(Object entity);

    /**
     * Assemble entity from DTO
     *
     * @param dto dto
     * @return entity with data
     */
    Object fromDto(Object dto);

}
