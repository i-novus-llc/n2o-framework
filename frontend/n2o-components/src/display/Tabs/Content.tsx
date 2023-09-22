import React from 'react'
import classNames from 'classnames'

import { Tab } from './Tabs'

interface Props {
    active: string
    tabs: Tab[]
    className?: string
    style: React.CSSProperties
    /* DOM contains only active content or contains all content, hiding inactive content with css */
    contentRenderMethod?: 'active' | 'all'
}

export function Content({ active, tabs, className, style, contentRenderMethod = 'active' }: Props) {
    return (
        <section style={style} className={classNames('tabs__content', className)}>
            {contentRenderMethod === 'active' && tabs.find(({ id }) => id === active)?.content}
            {contentRenderMethod === 'all' && tabs.map(({ content, id }) => (
                <div className={classNames('tabs__content--single', { 'd-none': id !== active, active: id === active })}>
                    {content}
                </div>
            ))}
        </section>
    )
}
