import React from 'react'
import PropTypes from 'prop-types'
import { map } from 'lodash'

import { REGIONS } from '../../core/factory/factoryLevels'
import { Factory } from '../../core/factory/Factory'

/**
 * Стандартный рендер регионов
 * @param id
 * @param regions
 * @param width
 * @return {*}
 * @constructor
 */
function PageRegions({ id, regions, width, routable }) {
    return map(regions, (place, key) => (
        <div key={key} className={`n2o-page__${key}`} style={{ width: width[key] }}>
            {map(place, (region, index) => (
                <Factory
                    key={`region-${key}-${index}`}
                    level={REGIONS}
                    {...region}
                    pageId={id}
                    routable={routable || region?.routable}
                />
            ))}
        </div>
    ))
}

PageRegions.propTypes = {
    id: PropTypes.string,
    regions: PropTypes.object,
    width: PropTypes.object,
}

PageRegions.defaultProps = {
    regions: {},
    width: {},
}

export default PageRegions
