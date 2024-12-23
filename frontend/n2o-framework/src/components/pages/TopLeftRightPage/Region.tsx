import React from 'react'

import { State as RegionsState } from '../../../ducks/regions/Regions'
import { REGIONS } from '../../../core/factory/factoryLevels'
import { Factory } from '../../../core/factory/Factory'

interface RegionProps {
    region?: RegionsState[]
    pageId?: string
}

export function Region({ region, pageId }: RegionProps) {
    // eslint-disable-next-line react/no-array-index-key,react/jsx-no-useless-fragment
    return <>{region?.map((region, i) => <Factory key={i} level={REGIONS} {...region} pageId={pageId} />)}</>
}
