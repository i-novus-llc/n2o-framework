import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import map from 'lodash/map'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'

import { Factory } from '../../core/factory/Factory'
import { WIDGETS } from '../../core/factory/factoryLevels'
import { ContentMeta } from '../../ducks/regions/Regions'

import { getFetchOnInit, getFetch } from './helpers'

interface Props {
    content: ContentMeta[]
    tabSubContentClass: string
    pageId: string
    className?: string
    active: string
    lazy: boolean
    regionId: string
    parent?: string | null
    tabId: string
}

export function RegionContent(props: Props) {
    const {
        content,
        tabSubContentClass,
        pageId,
        className,
        active,
        regionId,
        tabId,
        parent = null,
        lazy = false,
    } = props

    const mapClassNames = {
        TabsRegion: tabSubContentClass,
    }

    if (isEmpty(content)) { return null }

    return (
        <div className={className}>
            {map(content, (meta: ContentMeta, index) => {
                const { src, fetchOnInit: metaFetchOnInit } = meta

                const getClassName = (meta: ContentMeta | { TabsRegion: string }, path: string) => get(meta, path) || ''

                const regionClassName = getClassName(mapClassNames, src)
                const metaClassName = getClassName(meta, 'className')

                const className = classNames('nested-content', {
                    [regionClassName]: regionClassName,
                    [metaClassName]: metaClassName,
                })

                const fetchOnInit = getFetchOnInit(metaFetchOnInit, lazy, active)
                const fetch = getFetch(lazy, active, tabId)

                return (
                    <Factory
                        level={WIDGETS}
                        key={index}
                        {...meta}
                        // @ts-ignore import from js file
                        pageId={pageId}
                        className={className}
                        fetchOnInit={fetchOnInit}
                        fetch={fetch}
                        parent={parent || { regionId, tabId }}
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
