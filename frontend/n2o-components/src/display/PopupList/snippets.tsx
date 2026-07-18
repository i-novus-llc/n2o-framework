import React from 'react'
import { get } from 'lodash'

import { Icon } from '../Icon'

import { Option } from './types'

export function PopupImage({ item, imageFieldId }: {
    item: Option,
    imageFieldId?: string
}) {
    if (!imageFieldId) { return null }

    const image = get(item, imageFieldId, null)

    return image && <img src={image} alt={item.label} />
}

export function PopupIcon({ item, iconFieldId }: {
    item: Option,
    iconFieldId?: string
}) {
    if (!iconFieldId) { return null }

    const iconName = get(item, iconFieldId, null)

    return <Icon name={iconName} />
}
