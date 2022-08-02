import pick from 'lodash/pick'

import type { IBadgeProps } from './Badge'
import { Position } from './enums'

const badgeProps = ['position', 'shape', 'imagePosition', 'imageShape']

export const isBadgeLeftPosition = (badgePosition: Position) => badgePosition === Position.Left

export const isBadgeRightPosition = (badgePosition: Position) => badgePosition !== Position.Left

export const resolveBadgeProps = (
    props: IBadgeProps & {
        fieldId: string,
        colorFieldId: string,
        imageFieldId: string
    },
    data: Record<string, string>,
): IBadgeProps => {
    const { text, color, image, fieldId, colorFieldId, imageFieldId } = props

    return {
        ...pick(props, badgeProps),
        text: text || data[fieldId],
        color: color || data[colorFieldId],
        image: image || data[imageFieldId],
    }
}
