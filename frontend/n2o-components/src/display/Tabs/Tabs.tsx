import React from 'react'
import classNames from 'classnames'

import { EMPTY_ARRAY } from '../../utils/emptyTypes'

import { createOptions } from './helpers'
import { Pane } from './Pane'
import { Content } from './Content'

export interface Tab {
    content: JSX.Element[]
    disabled: boolean
    icon?: string
    id: string
    invalid?: boolean
    label: string
    opened: boolean
    visible?: boolean
    tooltip?: string
    className?: string
}

export interface Props {
    active: string
    onChange(event: React.ChangeEvent<HTMLInputElement>): void
    className?: string
    paneClassName?: string
    contentClassName?: string
    contentStyle: React.CSSProperties
    maxHeight?: string
    hideSingleTab?: boolean
    contentRenderMethod?: 'active' | 'all'
    style: React.CSSProperties
    tabs: Tab[]
}

export function Tabs({
    active,
    onChange,
    maxHeight,
    className,
    paneClassName,
    contentClassName,
    contentStyle,
    contentRenderMethod,
    hideSingleTab,
    style,
    tabs = EMPTY_ARRAY,
}: Props) {
    const options = createOptions(tabs, hideSingleTab)

    return (
        <section className={classNames('tabs', className)} style={style}>
            <Pane onChange={onChange} className={paneClassName} tabs={options} active={active} />
            <Content
                active={active}
                tabs={tabs}
                className={contentClassName}
                style={contentStyle}
                contentRenderMethod={contentRenderMethod}
            />
        </section>
    )
}
