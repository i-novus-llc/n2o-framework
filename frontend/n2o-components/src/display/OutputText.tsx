import React, { memo, MouseEventHandler, useRef } from 'react'
import isNumber from 'lodash/isNumber'
import isString from 'lodash/isString'
import classNames from 'classnames'
import { useTranslation } from 'react-i18next'

import { TBaseProps } from '../types'

import { Icon } from './Icon'
import { Text } from './Typography/Text'

import '../styles/components/OutputText.scss'

type RenderComponentsType = 'icon' | 'text'

type OutputTextProps = TBaseProps & {
    ellipsis?: boolean
    expandable?: boolean | number
    format?: string
    icon?: string
    preLine: boolean
    iconPosition?: string
    type?: RenderComponentsType
    value: string
}

export const OutputText = memo(({
    iconPosition = 'left',
    type,
    className = 'n2o',
    style,
    icon = '',
    ellipsis = false,
    expandable = false,
    value,
    format,
    preLine,
    ...rest
}: OutputTextProps) => {
    const [isOpen, setIsOpen] = React.useState(false)
    const { t } = useTranslation()
    const ref = useRef<HTMLDivElement>(null)

    const showFullText: MouseEventHandler<HTMLButtonElement> = (e) => {
        e.preventDefault()
        setIsOpen(isOpen => !isOpen)
    }

    const formatValue = (value: string) => {
        if (isNumber(expandable) && isString(value)) {
            return `${value.substr(0, expandable - 3)}...`
        }

        return value
    }

    const text = isOpen ? value : formatValue(value)

    return (
        <div
            className={classNames('n2o-output-text', className, iconPosition, {
                'n2o-output-text--ellipsis': (ellipsis || expandable === true) && !isOpen,
                'n2o-output-text--expandable': (expandable && isOpen) || isNumber(expandable),
            })}
            ref={ref}
            style={style}
        >
            {icon && <Icon className="icon" name={icon} />}
            {text && (
                <div className="text">
                    <Text text={text} format={format} preLine={preLine} />
                    {expandable && (
                        <button type="button" onClick={showFullText} className="details-label link-button">
                            {isOpen ? t('hide') : t('details')}
                        </button>
                    )}
                </div>
            )}
        </div>
    )
})
