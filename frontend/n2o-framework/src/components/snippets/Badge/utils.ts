import pick from 'lodash/pick'

import type { BadgeOptions } from './Badge'
import { Position } from './enums'

const badgeProps = ['position', 'shape', 'imagePosition', 'imageShape']

export const isBadgeLeftPosition = (badgePosition: Position) => badgePosition === Position.Left

export const isBadgeRightPosition = (badgePosition: Position) => badgePosition !== Position.Left

export const resolveBadgeProps = (
    props: BadgeOptions & {
        fieldId: string,
        colorFieldId: string,
        imageFieldId: string
    },
    data: Record<string, string>,
): BadgeOptions => {
    const { text, color, image, fieldId, colorFieldId, imageFieldId } = props

    return {
        ...pick(props, badgeProps),
        text: text || data[fieldId],
        color: color || data[colorFieldId],
        image: image || data[imageFieldId],
    }
}
