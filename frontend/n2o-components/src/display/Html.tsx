import React from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../types'

type Props = TBaseProps & {
    html: string,
    id: string
}

// eslint-disable-next-line react/no-danger
export const Html = ({ id, html, className }: Props) => <div id={id} className={classNames('n2o-html-snippet n2o-snippet', className)} dangerouslySetInnerHTML={{ __html: html }} />
