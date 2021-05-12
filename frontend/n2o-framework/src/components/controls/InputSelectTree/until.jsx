import React from 'react'
import uniqueId from 'lodash/uniqueId'
import has from 'lodash/has'
import get from 'lodash/get'
import isNil from 'lodash/isNil'
import Badge from 'reactstrap/lib/Badge'
import { SHOW_ALL, SHOW_CHILD, SHOW_PARENT } from 'rc-tree-select'

import Icon from '../../snippets/Icon/Icon'

export const visiblePartPopup = (
    item,
    {
        prefixCls,
        iconFieldId,
        imageFieldId,
        labelFieldId,
        badgeFieldId,
        badgeColorFieldId,
    },
) => (
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
            has(item, labelFieldId) && (
                <span key={uniqueId('tree_label_')} className={`${prefixCls}-label`}>
                    {item[labelFieldId]}
                </span>
            ),
            has(item, badgeFieldId) && (
                <Badge key={uniqueId('tree_badge_')} color={item[badgeColorFieldId]}>
                    {item[badgeFieldId]}
                </Badge>
            ),
        ]}
    </span>
)

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
