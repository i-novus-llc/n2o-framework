import classNames from 'classnames'

export const statusTextContainerStyle = (textPosition, className) => classNames('n2o-status-text n2o-snippet', className, {
    'n2o-status-text__left': textPosition === 'left',
    'n2o-status-text__right': textPosition === 'right',
})

export const statusTextIconStyle = (textPosition, color) => classNames(
    {
        'n2o-status-text_icon__right': textPosition === 'left',
        'n2o-status-text_icon__left': textPosition === 'right',
    },
    `bg-${color}`,
)
