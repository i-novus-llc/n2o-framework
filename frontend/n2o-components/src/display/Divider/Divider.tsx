import React, { CSSProperties, ReactNode } from 'react'
import classNames from 'classnames'

import { Text } from '../../Typography/Text'

export type Props = {
    className?: string
    dashed?: boolean
    position?: string
    type?: 'horizontal' | 'vertical'
    children?: ReactNode
    style?: CSSProperties
}

/**
 * Компонент Divider
 * @reactProps {string} className - Пользовательский className css (default : '');
 * @reactProps {object} style - css стили Divider. Необходимые параметры width-для горизонтального, height-для вертикального (default : none);
 * @reactProps {boolean} dashed - вкл/откл путктирной линии на Divider (default : false);
 * @reactProps{string} position - Позиционирование заголовка. Работает только на горизонтальном Divider и при наличии заголовка (default : 'left');
 * @reactProps {string} type - Тип Divider horizontal или vertical (default : 'horizontal');
 * @reactProps {string} children - Заголовок для горизонтального типа. Указывается между тегами компонента: <Divider>Заголовок</Divider> (default : none);
 * * */

export const Divider = ({ style, children, className, dashed = false, position = 'left', type = 'horizontal' }: Props) => {
    const horizontal = type === 'horizontal'
    const vertical = type === 'vertical'

    const computedClassName = classNames(
        className,
        {
            'divider-h': horizontal,
            'divider-h_with-title': horizontal && children,
            [`divider-h_${position}`]: horizontal && position,
            'divider-h_dashed': horizontal && dashed,
            'divider-v': vertical,
            'divider-v_dashed': vertical && dashed,
            divider_horizontal: horizontal && !children,
            divider_vertical: vertical,
        },
    )

    return (
        <div className={computedClassName} style={style}>
            {children && <span className="divider-h__title"><Text>{children}</Text></span>}
        </div>
    )
}
