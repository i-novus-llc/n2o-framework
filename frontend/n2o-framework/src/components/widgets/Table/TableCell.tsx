import React, { CSSProperties, ReactNode, ElementType } from 'react'
import classNames from 'classnames'
import omit from 'lodash/omit'

import { getElementType } from '../../../tools/getElementType'

interface TableCellProps {
    className?: string
    style?: CSSProperties
    component?: ElementType;
    model?: object;
    colSpan?: number;
    children?: ReactNode;
    contentAlignment?: 'left' | 'center' | 'right';
    hideOnBlur?: boolean;
    needRender?: boolean;
}

const resolveClassName = (
    className?: string,
    contentAlignment?: string,
    config: Record<string, boolean> = {},
) => classNames(className, {
    [`content-alignment-${contentAlignment}`]: contentAlignment,
    ...config,
})

export const TableCell = (props: TableCellProps) => {
    const {
        className,
        style,
        component,
        colSpan,
        children,
        model,
        contentAlignment,
        hideOnBlur = false,
        needRender = true,
    } = props

    if (!needRender) { return null }

    const ElementType: React.ElementType = getElementType(TableCell, { as: 'td', component }) as React.ElementType

    if (!ElementType) { return null }

    if (React.Children.count(children)) {
        return (
            <ElementType
                className={resolveClassName(className, contentAlignment)}
                colSpan={colSpan}
                model={model}
                style={style}
            >
                {children}
            </ElementType>
        )
    }

    return (
        <ElementType
            className={resolveClassName(className, contentAlignment, { 'hide-on-blur': hideOnBlur })}
            colSpan={colSpan}
            style={style}
        >
            {component && React.createElement(component, { ...omit(props, ['component', 'colSpan', 'as']) })}
        </ElementType>
    )
}

export default TableCell
