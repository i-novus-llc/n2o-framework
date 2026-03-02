import React, { useRef } from 'react'
import classNames from 'classnames'
import map from 'lodash/map'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'

import { Factory } from '../../core/factory/Factory'
import { WIDGETS } from '../../core/factory/factoryLevels'
import { ContentMeta } from '../../ducks/regions/Regions'
import { Widget as DummyWidget } from '../widgets/Dummy'
import { FETCH_TYPE } from '../../core/widget/const'

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
    const isActive = active === tabId
    const isInitRef = useRef(isActive)

    if (isEmpty(content)) { return null }

    const getContentVisibility = (meta: ContentMeta) => {
        if (!lazy) { return true }

        if (alwaysRefresh) { return (active === undefined || isActive) }

        return meta?.visible
    }

    isInitRef.current = isInitRef.current || isActive

    const needDummy = lazy && !isInitRef.current && !isActive
    const fetch = alwaysRefresh || !tabId ? FETCH_TYPE.always : FETCH_TYPE.lazy

    return (
        <div className={className}>
            {map(content, (meta: ContentMeta) => {
                const { src, id, className } = meta

                const options = {
                    ...meta,
                    pageId,
                    className: classNames(className, 'nested-content', get(mapClassNames, src, '')),
                    parent: parent || (tabId ? { regionId, tabId } : {}),
                    fetch,
                }

                if (needDummy) {
                    // Для lazy рисуем заглушку до первого перехода на вкладку
                    // @ts-ignore TODO fix types
                    return (<DummyWidget key={id} {...options} />)
                }

                return (
                    <Factory
                        level={WIDGETS}
                        key={id}
                        {...options}
                        visible={getContentVisibility(meta)}
                    />
                )
            })}
        </div>
    )
}

export default RegionContent
