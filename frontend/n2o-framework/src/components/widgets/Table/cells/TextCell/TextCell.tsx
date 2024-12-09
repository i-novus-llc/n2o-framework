import React from 'react'
import get from 'lodash/get'
import classNames from 'classnames'

import { withTooltip } from '../../withTooltip'
import { Text } from '../../../../snippets/Typography/Text/Text'
import { Icon } from '../../../../snippets/Icon/Icon'

import { SubText } from './SubText'
import { ICON_POSITIONS, type TextCellProps } from './types'

function TextCellBody({
    fieldKey,
    icon,
    id,
    model,
    preLine,
    subTextFieldKey,
    subTextFormat,
    forwardedRef,
    visible = true,
    iconPosition = ICON_POSITIONS.LEFT,
    ...rest
}: TextCellProps) {
    const text = model && get(model, fieldKey || id)

    if ((!Number.isFinite(text) && !text) || !visible) { return null }

    return (
        <div className="d-inline-flex flex-column" ref={forwardedRef}>
            <div
                className={classNames('icon-cell-container', {
                    'icon-cell-container__with-tooltip': model?.tooltipFieldId !== undefined,
                    'icon-cell-container__text-left': iconPosition === ICON_POSITIONS.RIGHT,
                })}
            >
                {icon && <Icon name={icon} />}
                <Text text={text} preLine={preLine} {...rest} />
            </div>
            {subTextFieldKey && <SubText subText={model && get(model, subTextFieldKey)} format={subTextFormat} /> }
        </div>
    )
}

export const TextCell = withTooltip(TextCellBody)
export default TextCell
