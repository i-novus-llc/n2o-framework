import React from 'react'
import { Link as LinkComponent } from '@i-novus/n2o-components/lib/navigation/Link'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'

import { Item } from '../../../../CommonMenuTypes'
import { Tooltip } from '../../../../../components/snippets/Tooltip/TooltipHOC'
import { useLink } from '../../../../../components/core/router/useLink'
import { useLinkPropsResolver } from '../../../../../components/navigation/useLinkPropsResolver'
import { ModelPrefix } from '../../../../../core/models/types'

import { LinkBody } from './LinkBody'

interface LinkProps {
    item: Item
    sidebarOpen: boolean
    isMiniView: boolean
    isStaticView: boolean
    showContent: boolean
}

/*
 * Костыль для SidebarsAT.testSidebarWithDatasource
 * TODO: Разобраться на сколько нужная фича
 */
const setDefaultLink = (item: Item) => {
    const { datasource, model: prefix = ModelPrefix.active, pathMapping } = item

    if (!datasource || !pathMapping || isEmpty(pathMapping)) { return item }

    return {
        ...item,
        pathMapping: Object.fromEntries(Object.entries(pathMapping).map(([key, mapping]) => [
            key,
            mapping.link
                ? mapping
                : {
                    ...mapping,
                    link: `models.${prefix}['${datasource}']`,
                    value: mapping.value.replace(/^:(.*)/i, '`$1`'),
                },
        ])),
    }
}

export function Link({ item, sidebarOpen, isMiniView, isStaticView, showContent }: LinkProps) {
    const { url: href, title, disabled, target } = useLinkPropsResolver({ ...setDefaultLink(item), url: item.href })

    const hint = isMiniView ? title : null
    const { active, ...linkProps } = useLink({ href, disabled, target })

    return (
        <Tooltip placement="right" hint={hint}>
            <LinkComponent
                className={classNames('n2o-sidebar__item', { active })}
                disabled={disabled}
                {...linkProps}
                label={(
                    <LinkBody
                        {...item}
                        sidebarOpen={sidebarOpen}
                        isStaticView={isStaticView}
                        showContent={showContent}
                        isMiniView={isMiniView}
                    />
                )}
            />
        </Tooltip>
    )
}

Link.displayName = 'n2o/Sidebar/Link'
