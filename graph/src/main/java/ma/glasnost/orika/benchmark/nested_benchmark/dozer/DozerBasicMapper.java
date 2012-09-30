/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package ma.glasnost.orika.benchmark.nested_benchmark.dozer;

import java.util.Arrays;

import org.dozer.DozerBeanMapper;

import com.inspiresoftware.lib.dto.geda.benchmark.Mapper;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;

/**
 * This is an update to the GeDA benchmark for the Dozer mapper,
 * where the mapping is performed on an already instantiated object
 * (the same as done by GeDA)
 */
public class DozerBasicMapper implements Mapper {

    private DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList("dozer-mapping.xml"));

    public Object fromEntity(final Object entity) {
        PersonDTO dto = new PersonDTO();
        mapper.map(entity, dto);
        return dto;
    }

    public Object fromDto(final Object dto) {
        Person entity = new Person();
        mapper.map(dto, entity);
        return entity;
    }
}
