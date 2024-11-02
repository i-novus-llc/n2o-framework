import React from 'react'
import map from 'lodash/map'

import { REGIONS } from '../../core/factory/factoryLevels'
import { Factory } from '../../core/factory/Factory'

import { type PageRegionsProps } from './types'

/**
 * Стандартный рендер регионов
 */
export function PageRegions({ id, regions = {}, width = {}, routable }: PageRegionsProps) {
    return (
        <>
            {
            map(regions, (place, key) => (
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
        </>
    )
}

export default PageRegions
