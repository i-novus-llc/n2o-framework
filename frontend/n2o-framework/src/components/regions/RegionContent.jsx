import React from 'react'
import classNames from 'classnames'
import map from 'lodash/map'
import get from 'lodash/get'

import Factory from '../../core/factory/Factory'
import { WIDGETS } from '../../core/factory/factoryLevels'

function RegionContent({ content, tabSubContentClass, pageId }) {
    const mapClassNames = {
        TabsRegion: tabSubContentClass,
    }

    return (
        <div>
            {map(content, (meta, index) => {
                const src = get(meta, 'src')

                const getClassName = (meta, path) => get(meta, path) || ''

                const regionClassName = getClassName(mapClassNames, src)
                const metaClassName = getClassName(meta, 'className')

                const className = classNames({
                    [regionClassName]: regionClassName,
                    [metaClassName]: metaClassName,
                })

                return (
                    <Factory
                        level={WIDGETS}
                        key={index}
                        pageId={pageId}
                        {...meta}
                        className={className}
                    />
                )
            })}
        </div>
    )
}

export default RegionContent
