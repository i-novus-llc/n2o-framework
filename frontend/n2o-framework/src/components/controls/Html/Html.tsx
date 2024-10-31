import React from 'react'

import { Html as HtmlResolver } from '../../widgets/Html/Html'

type HtmlProps = {
    id: string
    model: Record<string, unknown>
    visible: boolean
    html: string
} & React.HTMLProps<HTMLElement> // for any additional HTML props

/**
 * Html component
 * @param {HtmlProps} props - component props
 * @returns {JSX.Element|null}
 * @constructor
 */
export function Html({ visible, model, ...rest }: HtmlProps): JSX.Element | null {
    if (!visible) {
        return null
    }

    return <HtmlResolver {...rest} data={model} />
}

export default Html
