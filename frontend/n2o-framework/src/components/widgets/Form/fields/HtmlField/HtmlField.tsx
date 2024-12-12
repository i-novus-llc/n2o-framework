import React from 'react'
import classNames from 'classnames'

import { Html, type Props } from '../../../../snippets/Html/Html'

/**
 * Компонент поле html
 */
export function HtmlField({ id, html, className }: Props) {
    return <Html id={id} html={html} className={classNames('n2o-html-field', className)} />
}
