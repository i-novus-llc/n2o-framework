import React from 'react'
import { get } from 'lodash'

import { Icon } from '../../display/Icon'

import { TOption } from './types'

export function PopupImage({ item, imageFieldId }: {
    item: TOption,
    imageFieldId?: string
}) {
    if (!imageFieldId) { return null }

    const image = get(item, imageFieldId, null)

    return image && <img src={image} alt={item.label} />
}

export function PopupIcon({ item, iconFieldId }: {
    item: TOption,
    iconFieldId?: string
}) {
    if (!iconFieldId) { return null }

    const iconName = get(item, iconFieldId, null)

    return iconName && <Icon name={iconName} />
}
