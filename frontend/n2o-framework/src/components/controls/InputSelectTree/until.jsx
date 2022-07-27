import React from 'react'
import uniqueId from 'lodash/uniqueId'
import has from 'lodash/has'
import get from 'lodash/get'
import isNil from 'lodash/isNil'
import { SHOW_ALL, SHOW_CHILD, SHOW_PARENT } from 'rc-tree-select'

import { Icon } from '../../snippets/Icon/Icon'
import { renderSquareBadge } from '../../snippets/Badge/Badge'
import { isBadgeLeftPosition, isBadgeRightPosition, resolveBadgeProps } from '../../snippets/Badge/utils'

export const visiblePartPopup = (
    item,
    {
        prefixCls,
        iconFieldId,
        imageFieldId,
        labelFieldId,
        badge,
    },
) => {
    const {
        fieldId: badgeFieldId,
        position: badgePosition,
    } = badge || {}

    const hasBadge = has(item, badgeFieldId)

    const Badge = renderSquareBadge(resolveBadgeProps(badge, item))

    return (
        <span className={`${prefixCls}-content-wrapper`}>
            {[
                has(item, iconFieldId) && (
                    <Icon key={uniqueId('tree_icon_')} name={item[iconFieldId]} />
                ),
                !isNil(item[imageFieldId]) && (
                    <div className={`${prefixCls}-image-tree-wrapper`}>
                        <img
                            alt="not found"
                            key={uniqueId('tree_img_')}
                            src={item[imageFieldId]}
                        />
                    </div>
                ),
                hasBadge && isBadgeLeftPosition(badgePosition) && Badge,
                has(item, labelFieldId) && (
                    <span key={uniqueId('tree_label_')} className={`${prefixCls}-label`}>
                        {item[labelFieldId]}
                    </span>
                ),
                hasBadge && isBadgeRightPosition(badgePosition) && Badge,
            ]}
        </span>
    )
}

const STRATEGIES = {
    parent: SHOW_PARENT,
    child: SHOW_CHILD,
    all: SHOW_ALL,
}

export const getCheckedStrategy = (key) => {
    const strategy = get(STRATEGIES, key)

    if (strategy) {
        return strategy
    }

    return SHOW_ALL
}
