import React from 'react'
import classNames from 'classnames'
import DOMPurify, { type Config } from 'dompurify'

import { type TBaseProps } from '../types'
import { EMPTY_ARRAY } from '../utils/emptyTypes'

export type AllowedDomains = string[]

export type Csp = Config & { ALLOWED_DOMAINS: AllowedDomains }

export type Props = TBaseProps & {
    html: string
    id: string
    csp?: Csp
}

const isAllowedDomain = (src: string, allowedDomains: AllowedDomains): boolean => {
    try {
        const url = new URL(src, window.location.href)

        if (url.protocol !== 'https:') { return false }

        const { hostname } = url

        return allowedDomains.some((domain) => {
            const cleanDomain = domain.replace(/^https?:\/\//, '')

            // точное совпадение или поддомен
            return hostname === cleanDomain ||
                hostname.endsWith(`.${cleanDomain}`)
        })
    } catch {
        return false
    }
}

const createPurifyWithIframeHook = ({ allowedDomains }: { allowedDomains: AllowedDomains }) => {
    const purify = DOMPurify(window)

    purify.addHook('afterSanitizeAttributes', (node) => {
        if (node.tagName === 'IFRAME') {
            // Безопасность: удаляем srcdoc
            if (node.hasAttribute('srcdoc')) {
                console.warn('srcdoc removed for security')
                node.removeAttribute('srcdoc')
            }

            const src = node.getAttribute('src')

            if (!src || !isAllowedDomain(src, allowedDomains)) {
                node.removeAttribute('src')
            }
        }
    })

    return purify
}

export const Html = ({ id, html, className, csp }: Props) => {
    const { ALLOWED_DOMAINS = EMPTY_ARRAY, ...cfg } = csp || {}
    const purify = createPurifyWithIframeHook({ allowedDomains: ALLOWED_DOMAINS })

    const cleanedHtml = purify.sanitize(html, cfg)

    return (
        <div
            id={id}
            className={classNames('n2o-html-snippet n2o-snippet', className)}
            /* eslint-disable-next-line react/no-danger */
            dangerouslySetInnerHTML={{ __html: cleanedHtml }}
        />
    )
}

export default Html
