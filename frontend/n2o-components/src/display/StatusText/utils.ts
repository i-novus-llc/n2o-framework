import classNames from 'classnames'

import { StatusTextProps } from './types'

export const statusTextContainerStyle = (
    textPosition: StatusTextProps['textPosition'],
    className: StatusTextProps['className'],
) => classNames('n2o-status-text n2o-snippet', className, {
    'n2o-status-text__left': textPosition === 'left',
    'n2o-status-text__right': textPosition === 'right',
})

export const statusTextIconStyle = (
    textPosition: StatusTextProps['textPosition'],
    color: StatusTextProps['color'],
) => classNames(
    {
        'n2o-status-text_icon__right': textPosition === 'left',
        'n2o-status-text_icon__left': textPosition === 'right',
    },
    `bg-${color}`,
)
