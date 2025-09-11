import React from 'react'
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
    tabSubContentClass?: string
    pageId: string
    className?: string
    active?: string
    lazy?: boolean
    regionId?: string
    parent?: string | null
    tabId?: string
    alwaysRefresh?: boolean
}

export function RegionContent({
    content,
    tabSubContentClass,
    pageId,
    className,
    active,
    regionId,
    tabId,
    alwaysRefresh,
    parent = null,
    lazy = false,
}: Props) {
    const mapClassNames = {
        TabsRegion: tabSubContentClass,
    }

    if (isEmpty(content)) { return null }

    const getContentVisibility = (meta: ContentMeta) => {
        if (!lazy) { return true }

        if (alwaysRefresh) {
            return (active === undefined || active === tabId)
        }

        return meta?.visible
    }

    return (
        <div className={className}>
            {map(content, (meta: ContentMeta, index) => {
                const { src, fetchOnInit: metaFetchOnInit } = meta

                const getClassName = (meta: ContentMeta | { TabsRegion?: string }, path: string) => get(meta, path) || ''

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
                        pageId={pageId}
                        className={className}
                        fetchOnInit={fetchOnInit}
                        fetch={fetch}
                        parent={parent || (tabId ? { regionId, tabId } : {})}
                        visible={getContentVisibility(meta)}
                    />
                )
            })}
        </div>
    )
}

export default RegionContent
