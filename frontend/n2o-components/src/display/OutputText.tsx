import React, { memo, MouseEventHandler, useRef } from 'react'
import isNumber from 'lodash/isNumber'
import isString from 'lodash/isString'
import classNames from 'classnames'
import { useTranslation } from 'react-i18next'

import { TBaseProps } from '../types'

import { Icon } from './Icon'
import { Text } from './Typography/Text'

import '../styles/components/OutputText.scss'

type IconProps = {
    icon: string,
}

type TextProps = {
    expandable?: boolean | number,
    format?: string,
    isOpen?: boolean,
    preLine?: boolean,
    showFullText?: MouseEventHandler<HTMLAnchorElement>,
    t(key: string): string,
    value: string
}

type RenderComponentsType = 'icon' | 'text' | 'iconAndText'

type RenderComponentsMap = {
    icon(args: IconProps): JSX.Element;
    iconAndText(args: IconProps & TextProps): JSX.Element;
    text(args: TextProps): JSX.Element;
}

const RenderComponents: RenderComponentsMap = {
    icon: ({ icon }: IconProps) => <Icon className="icon" name={icon} />,
    text: ({ value, format, expandable, showFullText, preLine, isOpen, t }: TextProps) => (
        <div className="text">
            <Text text={value} format={format} preLine={preLine} />
            {expandable && (
                // eslint-disable-next-line jsx-a11y/anchor-is-valid
                <a href="#" onClick={showFullText} className="details-label">
                    {isOpen ? t('hide') : t('details')}
                </a>
            )}
        </div>
    ),
    iconAndText: ({
        icon,
        value,
        format,
        expandable,
        showFullText,
        preLine,
        isOpen,
        t,
    }: IconProps & TextProps) => (
        <>
            {icon && <Icon className="icon" name={icon} />}
            <div className="text">
                <Text text={value} format={format} preLine={preLine} />
                {expandable && (
                    // eslint-disable-next-line jsx-a11y/anchor-is-valid
                    <a href="#" onClick={showFullText} className="details-label">
                        {isOpen ? t('hide') : t('details')}
                    </a>
                )}
            </div>
        </>
    ),
}

type OutputTextProps = TBaseProps & {
    ellipsis?: boolean,
    expandable?: boolean | number,
    format?: string,
    icon?: string,
    textPlace?: string,
    type?: RenderComponentsType,
    value: string
}

export const OutputText = memo(({
    textPlace = 'left',
    type = 'iconAndText',
    className = 'n2o',
    style,
    icon = '',
    ellipsis = false,
    expandable = false,
    value,
    ...rest
}: OutputTextProps) => {
    const [isOpen, setIsOpen] = React.useState(false)
    const { t } = useTranslation()
    const ref = useRef<HTMLDivElement>(null)
    const RenderComponent = RenderComponents[type]

    const showFullText: MouseEventHandler<HTMLAnchorElement> = (e) => {
        e.preventDefault()
        setIsOpen(isOpen => !isOpen)
    }

    const formatValue = (value: string) => {
        if (isNumber(expandable) && isString(value)) {
            return `${value.substr(0, expandable - 3)}...`
        }

        return value
    }

    const formattedValue = formatValue(value)

    return (
        <div
            className={classNames('n2o-output-text', className, textPlace, {
                'n2o-output-text--ellipsis': (ellipsis || expandable === true) && !isOpen,
                'n2o-output-text--expandable': (expandable && isOpen) || isNumber(expandable),
            })}
            ref={ref}
            style={style}
        >
            <RenderComponent
                {...rest}
                icon={icon}
                value={isOpen ? value : formattedValue}
                expandable={expandable}
                showFullText={showFullText}
                isOpen={isOpen}
                t={t}
            />
        </div>
    )
})
