import React from 'react'
import classNames from 'classnames'
import { Html, type Props } from '@i-novus/n2o-components/lib/display/Html'

/**
 * Компонент поле html
 */
export function HtmlField({ id, html, className }: Props) {
    return <Html id={id} html={html} className={classNames('n2o-html-field', className)} />
}
