import React from 'react'
import get from 'lodash/get'
import classNames from 'classnames'
import { FormattedText } from '@i-novus/n2o-components/lib/Typography/FormattedText'
import { EllipsisText } from '@i-novus/n2o-components/lib/Typography/EllipsisText'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'

import { withTooltip } from '../../withTooltip'
import { EMPTY_OBJECT } from '../../../../../utils/emptyTypes'

import { SubText } from './SubText'
import { ICON_POSITIONS, type TextCellProps } from './types'

function TextCellBody({
    fieldKey,
    icon,
    id,
    model,
    subTextFieldKey,
    subTextFormat,
    forwardedRef,
    visible = true,
    iconPosition = ICON_POSITIONS.LEFT,
    style = EMPTY_OBJECT,
    className,
    isTextWrap,
    ...rest
}: TextCellProps) {
    const text = model && get(model, fieldKey || id)

    if ((!Number.isFinite(text) && !text) || !visible) { return null }

    return (
        <div className="d-inline-flex flex-column" ref={forwardedRef}>
            <div
                className={classNames(className, 'icon-cell-container', {
                    'icon-cell-container__with-tooltip': model?.tooltipFieldId !== undefined,
                    'icon-cell-container__text-left': iconPosition === ICON_POSITIONS.RIGHT,
                })}
                style={style}
            >
                {icon && <Icon name={icon} />}
                {
                    isTextWrap
                        ? <FormattedText {...rest}>{text?.toString()}</FormattedText>
                        : <EllipsisText {...rest}>{text}</EllipsisText>
                }
            </div>
            {subTextFieldKey && <SubText subText={model && get(model, subTextFieldKey)} format={subTextFormat} /> }
        </div>
    )
}

export const TextCell = withTooltip(TextCellBody)
export default TextCell
