import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import map from 'lodash/map'
import get from 'lodash/get'

import { Factory } from '../../core/factory/Factory'
import { WIDGETS } from '../../core/factory/factoryLevels'

import { getFetchOnInit, getFetch } from './helpers'

export function RegionContent({
    content,
    tabSubContentClass,
    pageId,
    className,
    active,
    lazy = false,
}) {
    const mapClassNames = {
        TabsRegion: tabSubContentClass,
    }

    return (
        <div className={className}>
            {map(content, (meta = {}, index) => {
                const { src, fetchOnInit: metaFetchOnInit } = meta

                const getClassName = (meta, path) => get(meta, path) || ''

                const regionClassName = getClassName(mapClassNames, src)
                const metaClassName = getClassName(meta, 'className')

                const className = classNames({
                    [regionClassName]: regionClassName,
                    [metaClassName]: metaClassName,
                })

                const fetchOnInit = getFetchOnInit(metaFetchOnInit, lazy, active)
                const fetch = getFetch(lazy, active)

                return (
                    <Factory
                        level={WIDGETS}
                        key={index}
                        pageId={pageId}
                        {...meta}
                        className={className}
                        fetchOnInit={fetchOnInit}
                        fetch={fetch}
                    />
                )
            })}
        </div>
    )
}

RegionContent.propTypes = {
    content: PropTypes.any,
    pageId: PropTypes.string,
    className: PropTypes.string,
    tabSubContentClass: PropTypes.any,
    active: PropTypes.bool,
    lazy: PropTypes.bool,
}

export default RegionContent
