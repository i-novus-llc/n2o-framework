import React from 'react'

import evalExpression, { parseExpression } from '../../utils/evalExpression'

export function withNewlines(str: string) {
    return str.split('\\n').map(text => (
        <>
            {text}
            <br />
        </>
    ))
}

export function resolveText(text?: string): string | null {
    if (!text) {
        return null
    }

    const expression = parseExpression(text)

    if (expression) {
        const value = evalExpression(expression)

        if (typeof value === 'string' || typeof value === 'number') {
            return String(value)
        }
    }

    return text
}
